package com.example.ElectionManagement.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ElectionManagement.dto.UserDto;
import com.example.ElectionManagement.entity.User;
import com.example.ElectionManagement.service.UserService;
import com.example.ElectionManagement.ui.request.dto.AddUser;
import com.example.ElectionManagement.ui.request.dto.LoginDto;
import com.example.ElectionManagement.ui.request.dto.UpdatePasswordDto;
import com.example.ElectionManagement.ui.request.dto.UpdateUserDto;
import com.example.ElectionManagement.ui.request.dto.VerifyOtpDto;
import com.example.ElectionManagement.ui.response.dto.Response;
import com.example.ElectionManagement.utils.UserUtils;

/*
 * 
 *   User Controller It Handles all the User Functionalities related to Election Management Application.
 * 
 *   Controller Layer Itself validate those input fields from user and return with proper error messages.
 *   
 *   So we can make use of proper data in Service Layer to implement business logic of that particular request.
 * 
 */

@RestController
@RequestMapping("/api/elections")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserUtils utils;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	// setters and getters

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

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	// API'S
	// ======================================================================================

	// REGISTER USER API

	/*
	 * The Add-User API will validate the input fileds from user and sent to
	 * database only if the data is correct
	 * 
	 * i.e. No Null values or NO Duplicate data should not eneteres into Database.
	 * 
	 */

	@PostMapping("/addUser")
	public Response addUser(@RequestBody AddUser addUser) {

		try {

			// validating those input fields ensure no null values coming from user ...

			boolean inputNotNullAndNotEmpty = utils.inputNotNullAndNotEmpty(addUser.getFullName());

			if (inputNotNullAndNotEmpty) {

				return new Response("Enter Valid Full Name can not be empty or null", null, 400, "BAD_REQUEST");

			}

			boolean inputNotNullAndNotEmpty2 = utils.inputNotNullAndNotEmpty(addUser.getAddress());

			if (inputNotNullAndNotEmpty2) {

				return new Response("Enter Valid Address can not be empty or null", null, 400, "BAD_REQUEST");

			}

			boolean inputNotNullAndNotEmpty3 = utils.inputNotNullAndNotEmpty(addUser.getEmail());

			if (inputNotNullAndNotEmpty3) {

				return new Response("Enter Valid Email can not be empty or null", null, 400, "BAD_REQUEST");

			}

			boolean inputNotNullAndNotEmpty6 = utils.inputNotNullAndNotEmpty(addUser.getMobileNumber());

			if (inputNotNullAndNotEmpty6) {

				return new Response("Enter Valid Mobile can not be empty or null", null, 400, "BAD_REQUEST");

			}

			boolean inputNotNullAndNotEmpty4 = utils.inputNotNullAndNotEmpty(addUser.getGender());

			if (inputNotNullAndNotEmpty4) {

				return new Response("Enter Valid Gender can not be empty or null", null, 400, "BAD_REQUEST");

			}

			boolean inputNotNullAndNotEmpty5 = utils.inputNotNullAndNotEmpty(addUser.getPassword());

			if (inputNotNullAndNotEmpty5) {

				return new Response("Enter Valid Password can not be empty or null", null, 400, "BAD_REQUEST");

			}

			// In this make sure that No Record with the particular email already exists in
			// database.

			boolean checkIfUserRecordExistsWithEmail = utils.checkIfUserRecordExistsWithEmail(addUser.getEmail());

			if (checkIfUserRecordExistsWithEmail) {

				return new Response("User Already Exists...with given email", null, 409, "CONFLICT");

			}

			// In this make sure that No Record with the particular mobile already exists in
			// database.

			boolean checkIfUserRecordExistsWithMobile = utils
					.checkIfUserRecordExistsWithMobile(addUser.getMobileNumber());

			if (checkIfUserRecordExistsWithMobile) {

				return new Response("User Already Exists...with given mobile", null, 409, "CONFLICT");

			}

			// After ensuring all the fields are not null and no duplicate record found
			// during registration we are sending data to database.

			UserDto userDto = new UserDto();

			BeanUtils.copyProperties(addUser, userDto);

			Response user = userService.addUser(userDto);

			return user;

		} catch (Exception e) {

			return new Response("Exception Occured in Controller Layer...while Adding User to Database...", null, 500,
					"INTERNAL_SERVER_ERROR");

		}
	}

	// ====================================================================================

	// UPDATE USER API

	@PutMapping("/updateUser")
	public Response updateUser(@RequestBody UpdateUserDto updateUserDto) {

		try {

			boolean inputNotNullAndNotEmpty = utils.inputNotNullAndNotEmpty(updateUserDto.getAddress());

			if (inputNotNullAndNotEmpty) {

				return new Response("Address Can not be null or Empty..", null, 400, "BAD_REQUEST");

			}

			boolean inputNotNullAndNotEmpty2 = utils.inputNotNullAndNotEmpty(updateUserDto.getFullName());

			if (inputNotNullAndNotEmpty2) {

				return new Response("Full Name Can not be null or Empty..", null, 400, "BAD_REQUEST");

			}

			boolean inputNotNullAndNotEmpty3 = utils.inputNotNullAndNotEmpty(updateUserDto.getGender());

			if (inputNotNullAndNotEmpty3) {

				return new Response("Gender Can not be null or Empty..", null, 400, "BAD_REQUEST");

			}

			boolean checkIfUserRecordExistsWithEmail = utils.checkIfUserRecordExistsWithEmail(updateUserDto.getEmail());

			if (checkIfUserRecordExistsWithEmail) {

				UserDto userDto = new UserDto();

				BeanUtils.copyProperties(updateUserDto, userDto);

				Response updateUser = userService.updateUser(userDto);

				return updateUser;

			} else {

				return new Response("User Record Not Exists..", null, 404, "RESOURCE_NOT_FOUND");

			}

		} catch (Exception e) {

			return new Response("Exception Occured in Controller Layer..", null, 500, "INTERNAL_SERVER_ERROR");

		}

	}

	// ====================================================================================

	// GET ALL USERS API

	@GetMapping("/getAllUsers")
	public Response getAllUsers() {

		try {

			Response all = userService.findAll();

			return all;

		} catch (Exception e) {

			return new Response("Exception Occured in Controller Layer...", null, 500, "INTERNAL_SERVER_ERROR");

		}

	}

	// ==============================================================================================

	// LOGIN API

	@PostMapping("/login")
	public Response doLoginProcess(@RequestBody LoginDto loginDto) {

		try {

			boolean inputNotNullAndNotEmpty = utils.inputNotNullAndNotEmpty(loginDto.getEmail());

			if (inputNotNullAndNotEmpty) {

				return new Response("Email Can not be null...", null, 400, "BAD_REQUEST");

			}

			boolean inputNotNullAndNotEmpty2 = utils.inputNotNullAndNotEmpty(loginDto.getPassword());

			if (inputNotNullAndNotEmpty2) {

				return new Response("Password Can not be null...", null, 400, "BAD_REQUEST");

			}

			boolean checkIfUserRecordExistsWithEmail = utils.checkIfUserRecordExistsWithEmail(loginDto.getEmail());

			if (checkIfUserRecordExistsWithEmail) {

				User user = utils.getUserByEmail(loginDto.getEmail());
				String encryptedPassword = user.getPassword();
				String rawPassowrd = loginDto.getPassword();

				boolean matches = passwordEncoder.matches(rawPassowrd, encryptedPassword);

				if (!(matches)) {

					return new Response("Credentials Mismatch.. Kindly Enter Valid Credentials..", null, 401,
							"UN_AUTHORIZED");

				}

				UserDto userDto = new UserDto();

				BeanUtils.copyProperties(loginDto, userDto);

				Response doLoginProcess = userService.doLoginProcess(userDto);

				return doLoginProcess;

			} else {

				return new Response("User Not Found With Given Email.. Kindly Register..", null, 404,
						"RESOURCE_NOT-FOUND");

			}

		} catch (Exception e) {

			return new Response("Exception Occured in Controller Layer...while Login User", null, 500,
					"INTERNAL_SERVER_ERROR");
		}

	}

	// =============================================================================================

	// UPDATE PASSWORD API

	@PatchMapping("/updatePassword")
	public Response updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto) {

		try {

			boolean inputNotNullAndNotEmpty = utils.inputNotNullAndNotEmpty(updatePasswordDto.getUserId());

			if (inputNotNullAndNotEmpty) {

				return new Response("UserId Can not be null or Empty...", null, 400, "BAD_REQUEST");

			}

			boolean inputNotNullAndNotEmpty2 = utils.inputNotNullAndNotEmpty(updatePasswordDto.getPassword());

			if (inputNotNullAndNotEmpty2) {

				return new Response("Password Can not be null or Empty...", null, 400, "BAD_REQUEST");

			}

			UserDto userDto = new UserDto();

			boolean checkIfUserRecordExistsWithUserId = utils
					.checkIfUserRecordExistsWithUserId(updatePasswordDto.getUserId());

			if (checkIfUserRecordExistsWithUserId) {

				BeanUtils.copyProperties(updatePasswordDto, userDto);

				Response updatePassword = userService.updatePassword(userDto);

				return updatePassword;

			} else {

				return new Response("UserId Not Exists.. Unable to Update Password..", null, 404, "RESOURCE_NOT_FOUND");

			}
		} catch (Exception e) {

			return new Response("Exception Occured in Controller Layer...", null, 500, "INTERNAL_SERVER_ERROR");

		}

	}

	// ==============================================================================================

	// GET USERS BY GENDER API

	@GetMapping("/getUserByGender/{gender}")
	public Response getUsersByGender(@PathVariable String gender) {

		try {

			boolean inputNotNullAndNotEmpty = utils.inputNotNullAndNotEmpty(gender);

			if (inputNotNullAndNotEmpty) {

				return new Response("Gender Can not be NUll...", null, 400, "BAD_REQUEST");

			}

			boolean checkIfUserRecordExistsWithGender = utils.checkIfUserRecordExistsWithGender(gender);

			if (checkIfUserRecordExistsWithGender) {

				Response userByGender = userService.getUserByGender(gender);

				return userByGender;

			} else {

				return new Response("No Record Found With Given Gender...", null, 404, "RESOURCE_NOT_FOUND");

			}
		} catch (Exception e) {

			return new Response("Exception Occured in Controller Layer...", null, 500, "INTERNAL_SERVER_ERROR");

		}

	}

	// ==============================================================================================

	// VALIDATE USER IF HE ELEIGIBLE TO GET VOTER CARD API

	@GetMapping("/validateUser/{mobileNumber}")
	public Response validateUserIfHeEligibleForVoterCard(String mobileNumber) {

		try {

			if (utils.inputNotNullAndNotEmpty(mobileNumber)) {
				
				return new Response("Mobile Can not be null or Empty.", null, 400, "BAD_REQUEST");
				
			}

			boolean checkIfUserRecordExistsWithMobile = utils.checkIfUserRecordExistsWithMobile(mobileNumber);

			if (checkIfUserRecordExistsWithMobile) {

				Response validatingUserEligibilityforVoterCard = userService
						.validatingUserEligibilityforVoterCardNumber(mobileNumber);

				return validatingUserEligibilityforVoterCard;

			} else {

				return new Response("User Not Found With Given Mobile Number..", null, 404, "RESOURCE_NOT_FOUND");

			}

		} catch (Exception e) {

			return new Response("Exception Occured in Controller Layer...", null, 500, "INTERNAL_SERVER_ERROR");

		}

	}

	// =================================================================================================

	// GET LIST OF USERS WITH BASED ON VERIFICATION OF EMAIL

	@GetMapping("/getListOfUsers/{emailVerificationStatus}")
	public Response getListOfUsersBasedOnEmailVerification(Boolean status) {

		try {

			Response listOfUsersWithVerifiedEmail = userService.listOfUsersWithVerifiedEmail(status);

			return listOfUsersWithVerifiedEmail;

		} catch (Exception e) {

			return new Response("Exception Occured in Controller Layer..", null, 500, "INTERNAL_SERVER_ERROR");

		}

	}

	// ===================================================================================================

	// GET USER BY MOBILE NUMBER

	@GetMapping("/getUserByMobile/{mobileNumber}")
	public Response getUserByMobile(@PathVariable String mobileNumber) {

		try {

			boolean checkIfLengthEqualsTen = utils.checkIfLengthEqualsTen(mobileNumber);

			if (checkIfLengthEqualsTen) {

				return new Response("Mobile Number Length Must be 10 digits", null, 400, "BAD_REQUEST");

			}

			User userByMobile = utils.getUserByMobile(mobileNumber);

			if (userByMobile != null) {

				Response userByMobileNumber = userService.getUserByMobileNumber(mobileNumber);

				return userByMobileNumber;

			} else {

				return new Response("User with Mobile Not Found..", null, 404, "RESOURCE_NOT_FOUND");
			}
		} catch (Exception e) {

			return new Response("Exception Occured in Controller Layer..", null, 500, "INTERNAL_SERVER_ERROR");

		}

	}

	// =================================================================================================

	// DELETE USER BY MOBILE NUMBER API

	@DeleteMapping("/deleteUser/{mobileNumber}")
	public Response deleteUserByMobile(@PathVariable String mobileNumber) {

		try {

			boolean checkIfLengthEqualsTen = utils.checkIfLengthEqualsTen(mobileNumber);

			if (checkIfLengthEqualsTen) {

				return new Response("Mobile NUmber must be 10 digits", null, 400, "BAD_REQUEST");

			}

			boolean checkIfUserRecordExistsWithMobile = utils.checkIfUserRecordExistsWithMobile(mobileNumber);

			if (checkIfUserRecordExistsWithMobile) {

				Response deleteUserByMobile = userService.deleteUserByMobile(mobileNumber);

				return deleteUserByMobile;

			} else {

				return new Response("User with Mobile Record Not Found..", null, 404, "RESOURCE_NOT_FOUND");

			}

		} catch (Exception e) {

			return new Response("Exception Occured in Controller Layer..", null, 500, "INTERNAL_SERVER_ERROR");

		}

	}

	// API FOR SENDING OTP TO EMAIL FOR VERIFICATION AND STORE IN DB..
	@PostMapping("/send-email/{email}")
	public Response sendOtpToEmail(@PathVariable String email) {

		try {

			boolean inputNotNullAndNotEmpty = utils.inputNotNullAndNotEmpty(email);

			if (inputNotNullAndNotEmpty) {

				return new Response("Email Can not be Null or Empty..", null, 400, "BAD_REQUEST");

			}

			boolean checkIfUserRecordExistsWithEmail = utils.checkIfUserRecordExistsWithEmail(email);

			if (checkIfUserRecordExistsWithEmail) {

				Response sendOtpToEmail = userService.sendOtpToEmail(email);

				return sendOtpToEmail;

			} else {

				return new Response("User Not Exists With Given Email...", null, 404, "RESOURCE_NOT_FOUND");

			}

		} catch (Exception e) {

			return new Response("Exception Occured in Controller Layer..", null, 500, "INTERNAL_SERVER_ERROR");

		}
	}

	// API FOR VERIFYING OTP THE ENTERED OTP BY USER
	@PostMapping("/verify-otp")
	public Response verifyOTP(@RequestBody VerifyOtpDto verifyOtpDto) {

		try {

			UserDto uerDto = new UserDto();

			boolean inputNotNullAndNotEmpty = utils.inputNotNullAndNotEmpty(verifyOtpDto.getEmail());

			if (inputNotNullAndNotEmpty) {

				return new Response("Email Can not be Null or Empty...", null, 400, "BAD_REQUEST");

			}

			boolean inputNotNullAndNotEmpty2 = utils.inputNotNullAndNotEmpty(verifyOtpDto.getOtp());

			if (inputNotNullAndNotEmpty2) {

				return new Response("Otp Can not be Null or Empty...", null, 400, "BAD_REQUEST");

			}

			BeanUtils.copyProperties(verifyOtpDto, uerDto);

			Response verifyOTP = userService.verifyOTP(uerDto);

			return verifyOTP;

		} catch (Exception e) {

			return new Response("Exception Occured in Service Layer...", null, 500, "INTERNAL_SERVER_ERROR");

		}
	}

}
