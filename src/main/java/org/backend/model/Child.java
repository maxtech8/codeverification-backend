package org.backend.model;

import lombok.Data;

import jakarta.persistence.*;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.time.ZonedDateTime;
import org.json.JSONObject;
import org.json.JSONException;

@Data
@Entity
@Table(name = "children")

public class Child {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Float paidAmount; // Amount paid by the child
    private String createdAt; // Creation timestamp of the child
    
    // FetchType.LAZY is used, which means that the associated entity
    @ManyToOne(fetch = FetchType.LAZY) 

    @JoinColumn(name = "parent_id")
    private Parent parent; // Parent associated with the child
    
    // Constructor with paidAmount and parent
    public Child() {
        this.setPaidAmount(paidAmount);
        this.setParent(parent);
    }

    // Method executed before persisting the child entity
    @PrePersist
	public void onPrePersist() {
        setCreatedAt(ZonedDateTime.now().toString());
    }

    // Getter and setter for paidAmount
	public Float getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Float paidAmount) {
		this.paidAmount = paidAmount;
	}

    // Getter and setter for createdAt
	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
    // Setter and getter for parent
	public void setParent(Parent parent) {
		this.parent = parent;
	}
	
	public Parent getParent() {
        return parent;
    }
	
	public JSONObject toJsonChild() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		
		jsonObject.put("paid_amount", this.getPaidAmount());
		jsonObject.put("created_at", this.getCreatedAt());
		jsonObject.put("parent", this.parent.toJsonParent());
		
		return jsonObject;
	}
}
