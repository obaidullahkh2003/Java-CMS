package com.obaidullah.cms.repository;

import com.obaidullah.cms.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    List<Roles> findByIdIn(Set<Long> roleIds);
    boolean existsByName(String name);
}
