package org.backend.repository;

import org.backend.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Repository
public interface ParentRepository extends JpaRepository<Parent, String> {

    // Find a parent by their ID
    Parent findById(Long id);

    // Find all parents and order them by the creation date in descending order
    Page<Parent> findAll(Pageable pageable);
}
