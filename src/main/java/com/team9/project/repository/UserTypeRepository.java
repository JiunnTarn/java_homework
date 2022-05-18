package com.team9.project.repository;

import com.team9.project.models.EUserType;
import com.team9.project.models.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Long> {
    UserType findByName(EUserType name);
}