package com.vestige.wrapper.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.vestige.core.model.ItemModel;
import com.vestige.core.model.dto.DistributorAddressDTO;
import com.vestige.core.model.dto.DistributorDTO;
import com.vestige.wrapper.model.DistributorConsistencyModel;
import com.vestige.wrapper.model.DistributorTitleModel;
import com.vestige.wrapper.model.DistributorUploadedKycModel;
import com.vestige.wrapper.model.LoginModel;
import com.vestige.wrapper.model.OrderCancelModel;
import com.vestige.wrapper.model.OrderPaymentVM;
import com.vestige.wrapper.model.OrdersCheckoutDTO;
import com.vestige.wrapper.model.PasswordModel;
import com.vestige.wrapper.model.PasswordResponseModel;
import com.vestige.wrapper.model.PointHistoryModel;
import com.vestige.wrapper.model.PositionModel;
import com.vestige.wrapper.model.ProductBrandsModel;
import com.vestige.wrapper.model.ProductDetailResponse;
import com.vestige.wrapper.model.StoreModel;
import com.vestige.wrapper.model.TopSellingProductModel;
import com.vestige.wrapper.model.UplineDistributorModel;
import com.vestige.wrapper.model.VestigeShopAddress;

/**
 * 
 * @author ashutosh.sharma
 *
 */

public interface MPOSService {

	LoginModel getLogin(String userName, String password, String ip);

	List<VestigeShopAddress> getAddress(String pinCode, String ip);

	List<DistributorTitleModel> getDistributorTitles(String ip);

	String checkDistributor(String distributorId, String ip);

	List<UplineDistributorModel> checkUplineDistributor(String upLineNumber, String ip);

	DistributorDTO saveDistributor(DistributorDTO distDTO, String ip);

	PositionModel getPositionOfDistributor(String distributorId, String ip);

	List<ItemModel> getAllInventories(String locationId, String ip, String sku);

	String saveKyc(String distributorId, MultipartFile file, MultipartFile file2, String ip, Integer docId,
			String docNumber)

			throws IOException;

	List<StoreModel> getStores(String ip);

	String getProductListByLocationId(String locationId, String ip);

	String getHistory(String distributorId, String ip);

	List<DistributorUploadedKycModel> getDistributorUploadedKyc(String distributorId, String ip);

	List<TopSellingProductModel> getTopSellingProduct(String locationId, String ip);

	DistributorConsistencyModel getDistributorConsistency(String distributorId, String ip);

	List<ProductBrandsModel> getProductBrands(String ip);

	PasswordResponseModel changePassword(PasswordModel passwordModel, String ip);

	String getStorePickupLog(String distributorId, String locationId, String ip, String ut);

	String getHomeDeliveryLog(DistributorAddressDTO addressDTO, String ip, String ut);

	String getVBDStores(String state, String ip);

	String getProductListByPinCode(String pincode, String ip);

	String getVoucherUseDetail(String distributorId, String giftVoucherNumber, String ip);

	String saveOrder(OrdersCheckoutDTO orderModel, String ip, String ut); ///

	String cancelOrder(OrderCancelModel cancelModel, String ip, String ut); ///

	String getLastSixMonthQualifyInvoiceData(String distributorId, String ip);

	String getDistributorVoucherDetail(String distributorId, String ip);

	String renderingDownload(String countryId, String ip);

	String getDistributorDesiredLevel(String id, String ip, String levelId);

	String getDistributorDesiredPercentage(String id, String ip, String desiredPercentage);

	String trackOrder(String logNumber,String distributorId, String ip, String ut); ///
	
	String getBonusVoucherStatusDetail(String bonusVoucherNumber, String ip);
	
	String getCourierChargesStatus(Long distributorId,String id, String ip, String ut);

	List<String> getDistributorCarHomeAndTripWinners(String id, String ip, String fundType); 
	
	String getPaymentUpdationStatus(String ordersToBePaid, String payableOrderList, String ip, String ut, Long distributorId); 

	Integer getDownlineCount(String distributorId, String ip); 

	Boolean getKYCStatus(String distributorId, String ip); 

	LoginModel getLogin(String mobileNo, String ip);

	String getDistributorDownlineInfo(Long distributorNumber, Long userName, String ip, String ut);

	String getOrderPaymentUpdate(OrderPaymentVM orderPayment, String ip, String ut);

	String getPromoMerchHierarchy(String username, Long locationId, String ip, String ut);

	String loginDistributorOrDownlineInfo(String username, Long distributorNo, String ip, String ut);

	List<ProductDetailResponse> repeatOrder(Long username, String orderNumber, String ip, String ut);

	String getLocationIdByBranchPincode(String pincode, String ip); 

	String getOrderPromotions(String locationId, String ip, String ut, String userName);

	List<String> getRandomDistributorId(String ip); 

	String getProductCategories(String ip);

	String getSubProductCategories(Integer categoryId, String ip);
	
	String getUplineIdsForDistributor(String distributorId,String ip);
	
	String getOrderPdf(String username,String ut,String orderNo,String ip);
}