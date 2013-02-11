package il.co.idanmoshe.digitalwallet;

import java.io.Serializable;

class Expense implements Serializable{

	String strDescription;
	int strPrice;
	String paymentOption;
	String productCategory;
	String photoURL;
	String isLocation;
	String isDate;
	String isTime;
	String isAudioNote;

	public Expense(String strDescription, int strPrice, String paymentOption,
			String productCategory, String photoURL, String isLocation,
			String isDate, String isTime, String isAudioNote) {
		super();
		this.strDescription = strDescription;
		this.strPrice = strPrice;
		this.paymentOption = paymentOption;
		this.productCategory = productCategory;
		this.photoURL = photoURL;
		this.isLocation = isLocation;
		this.isDate = isDate;
		this.isTime = isTime;
		this.isAudioNote = isAudioNote;
	}

	public String getStrDescription() {
		return strDescription;
	}

	public void setStrDescription(String strDescription) {
		this.strDescription = strDescription;
	}

	public int getStrPrice() {
		return strPrice;
	}

	public void setStrPrice(int strPrice) {
		this.strPrice = strPrice;
	}

	public String getPaymentOption() {
		return paymentOption;
	}

	public void setPaymentOption(String paymentOption) {
		this.paymentOption = paymentOption;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getPhotoURL() {
		return photoURL;
	}

	public void setPhotoURL(String photoURL) {
		this.photoURL = photoURL;
	}

	public String getIsLocation() {
		return isLocation;
	}

	public void setIsLocation(String isLocation) {
		this.isLocation = isLocation;
	}

	public String getIsDate() {
		return isDate;
	}

	public void setIsDate(String isDate) {
		this.isDate = isDate;
	}

	public String getIsTime() {
		return isTime;
	}

	public void setIsTime(String isTime) {
		this.isTime = isTime;
	}

	public String getIsAudioNote() {
		return isAudioNote;
	}

	public void setIsAudioNote(String isAudioNote) {
		this.isAudioNote = isAudioNote;
	}

	@Override
	public String toString() {
		return "Item: " + strDescription + ", Price: " + strPrice+" $";
	}
}