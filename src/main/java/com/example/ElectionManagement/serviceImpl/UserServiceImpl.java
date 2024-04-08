package com.example.ElectionManagement.serviceImpl;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ElectionManagement.dto.UserDto;
import com.example.ElectionManagement.entity.User;
import com.example.ElectionManagement.repository.UserRepository;
import com.example.ElectionManagement.service.UserService;
import com.example.ElectionManagement.ui.response.dto.Response;
import com.example.ElectionManagement.utils.UserUtils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserUtils utils;

	// setters and getters

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public BCryptPasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public UserUtils getUtils() {
		return utils;
	}

	public void setUtils(UserUtils utils) {
		this.utils = utils;
	}

	// ===============================================================

	// ADD USER API

	@Override
	public Response addUser(UserDto userDto) {

		try {

			User user = new User();

			BeanUtils.copyProperties(userDto, user);

			String encode = passwordEncoder.encode(userDto.getPassword());
			user.setPassword(encode);

			user.setEmailVerificationStatus(false);
			user.setUserId(utils.generateUserId(15));
			user.setEmailVerificationToken(utils.generateRandomString(15));
			UserDto newUserDto = new UserDto();

			LocalDate dob = userDto.getDateOfBirth();

			int age = Period.between(dob, LocalDate.now()).getYears();
			user.setAge(age);

			User save = userRepository.save(user);

			BeanUtils.copyProperties(save, newUserDto);

			return new Response("User Added Successfully", newUserDto, 201, "CREATED");

		} catch (Exception e) {

			return new Response("Exception Occured in Service Layer...", null, 500, "INTERNAL_SERVER_ERROR");

		}
	}

	// ==============================================================================

	// GET USER BY MOBILE API

	@Override
	public Response getUserByMobileNumber(String mobileNumber) {

		try {

			User byMobileNumber = userRepository.findByMobileNumber(mobileNumber);

			UserDto userDto = new UserDto();
			int age = byMobileNumber.getAge();
			// LocalDate dob = byMobileNumber.getDateOfBirth();

			// int age = Period.between(dob, LocalDate.now()).getYears();
			userDto.setAge(age);

			BeanUtils.copyProperties(byMobileNumber, userDto);

			return new Response("User Record Retrieved Successfully..", userDto, 200, "OK");

		} catch (Exception e) {

			return new Response("Exception Occured in Service Layer...", null, 500, "INTERNAL_SERVER_ERROR");

		}
	}

	// ================================================================================

	// DELETE USER BY MOBILE

	@Override
	public Response deleteUserByMobile(String mobileNumber) {

		try {

			User byMobileNumber = userRepository.findByMobileNumber(mobileNumber);

			UserDto userDto = new UserDto();

			int age = byMobileNumber.getAge();

			// LocalDate dob = byMobileNumber.getDateOfBirth();

			// int age = Period.between(dob, LocalDate.now()).getYears();
			userDto.setAge(age);

			BeanUtils.copyProperties(byMobileNumber, userDto);

			userRepository.delete(byMobileNumber);

			return new Response("User Deleted Successfully..", byMobileNumber, 200, "OK");

		} catch (Exception e) {

			return new Response("Exception Occured in Service Layer..", null, 500, "INTERNAL_SERVER_ERROR");

		}

	}

	// ==============================================================================

	// FIND ALL RECORDS IN DATABASE API

	@Override
	public Response findAll() {

		try {

			List<User> all = userRepository.findAll();

			List<UserDto> dtoList = new ArrayList<UserDto>();

			if (all.isEmpty()) {

				return new Response("No Records To Show.. ", null, 404, "RESOURCE_NOT_FOUND");

			} else {

				for (User user : all) {

					UserDto userDtoNew = new UserDto();
					int age = user.getAge();

					// LocalDate dob = user.getDateOfBirth();

					// int age = Period.between(dob, LocalDate.now()).getYears();
					userDtoNew.setAge(age);

					BeanUtils.copyProperties(user, userDtoNew);
					dtoList.add(userDtoNew);

				}

				return new Response("List Of Users Found...", dtoList, 200, "OK");

			}

		} catch (Exception e) {

			return new Response("Exception Occured in Service Layer..", null, 500, "INTERNAL_SERVER_ERROR");

		}
	}

	// ============================================================================

	// UPDATE USER DETAILS API

	@Override
	public Response updateUser(UserDto userDto) {

		try {

			Optional<User> userRecord = userRepository.findById(userDto.getId());

			UserDto userDtoNew = new UserDto();

			BeanUtils.copyProperties(userDto, userRecord);

			User user = userRecord.get();
			user.setAddress(userDto.getAddress());
			user.setEmailVerificationStatus(userDto.getEmailVerificationStatus());
			user.setFullName(userDto.getFullName());
			user.setGender(userDto.getGender());
			user.setId(userDto.getId());
			user.setDateOfBirth(userDto.getDateOfBirth());
			user.setMobileNumber(userDto.getMobileNumber());
			LocalDate dateOfBirth = userDto.getDateOfBirth();
			int years = Period.between(dateOfBirth, LocalDate.now()).getYears();
			user.setAge(years);

			User save = userRepository.save(user);

			// LocalDate dob = save.getDateOfBirth();

			// int age = Period.between(dob, LocalDate.now()).getYears();
			// userDtoNew.setAge(age);

			BeanUtils.copyProperties(save, userDtoNew);

			return new Response("User Record Updated Successfully...", userDtoNew, 200, "OK");

		} catch (Exception e) {

			return new Response("Exception Occured in Service Layer..", null, 500, "INTERNAL_SERVER_ERROR");

		}
	}

	// ==============================================================================

	// LOGIN API

	@Override
	public Response doLoginProcess(UserDto userDto) {

		try {

			User byEmail = userRepository.findByEmail(userDto.getEmail());

			UserDto userDtoNew = new UserDto();
			int age = byEmail.getAge();
			// LocalDate dob = byEmail.getDateOfBirth();

			// int age = Period.between(dob, LocalDate.now()).getYears();
			userDtoNew.setAge(age);

			BeanUtils.copyProperties(byEmail, userDtoNew);

			return new Response("User Login Successfully Done...", userDtoNew, 200, "OK");

		} catch (Exception e) {

			return new Response("Exception Occured in Service Layer", null, 500, "INTERNAL_SERVER_ERROR");

		}
	}

	// ====================================================================================

	// UPDATE PASSWORD API

	@Override
	public Response updatePassword(UserDto userDto) {

		try {

			User userByUserId = utils.getUserByUserId(userDto.getUserId());

			String encode = passwordEncoder.encode(userDto.getPassword());

			userByUserId.setPassword(encode);

			User save = userRepository.save(userByUserId);

			UserDto userDtoNew = new UserDto();
			// LocalDate dob = save.getDateOfBirth();

			// int age = Period.between(dob, LocalDate.now()).getYears();
			// userDtoNew.setAge(age);

			BeanUtils.copyProperties(save, userDtoNew);

			return new Response("User Password Updated Successfully...", userDtoNew, 200, "OK");

		} catch (Exception e) {

			return new Response("Exception Occured in Service Layer...", null, 500, "INTERNAL_SERVER_ERROR");

		}

	}

	// ================================================================================

	// GET USER DETAILS BY GENDER API

	@Override
	public Response getUserByGender(String gender) {

		try {

			List<User> byGenderIgnoreCase = userRepository.findByGenderIgnoreCase(gender);

			if (byGenderIgnoreCase.isEmpty() || byGenderIgnoreCase.equals(null)) {

				return new Response("No Records For this Option To Show..", null, 404, "RESOURCE_NOT_FOUND");

			}

			List<UserDto> userDtoList = new ArrayList<UserDto>();

			for (User newUser : byGenderIgnoreCase) {
				UserDto userDtoNew = new UserDto();

				int age2 = newUser.getAge();

				// int age = Period.between(dob, LocalDate.now()).getYears();
				userDtoNew.setAge(age2);

				BeanUtils.copyProperties(newUser, userDtoNew);
				userDtoList.add(userDtoNew);
			}

			return new Response("Records Retrived Successfully..", userDtoList, 200, "OK");

		} catch (Exception e) {

			return new Response("Exception Occured in Service Layer...", null, 500, "INTERNAL_SERVER_ERROR");

		}

	}

	// ====================================================================================

	// GET LIST OF USERS WHOSE EMAIL VERIFIED OR NOT API

	@Override
	public Response listOfUsersWithVerifiedEmail(Boolean status) {

		try {

			List<UserDto> userDtoList = new ArrayList<UserDto>();

			List<User> byEmailVerificationStatus = userRepository.findByEmailVerificationStatus(status);

			if (byEmailVerificationStatus.isEmpty() || byEmailVerificationStatus.equals(null)) {

				return new Response("No Records to Show...", null, 404, "RESOURCE_NOT_FOUND");

			}

			for (User userList : byEmailVerificationStatus) {

				UserDto userDto = new UserDto();
				int age2 = userList.getAge();

				// int age = Period.between(dob, LocalDate.now()).getYears();
				userDto.setAge(age2);

				BeanUtils.copyProperties(userList, userDto);

				userDtoList.add(userDto);

			}

			return new Response("Users With Email Verification Status Retrieved..", userDtoList, 200, "OK");

		} catch (Exception e) {

			return new Response("Exception Occured in Service Layer..", null, 500, "INTERNAL_SERVER_ERROR");

		}
	}

	// =============================================================================================

	// PRINT VOTER CARD IF HE IS ELEIGIBLE FOR VOTE

	@Override
	public Response validatingUserEligibilityforVoterCardNumber(String mobileNumber) {

		try {

			User byMobileNumber = userRepository.findByMobileNumber(mobileNumber);

			UserDto userDto = new UserDto();

			int age2 = byMobileNumber.getAge();

			// int age = Period.between(dob, LocalDate.now()).getYears();
			userDto.setAge(age2);
			BeanUtils.copyProperties(byMobileNumber, userDto);

			if (age2 >= 18) {

				return new Response("You are Eligible...", userDto, 200, "OK");

			} else {

				return new Response("Users Age is Not Eligible to Apply For Voter Card..", age2, 400, "BAD_REQUEST");

			}

		} catch (Exception e) {

			return new Response("Exception Occured in Service Layer..", null, 500, "INTERNAL_SERVER_ERROR");

		}

	}

	// ====================================================================================

	@Override
	public Response sendOtpToEmail(String email) {

		try {

			User userByEmail = utils.getUserByEmail(email);

			String sendEmail = utils.sendEmail(email);

			userByEmail.setOtp(sendEmail);

			userRepository.save(userByEmail);

			return new Response("Email With OTP send Successfully..", sendEmail, 200, "OK");

		} catch (Exception e) {

			return new Response("Exception Occured in Service Layer..", null, 500, "INTERNAL_SERVER_ERROR");

		}
	}

	// ====================================================================================

	@Override
	public Response verifyOTP(UserDto userDto) {

		try {

			User userByEmail = utils.getUserByEmail(userDto.getEmail());

			boolean verifyOTP = utils.verifyOTP(userDto.getOtp(), userByEmail.getOtp());

			if (verifyOTP) {

				userByEmail.setEmailVerificationStatus(true);

				userRepository.save(userByEmail);

				return new Response("User Email Verification Status Updated Successfully..", null, 200, "OOK");

			} else {

				return new Response("User Entered OTP doesnot Match with Actual OTP..", null, 400, "BAD_REQUEST");

			}

		} catch (Exception e) {

			return new Response("Exception Occured in Service Layer...", null, 500, "INTERNAL_SERVER_ERROR");

		}
	}

}
