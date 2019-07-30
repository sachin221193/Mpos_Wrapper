/**
 * 
 */
package com.vestige.wrapper.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.vestige.core.domain.Logs;
import com.vestige.core.exceptions.BadRequestAlertException;
import com.vestige.core.exceptions.ErrorConstants;
import com.vestige.core.model.ItemModel;
import com.vestige.core.model.dto.DistributorAddressDTO;
import com.vestige.core.model.dto.DistributorDTO;
import com.vestige.core.model.dto.PincodeDTO;
import com.vestige.core.utils.DateUtil;
import com.vestige.core.utils.SkuCodeUtils;
import com.vestige.core.utils.StringsUtil;
import com.vestige.soapapi.config.Constants;
import com.vestige.wrapper.config.MposUrlConstants;
import com.vestige.wrapper.config.WrapperConstants;
import com.vestige.wrapper.enums.ApiType;
import com.vestige.wrapper.model.DistributorConsistencyModel;
import com.vestige.wrapper.model.DistributorHieraracyModel;
import com.vestige.wrapper.model.DistributorTitleModel;
import com.vestige.wrapper.model.DistributorUploadedKycModel;
import com.vestige.wrapper.model.LoginModel;
import com.vestige.wrapper.model.OrderCancelModel;
import com.vestige.wrapper.model.OrderPaymentVM;
import com.vestige.wrapper.model.OrderType;
import com.vestige.wrapper.model.OrdersCheckoutDTO;
import com.vestige.wrapper.model.OrdersCheckoutDTO.OrderDetails;
import com.vestige.wrapper.model.OrdersCheckoutDTO.PaymentDetails;
import com.vestige.wrapper.model.OrdersCheckoutDTO.ProductDetails;
import com.vestige.wrapper.model.OrdersCheckoutDTO.SubOrderDetails;
import com.vestige.wrapper.model.OrdersCheckoutDTO.VoucherDetails;
import com.vestige.wrapper.model.PasswordModel;
import com.vestige.wrapper.model.PasswordResponseModel;
import com.vestige.wrapper.model.PaymentType;
import com.vestige.wrapper.model.PositionModel;
import com.vestige.wrapper.model.ProductBrandsModel;
import com.vestige.wrapper.model.ProductDetailResponse;
import com.vestige.wrapper.model.StoreModel;
import com.vestige.wrapper.model.StoresDTO;
import com.vestige.wrapper.model.TopSellingProductModel;
import com.vestige.wrapper.model.UplineDistributorModel;
import com.vestige.wrapper.model.VestigeShopAddress;
import com.vestige.wrapper.model.VoucherType;
import com.vestige.wrapper.repository.LogsRepository;
import com.vestige.wrapper.service.MPOSService;

/**
 * 
 * @author ashutosh.sharma
 *
 */
@Service
public class MPOSServiceImpl implements MPOSService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value(value = "${vestige.mpos.host}")
	private String host;

	@Value(value = "${vestige.vbd.host}")
	private String vdbHost;

	@Value(value = "${brand.image.url}")
	private String brandImageUrl;

	@Value(value = "${vestige.mpos.url}")
	private String kycBaseUrl;

	@Value(value = "${vestige.mpos.kyc.upload}")
	private String kycUploadUrl;

	@Autowired
	private LogsRepository logsRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Value(value = "${vestige.mpos.consistency.host}")
	private String consistencyHost;

	@Value(value = "${vestige.kyc.upload}")
	private String tempFolder;

	@Value(value = "${vestige.kyc.host}")
	private String fileUploadHost;

	@Value(value = "${product.image.url}")
	private String productImageUrl;

	private static final String UT = "ut";

	private static final String USERNAME = "username";

	private static final String STATIC_EMAIL = "nomail@myvestige.com";

	private static final String KELLTON = "kellton ";
	
	private static final String KELLTON_APP_REGISTRATION = "KAR";

	private static final String JSON_ARRAY_CLASS_NAME = "org.json.JSONArray";
	
	private static final String JSON_OBJECT_CLASS_NAME = "org.json.JSONObject";

	private static final String STRING_CLASS_NAME = "java.lang.String";
	
	/****
	 * @description to get distributor login
	 * @param userName
	 * @param password
	 * @return logindetails
	 * 
	 */

	@Override
	public LoginModel getLogin(String userName, String password, String ip) {
		logger.info("enter in getLogin>>>>>>>>> {}", ip);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.AUTHENTICATE_USER);
		params.add(USERNAME, userName);
		params.add("password", password);
		params.add("locationId", Constants.LOCATION);
		params.add("typeOfPOS", Constants.TYPE_OF_POS);
		params.add(WrapperConstants.SOURCE, Constants.MOBILE);
		params.add(UT, "");
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.LOGIN_CALLBACK).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.LOGIN_CALLBACK, ip);
		JSONObject body = getJSONObject(response.getBody());
		
		checkResponseType(body, JSON_OBJECT_CLASS_NAME);
				
		JSONObject data = body.getJSONObject(Constants.RESULT);
		LoginModel loginDetails = new LoginModel();
		if (!userName.startsWith("T")) {
			loginDetails.setUserId(String.valueOf(data.get("DistributorId")));
			loginDetails.setFirstName(String.valueOf(data.get("DistributorFullName")));
			loginDetails.setUserToken(String.valueOf(data.get("UserToken")));
			loginDetails.setMobileNo(String.valueOf(data.get("DistributorMobNumber")));
			/**
			 * Parse User Level
			 */
			if(data.has("PermLevelId")) {
				loginDetails.setUserLevelID(Integer.parseInt(data.get("PermLevelId").toString()));
			}
			loginDetails
					.setEmailId(StringsUtil.isNullOrEmpty(String.valueOf(data.get("DistributorEMailID"))) ? STATIC_EMAIL
							: String.valueOf(data.get("DistributorEMailID")));
		} else {
			loginDetails.setUserId(String.valueOf(data.get("TrainerId")));
			loginDetails.setFirstName(String.valueOf(data.get("TrainerName")));
		}
		List<String> userRole = new ArrayList<>();
		data.getJSONArray("userRole").forEach(role -> {
			userRole.add(String.valueOf(role));
		});
		loginDetails.setRoles(userRole);
		return loginDetails;
	}
	
	private void checkApiStatus(String body, Object request, String apiName, String ip) {
		saveLog(body, request, apiName, ip);
		JSONObject details = getJSONObject(body);
		if (WrapperConstants.STATUS_CODE_0.equals(details.get(WrapperConstants.STATUS)))
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request", body.getBytes(), null);
		if (!Objects.isNull(details.get(Constants.RESULT))
				&& ("[]".equals(String.valueOf(details.get(Constants.RESULT)))
						|| StringsUtil.isNullOrEmpty(String.valueOf(details.get(Constants.RESULT))))) {
			body = "{\"Status\":1,\"Description\":\"No data found.\",\"Result\":[],\"ResponseCode\":\"200\"}";
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No data found.", body.getBytes(), null);
		}
	}

	private void saveLog( String response, Object request, String apiName, String ip ) {
		Logs log = new Logs();
		log.setApiName(apiName);
		log.setApiType(ApiType.REST.getApiName());
		log.setLog(response);
		log.setSystemIp(ip);
		log.setLogTime(Instant.now());
		log.setRequest(String.valueOf(request));
		logsRepository.save(log);
	}
	
	private RestTemplate getRestTemplate() {
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
//		clientHttpRequestFactory.setConnectionRequestTimeout(connectionRequestTimeout);
		clientHttpRequestFactory.setConnectTimeout(60000);
		clientHttpRequestFactory.setReadTimeout(60000);
		return new RestTemplate(clientHttpRequestFactory);
	}
	
	private JSONObject getJSONObject(String body) {
		return new JSONObject(new String(StringsUtil.removeUTF8Bom(body)).replace("ï»¿", "").replace("\n", ""));
	}
	/****
	 * @description to get the distributor address on the basis of pincode id is the
	 *              name of field and pincode is the value of id
	 * @param id
	 * @param pincode
	 * @exception error in getting exception
	 * @return addresses of the distributor
	 * 
	 */
	@Override
	public List<VestigeShopAddress> getAddress(String pinCode, String ip) {
		VestigeShopAddress shopAddress = null;
		List<VestigeShopAddress> addresses = new ArrayList<>();
		JSONObject address = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> parameter = new LinkedMultiValueMap<>();
		parameter.add(WrapperConstants.ID, Constants.ADDRESS_ID);
		parameter.add("Pincode", pinCode);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameter, headers);

		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.VESTIGE_SHOP).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.VESTIGE_SHOP, ip);
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		JSONArray data = body.getJSONArray(Constants.RESULT);
		for (int i = 0; i < data.length(); i++) {
			address = data.getJSONObject(i);
			shopAddress = new VestigeShopAddress();
			shopAddress.setPinCode(address.getString("PinCode"));
			shopAddress.setCountryId(address.getLong("CountryId"));
			shopAddress.setCountryName(address.getString("CountryName"));
			shopAddress.setStateName(address.getString("StateName"));
			shopAddress.setStateId(address.getLong("StateId"));
			shopAddress.setCityId(address.getLong("CityId"));
			shopAddress.setCityName(address.getString("CityName"));
			shopAddress.setZoneId(address.getLong("ZoneId"));
			shopAddress.setZoneCode(address.getString("ZoneCode"));
			shopAddress.setZoneName(address.getString("ZoneName"));
			shopAddress.setSubZoneId(address.getLong("SubZoneId"));
			shopAddress.setSubZoneName(address.getString("SubZoneName"));
			shopAddress.setAreaName(address.getString("AreaName"));
			shopAddress.setAreaId(address.getLong("AreaId"));
			addresses.add(shopAddress);
		}
		return addresses;
	}

	/***
	 * @description to get the distributor titles on the basis of ID
	 * @param id
	 * @return distributorTitles
	 */

	@Override
	public List<DistributorTitleModel> getDistributorTitles(String ip) {
		DistributorTitleModel distributorTitleModel = null;
		JSONObject distributorTitle = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add(WrapperConstants.ID, Constants.TITLES);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(param, headers);
		ResponseEntity<String> response = getRestTemplate().postForEntity(
				getRootURL().append(MposUrlConstants.COMMON_ACTION_CALLBACK).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.COMMON_ACTION_CALLBACK, ip);
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		JSONArray data = body.getJSONArray(Constants.RESULT);
		
		List<DistributorTitleModel> distributorTitles = new ArrayList<>();
		for (int i = 0; i < data.length(); i++) {
			distributorTitle = data.getJSONObject(i);
			distributorTitleModel = new DistributorTitleModel();
			distributorTitleModel.setDistributorTitle(distributorTitle.getString("DistributorTitle"));
			distributorTitleModel.setDistributorTitleId(distributorTitle.getLong("DistributorTitleId"));
			distributorTitles.add(distributorTitleModel);
		}
		return distributorTitles;
	}

	/****
	 * @description vestige to check upline distributor
	 * @param id
	 * @param upLineNumber
	 * @return uplineDistributors
	 * 
	 */

	@Override
	public List<UplineDistributorModel> checkUplineDistributor(String upLineNumber, String ip) {
		UplineDistributorModel distributorModel = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, WrapperConstants.ID_CODE_3);
		params.add(WrapperConstants.UPLINE_NO, upLineNumber);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate().postForEntity(
				getRootURL().append(MposUrlConstants.POS_DISTRIBUTOR_REGISTRATION).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.POS_DISTRIBUTOR_REGISTRATION, ip );
		List<UplineDistributorModel> uplineDistributors = new LinkedList<>();
		
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		JSONObject distributor = body.getJSONArray(Constants.RESULT).getJSONObject(0);
		
		distributorModel = new UplineDistributorModel();
		distributorModel.setDistributorFirstName(distributor.getString("DistributorFirstName"));
		distributorModel.setAllInvoiceAmountSum(distributor.getString("AllInvoiceAmountSum"));
		distributorModel.setDistributorLastName(distributor.getString("DistributorLastName"));
		distributorModel.setDistributorAddress1(distributor.getString("DistributorAddress1"));
		distributorModel.setDistributorAddress2(distributor.getString("DistributorAddress2"));
		distributorModel.setDistributorAddress3(distributor.getString("DistributorAddress3"));
		distributorModel.setCityName(distributor.getString("CityName"));
		distributorModel.setStateName(distributor.getString("StateName"));
		uplineDistributors.add(distributorModel);

		return uplineDistributors;
	}

	/***
	 * @description to check the distributor
	 * @param id
	 * @param distributor
	 * @return distributors
	 */
	@Override
	public String checkDistributor(String distributor, String ip) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.CHECK_DISTRIBUTOR);
		params.add(Constants.CHECK_DISTRIBUTOR_DISTRIBUTOR_ID, distributor);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate().postForEntity(
				getRootURL().append(MposUrlConstants.POS_DISTRIBUTOR_REGISTRATION).toString(), request, String.class);
		checkApiStatus( response.getBody(), request, MposUrlConstants.POS_DISTRIBUTOR_REGISTRATION, ip );
		
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		
		return String.valueOf(body.getJSONArray(Constants.RESULT).getJSONObject(0));
	}

	/***
	 * @description to save the distributor
	 * @param distributorRegistrationModel
	 * @return distributionRegistration
	 */

	@Override
	public DistributorDTO saveDistributor(DistributorDTO distDTO, String ip) {
		if (!StringsUtil.isNullOrEmpty(distDTO.getTitle())) {
			distDTO.setTitle("1");
		}
		if (!StringsUtil.isNullOrEmpty(distDTO.getCoDistributorTitle())) {
			distDTO.setCoDistributorTitle("1");
		}
		if (StringsUtil.isNullOrEmpty(distDTO.getLastName())) {
			distDTO.setLastName("");
		}
		DistributorAddressDTO address = distDTO.getDistributorsAddress().stream().filter(ad -> ad.getIsDefault())
				.collect(Collectors.toList()).get(0);

		String formData = "distributor_no=" + distDTO.getDistributorId() + "&serial_no=&pv_month="
				+ DateUtil.getMMYYYY(new Date()) + "&upline_no=" + distDTO.getUplineDistributorId() + "&firstname="
				+ distDTO.getFirstName() + "&lastname=" + distDTO.getLastName() + "&title=" + distDTO.getTitle()
				+ "&distributor_firstname=" + distDTO.getFirstName() + "&distributor_lastname=" + distDTO.getLastName()
				+ "&distributor_dob=" + DateUtil.formatDob(distDTO.getDob()) + "&co_title="
				+ distDTO.getCoDistributorTitle() + "&co_distributor_firstname=&co_distributor_lastname=&"
				+ "co_distributor_dob=&address1=" + address.getAddress1() + "&address2=&address3=&phone=&mobile="
				+ distDTO.getMobileNumber() + "&email=" + distDTO.getEmailId() + "&pin="
				+ address.getPincode().getPincode() + "&country=" + address.getCountryId() + "&state="
				+ address.getStateId() + "&city=" + address.getCityId() + "&Zone="
				+ address.getZone().getZoneId() + "&Pan=&IFSCCode=&AccountNumber="
				+ "&panUploadedDocument=&addressUploadedDocument=&cancelledChequeUploadedDocument="
				+ (!StringsUtil.isNullOrEmpty(distDTO.getReferralCode())
						? KELLTON_APP_REGISTRATION + "|" + distDTO.getReferralCode()
						: KELLTON_APP_REGISTRATION);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> parameter = new LinkedMultiValueMap<>();
		parameter.add("form_data", formData);
		parameter.add(WrapperConstants.ID, Constants.DISTRIBUTOR_FORM_DATA_ID);
		parameter.add("functionName", Constants.DISTRIBUTOR_FUNCTION_NAME);
		parameter.add("moduleCode", Constants.DISTRIBUTOR_MODULE_CODE);
		parameter.add("forSkinCareItem", "");
		parameter.add("bankId", Constants.DISTRIBUTOR_BANK_ID);
		parameter.add("distributorPassword", distDTO.getPassword());
		parameter.add(WrapperConstants.SOURCE, Constants.DISTRIBUTOR_MOBILE);
		parameter.add(UT, "");
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameter, headers);

		ResponseEntity<String> response = getRestTemplate().postForEntity(
				getRootURL().append(MposUrlConstants.POS_DISTRIBUTOR_REGISTRATION).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.POS_DISTRIBUTOR_REGISTRATION, ip);
		
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		
		JSONObject distributorInfo = body.getJSONArray(Constants.RESULT).getJSONObject(0);
		distDTO.setDistributorId(distributorInfo.getLong("DistributorId"));
		return distDTO;
	}

	private StringBuilder getRootURL() {
		return new StringBuilder("http://").append(host);
	}

	private StringBuilder getRootVBDURL() {
		return new StringBuilder("http://").append(vdbHost);
	}

	private StringBuilder getkycBaseUrl() {
		return new StringBuilder("http://").append(kycBaseUrl);
	}

	/***
	 * @description to get the position of the director
	 * @param distributorId
	 * @return model
	 */
	@Override
	public PositionModel getPositionOfDistributor(String distributorId, String ip) {
		PositionModel positionModel = new PositionModel();
		JSONObject distributor = new JSONObject(checkDistributor(distributorId, ip));
		List<DistributorHieraracyModel> hieararchies = getAllHeirarchyLevel();
		int currentPosition = Integer.parseInt(String.valueOf(distributor.get("DistributorLevelId")));
		int previousPosition = currentPosition - 1 == -1 ? 0 : currentPosition - 1;
		int maxPosition = hieararchies.get(hieararchies.size() - 1).getLevelId();
		int nextPosition = currentPosition + 1 > maxPosition ? currentPosition : currentPosition + 1;
		for (DistributorHieraracyModel distributorHieraracyModel : getAllHeirarchyLevel()) {
			if (distributorHieraracyModel.getLevelId() == currentPosition) {
				positionModel.setCurrentPosition(distributorHieraracyModel.getLevelName());
			} else if (distributorHieraracyModel.getLevelId() == previousPosition) {
				positionModel.setPreviousPosition(distributorHieraracyModel.getLevelName());
			} else if (distributorHieraracyModel.getLevelId() == nextPosition) {
				positionModel.setNextPosition(distributorHieraracyModel.getLevelName());
			}
		}
		return positionModel;
	}

	private List<DistributorHieraracyModel> getAllHeirarchyLevel() {
		DistributorHieraracyModel distributorHieraracyModel = null;
		JSONObject hieraracyLevel = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.HEIRARCHY_ID);
		List<DistributorHieraracyModel> distHeirarchyList = new ArrayList<>();
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.KELLTONTECH_API).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.KELLTONTECH_API, null );
		
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		
		JSONArray data = body.getJSONArray(Constants.RESULT);
		
		for (int i = 0; i < data.length(); i++) {
			hieraracyLevel = data.getJSONObject(i);
			distributorHieraracyModel = new DistributorHieraracyModel();
			distributorHieraracyModel.setLevelId(hieraracyLevel.getInt("LevelId"));
			distributorHieraracyModel.setLevelName(hieraracyLevel.getString("LevelName"));
			distHeirarchyList.add(distributorHieraracyModel);
		}
		return distHeirarchyList;
	}

	/**
	 * @description to get inventories list
	 * @param locationId
	 * @param ip
	 * @param sku
	 */
	@Override
	public List<ItemModel> getAllInventories(String locationId, String ip, String sku) {
		List<ItemModel> itemList = new ArrayList<>();
		ItemModel item = null;
		JSONObject itemJson = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.GET_INVENTORY);
		params.add("Locationid", locationId);
		params.add("sku", SkuCodeUtils.formatSku(sku));
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.VESTIGE_SHOP).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.VESTIGE_SHOP, ip );
		
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		
		JSONArray data = body.getJSONArray(Constants.RESULT);
		for (int i = 0; i < data.length(); i++) {
			itemJson = data.getJSONObject(i);
			item = new ItemModel();
			item.setAlternativeCode(itemJson.getString("AlternativeCode"));
			item.setAlternativeCodeQuantity(itemJson.getInt("AlternativeCodeQty"));
			item.setAvailableQuantity(itemJson.getInt("AvailableQuantity"));
			item.setItemCode(itemJson.getString("itemcode"));
			item.setItemId(itemJson.getInt("ItemId"));
			itemList.add(item);
		}
		return itemList;
	}

	@Override
	public String saveKyc(String distributorId, MultipartFile file1, MultipartFile file2, String ip,
			Integer docId, String docNumber) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.KYC_SAVE_ID);
		params.add("Distributorid", distributorId);
		params.add("DocNo", docNumber);
		params.add("DocType", docId);
		params.add("validationDate", DateUtil.getTodayformattedDate());
		params.add(WrapperConstants.SOURCE, KELLTON + Constants.MOBILE);
		FileOutputStream fo = null;
		String uploadFileName2 = null;
		String uploadFileName1 = tempFolder + File.separator + distributorId + File.separator
				+ file1.getOriginalFilename();
		File imagefolder = new File(tempFolder);
		if (!imagefolder.exists()) {
			imagefolder.mkdir();
		}
		File distributorFolder = new File(tempFolder + File.separator + distributorId);
		if (!distributorFolder.exists()) {
			distributorFolder.mkdir();
		}
		fo = new FileOutputStream(uploadFileName1);
		fo.write(file1.getBytes());
		fo.close();
		params.add("UploadedImage", new FileSystemResource(uploadFileName1));
		if (!Objects.isNull(file2)) {
			uploadFileName2 = tempFolder + File.separator + distributorId + File.separator
					+ file2.getOriginalFilename();
			imagefolder = new File(tempFolder);
			if (!imagefolder.exists()) {
				imagefolder.mkdir();
			}
			distributorFolder = new File(tempFolder + File.separator + distributorId);
			if (!distributorFolder.exists()) {
				distributorFolder.mkdir();
			}
			fo = new FileOutputStream(uploadFileName2);
			fo.write(file2.getBytes());
			fo.close();
			params.add("UploadedImage2", new FileSystemResource(uploadFileName2));
		}
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(params, headers);
		uploadKyc(uploadFileName1, uploadFileName2, distributorId, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.KYC_CALL_BACK).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.KYC_CALL_BACK, ip );
		
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		
		Files.deleteIfExists(Paths.get(uploadFileName1));
		if (!StringsUtil.isNullOrEmpty(uploadFileName2)) {
			Files.deleteIfExists(Paths.get(uploadFileName2));
		}
//		Files.deleteIfExists(Paths.get(tempFolder + File.separator + distributorId));
//		Files.deleteIfExists(Paths.get(tempFolder));
		
		return String.valueOf(body.getJSONArray(Constants.RESULT).getJSONObject(0).get("status"));
	}

	private void uploadKyc(String uploadFileName1, String uploadFileName2, String distributorId, HttpHeaders headers) {
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.KYC_UPLOAD_ID);
		params.add("Distributorid", "K_".concat(distributorId));
		params.add("UploadedImage", new FileSystemResource(uploadFileName1));
		if (!StringsUtil.isNullOrEmpty(uploadFileName2)) {
			params.add("UploadedImage2", new FileSystemResource(uploadFileName2));
		}
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getkycBaseUrl().append(kycUploadUrl).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.KYC_SAVE, null);
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, STRING_CLASS_NAME);
	}

	@Override
	public List<StoreModel> getStores(String ip) {
		List<StoreModel> stores = new ArrayList<>();
		StoreModel store = null;
		JSONObject storeJson = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.GET_LOCATIONS);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.KELLTONTECH_API).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.KELLTONTECH_API, ip);
		
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		
		JSONArray data = body.getJSONArray(Constants.RESULT);
		for (int i = 0; i < data.length(); i++) {
			storeJson = data.getJSONObject(i);
			store = new StoreModel();
			store.setLocationId(storeJson.getInt("LocationId"));
			store.setLocationCode(storeJson.getString("LocationCode"));
			store.setLocationName(storeJson.getString("LocationName"));
			store.setType(storeJson.getString("Type"));
			store.setCityName(storeJson.getString("CityName"));
			store.setStateName(storeJson.getString("StateName"));
			store.setPincode(storeJson.getString("Pincode"));
			store.setContactNo(storeJson.getString("Contact No"));
			store.setContactName(storeJson.getString("Person Name"));
			store.setOrderCaterLocation("YES".equalsIgnoreCase(storeJson.getString("Order Cater Location")));
			store.setIsActive(storeJson.getString("isActive").equals("1")? Boolean.TRUE : Boolean.FALSE );
			store.setLatitude(storeJson.getDouble("latitude"));
			store.setLongitude(storeJson.getDouble("longitude"));
			stores.add(store);
		}
		return stores;
	}

	@Override
	public List<String> getRandomDistributorId(String ip) {
		List<String> distributorIds = new LinkedList<>();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

		params.add(WrapperConstants.ID, Constants.GENERATE_RAND_ID);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.MOBILE_POS_API).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.MOBILE_POS_API, ip );
		
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		
		JSONArray data = body.getJSONArray(Constants.RESULT);
		for (int i = 0; i < data.length(); i++) {
			distributorIds.add(data.getJSONObject(i).getString("distributorid"));
		}
		return distributorIds;
	}

	/**
	 * @description to get list of products (token free API)
	 * @param locationId
	 * @param ip
	 * @return List of products
	 */

	@Override
	public String getProductListByLocationId(String locationId, String ip) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, "getItemsForLocation");
		params.add("locationId", locationId);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate().postForEntity(
				getRootURL().append(MposUrlConstants.STOCK_COUNT_CALL_BACK).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.STOCK_COUNT_CALL_BACK, ip );
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		return String.valueOf(body.getJSONArray(Constants.RESULT));
	}

	/****
	 * @description get History of the point
	 * @param distributorId
	 * @return get 6 month point detail
	 */
	@Override
	public String getHistory(String distributorId, String ip) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.DISTRIBUTOR_PV_DETAIL);
		params.add("distributorId", distributorId);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.KELLTONTECH_API).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.KELLTONTECH_API, null);
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		return String.valueOf(body.getJSONArray(Constants.RESULT));
	}

	@Override
	public List<DistributorUploadedKycModel> getDistributorUploadedKyc(String distributorId, String ip) {
		List<DistributorUploadedKycModel> kycDocs = null;
		DistributorUploadedKycModel document = null;
		JSONObject documentJSON = null;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.SEARCH_DISTRIBUTOR_ID);
		params.add("Distributorid", distributorId);
		params.add("docNo", null);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.GET_KYC_UPLOADS).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.GET_KYC_UPLOADS, ip );
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		JSONArray data = body.getJSONArray(Constants.RESULT);
		kycDocs = new ArrayList<>();
		for (int i = 0; i < data.length(); i++) {
			documentJSON = data.getJSONObject(i);
			document = new DistributorUploadedKycModel();
			document.setName(String.valueOf(documentJSON.get("Name")));
			document.setDistributorName(String.valueOf(documentJSON.get("distributorName")));
			document.setDistributorid(documentJSON.getLong("Distributorid"));
			document.setDocNo(String.valueOf(documentJSON.get("DocNo")));
			document.setDocExpdate(String.valueOf(documentJSON.get("DocExpdate")));
			document.setDocType(String.valueOf(documentJSON.get("DocType")));
			document.setImageName(String.valueOf(documentJSON.get("ImageName")));
			// document.setImageName2(documentJSON.getString("ImageName2"));
			kycDocs.add(document);
		}
		return kycDocs;
	}

	@Override
	public DistributorConsistencyModel getDistributorConsistency(String distributorId, String ip) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.DISTRIBUTOR_CNC);
		params.add("distributorId", distributorId);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.KELLTONTECH_API).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.KELLTONTECH_API, ip );
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		JSONObject details = body.getJSONArray(Constants.RESULT).getJSONObject(0);
		DistributorConsistencyModel distributorConsistency = new DistributorConsistencyModel();
		distributorConsistency.setDistributorId(details.getLong("distributorid"));
		distributorConsistency.setBusinessDate(details.getString("monthindd"));
		distributorConsistency.setBusinessMonth(details.getString("businessmonth"));
		distributorConsistency.setBusinessYear(details.getString("year"));
		distributorConsistency.setCnc1(details.getString("CNC1"));
		distributorConsistency.setCnc2(details.getString("CNC2"));
		distributorConsistency.setCnc3(details.getString("CNC3"));
		distributorConsistency.setCnc4(details.getString("CNC4"));
		distributorConsistency.setInvoiceCount(Integer.valueOf(details.getString("invoice_count")));
		distributorConsistency.setCurrentMonthInvoices(String.valueOf(details.get("CurrentMonthInvoices")));

		return distributorConsistency;
	}

	/*****
	 * @description get top selling product on the basis of there locationId
	 * @param location id
	 * @return top selling product List
	 */
	@Override
	public List<TopSellingProductModel> getTopSellingProduct(String locationId, String ip) {
		List<TopSellingProductModel> topProductList = new ArrayList<>();
		TopSellingProductModel topProductModel = null;
		JSONObject topProductJson = null;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.TOP_SELLING_PRODUCT);
		params.add("locationId", locationId);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate().postForEntity(
				getRootURL().append(MposUrlConstants.KELLTONTECH_API).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.KELLTONTECH_API, ip );
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		JSONArray data = body.getJSONArray(Constants.RESULT);
		for (int i = 0; i < data.length(); i++) {
			topProductJson = data.getJSONObject(i);
			topProductModel = new TopSellingProductModel();
			topProductModel.setItemId(Integer.valueOf(String.valueOf(topProductJson.get("ItemID"))));
			topProductModel.setItemName(String.valueOf(topProductJson.get("ItemName")));
			topProductModel.setMposSubCategory(String.valueOf("MPOS_SubCat"));
			topProductModel.setNewDisplayCategoryName("Personal Care");
			topProductModel.setQuantity(Double.valueOf(String.valueOf(topProductJson.get("Quantity"))));
			topProductList.add(topProductModel);
		}
		return topProductList;
	}

	/**
	 * 
	 * get product branch for VBD
	 */
	@Override
	public List<ProductBrandsModel> getProductBrands(String ip) {

		List<ProductBrandsModel> brands = new ArrayList<>();
		ProductBrandsModel brand = null;
		JSONObject brandJson = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.FETCH_PRODUCT_BRAND);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate().postForEntity(
				getRootURL().append(MposUrlConstants.KELLTONTECH_API).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.KELLTONTECH_API, ip);
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		JSONArray data = body.getJSONArray(Constants.RESULT);
		for (int i = 0; i < data.length(); i++) {
			brandJson = data.getJSONObject(i);
			brand = new ProductBrandsModel();
			brand.setId(Integer.valueOf(String.valueOf(brandJson.get("BrandId"))));
			brand.setName(String.valueOf(brandJson.get("BrandName")));
			brand.setImage(brandImageUrl + "/" + String.valueOf(brandJson.get("BrandImage")));
			brand.setStatus(String.valueOf(brandJson.get("Status")));
			brands.add(brand);
		}
		
		return brands;
	}

	/*****
	 * @description to change the password
	 * @param distributorId, oldPassword, newPassword, ip
	 * @return success response
	 */
	@Override
	public PasswordResponseModel changePassword(PasswordModel passwordModel, String ip) {
		PasswordResponseModel responseModel = new PasswordResponseModel();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.CHANGE_DISTRIBUTOR_PASSWORD);
		params.add("distributorId", passwordModel.getDistributorId());
		params.add("oldPassword", passwordModel.getOldPassword());
		params.add("newPassword", passwordModel.getNewPassword());
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.MOBILE_POS_API).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.MOBILE_POS_API, ip );
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		JSONObject details = body.getJSONArray(Constants.RESULT).getJSONObject(0);
		responseModel.setStatus(details.getString("status"));
		responseModel.setStatusCode(details.getString("status_code"));
		return responseModel;
	}

	/*****
	 * @description to get VBD Stores
	 * @param state, ip
	 * @return success response
	 */
	public String getVBDStores(String state, String ip) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("api_user", Constants.API_USER);
		params.add("api_key", Constants.API_KEY);
		params.add(Constants.STATE, state);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootVBDURL().append(MposUrlConstants.VBD_STORES).toString(), request, String.class);
		saveLog(response.getBody(), request, MposUrlConstants.VBD_STORES, ip);
		return response.getBody();
	}

	@Override
	public String getProductListByPinCode(String pincode, String ip) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.GET_ITEMS_FOR_PINCODE);
		params.add("pincode", pincode);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate().postForEntity(
				getRootURL().append(MposUrlConstants.KELLTONTECH_API).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.KELLTONTECH_API, ip );
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		return String.valueOf(body.getJSONArray(Constants.RESULT));
	}

	/***
	 * 
	 * @description to create log generate
	 * @param distributorId, locationId
	 * @return String.valueOf(logGenerateInfo)
	 */
	@Override
	public String getStorePickupLog(String distributorId, String locationId, String ip, String ut) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.GENERATE_SELF_LOG);
		params.add("locationId", locationId);
		params.add("distributorId", distributorId);
		params.add("orderMode", "2");
		params.add(UT, ut);
		params.add(WrapperConstants.SOURCE, "mobile");
		params.add(USERNAME, distributorId);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.POS_CLIENT).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.POS_CLIENT, ip );
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		return String.valueOf(body.getJSONArray(Constants.RESULT).getJSONObject(0));
	}

	/***
	 * @description to create log generate with generate
	 * @param distDTO, locationId
	 * @return orderResponse
	 */
	@Override
	public String getHomeDeliveryLog(DistributorAddressDTO addressDTO, String ip, String ut) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		PincodeDTO pincode = addressDTO.getPincode();

		String formData = "GetAddressAddress1=" + (addressDTO.getAddress1() != null ? addressDTO.getAddress1() : "")
				+ "&GetAddressAddress2=" + (addressDTO.getAddress2() != null ? addressDTO.getAddress2() : "")
				+ "&GetAddressAddress3=" + (addressDTO.getAddress3() != null ? addressDTO.getAddress3() : "")
				+ "&GetAddressAddress4=" + (addressDTO.getAddress4() != null ? addressDTO.getAddress4() : "")
				+ "&GetAddressCountry="
				+ (addressDTO.getCountryId() != null ? String.valueOf(addressDTO.getCountryId()) : "")
				+ "&GetAddressState=" + (addressDTO.getStateId() != null ? String.valueOf(addressDTO.getStateId()) : "")
				+ "&GetAddressCity=" + (addressDTO.getCityId() != null ? String.valueOf(addressDTO.getCityId()) : "")
				+ "&GetAddressPinCode=" + (pincode.getPincode() != null ? pincode.getPincode() : "")
				+ "&GetAddressPhone1=" + (addressDTO.getContactNumber() != null ? addressDTO.getContactNumber() : "")
				+ "&GetAddressPhone2=" + (addressDTO.getContactNumber() != null ? addressDTO.getContactNumber() : "")
				+ "&GetAddressMobile1=" + (addressDTO.getContactNumber() != null ? addressDTO.getContactNumber() : "")
				+ "&GetAddressMobile2=" + (addressDTO.getContactNumber() != null ? addressDTO.getContactNumber() : "")
				+ "&GetAddressEmail1=" + (addressDTO.getEmail() != null ? addressDTO.getEmail() : "")
				+ "&GetAddressEmail2=" + (addressDTO.getEmail() != null ? addressDTO.getEmail() : "")
				+ "&GetAddressFax1=" + (addressDTO.getFax() != null ? addressDTO.getFax() : "") + "&GetAddressFax2="
				+ (addressDTO.getFax() != null ? addressDTO.getFax() : "") + "&GetAdderssWebsite=";

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.DELIVERY_ADDRESS_DATA);
		params.add("deliveryAddressFormData", formData);
		params.add("distributorId", String.valueOf(addressDTO.getDistributorId()));
		params.add("orderMode", "2");
		params.add(UT, ut);
		params.add(WrapperConstants.SOURCE, "mobile");
		params.add(USERNAME, String.valueOf(addressDTO.getDistributorId()));
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.POS_CLIENT).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.POS_CLIENT, ip );
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		return String.valueOf(body.getJSONArray(Constants.RESULT).getJSONObject(0));

	}

	/****
	 * @description to save the orders
	 * @param orderModel, ip, ut
	 * @return successResponse
	 */
	@Override
	public String saveOrder(OrdersCheckoutDTO orderModel, String ip, String ut) {
		String orderJson = null;
		String paymentJson = null;
		String formdata = null;
		Double amount = 0.0;
		Double totalPV = 0.0;
		Double totalBv = 0.0;
		Integer quantity = 0;
		//For which order is creating
		Long distibutorId = 0l;
		List<String> paymentJsonList = new ArrayList<>();
		List<String> orderJsonList = new ArrayList<>();
		if (Objects.nonNull(orderModel)) {
			for (OrderDetails orderDetails : orderModel.getOrders()) {
				distibutorId = orderDetails.getDistributorId();
				if (Objects.nonNull(orderDetails)) {
					amount = amount + Double.valueOf(orderDetails.getOrderAmount());
					for (SubOrderDetails subOrder : orderDetails.getSubOrders()) {
						if (Objects.nonNull(subOrder)) {
							for (ProductDetails productsDTO : subOrder.getProducts()) {
								totalPV = totalPV + (productsDTO.getPv() * productsDTO.getQuantity());
								totalBv = totalBv + (productsDTO.getBv() * productsDTO.getQuantity());
								quantity = quantity + productsDTO.getQuantity();
								if (Objects.nonNull(productsDTO)) {

									if (productsDTO.getIsGiftVoucher() == Boolean.TRUE
											&& !CollectionUtils.isEmpty(orderDetails.getVouchers())) {
										for (VoucherDetails voucher : orderDetails.getVouchers()) {
											if (Objects.nonNull(voucher) && voucher.getType()
													.equalsIgnoreCase(VoucherType.GIFT.getValue())) {
												Double total = productsDTO.getQuantity() * productsDTO.getUnitCost();
												orderJson = "{\"RowID\":\"" + productsDTO.getProductId()
														+ "\",\"RecordNo\":\"\",\"PV\":\"" + productsDTO.getPv()
														+ "\",\"DiscountPercent\":\"\","
														+ "\"DiscountAmount\":\"\",\"PromotionId\":\"\",\"PromotionType\":\"\",\"PromoDescription\":\"\","
														+ "\"PromotionParticipation\":\"\",\"MerchHierarchyDetailId\":\"\",\"GroupItemId\":\"\",\"BV\":\""
														+ productsDTO.getBv() + "\","
														+ "\"myac\":\"\",\"IS\":\"\",\"Qty\":\""
														+ productsDTO.getQuantity() + "\",\"Name\":\""
														+ productsDTO.getProductName() + "\",\"Price\":\"" + total
														+ "\"," + "\"IsPromo\":\"1\",\"DistributorPrice\":\"" + total
														+ "\",\"GiftVoucherNumber\":\"" + voucher.getCode()
														+ "\",\"IsEditableOrNot\":\"1\"," + "\"VoucherSrNo\":\""
														+ voucher.getNumber()
														+ "\",\"PromotionalItemTierFrom\":\"\",\"PromotionalItemTierTo\":\"\","
														+ "\"GiftVoucherMinAmt\":\"" + voucher.getAmount()
														+ "\",\"undefined\":\"\"}";
												orderJsonList.add(orderJson);
												break;
											}
										}
									} else if (productsDTO.getIsPromotional() == Boolean.TRUE
											&& !CollectionUtils.isEmpty(orderDetails.getVouchers())) {
										for (VoucherDetails voucher : orderDetails.getVouchers()) {
											if (Objects.nonNull(voucher) && voucher.getType()
													.equalsIgnoreCase(VoucherType.PROMOTIONAL.getValue())) {
												orderJson = "{\"RowID\":\"" + productsDTO.getProductId()
														+ "\",\"RecordNo\":\"\",\"PV\":\"" + productsDTO.getPv()
														+ "\",\"DiscountPercent\":\"\","
														+ "\"DiscountAmount\":\"\",\"PromotionId\":\""
														+ productsDTO.getPromotionalId()
														+ "\",\"PromotionType\":\"\",\"PromoDescription\":\"\","
														+ "\"PromotionParticipation\":\"\",\"MerchHierarchyDetailId\":\"\",\"GroupItemId\":\"\",\"BV\":\""
														+ productsDTO.getBv() + "\","
														+ "\"myac\":\"\",\"IS\":\"\",\"Qty\":\""
														+ productsDTO.getQuantity() + "\",\"Name\":\""
														+ productsDTO.getProductName() + "\",\"Price\":\""
														+ productsDTO.getTotal() + "\","
														+ "\"IsPromo\":\"1\",\"DistributorPrice\":\""
														+ productsDTO.getUnitCost() + "\",\"GiftVoucherNumber\":\""
														+ "\",\"IsEditableOrNot\":\"1\"," + "\"VoucherSrNo\":\""
														+ "\",\"PromotionalItemTierFrom\":\"\",\"PromotionalItemTierTo\":\"\","
														+ "\"GiftVoucherMinAmt\":\"" + "\",\"undefined\":\"\"}";
												orderJsonList.add(orderJson);
												break;
											}
										}
									} else {
										orderJson = "{\"RowID\":\"" + productsDTO.getProductId()
												+ "\",\"RecordNo\":\"\",\"PV\":\"" + productsDTO.getPv()
												+ "\",\"DiscountPercent\":\"\","
												+ "\"DiscountAmount\":\"\",\"PromotionId\":\"\",\"PromotionType\":\"\",\"PromoDescription\":\"\","
												+ "\"PromotionParticipation\":\"\",\"MerchHierarchyDetailId\":\"\",\"GroupItemId\":\"\",\"BV\":\""
												+ productsDTO.getBv() + "\"," + "\"myac\":\"\",\"IS\":\"\",\"Qty\":\""
												+ productsDTO.getQuantity() + "\",\"Name\":\""
												+ productsDTO.getProductName() + "\",\"Price\":\""
												+ productsDTO.getUnitCost() + "\","
												+ "\"IsPromo\":\"0\",\"DistributorPrice\":\"" + productsDTO.getTotal()
												+ "\",\"GiftVoucherNumber\":\"\",\"IsEditableOrNot\":\"1\","
												+ "\"VoucherSrNo\":\"\",\"PromotionalItemTierFrom\":\"\",\"PromotionalItemTierTo\":\"\","
												+ "\"GiftVoucherMinAmt\":\"\",\"undefined\":\"\"}";
										orderJsonList.add(orderJson);

									}

								} else {
									throw new BadRequestAlertException("productsDTO details error", "ordersCheckout",
											ErrorConstants.ERR_VALIDATION);
								}
							}
						} else {
							throw new BadRequestAlertException("subOrder details error", "ordersCheckout",
									ErrorConstants.ERR_VALIDATION);
						}
					}

					for (PaymentDetails paymentDetails : orderDetails.getPayment()) {
						if (Objects.nonNull(paymentDetails)) {
							if (PaymentType.CASH.getValue().equalsIgnoreCase(paymentDetails.getMethod())
									&& paymentDetails.getTotalAmount() > 0) {
								paymentJson = "{\"PaymentId\":\"1\",\"RecordNo\":\"1\",\"PaymentDescription\":\""
										+ paymentDetails.getMethod() + "\"," + "\"PaymentAmount\":\""
										+ paymentDetails.getTotalAmount()
										+ "\",\"RoundedPaymentAmount\":\"\",\"DBRoundedPaymentAmount\":\"\","
										+ "\"ForexAmount\":\"\",\"RoundedForexAmount\":\"\",\"DBRoundedForexAmount\":\"\","
										+ "\"CurrencyCode\":\"\",\"ExchangeRate\":\"\",\"RoundedExchangeRate\":\"\","
										+ "\"DBRoundedExchangeRate\":\"\",\"CardType\":\"\",\"ReceiptDisplay\":\"\","
										+ "\"CreditCardSuccessStatus\":\"\",\"ShowInPayment\":\"1\",\"ItemReceiptDisplay\":\"\","
										+ "\"VoucherId\":\"0\",\"TenderType\":\"1\",\"undefined\":\"\"}";
								paymentJsonList.add(paymentJson);

							} else if (paymentDetails.getMethod()
									.equalsIgnoreCase(PaymentType.BONUS_VOUCHER.getValue())) {
								paymentJson = "{\"PaymentId\":\"1\",\"RecordNo\":\"1\",\"PaymentDescription\":\""
										+ paymentDetails.getMethod() + "\"," + "\"PaymentAmount\":\""
										+ paymentDetails.getTotalAmount()
										+ "\",\"RoundedPaymentAmount\":\"\",\"DBRoundedPaymentAmount\":\"\","
										+ "\"ForexAmount\":\"\",\"RoundedForexAmount\":\"\",\"DBRoundedForexAmount\":\"\","
										+ "\"CurrencyCode\":\"\",\"ExchangeRate\":\"\",\"RoundedExchangeRate\":\"\","
										+ "\"DBRoundedExchangeRate\":\"\",\"CardType\":\"\",\"ReceiptDisplay\":\"\","
										+ "\"CreditCardSuccessStatus\":\"\",\"ShowInPayment\":\"1\",\"ItemReceiptDisplay\":\"\","
										+ "\"VoucherId\":\"" + paymentDetails.getVoucherId()
										+ "\",\"TenderType\":\"5\",\"undefined\":\"\"}";
								paymentJsonList.add(paymentJson);
							} else if (paymentDetails.getMethod().equalsIgnoreCase(PaymentType.ONLINE.getValue())) {
								paymentJson = "{\"PaymentId\":\"1\",\"RecordNo\":\"1\",\"PaymentDescription\":\""
										+ paymentDetails.getMethod() + "\"," + "\"PaymentAmount\":\""
										+ paymentDetails.getTotalAmount()
										+ "\",\"RoundedPaymentAmount\":\"\",\"DBRoundedPaymentAmount\":\"\","
										+ "\"ForexAmount\":\"\",\"RoundedForexAmount\":\"\",\"DBRoundedForexAmount\":\"\","
										+ "\"CurrencyCode\":\"\",\"ExchangeRate\":\"\",\"RoundedExchangeRate\":\"\","
										+ "\"DBRoundedExchangeRate\":\"\",\"CardType\":\"\",\"ReceiptDisplay\":\"\","
										+ "\"CreditCardSuccessStatus\":\"\",\"ShowInPayment\":\"1\",\"ItemReceiptDisplay\":\"\","
										+ "\"VoucherId\":\"0\",\"TenderType\":\"2\",\"undefined\":\"\"}";
								paymentJsonList.add(paymentJson);
							}
						} else {
							throw new BadRequestAlertException("paymentDetails error", "ordersCheckout",
									ErrorConstants.ERR_VALIDATION);
						}
					}

				} else {
					throw new BadRequestAlertException("orderDetails error", "ordersCheckout",
							ErrorConstants.ERR_VALIDATION);
				}
			}
		} else {
			throw new BadRequestAlertException("orderDetails error", "ordersCheckout", ErrorConstants.ERR_VALIDATION);
		}
		String orderParam = "" + orderJsonList;
		String paymentParam = "" + paymentJsonList;
		if (Objects.isNull(orderModel.getDeliveryType())) {
			throw new BadRequestAlertException("DeliveryType Should not be empty", "OrderCheckout",
					ErrorConstants.ERR_VALIDATION);
		} else if (orderModel.getDeliveryType().equalsIgnoreCase(OrderType.SHIPPING.getValue())) {
			DistributorAddressDTO distAddress = orderModel.getDistributorAddressDTO();
			formdata = "barcode_value=&item_quantity=" + quantity + "&changeAmount=0&payment_amount=" + amount
					+ "&delivery_mode=2&DeliverToPincode=&DeliverToCityId=" + "&DeliverToStateId="
					+ "&DeliverToCountryId=&DeliverToTelephone=" + "&DeliverToMobile=&DeliverToMobile="
					+ "&PUCOrderMode=0&self_consumption=1&consume=&stock_location_code=" + distAddress.getLocationCode()
					+ "&DeliverToPincode=&DeliverToCityId=" + "&DeliverFromStateId=&DeliverFromCountryId="
					+ "&DeliverFromTelephone=&DeliverFromMobile=&stock_id=&pc_id=" + distAddress.getLocationCode()
					+ "&bo_id=&location_id=" + "&total_pv=" + totalPV + "&TotalQty=" + quantity + "&total_bv=" + totalBv
					+ "&order_amount=" + amount + "&DistributorId=" + distibutorId
					+ "&OrderDispatchMode=3&AddressId=" + distAddress.getAddressId()
					+ "&OrderType=&CourierCharges=0&changedBoId=" + "&POSRemarks=&log_or_team_order="
					+ orderModel.getGroupOrderId();
		} else if (orderModel.getDeliveryType().equalsIgnoreCase(OrderType.STORE_PICKUP.getValue())) {
			StoresDTO storesDTO = orderModel.getStoresDTO();
			formdata = "barcode_value=&item_quantity=" + quantity + "&changeAmount=0&payment_amount=" + amount
					+ "&delivery_mode=1&DeliverToPincode=&DeliverToCityId=" + "&DeliverToStateId="
					+ "&DeliverToCountryId=&DeliverToTelephone=" + "&DeliverToMobile=&DeliverToMobile="
					+ "&PUCOrderMode=0&self_consumption=1&consume=&stock_location_code=" + storesDTO.getLocationCode()
					+ "&DeliverToPincode=&DeliverToCityId=" + "&DeliverFromStateId=&DeliverFromCountryId="
					+ "&DeliverFromTelephone=&DeliverFromMobile=&stock_id=&pc_id=" + storesDTO.getLocationId()
					+ "&bo_id=&location_id=" + "&total_pv=" + totalPV + "&TotalQty=" + quantity + "&total_bv=" + totalBv
					+ "&order_amount=" + amount + "&DistributorId=" + distibutorId
					+ "&OrderDispatchMode=&AddressId=&OrderType=&CourierCharges=0&changedBoId="
					+ "&POSRemarks=&log_or_team_order=" + orderModel.getGroupOrderId();
		} // pcid and stocklocation id will be same //order dispatch mode must
			// be 3 or empty string
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.POS_ORDER_SAVE);
		params.add("moduleCode", "POS01");
		params.add("functionName", "Confirm");
		params.add("order_json", orderParam);
		params.add("payment_json", paymentParam);
		params.add("form_data", formdata);
		params.add("posOrderNumber", "");
		params.add("creditCardPaymentOrNot", "0");
		params.add("changedBoid", "");
		params.add(UT, ut);
		params.add(WrapperConstants.SOURCE, "mobile");
		params.add(USERNAME, String.valueOf(orderModel.getDistributorId()));

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		logger.info("request = " + request);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.SAVE_ORDERS).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.SAVE_ORDERS, ip );
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		return String.valueOf(body.getJSONArray(Constants.RESULT).getJSONObject(0));
	}

	/***
	 * @description to get the gift voucher list
	 * @param distributorId, giftVoucherNumber
	 * @return list of gift voucher
	 */
	@Override
	public String getVoucherUseDetail(String distributorId, String giftVoucherNumber, String ip) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.GET_GIFT_VOUCHER_INFO);
		params.add("distributorId", distributorId);
		params.add("giftVoucherNo", giftVoucherNumber);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.POS_CLIENT).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.POS_CLIENT, ip );
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		return String.valueOf(body.getJSONArray(Constants.RESULT));

	}

	/***
	 * @param bonusChequeNo
	 * @param ip
	 * @return String
	 */
	@SuppressWarnings("deprecation")
	@Override
	public String getBonusVoucherStatusDetail(String bonusChequeNo, String ip) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(Constants.ID, Constants.GET_BONUS_CHEQUE_INFO);
		params.add("bonusChequeNo", URLDecoder.decode(bonusChequeNo));
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.POS_CLIENT).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.POS_CLIENT, ip );
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		return String.valueOf(body.getJSONArray(Constants.RESULT));
	}

	/***
	 * @description to cancel order
	 * @param orderModel, ip
	 * @return String.valueOf(orderInfo);
	 */
	@Override
	public String cancelOrder(OrderCancelModel cancelModel, String ip, String ut) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.POS_CANCEL_ORDER);
		params.add("moduleCode", "POS01");
		params.add("functionName", "Cancel");
		params.add(USERNAME, String.valueOf(cancelModel.getDistributorId()));
		params.add("source", "mobile");
		params.add(UT, ut);

		String formData = "barcode_value=&item_quantity=&changeAmount=0" + "&payment_amount=" + "&delivery_mode="
				+ "&DeliverToPincode=" + "&DeliverToCityId=" + "&DeliverToStateId=" + "&DeliverToCountryId="
				+ "&DeliverToTelephone=" + "&DeliverToMobile=" + "&DeliverToMobile=" + "&PUCOrderMode="
				+ "&self_consumption=&consume=&stock_location_code=" + "&DeliverToPincode=" + "&DeliverToCityId="
				+ "&DeliverFromStateId=" + "&DeliverFromCountryId=" + "&DeliverFromTelephone="
				+ "&DeliverFromMobile=&stock_id=&pc_id=&bo_id=&location_id=" + "&total_pv=" + "&TotalQty="
				+ "&total_bv=" + "&order_amount=&DistributorId=" + cancelModel.getDistributorId()
				+ "&OrderDispatchMode=&AddressId="
				+ "&OrderType=&CourierCharges=0&changedBoId=&POSRemarks=&log_or_team_order="
				+ cancelModel.getLogNumber();
		params.add("form_data", formData);
		params.add("orderNumber", cancelModel.getOrderId());

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.SAVE_ORDERS).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.SAVE_ORDERS, ip );
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		return String.valueOf(body.getJSONArray(Constants.RESULT).getJSONObject(0));
	}

	@Override
	public String getDistributorVoucherDetail(String distributorId, String ip) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.GET_DISTRIBUTOR_VOUCHER_DETAIL);
		params.add("distributorId", distributorId);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate().postForEntity(
				getRootURL().append(MposUrlConstants.KELLTONTECH_API).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.KELLTONTECH_API, ip );
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		return response.getBody();
	}

	/****
	 * @description Service for rendering My downloads
	 * @param countryId
	 * @param ip
	 * @return downloads file
	 */
	@Override
	public String renderingDownload(String countryId, String ip) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.GET_DOWNLOAD_DETAIL);
		params.add("countryId", countryId);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.KELLTONTECH_API).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.KELLTONTECH_API, ip );
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		return String.valueOf(body.getJSONArray(Constants.RESULT));
	}

	@Override
	public String getDistributorDesiredLevel(String id, String ip, String levelId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.GET_DISTRIBUTOR_DESIRED_LEVEL);
		params.add("distributorId", id);
		params.add("levelId", levelId);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate().postForEntity(
				getRootURL().append(MposUrlConstants.KELLTONTECH_API).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.KELLTONTECH_API, ip );
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		return String.valueOf(body.getJSONArray(Constants.RESULT).getJSONObject(0));
	}

	/****
	 * @description get distributor desired percentage
	 * @param desiredPercentage
	 * @param                   ip, id
	 * @return downloads file
	 */
	@Override
	public String getDistributorDesiredPercentage(String id, String ip, String desiredPercentage) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.GET_DISTRIBUTOR_DESIRED_PERCENTAGE);
		params.add("distributorId", id);
		params.add("desirePercent", desiredPercentage);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.KELLTONTECH_API).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.KELLTONTECH_API, ip);
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		return String.valueOf(body.getJSONArray(Constants.RESULT).getJSONObject(0));
	}

	@Override
	public String getLastSixMonthQualifyInvoiceData(String distributorId, String ip) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.GET_CNC_QUALIFY_INVOICE);
		params.add("distributorId", distributorId);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate().postForEntity(
				getRootURL().append(MposUrlConstants.KELLTONTECH_API).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.KELLTONTECH_API, ip );
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		return response.getBody();
	}

	/*****
	 * @description Order Track detail API
	 * @param logNumber
	 * @return track details
	 */
	@Override
	public String trackOrder(String logNumber, String distributorId, String ip, String ut) {
		if (logNumber.equals("%25")) {
			logNumber = "%";
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.MY_ACCOUNT_LOOKUP_DATA);
		params.add("formData", "");
		params.add("logNo", logNumber);
		params.add(USERNAME, distributorId);
		params.add("source", "mobile");
		params.add(UT, ut);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.LOOKUP_CALLBACK).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.LOOKUP_CALLBACK, ip );
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		return String.valueOf(body.getJSONArray(Constants.RESULT));
	}

	@Override
	public String getCourierChargesStatus(Long distributorId, String logsId, String ip, String ut) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.PAY_MULTIPLE_LOGS);
		params.add("logsToBePaid", logsId);
		params.add(UT, ut);
		params.add(WrapperConstants.SOURCE, "mobile");
		params.add(USERNAME, String.valueOf(distributorId));
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.POS_CLIENT).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.POS_CLIENT, ip );
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		return String.valueOf(getJSONObject(response.getBody()).getJSONArray(Constants.RESULT).getJSONObject(0));
	}

	@Override
	public List<String> getDistributorCarHomeAndTripWinners(String id, String ip, String fundType) {
		Logs log = new Logs();
		List<String> achievers = new ArrayList<>();
		log.setSystemIp(ip);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.CAR_HOME_TRIP_FUND_ACHIEVERS);
		params.add("distributorId", id);
		params.add("type", fundType);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.KELLTONTECH_API).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.KELLTONTECH_API, ip );
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		JSONArray data = body.getJSONArray(Constants.RESULT);
		for (int i = 0; i < data.length(); i++) {
			achievers.add(data.getJSONObject(i).getString("Name"));
		}
		return achievers;
	}

	/*****
	 * @description Downline count API
	 * @param distributorId
	 * @return Downline count
	 */
	@Override
	public Integer getDownlineCount(String distributorId, String ip) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(Constants.ID, Constants.GET_DISTRIBUTOR_BONUS_DETAIL);
		params.add(Constants.DISTRIBUTOR_ID_S, distributorId);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.KELLTONTECH_API).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.KELLTONTECH_API, ip);
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		JSONObject data = body.getJSONArray(Constants.RESULT).getJSONObject(0);
		return StringsUtil.isNullOrEmpty(String.valueOf(data.get("DownlineCount"))) ? 0
				: Integer.valueOf(String.valueOf(data.get("DownlineCount")));
	}

	/**
	 * get Payment Updation Status
	 * 
	 * @param ordersToBePaid
	 * @param payableOrderList
	 * 
	 * @return String
	 */
	@Override
	public String getPaymentUpdationStatus(String ordersToBePaid, String payableOrderList, String ip, String ut,
			Long distributorId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.PAY_MULTIPLE_ORDERS);
		params.add("ordersToBePaid", ordersToBePaid);
		params.add("payAbleOrderList", payableOrderList);
		params.add(UT, ut);
		params.add(WrapperConstants.SOURCE, "mobile");
		params.add(USERNAME, String.valueOf(distributorId));
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.POS_CLIENT).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.POS_CLIENT, ip);
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		return String.valueOf(body.getJSONArray(Constants.RESULT).getJSONObject(0));
	}

	/**
	 * get KYC Status
	 * 
	 * @param distributorId
	 * @param ip
	 * 
	 * @return Boolean
	 */
	@Override
	public Boolean getKYCStatus(String distributorId, String ip) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(Constants.ID, Constants.VALIDATE_DISTRIBUTOR_ID);
		params.add(Constants.DISTRIBUTOR_ID_C, distributorId);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.KYC_SAVE).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.KYC_SAVE, ip);
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		JSONObject data =body.getJSONArray(Constants.RESULT).getJSONObject(0);
		
		return StringsUtil.isNullOrEmpty(String.valueOf(data.get("status"))) ? Boolean.FALSE
					: String.valueOf(data.get("status")).equals("1");
	}

	/****
	 * @description to get distributor login by mobile Number
	 * @param mobileNo
	 * @param id
	 * @return logindetails
	 * 
	 */
	@Override
	public LoginModel getLogin(String mobileNoOrDistributorId, String ip) {

		LoginModel model = null;
		if (mobileNoOrDistributorId.length() == 8) {
			JSONObject data = new JSONObject(checkDistributor(mobileNoOrDistributorId, ip));
			model = new LoginModel();
			model.setUserId(String.valueOf(data.get("DistributorId")));
			model.setFirstName(String.valueOf(data.get("DistributorFirstName")));
			model.setLastName(String.valueOf(data.get("DistributorLastName")));
			model.setMobileNo(String.valueOf(data.get("DistributorMobNumber")));
			model.setEmailId(StringsUtil.isNullOrEmpty(String.valueOf(data.get("DistributorEMailID"))) ? STATIC_EMAIL
					: String.valueOf(data.get("DistributorEMailID")));
			/**
			 * Distributor level ID
			 */
			if(data.has("DistributorLevelId")) {
				model.setUserLevelID(Integer.parseInt(data.get("DistributorLevelId").toString()));
			}
			if (!StringsUtil.isNullOrEmpty(String.valueOf(data.get("userToken"))))
				model.setUserToken(String.valueOf(data.get("userToken")));
			else
				model.setUserToken(getToken(model.getUserId(), String.valueOf(data.get("Password")), ip));

			List<String> userRole = new ArrayList<>();
			if (!mobileNoOrDistributorId.startsWith("T"))
				userRole.add("Distributor");
			else
				userRole.add("Trainer");
			model.setRoles(userRole);
			
			
			
		} else {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			params.add(WrapperConstants.ID, Constants.USER_LOGIN_BY_MOBILE_NO);
			params.add("mobileNo", mobileNoOrDistributorId);
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
			ResponseEntity<String> response = restTemplate.postForEntity(
					getRootURL().append(MposUrlConstants.KELLTONTECH_API).toString(), request, String.class);
			checkApiStatus(response.getBody(), request, MposUrlConstants.KELLTONTECH_API, ip);
			JSONObject body = getJSONObject(response.getBody());
			checkResponseType(body, JSON_OBJECT_CLASS_NAME);
			JSONObject data = body.getJSONObject(Constants.RESULT);
			model = new LoginModel();
			model.setUserId(String.valueOf(data.get("DistributorId")));
			model.setFirstName(String.valueOf(data.get("DistributorFullName")));
			model.setMobileNo(String.valueOf(data.get("DistributorMobNumber")));
			model.setEmailId(StringsUtil.isNullOrEmpty(String.valueOf(data.get("DistributorEMailID"))) ? STATIC_EMAIL
					: String.valueOf(data.get("DistributorEMailID")));
			/**
			 * Distributor level ID
			 */
			if(data.has("DistributorLevelId")) {
				model.setUserLevelID(Integer.parseInt(data.get("DistributorLevelId").toString()));
			}
			
			/**
			 * Parse User Level. This is same as DistributorLevelId
			 */
			if(data.has("PermLevelId")) {
				model.setUserLevelID(Integer.parseInt(data.get("PermLevelId").toString()));
			}
			
			if (!StringsUtil.isNullOrEmpty(String.valueOf(data.get("UserToken"))))
				model.setUserToken(String.valueOf(data.get("UserToken")));
			else
				model.setUserToken(getToken(model.getUserId(), null, ip));

			List<String> userRole = new ArrayList<>();
			if (Objects.nonNull(data.get("userRole"))) {
				data.getJSONArray("userRole").forEach(role -> {
					userRole.add(String.valueOf(role));
				});
			} else {
				userRole.add("Distributor");
			}
			
			model.setRoles(userRole);
		}
		return model;
	}
	
	private String getToken(String distributorId, String password, String ip) {
		String userToken = null;
		try {
			if(StringsUtil.isNullOrEmpty(password)) {
				JSONObject  data = new JSONObject(checkDistributor(distributorId, ip));
				userToken = getLogin(distributorId, String.valueOf(data.get("Password")), ip).getUserToken();
			}else {
				userToken = getLogin(distributorId, password, ip).getUserToken();
			}
		}catch (Exception e) {
			logger.error("getting error : {}", e.getMessage());
			String body = "{\"Status\":0,\"Description\":\"Something went wrong, Please try again later after sometime.\",\"Result\":[],\"ResponseCode\":\"401\"}";
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Something went wrong, Please try again later after sometime.", body.getBytes(), null);
		}
		return userToken;
	}

	@Override
	public String getDistributorDownlineInfo(Long distributorNumber, Long userName, String ip, String ut) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.DISTRIBUTOR_INFO);
		params.add("distributor_no", String.valueOf(distributorNumber));
		params.add(UT, ut);
		params.add(USERNAME, String.valueOf(userName));
		params.add("source", Constants.MOBILE);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.POS_CLIENT).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.POS_CLIENT, ip);
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		return response.getBody();
	}

	/****
	 * @description make invoice from vestige mobile app
	 * @param orderPayment , ip
	 * @return invoice
	 */
	@Override
	public String getOrderPaymentUpdate(OrderPaymentVM orderPayment, String ip, String ut) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(UT, ut);
		params.add(USERNAME, String.valueOf(orderPayment.getDistributorId()));
		params.add(WrapperConstants.SOURCE, Constants.MOBILE);
		params.add(WrapperConstants.ID, Constants.MOBILE_INVOICE);
		params.add(WrapperConstants.TRANSACTION_ID,orderPayment.getTransId());
		params.add(WrapperConstants.ORDER_ID,orderPayment.getVestigeTransactionId());
		params.add(WrapperConstants.BANK_TRANSACTION_ID, orderPayment.getBankReferenceNumber());
		params.add(WrapperConstants.PAYTM_STATUS,orderPayment.getStatus());
		params.add(WrapperConstants.PAYTM_RESPONSE, orderPayment.getResponseCode());
		params.add(WrapperConstants.ORDER_LOG_NUMBER, orderPayment.getLogNumber());
		params.add(WrapperConstants.TRANSACTION_AMOUNT, String.valueOf(orderPayment.getTotalPay()));
		
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.KELLTONTECH_API).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.KELLTONTECH_API, ip );
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		return String.valueOf(body.getJSONArray(Constants.RESULT).getJSONObject(0));
	}



	@Override
	public String getPromoMerchHierarchy(String username, Long locationId, String ip, String ut) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.UT, ut);
		params.add(WrapperConstants.SOURCE, Constants.MOBILE);
		params.add(WrapperConstants.ID, Constants.GET_MERCH_HIERARCHY_ITEM);
		params.add(USERNAME, username);
		params.add("locationId", String.valueOf(locationId));
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.STORE_API).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.STORE_API, ip);
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		return String.valueOf(getJSONObject(response.getBody()).get(Constants.RESULT));
	}

	@Override
	public String loginDistributorOrDownlineInfo(String username, Long distributorNo, String ip, String ut) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.DISTRIBUTOR_INFO);
		params.add(WrapperConstants.DISTRIBUTOR_NO, String.valueOf(distributorNo));
		params.add(WrapperConstants.UT, ut);
		params.add(USERNAME, username);
		params.add(WrapperConstants.SOURCE, Constants.MOBILE);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.STORE_API).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.STORE_API, ip);
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		return String.valueOf(body.getJSONArray(Constants.RESULT).getJSONObject(0));
	}

	/***
	 * 
	 * @description API for repeat Order
	 * @param ip
	 * @param ut
	 * @param username
	 * @param orderNumber
	 * @return lsit of product detail
	 */
	@Override
	public List<ProductDetailResponse> repeatOrder(Long username, String orderNumber, String ip, String ut) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.GET_ORDER_ITEM_DETAIL);
		params.add("Order_InvoiceNO", orderNumber);
		params.add(UT, ut);
		params.add(USERNAME, String.valueOf(username));
		params.add(WrapperConstants.SOURCE, Constants.MOBILE);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate().postForEntity(
				getRootURL().append(MposUrlConstants.ORDER_INFORMATION).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.ORDER_INFORMATION, ip );
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		JSONArray data = body.getJSONArray(Constants.RESULT);
		List<ProductDetailResponse> productDetailList = new ArrayList<>();
		for (int i = 0; i < data.length(); i++) {
			JSONObject productDetailJson = data.getJSONObject(i);
			ProductDetailResponse productDetailResponse = new ProductDetailResponse();
			productDetailResponse.setGiftVoucherCode(String.valueOf(productDetailJson.getString("GiftVoucherCode")));
			productDetailResponse.setIsPromo(String.valueOf(productDetailJson.getString("isPromo")));
			productDetailResponse.setProductAmount(Double.valueOf(productDetailJson.getString("PaymentAmount")));
			productDetailResponse.setProductName(String.valueOf(productDetailJson.getString("ItemName")));
			productDetailResponse.setQuantity(
					Integer.valueOf(Double.valueOf(String.valueOf(productDetailJson.get("Quantity"))).intValue()));
			productDetailResponse.setSkuCode(String.valueOf(productDetailJson.getString("Itemcode")));
			String imageUrl = productImageUrl + String.valueOf(productDetailJson.getString("ItemImageUrl"));
			productDetailResponse.setUrl(imageUrl);
			productDetailResponse.setVoucherSerialNumber(String.valueOf(productDetailJson.getString("VoucherSrNo")));
			productDetailList.add(productDetailResponse);
		}
		return productDetailList;
	}

	@Override
	public String getOrderPromotions(String locationId, String ip, String ut, String userName) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.GET_PROMOTION_ID);
		params.add(UT, ut);
		params.add("locationId", locationId);
		params.add(USERNAME, userName);
		params.add("source", Constants.MOBILE);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.POS_CLIENT).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.POS_CLIENT, ip);
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		return String.valueOf(body.get(Constants.RESULT));
	}

	@Override
	public String getLocationIdByBranchPincode(String pincode, String ip) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(WrapperConstants.ID, Constants.SERVING_LOCATION_ID_BY_PINCODE);
		params.add("pincode", pincode);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.KELLTONTECH_API).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.KELLTONTECH_API, ip);
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		JSONObject orderPromotionsResponse = body.getJSONArray(Constants.RESULT)
				.getJSONObject(0);

		return String.valueOf(orderPromotionsResponse.get("LocationID"));
	}

	/****
	 * @description get list of category
	 * @param categoryId,ip
	 * @return CategoryList
	 */
	@Override
	public String getProductCategories(String ip) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(Constants.ID, Constants.ITEM_CATEGORY_MASTER);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.KELLTONTECH_API).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.KELLTONTECH_API, ip);
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		return String.valueOf(body.get(Constants.RESULT));
	}

	/****
	 * @description get list of sub-category on basis of categoryId
	 * @param categoryId, ip
	 * @return SubCategoryList
	 */
	@Override
	public String getSubProductCategories(Integer categoryId, String ip) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(Constants.ID, Constants.SUB_CATEGORY_ID);
		params.add("categoryId", String.valueOf(categoryId));
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.KELLTONTECH_API).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.KELLTONTECH_API, ip);
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		return String.valueOf(body.get(Constants.RESULT));
	}
	
	private void checkResponseType(JSONObject body,String className){
		if(body.has(Constants.RESULT)) {
			Object objResult= body.get(Constants.RESULT);
			try {
				if(!(Class.forName(className).isInstance(objResult))) {
					throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Ohh..shit","{\"Status\":1,\"Description\":\"Seems like you are not part of Vestige familly.1011\",\"Result\":null,\"Tag\":\"doLogin\",\"ResponseCode\":\"200\"}".getBytes(),null);
				}
			} catch (Exception e) {
				logger.error("Getting error in class instance time");
				throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Ohh..shit","{\"Status\":1,\"Description\":\"Seems like you are not part of Vestige familly.1013\",\"Result\":null,\"Tag\":\"doLogin\",\"ResponseCode\":\"200\"}".getBytes(),null);
			}
		}
	}

	@Override
	public String getUplineIdsForDistributor(String distributorId,String ip) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(Constants.ID, Constants.GET_UPLINE_IDS_OF_DISTRIBUTOR);
		params.add(Constants.DISTRIBUTOR_ID_S, distributorId);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.KELLTONTECH_API).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.KELLTONTECH_API, ip);
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_ARRAY_CLASS_NAME);
		return String.valueOf(body.get(Constants.RESULT));
	}

	@Override
	public String getOrderPdf(String username, String ut, String orderNo, String ip) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(Constants.ID, Constants.INVOICE_PRINT_MOBILE);
		params.add("orderNo", orderNo);
		params.add(WrapperConstants.SOURCE, Constants.MOBILE_S);
		params.add(WrapperConstants.UT, ut);
		params.add("username", username);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = getRestTemplate()
				.postForEntity(getRootURL().append(MposUrlConstants.POS_CLIENT).toString(), request, String.class);
		checkApiStatus(response.getBody(), request, MposUrlConstants.POS_CLIENT, ip);
		JSONObject body = getJSONObject(response.getBody());
		checkResponseType(body, JSON_OBJECT_CLASS_NAME);
		return String.valueOf(body.get(Constants.RESULT));
	}
}
