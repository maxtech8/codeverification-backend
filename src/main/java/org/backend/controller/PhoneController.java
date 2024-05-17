package org.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.backend.service.PhoneService;
import org.backend.service.UserService;
import org.backend.model.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")

public class PhoneController {
	
	public final UserService userService;
	public final PhoneService phoneService;
	
	public PhoneController(UserService userService, PhoneService phoneService) {
		this.userService = userService;
		this.phoneService = phoneService;
	}
	
	@GetMapping("/receiveSmsPool/getReceivePhone")
	public ResponseEntity<?> getPhoneNumber(
			@RequestParam("num") Integer num,
			@RequestParam("projectId") String projectId, 
			@RequestParam("appName") String appName) throws JSONException {

		Integer countryCode = 1; // default

		// usign == SHA1(uid + token + requestURI + timestamp) from RequestHeader
		List<String> phoneNumbers = generatePhoneNumbers(num, projectId, appName, countryCode);
		
		String email = getEmailFromAuthentication();
		User user = userService.getUserByEmail(email);
		System.out.println(phoneNumbers);
		phoneService.registerPhoneNumbers(user.getId(), phoneNumbers);
		
		System.out.println(phoneNumbers);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("statusCode", 1);
		jsonObject.put("data", phoneNumbers);
		
		return ResponseEntity.ok(jsonObject.toString());
	}
	
	@GetMapping("/receiveSmsPool/getReceiveCode")
	public ResponseEntity<?> getVerificationCode(
			@RequestParam("phoneNum") String num,
			@RequestParam("projectId") String projectId, 
			@RequestParam("appName") String appName
		) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		
		String verifyCode = generateVerificationCode();
		String email = getEmailFromAuthentication();
		User user = userService.getUserByEmail(email);
		
		phoneService.saveVerifyCode(user.getId(), verifyCode);
		jsonObject.put("statusCode", 1);
		jsonObject.put("verifyCode", verifyCode);

		return ResponseEntity.ok(jsonObject.toString());
	}

	public static String generatePhoneNumber(String projectId, String appName, Integer countryCode) {
		// You can customize this method to generate phone numbers according to your
		// specific requirements
		return countryCode.toString() + "-" + projectId + "-" + appName + "-" + (int) (Math.random() * 1000000000);
	}

	public static List<String> generatePhoneNumbers(int quantity, String projectId, String appName,
			Integer countryCode) {
		List<String> phoneNumbers = new ArrayList<>();

		for (int i = 0; i < quantity; i++) {
			// Generate a single phone number
			String phoneNumber = generatePhoneNumber(projectId, appName, countryCode);
			phoneNumbers.add(phoneNumber);
		}

		return phoneNumbers;
	}
	public static String generateVerificationCode() {
        // Define the length of the verification code
        int codeLength = 6;
        
        // Define the characters allowed in the verification code
        String allowedChars = "0123456789";

        // Generate a random verification code using allowed characters
        StringBuilder verificationCode = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < codeLength; i++) {
            int index = random.nextInt(allowedChars.length());
            verificationCode.append(allowedChars.charAt(index));
        }

        return verificationCode.toString();
    }
	
    public String getEmailFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = (String) authentication.getName();
        return userEmail;
    }
}
