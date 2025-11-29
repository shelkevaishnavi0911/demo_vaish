package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.TempUsers;
import com.example.demo.serviceImpl.CalendlyService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/integration")
@RequiredArgsConstructor
public class IntegrationController {
	
	@Autowired
	 CalendlyService calendlyService;

	  public IntegrationController(CalendlyService calendlyService) {
	        this.calendlyService = calendlyService;
	    }

	  
	    @GetMapping("/fetch/calendly/users")
	    public ResponseEntity<List<TempUsers>> fetchCalendlyUsers(@RequestParam String token) throws Exception {
	        List<TempUsers> users = calendlyService.fetchUsers(token);
	        return ResponseEntity.ok(users);
	    }

}
