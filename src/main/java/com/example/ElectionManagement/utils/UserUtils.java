package com.example.ElectionManagement.utils;

import java.security.SecureRandom;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.ElectionManagement.entity.User;
import com.example.ElectionManagement.repository.UserRepository;

@Component
public class UserUtils {

	@Autowired
	private UserRepository userRepository;

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	// for generating random user_id and generating verificationToken at runtime

	private final Random RANDOM = new SecureRandom();
	private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	public String generateUserId(int length) {
		return generateRandomString(length);
	}

	public String generateRandomString(int length) {
		StringBuilder returnValue = new StringBuilder(length);
		for (int i = 0; i <= length; i++) {
			returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}
		return new String(returnValue);
	}

	// utility methods to validate input fields

	public boolean checkIfUserRecordExistsWithUserId(String userId) {

		return userRepository.findByUserId(userId) != null ? true : false;

	}

	public boolean checkIfUserRecordExistsWithEmail(String email) {

		return userRepository.findByEmail(email) != null ? true : false;

	}

	public boolean checkIfUserRecordExistsWithMobile(String mobile) {

		return userRepository.findByMobileNumber(mobile) != null ? true : false;

	}

	public boolean checkIfUserRecordExistsWithGender(String gender) {

		return userRepository.findByGenderIgnoreCase(gender) != null ? true : false;

	}

	public boolean checkIfUserExistsWithGivenStatus(Boolean status) {

		return userRepository.findByEmailVerificationStatus(status) != null ? true : false;

	}

	// some common validations for user input fields

	// -----------------------------------------------------------------------

	public boolean inputNotNullAndNotEmpty(String input) {

		if (input.equals(null) || input.isEmpty() || input.isBlank() || input.contains("null")) {

			return true;

		} else {

			return false;

		}
	}

	// ----------------------------------------------------------

	public boolean checkIfLengthEqualsTen(String mobileNumber) {

		if (mobileNumber.length() != 10) {

			return true;

		} else {

			return false;

		}

	}

	// -------------------------------------------------------------

	// GET USER RECORD BY EMAIL IF NEEDED

	public User getUserByEmail(String email) {

		User byEmail = userRepository.findByEmail(email);

		return byEmail;

	}

	// ======================================================================

	// GET USER RECORD BY USERID IF NEEDED

	public User getUserByUserId(String userId) {

		User byUserId = userRepository.findByUserId(userId);

		return byUserId;

	}

	// ======================================================================

	// GET USER RECORD BY USERID IF NEEDED

	public User getUserByMobile(String mobile) {

		User byMobile = userRepository.findByMobileNumber(mobile);

		return byMobile;

	}

	// for sending email

	// Method to generate OTP
	public String generateOTP() {
		Random random = new Random();
		int otpLength = 6; // Length of OTP
		StringBuilder otp = new StringBuilder();
		for (int i = 0; i < otpLength; i++) {
			otp.append(random.nextInt(10)); // Generate random digits
		}
		return otp.toString();
	}

// Method to send email with OTP
	public void sendEmail(String userEmail, String otp) throws AddressException, MessagingException {

		// Get the Properties
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com"); // Replace with your SMTP server
		properties.put("mail.smtp.port", "587"); // Replace with your SMTP server's port
		properties.put("mail.smtp.auth", "true"); // Enable SMTP authentication
		properties.put("mail.smtp.starttls.enable", "true"); // Use STARTTLS for a secure connection
		properties.put("mail.smtp.ssl.trust", "*"); // Trust any SSL certificate (use with caution)
		properties.put("mail.smtp.ssl.protocols", "TLSv1.3");
		properties.put("mail.smtp.ssl.ciphersuites", "TLS_AES_128_GCM_SHA256");

		// Get the Session Object
		Session session = Session.getInstance(properties, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("gurubellisantoshkumar@gmail.com", "yuphdugpjonxbpnx");
			}
		});

		// Compose the Message
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("gurubellisantoshkumar@gmail.com"));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
		message.setSubject("Email Verification OTP");
		message.setText("Your OTP for email verification is: " + otp);
		Transport.send(message);
		// System.out.println("Email sent with OTP: " + otp);

		// Code to send email using JavaMailSender or any other email sending library
		// Example:
		// SimpleMailMessage message = new SimpleMailMessage();
		// message.setTo(userEmail);
		// message.setSubject("Email Verification OTP");
		// message.setText("Your OTP for email verification is: " + otp);
		// javaMailSender.send(message);
		// System.out.println("Email sent with OTP: " + otp); // Example output
	}

// Method to verify OTP
	public boolean verifyOTP(String enteredOTP, String storedOTP) {
		return enteredOTP.equals(storedOTP);
	}

	// Method to send email with OTP
	public String sendEmail(String userEmail) throws AddressException, MessagingException {

		// Get the Properties
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com"); // Replace with your SMTP server
		properties.put("mail.smtp.port", "587"); // Replace with your SMTP server's port
		properties.put("mail.smtp.auth", "true"); // Enable SMTP authentication
		properties.put("mail.smtp.starttls.enable", "true"); // Use STARTTLS for a secure connection
		properties.put("mail.smtp.ssl.trust", "*"); // Trust any SSL certificate (use with caution)
		properties.put("mail.smtp.ssl.protocols", "TLSv1.3");
		properties.put("mail.smtp.ssl.ciphersuites", "TLS_AES_128_GCM_SHA256");

		// Get the Session Object
		Session session = Session.getInstance(properties, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("yeshwanthkrishna665@gmail.com", "bxrx fvfn tapn xeai");
			}
		});

		// Compose the Message
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("gurubellisantoshkumar@gmail.com"));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
		message.setSubject("Email Verification OTP");

		String otp = generateOTP();
		message.setText("Your OTP for email verification is: " + otp);
		Transport.send(message);
		return otp;
	}

}
