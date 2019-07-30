package com.vestige.wrapper.web.rest;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vestige.core.model.dto.DistributorDTO;
import com.vestige.core.utils.StringsUtil;
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
import com.vestige.wrapper.service.MposSoapService;

/***
 * 
 * @author ashutosh.sharma
 *
 */
@RestController
public class MposSoapController {

	private final MposSoapService mposSoapService;

	public MposSoapController(MposSoapService mposSoapService) {
		this.mposSoapService = mposSoapService;
	}

	/****
	 * 
	 * @description get distributor car fund(s)
	 * @param distributorId
	 * @param yearmonth(YYYY-mm)
	 * @return distributor car fund(s)
	 */
	@GetMapping("/distributor/{distributorId}/funds")
	public List<FundModel> getFunds(@RequestParam(name = "fundtype", required = true) String fundtype,
			@PathVariable String distributorId,
			@RequestParam(name = "yearmonth", required = false) String yearmonth, @RequestHeader String ip) {
		return mposSoapService.getFunds(fundtype, distributorId, yearmonth, ip);
	}
	/****
	 * 
	 * @description get distributor product voucher(s)
	 * @param distributorId
	 * @return distributor product voucher(s)
	 */
	@GetMapping("/distributor/{distributorId}/vouchers")
	public List<VouchersModel> getVouchers(@PathVariable String distributorId, @RequestHeader String ip) {
		if (StringsUtil.isNullOrEmpty(distributorId))
			return null;
		return mposSoapService.getVouchres(distributorId, ip);
	}

	/****
	 * @description to get the distributor point details
	 * @param distributorId
	 * @return distributor point detail
	 * 
	 */
	@GetMapping("/point-details/{distributorId}")
	public DistributorPointDetailsResponseModel getDistributorPointDetails(@PathVariable String distributorId, @RequestHeader String ip)
			throws RemoteException, ServiceException {
		if (!StringUtils.isEmpty(distributorId)) {
			return mposSoapService.getDistributorPointDetails(distributorId, ip);
		}
		return null;
	}

	/****
	 * 
	 * @description get distributor down line
	 * @param distributorId
	 * @return distributor down line
	 */
	@GetMapping("/distributor/{distributorId}/downline")
	public List<DistributorDownAndUpLineModel> getDistributorDownlines(@PathVariable String distributorId, @RequestHeader String ip) {
		if (!StringUtils.isEmpty(distributorId)) {
			return mposSoapService.getDistributorDownLines(distributorId, ip);
		}
		return new ArrayList<>();
	}

	/****
	 * @description Api for getting upline distributor
	 * @param distributorId
	 * @param isImmediate
	 * @return distributor upline
	 */
	@GetMapping("/distributor/{distributorId}/uplines")
	public List<DistributorDownAndUpLineModel> getDistributorUpLines(@PathVariable String distributorId,
			@RequestParam String clientId, @RequestHeader String ip) {

		if (!StringUtils.isEmpty(distributorId)) {
			return mposSoapService.getDistributorUplines(distributorId, clientId, ip);
		}
		return new ArrayList<>();
	}

	/***
	 * @description API for getting the product category
	 * @return {@link ResponseEntity} product category list
	 */
	@GetMapping("product-category")
	public List<ProductCategoryModel> getProductCategory(@RequestHeader String ip) {
		return mposSoapService.getProductCategory(ip);

	}

	/****
	 * @description getSub product category
	 * @param categoryId
	 * @return {@link ResponseEntity} Sub Product Category List
	 */

	@GetMapping("sub-product-category/{categoryId}")
	public List<SubProductCategoryModel> getSubProductCategory(@PathVariable String categoryId, @RequestHeader String ip) {
			return mposSoapService.getSubProduct(categoryId, ip);
	}

	/****
	 * @description get category detail
	 * @param categoryId
	 * @param subCategoryId
	 * @return {@link ResponseEntity} get sub category detail list
	 */
	@GetMapping("/category-detail")
	public ResponseEntity<List<CategoriesDetailsModel>> getCategoryDetail(
			@RequestParam String categoryId, @RequestParam String subCategoryId, @RequestHeader String ip) {
		if (!StringUtils.isEmpty(categoryId) || !StringUtils.isEmpty(subCategoryId)) {
			return ResponseEntity.ok(
					mposSoapService.getSubProductDetails(categoryId, subCategoryId, ip));
		}
		return null;

	}

	/****
	 * 
	 * @description distributor down line Search
	 * @param distributorId
	 * @param date
	 * @return distributor down line
	 */
	@GetMapping("/distributor/{distributorId}/downLineSearch")
	public DistributorDownAndUpLineModel distributorDownlineSearch(@PathVariable String distributorId,
			@RequestParam(required = true) String clientId, @RequestHeader String ip) {
		if (!StringUtils.isEmpty(distributorId)) {
			return mposSoapService.distributorDownLineSearch(distributorId, clientId, ip);
		}
		return null;
	}

	/**
	 * @description update distributor personal details
	 * @param distributorInfo
	 * @return
	 */
	@PostMapping("/distributor-personal-details")
	public DistributorVM updateDistributorPersonalDetails(@RequestBody DistributorDTO distributorInfo, @RequestHeader String ip) {
		return mposSoapService.updateDistributorPersonalInformation(distributorInfo, ip);
	}

	/**
	 * @description update distributor contact details
	 * @param distributorInfo
	 * @return
	 */
	@PostMapping("/distributor-contact-details")
	public DistributorVM updateDistributorContactDetails(@RequestBody DistributorDTO distributorInfo, @RequestHeader String ip) {
		return mposSoapService.updateDistributorContactInformation(distributorInfo, ip);
	}
	
	/****
	 * 
	 * @description get distributor payouts(bouns)
	 * @param distributorId
	 * @param yearmonth(YYYY-mm)
	 * @return payouts(bonus) entities
	 */
	@GetMapping("/distributor/payouts/{distributorId}")
	public ResponseEntity<PayoutModel> getPayouts(@PathVariable String distributorId,
			@RequestParam(name = "yearmonth", required = true) String yearmonth,@RequestHeader String ip) {
		return ResponseEntity.ok(mposSoapService.getPayouts(distributorId, yearmonth,ip));
	}
	
	/****
	 * 
	 * @description send Feedback
	 * @return result
	 */
	@PostMapping("/distributor/feedback")
	public String sendFeedback(@RequestBody FeedbackModel model, @RequestHeader String ip) {
		return mposSoapService.sendFeedback(model, ip);
	}
	
	/****
	 * 
	 * @description get distributor Bonus_Details
	 * @param distributorId
	 * @param yearmonth(YYYY-mm)
	 * @return bonus_details entities
	 */
	@GetMapping("/distributor/{distributorId}/bonus-detail")
	public ResponseEntity<BonusDetailModel> getBonusDetail(@PathVariable String distributorId,
			@RequestParam(name = "yearmonth", required = true) String yearmonth,@RequestHeader String ip) {
		return ResponseEntity.ok(mposSoapService.getBonusDetail(distributorId, yearmonth,ip));
	}
	
	/****
	 * 
	 * @description get distributor Bonus voucher list
	 * @param distributorId
	 * @return bonus voucher entities
	 */
	@GetMapping("distributor/{distributorId}/bonus-vouchers")
	public List<BonusVoucherModel> getBonusVoucher(@PathVariable String distributorId, @RequestHeader String ip) {
		return mposSoapService.getBonusVouchers(distributorId, ip);
	}
	
	/****
	 * 
	 * @description get branch details
	 * @param branchType
	 * @param stateId
	 * @return Branchlists
	 */
	@GetMapping("product/branch-details")
	public List<BranchDetailsModel> getBranchDetails(@RequestParam String branchType, @RequestParam String stateId, @RequestHeader String ip) {
		return mposSoapService.getBranchDetails(branchType, stateId, ip);
	}

}