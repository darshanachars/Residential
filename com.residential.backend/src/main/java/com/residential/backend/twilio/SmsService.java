package com.residential.backend.twilio;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.residential.backend.config.TwilioConfig;
import com.residential.backend.model.Students;
import com.residential.backend.repo.StudentRepo;
import com.twilio.rest.api.v2010.account.Message;

import com.twilio.type.PhoneNumber;

@Service
public class SmsService {
	@Autowired
	private TwilioConfig twilioConfig;
	@Autowired
	private StudentRepo studentRepo;
	
	Map<String,String> otpMap=new HashMap<>();
	
	public OtpResponse sendSms(OtpRequest otpRequest) {
		OtpResponse otpResponse=null;
		Students stu=studentRepo.findByEmail(otpRequest.getEmail()).orElseThrow(null);
		if(stu!=null) {
		try {
			PhoneNumber to = new PhoneNumber("+91"+stu.getPhoneNo());
			PhoneNumber from = new PhoneNumber(twilioConfig.getPhoneNumber());
			String otp=generateOtp();
			 Message create = Message.creator(to, from, otp).create();
			otpMap.put(otpRequest.getEmail(), otp);
			otpResponse=new OtpResponse(OtpStatus.DELIVERED, otp);
		} catch (Exception e) {
			otpResponse=new OtpResponse(OtpStatus.FAILED,e.getMessage());
			
		}
		}
		
		return otpResponse;
	}
	public boolean validateOtp(OtpValidationRequest otpValidationRequest) {
		
		String string = otpMap.get(otpValidationRequest.getEmail());
		String otp = otpValidationRequest.getOtp();
		if(string.equals(otp)){
			
			otpMap.remove(string);
			return true;	
		}
		return false;
	}
	public boolean registerMessage(Students stu) {
		boolean res=false;
		try {
			PhoneNumber to=new PhoneNumber("+91"+stu.getPhoneNo());
			PhoneNumber from=new PhoneNumber(twilioConfig.getPhoneNumber());
			Message message=Message.creator(to, from, "User has been registered successfully").create();
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
		
		return res;
	}
	public String generateOtp() {
		return new DecimalFormat("000000").
				format(new Random().nextInt(999999));
	}
}
