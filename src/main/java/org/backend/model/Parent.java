package org.backend.model;

import lombok.Data;

import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONException;

@Data
@Entity
@Table(name = "parents")
public class Parent {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public String sender; // Sender of the parent object
    private String receiver; // Receiver of the parent object
   
    private String createdAt; // Creation timestamp of the parent object
    
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Child> children = new ArrayList<Child>(); // List of associated child objects
    
    @PrePersist
    public void onPrePersist() {
        this.setCreatedAt(ZonedDateTime.now().toString()); // Set the creation timestamp before persisting the parent object
    }
    
    public Parent() {
        this.setSender(sender);
        this.setReceiver(receiver);
    }

    // Getters and setters for class fields
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String recevier) {
        this.receiver = recevier;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    public Long getId() {
    	return id;
    }
    
    public JSONObject toJsonParent() throws JSONException {
    	JSONObject jsonObject = new JSONObject();
    
    	jsonObject.put("receiver", this.getReceiver());
    	jsonObject.put("sender", this.getSender());
    	jsonObject.put("created_at", this.getCreatedAt());
    	
    	return jsonObject;
    }
}
