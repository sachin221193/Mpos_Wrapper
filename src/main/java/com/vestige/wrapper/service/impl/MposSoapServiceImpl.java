package com.vestige.wrapper.service.impl;

import java.io.File;
import java.rmi.RemoteException;
import java.time.Instant;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.vestige.core.domain.Logs;
import com.vestige.core.model.dto.DistributorDTO;
import com.vestige.core.utils.DateUtil;
import com.vestige.soapapi.config.Constants;
import com.vestige.soapapi.config.MposSoapUrlConstants;
import com.vestige.soapapi.model.BonusDetailModel;
import com.vestige.soapapi.model.BonusVoucherModel;
import com.vestige.soapapi.model.BranchDetailsModel;
import com.vestige.soapapi.model.CategoriesDetailsModel;
import com.vestige.soapapi.model.DistributorDownAndUpLineModel;
import com.vestige.soapapi.model.DistributorPointDetailsResponseModel;
import com.vestige.soapapi.model.DistributorVM;
import com.vestige.soapapi.model.FeedbackModel;
import com.vestige.soapapi.model.FundModel;
import com.vestige.soapapi.model.PayoutModel;
import com.vestige.soapapi.model.ProductCategoryModel;
import com.vestige.soapapi.model.SubProductCategoryModel;
import com.vestige.soapapi.model.UserInfoModel;
import com.vestige.soapapi.model.VouchersModel;
import com.vestige.soapapi.service.VestigeSoapService;
import com.vestige.wrapper.config.MposUrlConstants;
import com.vestige.wrapper.enums.ApiType;
import com.vestige.wrapper.repository.LogsRepository;
import com.vestige.wrapper.service.MposSoapService;

/**
 * 
 * @author ashutosh.sharma
 *
 */
@Service
public class MposSoapServiceImpl implements MposSoapService {
	
	@Value(value = "${vestige.mpos.url}")
	private String hostUrl;

	private final Logger logger = LoggerFactory.getLogger(MposSoapServiceImpl.class);

	private final LogsRepository logsRepository;
	
	private final VestigeSoapService vestigeSoapService;

	public MposSoapServiceImpl(LogsRepository logsRepository, VestigeSoapService vestigeSoapService ) {
		this.logsRepository = logsRepository;
		this.vestigeSoapService = vestigeSoapService;
	}

	private StringBuilder getURL() {
		return new StringBuilder("http://").append(hostUrl);
	}
	
	/***
	 * @description get Distributor Point detail
	 * @param distributorId
	 * @return distributorPointDetails
	 */
	@Override
	public DistributorPointDetailsResponseModel getDistributorPointDetails(String distributorId,String ip)
			throws RemoteException, ServiceException {
		logger.debug("enter in getDistributorPointDetails");
		DistributorPointDetailsResponseModel distributorPointDetails = vestigeSoapService
				.getDistributorPointDetails(distributorId);
		
		saveLog(distributorPointDetails, distributorId, MposSoapUrlConstants.DISTRIBUTOR_POINT_DETAILS, ip);
		return distributorPointDetails;
	}

	
	@Override
	public List<DistributorDownAndUpLineModel> getDistributorDownLines(String distributorId, String ip) {
		logger.debug("enter in getDistributorDownLines");
		List<DistributorDownAndUpLineModel> list = null ;
		if (!StringUtils.isEmpty(distributorId)) 
			list =  vestigeSoapService.getDistributorDownline(distributorId);
			
		saveLog(list, distributorId, MposSoapUrlConstants.DISTRIBUTOR_DOWNLINES, ip);
		return list;
	}

	@Override
	public List<DistributorDownAndUpLineModel> getDistributorUplines(String distributorId, String clientId, String ip) {
		logger.debug("enter in getDistributorUplines");
		List<DistributorDownAndUpLineModel> list = null ;
		if (!StringUtils.isEmpty(distributorId) && !StringUtils.isEmpty(clientId)) 
			list =  vestigeSoapService.getDistributorImmediateUplines(distributorId, clientId);
			
		saveLog(list, distributorId, MposSoapUrlConstants.DISTRIBUTOR_IMMEDIATE_UPLINE, ip);
		return list;
	}

	/***
	 * @description to get the product category
	 * @return productCategoryList
	 */
	@Override
	public List<ProductCategoryModel> getProductCategory(String ip) {
		return vestigeSoapService.getProductCategory();
	}

	/**
	 * @author Tarsem.Kumar
	 * @description Get Car Funds of Distributor
	 * @param fundType(car, house, travel), DistributorId , YearMonth(YYYY-mm)
	 */
	@Override
	public List<FundModel> getFunds(String fundType, String distributorId, String yearMonth, String ip) {
		List<FundModel> distributorFundsList = vestigeSoapService.getFunds(fundType, distributorId, yearMonth);

		saveLog(distributorFundsList, distributorId, null, ip);
		return distributorFundsList;
	}

	@Override
	public List<SubProductCategoryModel> getSubProduct(String categoryId, String ip) {
		
		return vestigeSoapService.getSubProduct(categoryId);
	}

	/***
	 * @description to get the sub product category details using the soap api
	 * @param categoryId
	 * @param subCategoryId
	 * @return categoryDetailList
	 */
	@Override
	public List<CategoriesDetailsModel> getSubProductDetails(String categoryId, String subCategoryId, String ip) {
		
		return vestigeSoapService.getSubProductDetails(categoryId, subCategoryId);
	}

	@Override
	public DistributorDownAndUpLineModel distributorDownLineSearch(String distributorId, String clientId, String ip) {
		DistributorDownAndUpLineModel model = null;
		if (!StringUtils.isEmpty(distributorId) && !StringUtils.isEmpty(clientId))
			model = vestigeSoapService.searchDistributorDownline(distributorId, clientId);

		saveLog(model, distributorId, MposSoapUrlConstants.DISTRIBUTOR_DOWNLINE_SEARCH, ip);
		return model;
	}

	/**
	 * @description update distributor personal details
	 * @param distributorInfo
	 * @return
	 */
	@Override
	public DistributorVM updateDistributorPersonalInformation(DistributorDTO distributorInfo, String ip) {
		UserInfoModel userInfoModel = new UserInfoModel();
		userInfoModel.setDistributorId(String.valueOf(distributorInfo.getDistributorId()));
		userInfoModel.setTitleId(Integer.valueOf(distributorInfo.getTitle()));
		userInfoModel.setDob(DateUtil.formatDob(distributorInfo.getDob()));
		userInfoModel.setCoDistributorTitleId(Integer.valueOf(distributorInfo.getCoDistributorTitle()));
		userInfoModel.setCoDistributorDob(DateUtil.formatDob(distributorInfo.getCoDistributorDob()));
		DistributorVM distributorVM = vestigeSoapService.updateDistributorPersonalInformation(userInfoModel);
		saveLog(distributorVM, distributorInfo, MposSoapUrlConstants.UPDATE_DISTRIBUTOR_PERSONAL, ip);
		return distributorVM;
	}

	/**
	 * @description update distributor contact details
	 * @param distributorInfo
	 * @return
	 */
	@Override
	public DistributorVM updateDistributorContactInformation(DistributorDTO distributorInfo, String ip) {
		UserInfoModel userInfoModel = new UserInfoModel();
		userInfoModel.setDistributorId(String.valueOf(distributorInfo.getDistributorId()));
		userInfoModel.setTelephoneNumber(distributorInfo.getTelephoneNumber());
		userInfoModel.setEmailId(distributorInfo.getEmailId());
		userInfoModel.setMobileNumber(distributorInfo.getMobileNumber());
		DistributorVM distributorVM = vestigeSoapService.updateDistributorContactInformation(userInfoModel);
		saveLog(distributorVM, distributorInfo, MposSoapUrlConstants.UPDATE_DISTRIBUTOR_PERSONAL, ip);
		return distributorVM;
	}

	/**
	 * @descriptions this is use for getting distributor voucher
	 * @param distributorId
	 */

	public List<VouchersModel> getVouchres(String distributorId,  String ip) {
		
		List<VouchersModel> list =  vestigeSoapService.getVouchres(distributorId);
		saveLog(list, distributorId, null, ip);
		return list;
	}

	/**
	 * @description Get Distributor payouts(bonus)
	 * @param DistributorId , YearMonth(YYYY-mm)
	 * @return payouts(bonus) entities	 
	 */
	@Override
	public PayoutModel getPayouts(String distributorId, String yearMonth,String ip) {
		
		PayoutModel payout = vestigeSoapService.getPayouts(distributorId, yearMonth);
		saveLog(payout, distributorId, MposSoapUrlConstants.DISTRIBUTOR_PAYOUTS, ip);
		return payout;
	}
	
	@Override
	public String sendFeedback(FeedbackModel model, String ip) {
		return vestigeSoapService.sendFeedback(model);
	}
	
	@Override
	public String uploadKYC(String distributorId, MultipartFile file, String ip) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();

		File file1 = new File("/home/user/Downloads/Yashwanth.jpg");
		params.add("id", Constants.KYC_SAVE_ID);
		params.add("Distributorid", distributorId);
		params.add("UploadedImage", file1);
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = restTemplate
				.postForEntity(getURL().append(MposUrlConstants.KYC_SAVE).toString(), request, String.class);
		saveLog(response.getBody(), request, MposUrlConstants.KYC_SAVE, ip);
		return "success";
	}

	/**
	 * @description Get Distributor bonus detail
	 * @param DistributorId , YearMonth(YYYY-mm)
	 * @return bonus detail entities
	 */
	@Override
	public BonusDetailModel getBonusDetail(String distributorId, String yearMonth, String ip) {

		BonusDetailModel bonusDetailModel = vestigeSoapService.getBonusDetail(distributorId, yearMonth);
		saveLog(bonusDetailModel, distributorId, MposSoapUrlConstants.DISTRIBUTOR_PAYOUTS, ip);
		return bonusDetailModel;
	}

	@Override
	public List<BonusVoucherModel> getBonusVouchers(String distributorId, String ip) {
		List<BonusVoucherModel> bonusVoucherModel = vestigeSoapService.getBonusVouchres(distributorId);
		saveLog( bonusVoucherModel, distributorId, MposSoapUrlConstants.DISTRIBUTOR_PAYOUTS, ip ); 
		return bonusVoucherModel;
	}
	
	@Override
	public List<BranchDetailsModel> getBranchDetails(String branchType, String stateId,String ip) {
		List<BranchDetailsModel> branchDetails = vestigeSoapService.getBranchDetails(branchType, stateId);
		saveLog(branchDetails, stateId, MposSoapUrlConstants.DISTRIBUTOR_PAYOUTS, ip);
		return branchDetails;
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
