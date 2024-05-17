package org.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;

import org.backend.repository.PhoneRepository;
import org.backend.model.Phone;

@Service
public class PhoneServiceImpl implements PhoneService {
	private final PhoneRepository phoneRepository;
	//constructor
	PhoneServiceImpl(PhoneRepository phoneRepository){
		this.phoneRepository = phoneRepository;
	}

	@Override
	public void registerPhoneNumbers(Long userId, List<String> phoneNumbers) {
		List<Phone> phones = new ArrayList<>();
		for (String phoneNumber: phoneNumbers) {

			Phone phone = new Phone(userId, phoneNumber, "");
			phones.add(phone);
		}
		phoneRepository.saveAll(phones);
	}
	
	@Override
    @Transactional
	public void saveVerifyCode(Long userId, String verifyCode) {
	    phoneRepository.updateVerifyCodeByUserId(userId, verifyCode);
	}
}

