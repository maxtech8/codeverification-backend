package org.backend.repository;

import java.util.List;
import org.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // Find a parent by their ID
    User findById(Long id);

    // Find all users and order them by the creation date in descending order
    Page<User> findAll(Pageable pageable);
    
    // Find all users with username and password
    List<User> findByEmailAndPassword(String email, String password);
    
    @Query("SELECT c FROM User c WHERE c.email = :email")
    List<User> findByEmail(String email);
}
