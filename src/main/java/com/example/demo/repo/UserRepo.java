package com.example.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.TempUsers;

@Repository
public interface UserRepo extends JpaRepository<TempUsers, Long> {
	List<TempUsers> findByAppName(String appName);

}
