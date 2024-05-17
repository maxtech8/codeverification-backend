package org.backend.service;

import java.util.List;

public interface PhoneService {
	void registerPhoneNumbers(Long userId, List<String> phoneNumbers);
	void saveVerifyCode(Long userId, String verifyCode);
}