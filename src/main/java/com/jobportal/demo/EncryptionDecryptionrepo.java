package com.jobportal.demo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EncryptionDecryptionrepo extends JpaRepository<EncryptDecrypt, Integer>{
	java.util.List<EncryptDecrypt> findAll();
}