package com.example.ElectionManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ElectionManagement.entity.User;
import java.util.List;
import java.time.LocalDate;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	// custom finder methods (implementation given by JPA)
	List<User> findByDateOfBirth(LocalDate dateOfBirth);

	List<User> findByEmailVerificationStatus(Boolean emailVerificationStatus);

	List<User> findByGenderIgnoreCase(String gender);
	
	User findByMobileNumber(String mobileNumber);

	User findByUserId(String userId);
	
	User findByEmail(String email);
}
