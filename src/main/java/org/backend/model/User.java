package org.backend.model;

import java.time.ZonedDateTime;
import java.util.Collection;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.Transient; // Import this annotation

@Data
@Entity
@Table(name = "users")

public class User implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private String email;
    private String password;
    private String firstName; 
    public String lastName;
    private String createdAt;
    
    @Transient
    private Collection<? extends GrantedAuthority> authorities;
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
	
    public User() {
    	//default constructor	
    	this.email = "john";
    	this.password = "123";
    	this.firstName = "";
    	this.lastName = "";
    }
    
    public User(String email, String password, String firstName, String lastName) {
    	this.setEmail(email);
    	this.setPassword(password);
    	this.setFirstName(firstName);
    	this.setLastName(lastName);
    	onPrePersist();
    }

    public Long getId() {
    	return this.id;
    }
    
	public String getEmail() {
    	return this.email;
    }
    
    public void setEmail(String email) {
    	this.email = email;
    }
    
    public String getFirstName() {
    	return this.firstName;
    }
    
    public void setFirstName(String firstName) {
    	this.firstName = firstName;
    }
    
    public String getLastName() {
    	return this.lastName;
    }
    
    public void setLastName(String lastName) {
    	this.lastName = lastName;
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
    
    public JSONObject toJsonUser() throws JSONException {
    	JSONObject jsonObject = new JSONObject();
    
    	jsonObject.put("firstName", this.getFirstName());
    	jsonObject.put("lastName", this.getLastName());
    	jsonObject.put("createdAt", this.getCreatedAt());
    	
    	return jsonObject;
    }

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getUsername() {
		return email;
	}
}
