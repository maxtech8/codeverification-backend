package org.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.backend.service.ConsumerService;
import org.backend.service.ProducerService;

@RestController
public class ServiceController {
	
	@Autowired
    private ProducerService producerService;
	private ConsumerService consumerService;
	
	@RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    public @ResponseBody String sendMessage(@RequestBody String message) {
        producerService.produceMessage(message);
		return "sent";
    }
	@GetMapping(value = "/receiveMessage")
    public @ResponseBody String receiveMessage(@RequestBody byte[] message) {
		consumerService.consumerMessage(message);
		return "received";
    }
}
