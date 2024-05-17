package org.backend.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.backend.controller.dto.CreateUserRequest;
import org.backend.controller.dto.LoginRequest;
import org.backend.model.User;
import org.backend.security.TokenProvider;
import org.backend.service.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.UUID;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@RestController
@RequestMapping("/api/auth")

public class UserController implements AuthenticationManager {
	
	@Autowired
	private final UserService userService;
    private final TokenProvider tokenProvider;
    
	public UserController(UserService userService) {
		this.userService = userService;
		this.tokenProvider = new TokenProvider();
	}
	
    private String authenticateAndGetToken(String email, String password) {
        Authentication authentication = authenticate(new UsernamePasswordAuthenticationToken(email, password));
        return tokenProvider.generate(authentication);
    }
    
	@GetMapping("/")
	public List<User> getUser() {
		List<User> users = userService.getUsers();
		return users.stream().collect(Collectors.toList());
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> createUser(@RequestBody CreateUserRequest createUserRequest ) throws JSONException {
		String email = createUserRequest.getEmail();
		String password = createUserRequest.getPassword();
		String firstName = createUserRequest.getFirstName();
		String lastName = createUserRequest.getLastName();
		JSONObject jsonObject = new JSONObject();
		//email isexist
		if(isExist(email)) {
			jsonObject.put("status", 0);
			return ResponseEntity.ok(jsonObject.toString());
		}
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		// Hash the password
        String hashedPassword = passwordEncoder.encode(password);
        
		userService.addUser(email, hashedPassword, firstName, lastName);

        jsonObject.put("status", 1);
        
		return ResponseEntity.ok(jsonObject.toString());
    }
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest ) throws JSONException {
        String token = authenticateAndGetToken(loginRequest.getEmail(), loginRequest.getPassword());
        String uuid = UUID.randomUUID().toString();
		JSONObject jsonObject = new JSONObject();
		
		if(token == null) 
			return ResponseEntity.status(401).build();
		else {
			jsonObject.put("status", 200);
			jsonObject.put("uid", uuid);
			jsonObject.put("token", token);
		}
        
		return ResponseEntity.ok(jsonObject.toString());
    }
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        User userData = userService.getUserByEmail(email);
        if (userData == null) return null;
        
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean passwordMatch = passwordEncoder.matches(password, userData.getPassword());
        if (passwordMatch) {
        	return new UsernamePasswordAuthenticationToken(userData.getEmail(), userData.getPassword(), userData.getAuthorities());
        } else return null;
	}
	

	public boolean isExist(String email) {
		User userData = userService.getUserByEmail(email);
		return userData != null; 
	}
}
