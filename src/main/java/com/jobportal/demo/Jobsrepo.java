package com.jobportal.demo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Jobsrepo extends JpaRepository<Jobs, Integer>{
	java.util.List<Jobs> findAll();
}