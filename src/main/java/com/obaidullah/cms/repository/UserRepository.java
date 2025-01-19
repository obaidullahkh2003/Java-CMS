package com.obaidullah.cms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.obaidullah.cms.model.Users;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    List<Users> findByEmail(String email);
    Optional<Users> findFirstByEmail(String email);
    Boolean existsByEmail(String email);
}
