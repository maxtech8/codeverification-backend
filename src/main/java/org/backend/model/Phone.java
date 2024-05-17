package org.backend.model;

import java.time.ZonedDateTime;

import org.json.JSONException;
import org.json.JSONObject;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Phones")

public class Phone {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private Long userId;
    private String phoneNumber;
    private String verifyCode;
    private String createdAt;
    
    public Phone() {
    	//default constructor	
    	this.userId = (long) 23423423;
    	this.phoneNumber = "234234";
    	this.verifyCode = "";
    }
    
    public Phone(Long userId, String phoneNumber, String verifyCode) {
    	this.setUserId(userId);
    	this.setPhoneNumber(phoneNumber);
    	this.setVerifyCode(verifyCode);
    	onPrePersist();
    }

	public Long getUserId() {
    	return this.userId;
    }
    
    public String getPhoneNumber() {
    	return this.phoneNumber;
    }
    
    public String getVerifyCode() {
    	return this.verifyCode;
    }
    
    public void setUserId(Long userId) {
    	this.userId = userId;
    }
    
    
    public void setPhoneNumber(String phoneNumber) {
    	this.phoneNumber = phoneNumber;
    }
    public void setVerifyCode(String verifyCode) {
    	this.verifyCode = verifyCode;
    }
    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    @PrePersist
    public void onPrePersist() {
        this.setCreatedAt(ZonedDateTime.now().toString()); // Set the creation timestamp before persisting the parent object
    }
    
    public JSONObject toJsonPhone() throws JSONException {
    	JSONObject jsonObject = new JSONObject();
    
    	jsonObject.put("userId", this.getUserId());
    	jsonObject.put("phoneNumber", this.getPhoneNumber());
    	jsonObject.put("createdAt", this.getCreatedAt());
    	
    	return jsonObject;
    }
}
