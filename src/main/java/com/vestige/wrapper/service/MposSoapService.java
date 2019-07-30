package com.vestige.wrapper.service;

import java.rmi.RemoteException;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.springframework.web.multipart.MultipartFile;

import com.vestige.core.model.dto.DistributorDTO;
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
import com.vestige.soapapi.model.VouchersModel;


/**
 * 
 * @author ashutosh.sharma
 *
 */
public interface MposSoapService {

	 DistributorPointDetailsResponseModel getDistributorPointDetails(String distributorId, String ip)
			throws RemoteException, ServiceException;

	List<DistributorDownAndUpLineModel> getDistributorUplines(String distributorId, String clientId, String ip);

	List<DistributorDownAndUpLineModel> getDistributorDownLines(String distributorId, String ip);

	List<ProductCategoryModel> getProductCategory(String ip);

	List<SubProductCategoryModel> getSubProduct(String categoryId, String ip);

	List<CategoriesDetailsModel> getSubProductDetails(String categoryId, String subCategoryId, String ip);

	/**
	 * @description Get Car Funds of  Distributor
	 * @param FundType(car, house, travel), DistributorId , YearMonth(YYYY-mm)	 
	 */
	List<FundModel> getFunds(String fundType, String distributorId, String yearMonth, String ip);
	
	DistributorDownAndUpLineModel distributorDownLineSearch(String distributorId, String clientId, String ip);


	DistributorVM updateDistributorPersonalInformation(DistributorDTO distributorInfo, String ip);
	
	List<VouchersModel> getVouchres(String distributorId, String ip);

	DistributorVM updateDistributorContactInformation(DistributorDTO distributorInfo, String ip);
	
	/**
	 * @description Get Distributor payouts(bonus)
	 * @param DistributorId , YearMonth(YYYY-mm)
	 * @return payouts(bonus) entities	 
	 */
	PayoutModel getPayouts(String distributorId, String yearMonth,String ip);

	String uploadKYC(String distributorId, MultipartFile file, String ip);

	String sendFeedback(FeedbackModel model, String ip);
	
	/**
	 * @description Get Distributor bonus detail
	 * @param DistributorId , YearMonth(YYYY-mm)
	 * @return bonus detail entities	 
	 */
	BonusDetailModel getBonusDetail(String distributorId, String yearMonth,String ip);
	
	List<BonusVoucherModel> getBonusVouchers(String distributorId, String ip);

	List<BranchDetailsModel> getBranchDetails(String branchType, String stateId,String ip);

}
