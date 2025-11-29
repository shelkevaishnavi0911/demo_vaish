package com.example.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.ApiConfig;

@Repository
public interface ConfigRepository extends JpaRepository<ApiConfig, Long> {
	 Optional<ApiConfig> findByAppName(String appName);

}
