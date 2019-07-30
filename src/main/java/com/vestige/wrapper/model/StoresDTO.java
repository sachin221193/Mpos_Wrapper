package com.vestige.wrapper.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import javax.validation.constraints.NotNull;

/**
 * A DTO for the Stores entity.
 */
public class StoresDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

    @NotNull
    private String locationCode;

    @NotNull
    private String locationName;

    private String type;

    private String pincode;

    private String contactNo;
    
    private String contactName;

    private Boolean orderCaterLocation;
    
    private Integer cityId;
    
    private Integer stateId;
    
    private Integer countryId;

    private Long createdBy;

    private Instant createdOn;

    private Long updatedBy;

    private Instant updatedOn;
    
    private GeoLocationDTO geoLocation;
    
    private Integer locationId;
    
    private String address;
    
    private String timings;
    
    private String distance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getContactNo() {
		return contactNo;
	}
    
    public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
    
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Boolean isOrderCaterLocation() {
        return orderCaterLocation;
    }

    public void setOrderCaterLocation(Boolean orderCaterLocation) {
        this.orderCaterLocation = orderCaterLocation;
    }

    public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getStateId() {
		return stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

	public GeoLocationDTO getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(GeoLocationDTO geoLocation) {
		this.geoLocation = geoLocation;
	}
	
	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTimings() {
		return timings;
	}

	public void setTimings(String timings) {
		this.timings = timings;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StoresDTO storesDTO = (StoresDTO) o;
        if (storesDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), storesDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StoresDTO{" +
            "id=" + getId() +
            ", locationCode='" + getLocationCode() + "'" +
            ", locationName='" + getLocationName() + "'" +
            ", type='" + getType() + "'" +
            ", pincode='" + getPincode() + "'" +
            ", contactName='" + getContactName() + "'" +
            ", orderCaterLocation='" + isOrderCaterLocation() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            "}";
    }
}
