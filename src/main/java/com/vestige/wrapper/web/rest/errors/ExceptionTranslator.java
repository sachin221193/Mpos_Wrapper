package com.vestige.wrapper.web.rest.errors;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.validation.ConstraintViolationProblem;

import com.vestige.core.exceptions.BadRequestAlertException;
import com.vestige.core.exceptions.ErrorConstants;
import com.vestige.core.exceptions.FieldErrorVM;
import com.vestige.core.utils.HttpStatusAdapter;
import com.vestige.core.utils.StringsUtil;
import com.vestige.wrapper.config.WrapperConstants;
import com.vestige.wrapper.web.rest.util.HeaderUtil;

/**
 * Controller advice to translate the server side exceptions to client-friendly
 * json structures. The error response follows RFC7807 - Problem Details for
 * HTTP APIs (https://tools.ietf.org/html/rfc7807)
 */
@ControllerAdvice
public class ExceptionTranslator implements ProblemHandling {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String THIRD_PARTY_API_PROBLEM = "Some issue occured in Vestige backend system. Please try again after some time.";
	/**
	 * Post-process Problem payload to add the message key for front-end if needed
	 */
	@Override
	public ResponseEntity<Problem> process(@Nullable ResponseEntity<Problem> entity, NativeWebRequest request) {
		if (entity == null || entity.getBody() == null) {
			return entity;
		}
		Problem problem = entity.getBody();
		if (!(problem instanceof ConstraintViolationProblem || problem instanceof DefaultProblem)) {
			ProblemBuilder builder = Problem.builder()
					.withType(Problem.DEFAULT_TYPE.equals(problem.getType()) ? ErrorConstants.DEFAULT_TYPE
							: problem.getType())
					.withStatus(problem.getStatus()).withTitle(problem.getTitle())
					.with(ErrorConstants.PATH, request.getNativeRequest(HttpServletRequest.class).getRequestURI());
			return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
		}
		ProblemBuilder builder = Problem.builder()
				.withType(Problem.DEFAULT_TYPE.equals(problem.getType()) ? ErrorConstants.DEFAULT_TYPE
						: problem.getType())
				.withStatus(problem.getStatus()).withTitle(problem.getTitle())
				.with(ErrorConstants.PATH, request.getNativeRequest(HttpServletRequest.class).getRequestURI());

		if (problem instanceof ConstraintViolationProblem) {
			builder.with(ErrorConstants.VIOLATIONS, ((ConstraintViolationProblem) problem).getViolations())
					.with(ErrorConstants.MESSAGE, ErrorConstants.ERR_VALIDATION);
			return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
		} else {
			builder.withType(
					Problem.DEFAULT_TYPE.equals(problem.getType()) ? ErrorConstants.DEFAULT_TYPE : problem.getType())
					.withCause(((DefaultProblem) problem).getCause()).withDetail(problem.getDetail())
					.withInstance(problem.getInstance()).withTitle(problem.getTitle()).withStatus(problem.getStatus());
			problem.getParameters().forEach(builder::with);
			if (!problem.getParameters().containsKey(ErrorConstants.MESSAGE) && problem.getStatus() != null) {
				builder.with(ErrorConstants.MESSAGE, ErrorConstants.ERROR_HTTP + problem.getStatus().getStatusCode());
			}
			return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
		}
	}

	@Override
	public ResponseEntity<Problem> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			@Nonnull NativeWebRequest request) {
		BindingResult result = ex.getBindingResult();
		List<FieldErrorVM> fieldErrors = result.getFieldErrors().stream()
				.map(f -> new FieldErrorVM(f.getObjectName(), f.getField(), f.getCode())).collect(Collectors.toList());

		Problem problem = Problem.builder().withType(ErrorConstants.CONSTRAINT_VIOLATION_TYPE)
				.withTitle(ErrorConstants.METHOD_ARGUMENT_NOT_VALID).withStatus(defaultConstraintViolationStatus())
				.with(ErrorConstants.MESSAGE, ErrorConstants.ERR_VALIDATION)
				.with(ErrorConstants.FIELD_ERRORS, fieldErrors).build();
		return create(ex, problem, request);
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<Problem> handleNoSuchElementException(NoSuchElementException ex, NativeWebRequest request) {
		Problem problem = Problem.builder().withStatus(Status.NOT_FOUND)
				.with(ErrorConstants.MESSAGE, ErrorConstants.ENTITY_NOT_FOUND_TYPE).build();
		return create(ex, problem, request);
	}

	public ResponseEntity<Problem> handleBadRequestAlertException(BadRequestAlertException ex,
			NativeWebRequest request) {
		return create(ex, request,
				HeaderUtil.createFailureAlert(ex.getEntityName(), ex.getErrorKey(), ex.getMessage()));
	}

	@ExceptionHandler(ConcurrencyFailureException.class)
	public ResponseEntity<Problem> handleConcurrencyFailure(ConcurrencyFailureException ex, NativeWebRequest request) {
		Problem problem = Problem.builder().withStatus(Status.CONFLICT)
				.with(ErrorConstants.MESSAGE, ErrorConstants.ERR_CONCURRENCY_FAILURE).build();
		return create(ex, problem, request);
	}

	/**
	 * this is called from REST Template Error for 4xx Th
	 * 
	 * @param clientException
	 * @param request
	 * @return
	 */
	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<Problem> handleHttpClientErrorException(HttpClientErrorException clientException,
			NativeWebRequest request) {
		
		logger.error("ResponseStatusCode : {}<------> URL : {}<--------> Response : {}",
				clientException.getStatusCode(),
				request.getNativeRequest(HttpServletRequest.class).getRequestURI(),
				clientException.getResponseBodyAsString());
		Status status = HttpStatusAdapter.get4xStatus(clientException.getStatusCode());
		if(clientException.getStatusCode() == HttpStatus.UNAUTHORIZED && request.getNativeRequest(HttpServletRequest.class).getRequestURI().endsWith("login")) {
			status = Status.LOCKED;
		}
		Problem problem = Problem.builder().withStatus(status)
				.with(ErrorConstants.MESSAGE, getTitle(clientException)).withTitle(getTitle(clientException)).build();
		return create(clientException, problem, request);
	}

	/**
	 * this is called from REST Template Error for 5xx Th
	 * 
	 * @param clientException
	 * @param request
	 * @return
	 */
	@ExceptionHandler(HttpServerErrorException.class)
	public ResponseEntity<Problem> handleHttpServerErrorException(HttpServerErrorException serverException,
			NativeWebRequest request) {
		logger.error("ResponseStatusCode : {}<------> URL : {}<--------> Response : {}",
				serverException.getStatusCode(),
				request.getNativeRequest(HttpServletRequest.class).getRequestURI(),
				serverException.getResponseBodyAsString());
		Problem problem = Problem.builder().withStatus(HttpStatusAdapter.get5xStatus(serverException.getStatusCode()))
				.with(ErrorConstants.MESSAGE, getTitle(serverException)).withTitle(getTitle(serverException)).build();
		return create(serverException, problem, request);
	}

	private String getTitle(HttpStatusCodeException clientException) {
		JSONObject details = getResponseJSON(clientException);
		try {
			if(details.has(WrapperConstants.DESCRIPTION)) {
				return details.getString(WrapperConstants.DESCRIPTION);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Unable to understand server response. Please try again later.";
	}
	
	private JSONObject getResponseJSON(HttpStatusCodeException clientException) {
		JSONObject details = new JSONObject();
		try {
			String payload = clientException.getResponseBodyAsString();
			if(!StringsUtil.isNullOrEmpty(payload) ) {
				if(clientException.getStatusCode()== HttpStatus.SERVICE_UNAVAILABLE)
					details = new JSONObject(THIRD_PARTY_API_PROBLEM);
				else
					details = new JSONObject(
						new String(StringsUtil.removeUTF8Bom(clientException.getResponseBodyAsString())).replace("ï»¿", "").replace("\n", ""));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return details;
	}
}
