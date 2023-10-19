package com.residential.backend.config;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.residential.backend.email.EmailRequest;
import com.residential.backend.email.EmailResponse;
import com.residential.backend.email.EmailServiceImpl;
import com.residential.backend.model.JwtRequest;
import com.residential.backend.model.JwtResponse;
import com.residential.backend.model.Students;
import com.residential.backend.repo.StudentRepo1;
import com.residential.backend.security.JwtHelper;
import com.residential.backend.service.StudentService;
import com.residential.backend.twilio.OtpRequest;
import com.residential.backend.twilio.OtpResponse;
import com.residential.backend.twilio.OtpValidationRequest;
import com.residential.backend.twilio.SmsService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private SmsService smsService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager manager;

	@Autowired
	private JwtHelper helper;

	@Autowired
	private EmailServiceImpl emailService;

	@Autowired
	private StudentService studentService;

	private Logger logger = LoggerFactory.getLogger(AuthController.class);
	private Map<String, String> map = new HashMap<>();

//	This is method is to send otp to email before login
	@PostMapping("/login-otp")
	public ResponseEntity<Response> sendEmailOtp(@RequestBody JwtRequest request) {
		if (request.getRole().equals("Admin")) {
			request.setEmail("Admin " + request.getEmail());
		}
		boolean f = this.doAuthenticate(request.getEmail(), request.getPassword());
		Response response = new Response();
		if (f) {
			String otp = new DecimalFormat("000000").format(new Random().nextInt(999999));
			map.put(request.getEmail() + "e", otp);
			String email = request.getEmail();
			if (request.getEmail().contains("Admin")) {
				email = request.getEmail().substring(6);
			}

			emailService.sendSimpleMessage(email, "One time password", otp);
			response.setMessage(otp);
			response.setStatus(true);
		} else {
			response.setMessage("validation unsuccessful");
			response.setStatus(false);
		}
		return ResponseEntity.ok(response);
	}

//	This mapping is to validate the OTP which was received from front end and generate the jwt token for correct credentials.
	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody EmailResponse emailResponse) {
		if (emailResponse.getRole().equals("Admin")) {
			emailResponse.setEmail("Admin " + emailResponse.getEmail());
		}
		if (map.get(emailResponse.getEmail() + "e").equals(emailResponse.getOtp())) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(emailResponse.getEmail());
			String token = this.helper.generateToken(userDetails);

			JwtResponse response = JwtResponse.builder().JwtToken(token).username(userDetails.getUsername()).build();
			map.remove(emailResponse.getEmail() + "e");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			JwtResponse response = new JwtResponse();
			response.setJwtToken("cannot genereate token");
			response.setUsername(emailResponse.getEmail());

			return new ResponseEntity<>(response, HttpStatus.OK);
		}

	}

//	This method is to generate OTP for forget password
	@PostMapping("/send-otp")
	public ResponseEntity<OtpResponse> forgetPassword(@RequestBody OtpRequest otprequest) {
		OtpResponse otpRes = smsService.sendSms(otprequest);
		return ResponseEntity.ok(otpRes);
	}

//	This method is to validate the OTP sent for the forget password
	@PostMapping("/validate-otp")
	public ResponseEntity<Response> validateOtp(@RequestBody OtpValidationRequest otpValidationRequest) {
		boolean validateOtp = smsService.validateOtp(otpValidationRequest);
		Response response = new Response();
		if (validateOtp) {
			String randomUUID = UUID.randomUUID().toString().replaceAll("_", "");
			map.put(otpValidationRequest.getEmail(), randomUUID);
			response.setMessage(randomUUID);
			response.setStatus(true);
		} else {
			response.setMessage("validation unsuccessful,enter valid OTP" + "");
			response.setStatus(false);
		}
		return ResponseEntity.ok(response);
	}

// 	This mapping is to change the password after OTP validation
	@PutMapping("/change-password")
	public ResponseEntity<Response> changePassword(@RequestBody ForgetPasswordRequest forgetPasswordRequest) {
		Response res = new Response();
		if (forgetPasswordRequest.getUuid().equals(map.get(forgetPasswordRequest.getEmail()))) {
			studentService.forgetPassword(forgetPasswordRequest.getEmail(), forgetPasswordRequest.getPassword());
			res.setMessage("password updated successfully");
			res.setStatus(true);
			map.clear();

		} else {
			res.setMessage("invalid UUID");
			res.setStatus(false);
		}
		return ResponseEntity.ok(res);
	}

	private boolean doAuthenticate(String email, String password) {

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
		boolean f = false;
		try {
			manager.authenticate(authentication);
			f = true;

		} catch (BadCredentialsException e) {
			throw new BadCredentialsException(" Invalid Username or Password  !!");
		}
		return f;
	}

	@ExceptionHandler(BadCredentialsException.class)
	public String exceptionHandler() {
		return "Credentials Invalid !!";
	}

}
