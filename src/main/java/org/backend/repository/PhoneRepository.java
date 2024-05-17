package org.backend.repository;

import java.util.List;

import org.backend.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, String> {

    // Find a parent by their ID
    List<String> findByUserId(Long userId);
    
    @Modifying
    @Query("UPDATE Phone p SET p.verifyCode = :verifyCode WHERE p.userId = :userId")
    void updateVerifyCodeByUserId(Long userId, String verifyCode);
}
