package com.example.jobtracker.repository;

import com.example.jobtracker.model.Application;
import com.example.jobtracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByUser(User user);
}