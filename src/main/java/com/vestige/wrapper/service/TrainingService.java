package com.vestige.wrapper.service;

import java.util.List;

import com.vestige.soapapi.model.KeyValueModel;
import com.vestige.soapapi.model.TrainerProfileModel;
import com.vestige.soapapi.model.TrainingDetailsModel;
import com.vestige.soapapi.model.TrainingResponseModel;
import com.vestige.soapapi.model.TrainingSearchModel;
import com.vestige.soapapi.model.TrainingTypeDetailsModel;
import com.vestige.soapapi.model.TrainingTypeModel;
import com.vestige.wrapper.model.LoginModel;


/**
 * 
 * @author sohan.sharma
 *
 */
public interface TrainingService {

	/**
	 * @description Get all Training_Type_List
	 * @return Objects List
	 */
	List<TrainingTypeModel> getAllTrainingTypes(String ip);

	/**
	 * @description Get sale target visible status for trainer
	 * @return boolean
	 */
//	Boolean getIsTargetSaleVisbleForTrainer(String trainerId);
//	
//	String getCityWiseSales(String cityIds, String yearMonth);
	
	List<KeyValueModel> getTraningCountries(String userId, String ip);
	
	List<KeyValueModel> getTraningStates(String countryId, String userId, String ip);
	
	List<KeyValueModel> getTrainingCities(String stateId, String userId, String ip);
	
	String saveTrainingRequests(TrainingDetailsModel model, String ip);

	List<TrainingResponseModel> findTrainingRequests(TrainingSearchModel model, String ip);
	
	List<TrainingTypeDetailsModel> findTrainingTypeDetails(String evenType, String countryName, String stateName, String cityName, String ip);
	
	Boolean login(String trainerId, String password, String ip);
	
	TrainerProfileModel trainerProfile(String trainerId, String ip);
}
