package com.fundooproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fundooproject.model.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);
	Optional<User> findByEmailAndPassword(String email, String password);
	Optional<User> findById(long Id);


}
