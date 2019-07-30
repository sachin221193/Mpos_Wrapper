package com.vestige.wrapper.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.vestige.core.model.dto.DistributorAddressDTO;

/**
 * A DTO for the OrdersCheckout entity.
 */
public class OrdersCheckoutDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private Integer versionId;

	private String groupOrderId;

	@NotNull
	private Long distributorId;

	private String orderType;

	private List<OrderDetails> orders;

	private ShippingDetails shipping;

	private Instant orderedDate;

	private String createdBy;

	private Instant createdOn;

	private String updatedBy;

	private Instant updatedOn;

	private DistributorAddressDTO distributorAddressDTO;

	private StoresDTO storesDTO;

	private String deliveryType;

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not
	// remove
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getVersionId() {
		return versionId;
	}

	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}

	public String getGroupOrderId() {
		return groupOrderId;
	}

	public void setGroupOrderId(String groupOrderId) {
		this.groupOrderId = groupOrderId;
	}

	public Long getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(Long distributorId) {
		this.distributorId = distributorId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Instant getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Instant createdOn) {
		this.createdOn = createdOn;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Instant getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Instant updatedOn) {
		this.updatedOn = updatedOn;
	}

	public ShippingDetails getShipping() {
		return shipping;
	}

	public void setShipping(ShippingDetails shipping) {
		this.shipping = shipping;
	}

	public Instant getOrderedDate() {
		return orderedDate;
	}

	public void setOrderedDate(Instant orderedDate) {
		this.orderedDate = orderedDate;
	}

	public List<OrderDetails> getOrders() {
		return orders;
	}

	public void setOrderDetails(List<OrderDetails> orders) {
		this.orders = orders;
	}

	public DistributorAddressDTO getDistributorAddressDTO() {
		return distributorAddressDTO;
	}

	public void setDistributorAddressDTO(DistributorAddressDTO distributorAddressDTO) {
		this.distributorAddressDTO = distributorAddressDTO;
	}

	public StoresDTO getStoresDTO() {
		return storesDTO;
	}

	public void setStoresDTO(StoresDTO storesDTO) {
		this.storesDTO = storesDTO;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public void setOrders(List<OrderDetails> orders) {
		this.orders = orders;
	}

	public static class OrderDetails {

		private String orderId;

		private Double orderAmount;

		private List<SubOrderDetails> subOrders;

		private List<PaymentDetails> payment;

		private List<VoucherDetails> vouchers;

		private String orderStatus;
		
		private Long distributorId;

		public String getOrderId() {
			return orderId;
		}

		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}

		public Double getOrderAmount() {
			return orderAmount;
		}

		public void setOrderAmount(Double orderAmount) {
			this.orderAmount = orderAmount;
		}

		public List<SubOrderDetails> getSubOrders() {
			return subOrders;
		}

		public void setSubOrders(List<SubOrderDetails> subOrders) {
			this.subOrders = subOrders;
		}

		public List<PaymentDetails> getPayment() {
			return payment;
		}

		public void setPayment(List<PaymentDetails> payment) {
			this.payment = payment;
		}

		public String getOrderStatus() {
			return orderStatus;
		}

		public void setOrderStatus(String orderStatus) {
			this.orderStatus = orderStatus;
		}

		public List<VoucherDetails> getVouchers() {
			return vouchers;
		}

		public void setVouchers(List<VoucherDetails> vouchers) {
			this.vouchers = vouchers;
		}

		public Long getDistributorId() {
			return distributorId;
		}

		public void setDistributorId(Long distributorId) {
			this.distributorId = distributorId;
		}
	}

	public static class SubOrderDetails {

		private String subOrderId;

		private String subOrderAmount;

		private String distributorId;

		private List<ProductDetails> products;

		private String trackingId;

		public String getSubOrderId() {
			return subOrderId;
		}

		public void setSubOrderId(String subOrderId) {
			this.subOrderId = subOrderId;
		}

		public String getSubOrderAmount() {
			return subOrderAmount;
		}

		public void setSubOrderAmount(String subOrderAmount) {
			this.subOrderAmount = subOrderAmount;
		}

		public String getDistributorId() {
			return distributorId;
		}

		public void setDistributorId(String distributorId) {
			this.distributorId = distributorId;
		}

		public List<ProductDetails> getProducts() {
			return products;
		}

		public void setProducts(List<ProductDetails> products) {
			this.products = products;
		}

		public String getTrackingId() {
			return trackingId;
		}

		public void setTrackingId(String trackingId) {
			this.trackingId = trackingId;
		}
	}

	public static class VoucherDetails {

		private String type;

		private String number;

		private Double amount;
		
		private String code;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public Double getAmount() {
			return amount;
		}

		public void setAmount(Double amount) {
			this.amount = amount;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}
		
	}

	public static class ProductDetails {

		private String sku;

		private Long productId;

		private Double pv;

		private Double bv;

		private String title;

		private Integer quantity;

		private Double unitCost;

		private Double total;

		private String imageUrl;
		
		private Boolean isGiftVoucher;
		
		private Boolean isPromotional;
		
		private Integer promotionalId;
		
		private Double discountPrice;

		// Fields added to save order on third party
		private String productName;

		public String getSku() {
			return sku;
		}

		public void setSku(String sku) {
			this.sku = sku;
		}

		public Long getProductId() {
			return productId;
		}

		public void setProductId(Long productId) {
			this.productId = productId;
		}

		public Double getPv() {
			return pv;
		}

		public void setPv(Double pv) {
			this.pv = pv;
		}

		public Double getBv() {
			return bv;
		}

		public void setBv(Double bv) {
			this.bv = bv;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public Integer getQuantity() {
			return quantity;
		}

		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}

		public Double getUnitCost() {
			return unitCost;
		}

		public void setUnitCost(Double unitCost) {
			this.unitCost = unitCost;
		}

		public Double getTotal() {
			return total;
		}

		public void setTotal(Double total) {
			this.total = total;
		}

		public String getImageUrl() {
			return imageUrl;
		}

		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}

		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

		public Boolean getIsGiftVoucher() {
			return isGiftVoucher;
		}

		public void setIsGiftVoucher(Boolean isGiftVoucher) {
			this.isGiftVoucher = isGiftVoucher;
		}

		public Boolean getIsPromotional() {
			return isPromotional;
		}

		public void setIsPromotional(Boolean isPromotional) {
			this.isPromotional = isPromotional;
		}

		public Integer getPromotionalId() {
			return promotionalId;
		}

		public void setPromotionalId(Integer promotionalId) {
			this.promotionalId = promotionalId;
		}

		public Double getDiscountPrice() {
			return discountPrice;
		}

		public void setDiscountPrice(Double discountPrice) {
			this.discountPrice = discountPrice;
		}
		
	}

	public static class PaymentDetails {

		private Double totalAmount;

		private String method; // credit card/debit cart/net banking/COD/wallet

		private String transactionId;

		private String status; // status of the payment

		private String responseText; // response from payment gateway

		private Instant paymentDate;
		
		private String voucherId;

		public String getMethod() {
			return method;
		}

		public void setMethod(String method) {
			this.method = method;
		}

		public Double getTotalAmount() {
			return totalAmount;
		}

		public void setTotalAmount(Double totalAmount) {
			this.totalAmount = totalAmount;
		}

		public String getTransactionId() {
			return transactionId;
		}

		public void setTransactionId(String transactionId) {
			this.transactionId = transactionId;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getResponseText() {
			return responseText;
		}

		public void setResponseText(String responseText) {
			this.responseText = responseText;
		}

		public Instant getPaymentDate() {
			return paymentDate;
		}

		public void setPaymentDate(Instant paymentDate) {
			this.paymentDate = paymentDate;
		}

		public String getVoucherId() {
			return voucherId;
		}

		public void setVoucherId(String voucherId) {
			this.voucherId = voucherId;
		}

	}

	public static class ShippingDetails {

		private Address address;

		private Boolean isShipped;

		private String deliveryNotes;

		private Instant shippedDate;

		public Address getAddress() {
			return address;
		}

		public void setAddress(Address address) {
			this.address = address;
		}

		public Boolean getIsShipped() {
			return isShipped;
		}

		public void setIsShipped(Boolean isShipped) {
			this.isShipped = isShipped;
		}

		public String getDeliveryNotes() {
			return deliveryNotes;
		}

		public void setDeliveryNotes(String deliveryNotes) {
			this.deliveryNotes = deliveryNotes;
		}

		public Instant getShippedDate() {
			return shippedDate;
		}

		public void setShippedDate(Instant shippedDate) {
			this.shippedDate = shippedDate;
		}

	}

	public static class Address {

		private String address1;

		private String address2;

		private String cityName;

		private String stateName;

		private String countryName;

		private String pincode;

		private String contactName;

		private String contactNumber;

		public String getAddress1() {
			return address1;
		}

		public void setAddress1(String address1) {
			this.address1 = address1;
		}

		public String getAddress2() {
			return address2;
		}

		public void setAddress2(String address2) {
			this.address2 = address2;
		}

		public String getCityName() {
			return cityName;
		}

		public void setCityName(String cityName) {
			this.cityName = cityName;
		}

		public String getStateName() {
			return stateName;
		}

		public void setStateName(String stateName) {
			this.stateName = stateName;
		}

		public String getCountryName() {
			return countryName;
		}

		public void setCountryName(String countryName) {
			this.countryName = countryName;
		}

		public String getPincode() {
			return pincode;
		}

		public void setPincode(String pincode) {
			this.pincode = pincode;
		}

		public String getContactName() {
			return contactName;
		}

		public void setContactName(String contactName) {
			this.contactName = contactName;
		}

		public String getContactNumber() {
			return contactNumber;
		}

		public void setContactNumber(String contactNumber) {
			this.contactNumber = contactNumber;
		}
	}
}
