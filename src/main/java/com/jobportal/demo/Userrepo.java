package com.jobportal.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Userrepo extends JpaRepository<User,Long> {
	User findByEmail(String email);

}
