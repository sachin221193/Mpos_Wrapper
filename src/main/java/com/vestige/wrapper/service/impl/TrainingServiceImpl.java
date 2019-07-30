package com.vestige.wrapper.service.impl;

import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.vestige.core.domain.Logs;
import com.vestige.soapapi.config.MposSoapUrlConstants;
import com.vestige.soapapi.model.KeyValueModel;
import com.vestige.soapapi.model.TrainerProfileModel;
import com.vestige.soapapi.model.TrainingDetailsModel;
import com.vestige.soapapi.model.TrainingResponseModel;
import com.vestige.soapapi.model.TrainingSearchModel;
import com.vestige.soapapi.model.TrainingTypeDetailsModel;
import com.vestige.soapapi.model.TrainingTypeModel;
import com.vestige.soapapi.service.VestigeSoapTrainingService;
import com.vestige.wrapper.enums.ApiType;
import com.vestige.wrapper.repository.LogsRepository;
import com.vestige.wrapper.service.TrainingService;

/**
 * 
 * @author sohan.maurya
 *
 */
@Service
public class TrainingServiceImpl implements TrainingService {
	

	private final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);

	private final LogsRepository logsRepository;
	
	private final VestigeSoapTrainingService vestigeSoapTrainingService;

	public TrainingServiceImpl(LogsRepository logsRepository,
			VestigeSoapTrainingService vestigeSoapTrainingService ) {
		this.logsRepository = logsRepository;
		this.vestigeSoapTrainingService = vestigeSoapTrainingService;
	}

	/**
	 * @description Get all Training_Types_List
	 * @return Objects List
	 */
	@Override
	public List<TrainingTypeModel> getAllTrainingTypes(String ip) {
		logger.debug("enter in getAllTrainingTypes");
		List<TrainingTypeModel> trainingModels = vestigeSoapTrainingService.getAllTrainingTypes();
		saveLog(trainingModels, null, MposSoapUrlConstants.ALL_TRAINING_TYPE_LIST, ip);
		return trainingModels;
	}

	@Override
	public List<KeyValueModel> getTraningCountries(String userId, String ip) {
		
		List<KeyValueModel> countries  = vestigeSoapTrainingService.getTraningCountries(userId);
		saveLog(countries, userId, "training_countries", ip);
		return countries;
	}

	@Override
	public List<KeyValueModel> getTraningStates(String countryId, String userId, String ip) {
		List<KeyValueModel> states = vestigeSoapTrainingService.getTraningStates(countryId, userId);
		saveLog(states, userId, "training_states", ip);
		return states;
	}

	@Override
	public List<KeyValueModel> getTrainingCities(String stateId, String userId, String ip) {
		List<KeyValueModel> cities = vestigeSoapTrainingService.getTrainingCities(stateId, userId);
		saveLog(cities, userId, "training_cities", ip);
		return cities;
	}

	@Override
	public String saveTrainingRequests(TrainingDetailsModel model, String ip) {
		String result = vestigeSoapTrainingService.saveTrainingRequests(model);
		saveLog(result, model, "save_training", ip);
		return result;
	}

	@Override
	public List<TrainingResponseModel> findTrainingRequests(TrainingSearchModel model, String ip) {

		List<TrainingResponseModel> result = vestigeSoapTrainingService.getTrainingsDetails(model);
		saveLog(result, model, MposSoapUrlConstants.ALL_TRAINING_TYPE_LIST, ip);
		return result;
	}

	@Override
	public List<TrainingTypeDetailsModel> findTrainingTypeDetails(String eventType, String countryName,
			String stateName, String cityName, String ip) {
		List<TrainingTypeDetailsModel> result = vestigeSoapTrainingService.getTrainigTypeDetails(eventType, countryName,
				stateName, cityName);
		saveLog(result, countryName, MposSoapUrlConstants.ALL_TRAINING_TYPE_LIST, ip);
		return result;
	}

	@Override
	public Boolean login(String trainerId, String password, String ip) {
		Boolean result  = vestigeSoapTrainingService.loginStatus(trainerId, password);
		saveLog( result, trainerId, MposSoapUrlConstants.ALL_TRAINING_TYPE_LIST, ip );
		return result;
	}

	@Override
	public TrainerProfileModel trainerProfile(String trainerId, String ip) {
		TrainerProfileModel result = vestigeSoapTrainingService.trainerProfile(trainerId);
		saveLog( result, trainerId, MposSoapUrlConstants.TRAINER_DETAILS, ip );
		return result;
	}
	
	private void saveLog( Object response, Object request, String apiName, String ip ) {
		Logs log = new Logs();
		log.setApiName(apiName);
		log.setApiType(ApiType.SOAP.getApiName());
		log.setLog(String.valueOf(response));
		log.setSystemIp(ip);
		log.setLogTime(Instant.now());
		log.setRequest(String.valueOf(request));
		logsRepository.save(log);
	}
}
