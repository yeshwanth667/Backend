package com.example.ElectionManagement.service;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.example.ElectionManagement.dto.UserDto;
import com.example.ElectionManagement.ui.response.dto.Response;

public interface UserService {

	Response addUser(UserDto userDto);

	Response getUserByMobileNumber(String mobileNumber);

	Response deleteUserByMobile(String mobileNumber);

	Response findAll();

	Response updateUser(UserDto userDto);

	Response doLoginProcess(UserDto userDto);

	Response updatePassword(UserDto userDto);

	Response getUserByGender(String gender);

	Response validatingUserEligibilityforVoterCardNumber(String mobileNumber);

	Response listOfUsersWithVerifiedEmail(Boolean status);

	Response sendOtpToEmail(String email) throws AddressException, MessagingException;

	Response verifyOTP(UserDto userDto);
}
