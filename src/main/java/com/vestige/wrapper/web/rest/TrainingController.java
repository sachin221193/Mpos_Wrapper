package com.vestige.wrapper.web.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vestige.soapapi.model.KeyValueModel;
import com.vestige.soapapi.model.TrainerProfileModel;
import com.vestige.soapapi.model.TrainingDetailsModel;
import com.vestige.soapapi.model.TrainingResponseModel;
import com.vestige.soapapi.model.TrainingSearchModel;
import com.vestige.soapapi.model.TrainingTypeDetailsModel;
import com.vestige.soapapi.model.TrainingTypeModel;
import com.vestige.wrapper.service.TrainingService;

/***
 * 
 * @author sohan.maurya
 *
 */
@RestController
public class TrainingController {

	private final TrainingService trainingService;

	public TrainingController(TrainingService trainingService) {
		this.trainingService = trainingService;
	}

	/****
	 * 
	 * @description get all Training_Types_List
	 * @return Objects List
	 */
	@GetMapping("/training/types")
	public List<TrainingTypeModel> getTrainingTypes(@RequestHeader String ip) {
		return trainingService.getAllTrainingTypes(ip);
	}
	
	/****
	 * 
	 * @description get all Training_Type Details
	 * @return Objects List
	 */
	@GetMapping("/training/types/{eventType}")
	public List<TrainingTypeDetailsModel> getTrainingTypeDetails(@PathVariable String eventType,
			@RequestParam(name = "countryName", required = false) String countryName,
			@RequestParam(name = "stateName", required = false) String stateName,
			@RequestParam(name = "cityName", required = false) String cityName, @RequestHeader String ip) {
		return trainingService.findTrainingTypeDetails(eventType, countryName, stateName, cityName, ip);
	}
	
	/****
	 * 
	 * @description get Countries List
	 * @return Objects List
	 */
	@GetMapping("/training/country/user/{userId}")
	public List<KeyValueModel> getTraningCountries(@PathVariable String userId, @RequestHeader String ip) {
		return trainingService.getTraningCountries(userId, ip);
	}

	/****
	 * 
	 * @description get States List
	 * @return Objects List
	 */
	@GetMapping("/training/state/user/{userId}/country/{countryId}")
	public List<KeyValueModel> getTraningStates(@PathVariable String userId, @PathVariable String countryId,
			@RequestHeader String ip) {
		return trainingService.getTraningStates(countryId, userId, ip);
	}

	/****
	 * 
	 * @description get City List
	 * @return Objects List
	 */
	@GetMapping("/training/city/user/{userId}/state/{stateId}")
	public List<KeyValueModel> getTrainingCities(@PathVariable String userId, @PathVariable String stateId,
			@RequestHeader String ip) {
		return trainingService.getTrainingCities(stateId, userId, ip);
	}

	/****
	 * 
	 * @description save training details
	 * @return result
	 */
	@PostMapping("/training/details")
	public String trainingDetails(@Valid @RequestBody TrainingDetailsModel model, @RequestHeader String ip) {
		return trainingService.saveTrainingRequests(model, ip);
	}

	/****
	 * 
	 * @description save training details
	 * @return result
	 */
	@PostMapping("/training/details/search")
	public List<TrainingResponseModel> trainingDetails(@Valid @RequestBody TrainingSearchModel model, @RequestHeader String ip) {
		return trainingService.findTrainingRequests(model, ip);
	}
	
	/****
	 * 
	 * @description get trainer details
	 * @return result
	 */
	@GetMapping("/trainer/{trainerId}")
	public TrainerProfileModel trainerDetails(@PathVariable String trainerId, @RequestHeader String ip) {
		return trainingService.trainerProfile(trainerId, ip);
	}
	
	/****
	 * 
	 * @description to check server health  
	 * @return result
	 */
	@GetMapping("/management/health")
	public ResponseEntity<String> health() {
		return ResponseEntity.ok("UP");
	}
}