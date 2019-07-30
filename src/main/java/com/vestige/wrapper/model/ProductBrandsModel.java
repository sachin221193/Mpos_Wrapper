/**
 * 
 */
package com.vestige.wrapper.model;

/**
 * @author user
 * 
 * use for product brands listing
 *
 */
public class ProductBrandsModel {
	
	private Integer id;
	private String name;
	private String image;
	private String status;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
