package com.vestige.wrapper.web.rest;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import com.vestige.wrapper.model.PositionModel;
import com.vestige.wrapper.model.ProductBrandsModel;
import com.vestige.wrapper.model.ProductDetailResponse;
import com.vestige.wrapper.model.StoreModel;
import com.vestige.wrapper.model.TopSellingProductModel;
import com.vestige.wrapper.model.UplineDistributorModel;
import com.vestige.wrapper.model.VestigeShopAddress;
import com.vestige.wrapper.service.MPOSService;
import com.vestige.wrapper.web.rest.vm.LoginVm;

/***
 * 
 * @author ashutosh.sharma
 *
 */
@RestController
public class MposRestController {

	private final MPOSService mposService;

	public MposRestController(MPOSService mposService) {
		this.mposService = mposService;
	}

	/****
	 * @description to get user login
	 * @param loginCredentials
	 * @return logindetails
	 * 
	 */
	@PostMapping("/login")
	public ResponseEntity<LoginModel> login(@Valid @RequestBody LoginVm loginCredentials, @RequestHeader String ip) {
		return ResponseEntity
				.ok(mposService.getLogin(loginCredentials.getUserName(), loginCredentials.getPassword(), ip));
	}

	/****
	 * @description to get user login by mobile
	 * @param mobileNo
	 * @return logindetails
	 * 
	 */
	@GetMapping("/login/{mobileNo}")
	public ResponseEntity<LoginModel> login(@PathVariable String mobileNo, @RequestHeader String ip) {
		return ResponseEntity.ok(mposService.getLogin(mobileNo, ip));

	}

	/****
	 * @description to check the distributor
	 * @param id
	 * @param distributorId
	 * @return list of Distributor
	 * 
	 */
	@GetMapping("/distributor")
	public ResponseEntity<String> checkDistributor(@RequestParam String distributorId, @RequestHeader String ip) {
		return ResponseEntity.ok(mposService.checkDistributor(distributorId, ip));
	}

	/****
	 * @description to check upline distributor
	 * @param id
	 * @param uplineNumber
	 * @return list of uplineDistributor
	 * 
	 */
	@GetMapping("/check-upline-distributor")
	public ResponseEntity<List<UplineDistributorModel>> checkUplineDistributor(
			@RequestParam String uplineNumber, @RequestHeader String ip) {
		return ResponseEntity.ok(mposService.checkUplineDistributor(uplineNumber, ip));
	}

	/****
	 * @description to get distributor title
	 * @param id
	 * @return distributor title list
	 * 
	 */
	@GetMapping("/distributor-title")
	public ResponseEntity<List<DistributorTitleModel>> getDistributorTitle(@RequestHeader String ip) {
		return ResponseEntity.ok(mposService.getDistributorTitles(ip));
	}

	/****
	 * @description to get address of distributor using pincode
	 * @param id
	 * @param pinCode
	 * @return address of distributor
	 * 
	 */
	@GetMapping("/address")
	public ResponseEntity<List<VestigeShopAddress>> getAddressByPincode(@RequestParam String pinCode, @RequestHeader String ip) {
			return ResponseEntity.ok(mposService.getAddress(pinCode, ip));
	}

	/****
	 * @description to save the distributor
	 * @param distributorRegistration
	 * @return distributor sequenceNumber and distributorId
	 * 
	 */
	@PostMapping("/distributor")
	public ResponseEntity<DistributorDTO> saveDistributor(@Valid @RequestBody DistributorDTO distributorRegistration,
			@RequestHeader String ip) {
			return ResponseEntity.ok(mposService.saveDistributor(distributorRegistration, ip));
		
	}

	/***
	 * @description to the get the position of the director
	 * @param distributorId
	 * @return {@link ResponseEntity}
	 */
	@GetMapping("/distributor/{id}/hierarchy-level")
	public ResponseEntity<PositionModel> getDistributorPosition(@PathVariable String id, @RequestHeader String ip) {
		return ResponseEntity.ok(mposService.getPositionOfDistributor(id, ip));

	}

	/**
	 * @description to get list of products
	 * @param locationId
	 * @param ip
	 * @return
	 */

	@GetMapping("/products/list/location/{id}")
	public ResponseEntity<String> getProductList(@PathVariable String id, @RequestHeader String ip) {
		return ResponseEntity.ok(mposService.getProductListByLocationId(id, ip));
	}

	/**
	 * @description to get inventories list
	 * @param locationId
	 * @param ip
	 * @param sku
	 * @return
	 * @throws JsonProcessingException
	 */
	@GetMapping("/inventories/{locationId}")
	public ResponseEntity<List<ItemModel>> getInventory(@PathVariable String locationId, @RequestHeader String ip,
			@RequestParam String sku) throws JsonProcessingException {
		return ResponseEntity.ok(mposService.getAllInventories(locationId, ip, sku));
	}

	/**
	 * 
	 * @param distributorId
	 * @param file1
	 * @header ip
	 * @param docId
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/distributor/kyc-upload")
	public ResponseEntity<String> upload(@RequestParam("distributorId") String distributorId,
			@RequestParam("file1") MultipartFile file1,
			@RequestParam(value = "file2", required = false) MultipartFile file2, @RequestParam String ip,
			@RequestParam("docId") Integer docId, @RequestParam("docNumber") String docNumber) throws IOException {
		return ResponseEntity.ok(mposService.saveKyc(distributorId, file1, file2, ip, docId, docNumber));
	}

	/**
	 * @description to get store list
	 * @header ip
	 * @return
	 */
	@GetMapping("/stores")
	public ResponseEntity<List<StoreModel>> getInventory(@RequestHeader String ip) {
		return ResponseEntity.ok(mposService.getStores(ip));
	}

	/****
	 * @description to get the six month of point history
	 * @param distributorId
	 * @return
	 */
	@GetMapping("distributor/{distributorId}/points-history")
	public ResponseEntity<String> getPointHistory(@PathVariable String distributorId, @RequestHeader String ip) {
		return ResponseEntity.ok(mposService.getHistory(distributorId, ip));
	}

	/****
	 * @description to get uploaded distributor kyc document
	 * @param distributorId
	 * 
	 * @return
	 */
	@GetMapping("/distributor/{distributorId}/uploaded-kyc")
	public ResponseEntity<List<DistributorUploadedKycModel>> getDistributorUploadedKyc(@PathVariable String distributorId,
			@RequestHeader String ip) {
		return ResponseEntity.ok(mposService.getDistributorUploadedKyc(distributorId, ip));
	}

	/****
	 * @description to get distributor consistency
	 * @param distributorId
	 * 
	 * @return
	 */
	@GetMapping("/distributor/{distributorId}/consistency")
	public ResponseEntity<DistributorConsistencyModel> getDistributorConsistency(@PathVariable String distributorId,
			@RequestHeader String ip) {
		return ResponseEntity.ok(mposService.getDistributorConsistency(distributorId, ip));
	}

	/*****
	 * @description get top selling product on the basis of there locationId
	 * @param location id
	 * @return top selling product List
	 */
	@GetMapping("/distributor/{locationId}/top-selling-product")
	public List<TopSellingProductModel> getTopSellingProduct(@PathVariable String locationId,
			@RequestHeader String ip) {
		return mposService.getTopSellingProduct(locationId, ip);
	}

	/*****
	 * @description get product brands
	 * @return product brands List
	 */
	@GetMapping("/product/brands")
	public ResponseEntity<List<ProductBrandsModel>> getProductBrands(@RequestHeader String ip) {
		return ResponseEntity.ok(mposService.getProductBrands(ip));
	}

	
	
	/****
	 * @description to change the password
	 * @return success response
	 */
	@PostMapping("/distributor/change-password")
	public ResponseEntity<PasswordResponseModel> changePassword(@RequestBody PasswordModel passwordModel, @RequestHeader String ip) {
		return ResponseEntity.ok(mposService.changePassword(passwordModel, ip));
	}

	/***
	 * @description to create log generate
	 * @param distributorId, locationId
	 * @return String.valueOf(logGenerateInfo)
	 */
	@GetMapping("/distributor/{distributorId}/order/store-pickup-log")
	public ResponseEntity<String> getStorePickupLog(@PathVariable String distributorId,
			@RequestParam String locationId, @RequestHeader String ip, @RequestHeader String ut) {
		return ResponseEntity.ok(mposService.getStorePickupLog(distributorId, locationId, ip, ut));
	}

	/***
	 * @description to create log generate with generate
	 * @param distDTO, locationId
	 * @return orderResponse
	 */
	@PostMapping("/distributor/order/home-delivery-log")
	public ResponseEntity<String> getHomeDeliveryLog(@Valid @RequestBody DistributorAddressDTO addressDTO,
			@RequestHeader String ip, @RequestHeader String ut) {
		return ResponseEntity.ok(mposService.getHomeDeliveryLog(addressDTO, ip, ut));
	}

	/****
	 * @description to get VBD Stores
	 * @return success response
	 */
	@GetMapping("vbd/stores/{state}")
	public ResponseEntity<String> getVBDStores(@PathVariable String state, @RequestHeader String ip) {
		return ResponseEntity.ok(mposService.getVBDStores(state, ip));
	}

	/**
	 * @description to get list of products
	 * @param locationId
	 * @param ip
	 * @return
	 */
	@GetMapping("/products/list/pincode/{pincode}")
	public ResponseEntity<String> getProductListByPinCode(@PathVariable String pincode, @RequestHeader String ip) {
		return ResponseEntity.ok(mposService.getProductListByPinCode(pincode, ip));
	}

	/****
	 * @description to cancel the order
	 * @param OrderModel
	 * @param ip
	 * @return
	 */
	@PostMapping("/order/cancel")
	public ResponseEntity<String> cancelOrder(@RequestBody OrderCancelModel cancelModel,@RequestHeader String ip,
			@RequestHeader String ut) {
		return ResponseEntity.ok(mposService.cancelOrder(cancelModel,ip, ut));
	}

	/**
	 * @description to get list of last six months cnc qualify invoice
	 * @param distributorId
	 * @param ip
	 * @return
	 */

	@GetMapping("/distributor/{id}/cnc-invoices")
	public ResponseEntity<String> getLastSixMonthsQualifyInvoiceData(@PathVariable String id, @RequestHeader String ip) {
		return ResponseEntity.ok(mposService.getLastSixMonthQualifyInvoiceData(id, ip));
	}

	/**
	 * @description to get list of last six months cnc qualify invoice
	 * @param distributorId
	 * @param               ip,
	 * @return
	 */

	@GetMapping("/distributor/{id}/cnc-voucher")
	public ResponseEntity<String> getDistributorTotalVoucher(@PathVariable String id, @RequestHeader String ip) {
		return ResponseEntity.ok(mposService.getDistributorVoucherDetail(id, ip));
	}

	/***
	 * @description Gift Voucher Use Detail API
	 * @param distributorId, giftVoucherNumber
	 * @return voucher list
	 */
	@GetMapping("/distributor/{distributorId}/voucher")
	public String getVouchers(@PathVariable String distributorId, @RequestParam String giftVoucherNumber,
			@RequestHeader String ip) {
		return mposService.getVoucherUseDetail(distributorId, giftVoucherNumber, ip);
	}

	@GetMapping("/distributor/bonus-vouchers")
	public ResponseEntity<String> getBounsVoucherStatusDetail(@RequestParam String bonusChequeNo, @RequestHeader String ip) {
		return ResponseEntity.ok(mposService.getBonusVoucherStatusDetail(bonusChequeNo, ip));
	}

	/****
	 * @description to save the order
	 * @param OrderDTO
	 * @return
	 */
	@PostMapping("/order/save")
	public ResponseEntity<String> saveOrder(@RequestBody OrdersCheckoutDTO orderDTO, @RequestHeader String ip,
			@RequestHeader String ut) {
		return ResponseEntity.ok(mposService.saveOrder(orderDTO, ip, ut));

	}

	/*****
	 * @description Order Track detail API
	 * @param logNumber
	 * @return track details
	 */
	@GetMapping("/orders/{distributorId}/track")
	public ResponseEntity<String> trackOrder(@RequestParam(name = "logNumber") String logNumber, @PathVariable String distributorId,
			@RequestHeader String ip, @RequestHeader String ut) {
		return ResponseEntity.ok(mposService.trackOrder(logNumber, distributorId, ip, ut));

	}

	/****
	 * @description Service for rendering My downloads
	 * @param countryId
	 * @param ip
	 * @return downloads file
	 */
	@GetMapping("/distributor/downloads")
	public String renderingDownloads(@RequestParam String countryId, @RequestHeader String ip) {
		return mposService.renderingDownload(countryId, ip);

	}

	/**
	 * @description to get distributor's desired level
	 * @param distributorId
	 * @param levelId
	 * @param               ip,
	 * @return
	 */

	@GetMapping("/distributor/{id}/desired-level")
	public ResponseEntity<String> getDistributorDesiredLevel(@PathVariable String id, @RequestParam String levelId,
			@RequestHeader String ip) {
		return ResponseEntity.ok(mposService.getDistributorDesiredLevel(id, ip, levelId));
	}

	/**
	 * @description to get distributor's desired percentage
	 * @param distributorId
	 * @param desiredPercentage
	 * @param                   ip,
	 * @return
	 */

	@GetMapping("/distributor/{id}/desired-percentage")
	public ResponseEntity<String> getDistributorDesiredPercentage(@PathVariable String id, @RequestParam String desiredPercentage,
			@RequestHeader String ip) {
		return ResponseEntity.ok(mposService.getDistributorDesiredPercentage(id, ip, desiredPercentage));
	}

	/***
	 * @description to create log generate with generate
	 * @param distDTO, locationId
	 * @return orderResponse
	 */
	@GetMapping("/courier-charges/{distributorId}")
	public ResponseEntity<String> getCourierChargesStatus(@RequestParam String logId, @RequestHeader String ip,
			@PathVariable Long distributorId, @RequestHeader String ut) {
		if (!Objects.isNull(logId)) {
			return ResponseEntity.ok(mposService.getCourierChargesStatus(distributorId, logId, ip, ut));
		}
		return null;
	}

	/**
	 * @description to get distributor's car home and trip Winners
	 * @param distributorId
	 * @param fundType
	 * @param               ip,
	 * @return
	 */

	@GetMapping("/distributor/{id}/achievers")
	public ResponseEntity<List<String>> getDistributorCarHomeAndTripWinners(@PathVariable String id, @RequestParam String fundType,
			@RequestHeader String ip) {
		return ResponseEntity.ok(mposService.getDistributorCarHomeAndTripWinners(id, ip, fundType));
	}

	/**
	 * 
	 * @param ordersToBePaid
	 * @param payableOrderList
	 * @param ip
	 * @return
	 */
	@PutMapping("/order/{distributorId}/shipment-payments")
	public ResponseEntity<String> getPaymentUpdationStatus(@RequestParam String ordersToBePaid,
			@RequestParam String payableOrderList, @RequestHeader String ip, @PathVariable Long distributorId,
			@RequestHeader String ut) {
		return ResponseEntity.ok(mposService.getPaymentUpdationStatus(ordersToBePaid, payableOrderList, ip, ut, distributorId));
	}

	/**
	 * 
	 * @param distributorId
	 * @param ip
	 * @return
	 */
	@GetMapping("/distributor/{distributorId}/kyc-status")
	public ResponseEntity<Boolean> getKycStatus(@PathVariable String distributorId, @RequestHeader String ip) {
		return ResponseEntity.ok(mposService.getKYCStatus(distributorId, ip));
	}

	@GetMapping("/distributors/downline-info")
	public ResponseEntity<String> getDistributorDownlineInfo(@RequestParam Long distributorNumber, @RequestParam Long userName,
			@RequestHeader String ip, @RequestHeader String ut) {
		return ResponseEntity.ok(mposService.getDistributorDownlineInfo(distributorNumber, userName, ip, ut));
	}

	/****
	 * @description make invoice from vestige mobile app
	 * @param orderPayment , ip
	 * @return invoice
	 */
	@PostMapping("/order/update-payment")
	public ResponseEntity<String> getOrderPaymentUpdate(@RequestBody OrderPaymentVM orderPayment, @RequestHeader String ip,
			@RequestHeader String ut) {
		return ResponseEntity.ok(mposService.getOrderPaymentUpdate(orderPayment, ip, ut));

	}

	/**
	 * Promotion Merch Hierarchy
	 * 
	 * @param username
	 * @param locationId
	 * @param ip
	 * @param ut
	 * @return hierarchy list
	 */
	@PostMapping("/orders-promotion/merchHierarchy/{username}")
	public ResponseEntity<String> getPromoMerchHierarchy(@PathVariable String username,
			@RequestParam(name = "locationId") Long locationId, @RequestHeader String ip, @RequestHeader String ut) {
		return ResponseEntity.ok(mposService.getPromoMerchHierarchy(username, locationId, ip, ut));

	}

	/**
	 * Login Distributor or Down-line Info
	 * 
	 * @param username
	 * @param distributorNo
	 * @param ip
	 * @param ut
	 * @return
	 */
	@PostMapping("/orders-promotion/login/distributor/downline-info/{username}")
	public ResponseEntity<String> loginDistributorOrDownlineInfo(@PathVariable String username,
			@RequestParam(name = "distributorId") Long distributorId, @RequestHeader String ip,
			@RequestHeader String ut) {
		return ResponseEntity.ok(mposService.loginDistributorOrDownlineInfo(username, distributorId, ip, ut));
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
	@GetMapping("/order/{username}")
	public ResponseEntity<List<ProductDetailResponse>> repeatOrder(@RequestHeader String ip, @RequestHeader String ut,
			@PathVariable Long username, @RequestParam String orderNumber) {
		return ResponseEntity.ok(mposService.repeatOrder(username, orderNumber, ip, ut));
	}

	/****
	 * @description make invoice from vestige mobile app
	 * @param orderPayment , ip
	 * @return invoice
	 */
	@GetMapping("/order/promotion")
	public ResponseEntity<String> getOrderPromotions(@RequestParam String username, @RequestHeader String ip,
			@RequestHeader String ut, @RequestParam String locationId) {
		return ResponseEntity.ok(mposService.getOrderPromotions(locationId, ip, ut, username));

	}

	/****
	 * @description get location
	 * @param orderPayment , ip
	 * @return invoice
	 */
	@GetMapping("/branch/pincode/{pincode}")
	public ResponseEntity<String> getLocationIdByBranchPincode(@PathVariable String pincode, @RequestHeader String ip) {
		return ResponseEntity.ok(mposService.getLocationIdByBranchPincode(pincode, ip));
	}

	/****
	 * @description get random distributor id's
	 * @param ip
	 * @return list of distributor id's
	 */
	@GetMapping("/distributor/random")
	public ResponseEntity<List<String>> getRandomDistributorIds(@RequestHeader String ip) {
		return ResponseEntity.ok(mposService.getRandomDistributorId(ip));
	}
	
	/****
	 * @description get product categories
	 * @param ip
	 * @return list of product categories
	 */
	@GetMapping("/products/categories")
	public ResponseEntity<String> getProductCategories(@RequestHeader String ip) {
		return ResponseEntity.ok(mposService.getProductCategories(ip));
	}
	
	/****
	 * @description get product subcategories
	 * @param ip, categoryid
	 * @return list of product sub categories
	 */
	@GetMapping("/products/categories/{categoryId}")
	public ResponseEntity<String> getProductSubCategories(@PathVariable Integer categoryId,@RequestHeader String ip) {
		return ResponseEntity.ok(mposService.getSubProductCategories(categoryId, ip));
	}
	
	/****
	 * 
	 * @description get downline count of the distributor
	 * @param id
	 * @param ip
	 * @return getDownlineCount
	 */
	@GetMapping("/distributor/{id}/downline-count")
	public ResponseEntity<Integer> getDownlineCount(@PathVariable String id, @RequestHeader String ip) {
		return ResponseEntity.ok(mposService.getDownlineCount(id, ip));
	}
	
	/****
	 * 
	 * @description get downline count of the distributor
	 * @param id
	 * @param ip
	 * @return getDownlineCount
	 */
	@GetMapping("/distributor/{id}/distributor-uplines")
	public ResponseEntity<String> getUplineIdsForDistributor(@PathVariable String id, @RequestHeader String ip) {
		return ResponseEntity.ok(mposService.getUplineIdsForDistributor(id, ip));
	}
	
	/****
	 * 
	 * @description get invoice of distributor's order
	 * @param id
	 * @param ip
	 * @param ut
	 * @param orderNo
	 * @return orderPdf
	 */
	@GetMapping("/distributor/order/invoice")
	public ResponseEntity<String> getUplineIdsForDistributor(@RequestHeader String username, @RequestHeader String ut,
			@RequestParam String orderNumber, @RequestHeader String ip) {
		return ResponseEntity.ok(mposService.getOrderPdf(username, ut, orderNumber, ip));
	}
}
