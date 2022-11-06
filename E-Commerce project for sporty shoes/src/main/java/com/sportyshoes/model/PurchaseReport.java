package com.sportyshoes.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PurchaseReport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String categoryOfProduct;
	
	private String productName;

	private int priceOfTheProduct;

	private String userWhoBoughtTheProduct;
	
	private String userEmailBoughtTheProduct;

	@Temporal(TemporalType.DATE)
	private Date dateOfProductPurchase;

	public PurchaseReport(String productName, String categoryOfProduct, int priceOfTheProduct, String userWhoBoughtTheProduct, String userEmailBoughtTheProduct, Date dateOfProductPurchase) {
		this.productName = productName;
		this.categoryOfProduct = categoryOfProduct;
		this.userWhoBoughtTheProduct = userWhoBoughtTheProduct;
		this.dateOfProductPurchase = dateOfProductPurchase;
		this.userEmailBoughtTheProduct = userEmailBoughtTheProduct;
		this.priceOfTheProduct = priceOfTheProduct;
	}

}
