package com.kt_giga_fms.user.repository;

import com.kt_giga_fms.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUserId(String userId);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUserId(String userId);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE " +
           "(:searchKeyword IS NULL OR " +
           "u.name LIKE %:searchKeyword% OR " +
           "u.userId LIKE %:searchKeyword% OR " +
           "u.department LIKE %:searchKeyword%) AND " +
           "(:status IS NULL OR u.status = :status) AND " +
           "(:role IS NULL OR u.role = :role) AND " +
           "(:department IS NULL OR u.department = :department) AND " +
           "(:vehicleType IS NULL OR u.vehicleType = :vehicleType) AND " +
           "(:driverLicense IS NULL OR u.driverLicense = :driverLicense)")
    Page<User> findBySearchCriteria(
            @Param("searchKeyword") String searchKeyword,
            @Param("status") String status,
            @Param("role") String role,
            @Param("department") String department,
            @Param("vehicleType") String vehicleType,
            @Param("driverLicense") String driverLicense,
            Pageable pageable
    );
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.status = 'ACTIVE'")
    long countActiveUsers();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.vehicleType = '법인'")
    long countCorporateVehicleUsers();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.driverLicense LIKE '%1종%'")
    long countClass1LicenseHolders();
    
    List<User> findByStatus(String status);
    
    List<User> findByDepartment(String department);
}

