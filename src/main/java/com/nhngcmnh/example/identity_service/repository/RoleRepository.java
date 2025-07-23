package com.nhngcmnh.example.identity_service.repository;

import com.nhngcmnh.example.identity_service.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    @Query("SELECT r FROM Role r LEFT JOIN FETCH r.permissions WHERE r.name = :name")
    Optional<Role> findByIdFetchPermissions(@Param("name") String name);

    @Query("SELECT DISTINCT r FROM Role r LEFT JOIN FETCH r.permissions")
    List<Role> findAllFetchPermissions();
}
