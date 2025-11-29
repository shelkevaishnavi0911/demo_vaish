package com.example.demo.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.ApiConfig;
import com.example.demo.entity.TempUsers;
import com.example.demo.repo.ConfigRepository;
import com.example.demo.repo.UserRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CalendlyService {


	@Autowired
	ConfigRepository configRepository;

	@Autowired
	GenericApiService genericApiService;

	@Autowired
	UserRepo userRepo;

	private final ObjectMapper mapper = new ObjectMapper();

	public CalendlyService(ConfigRepository configRepository, GenericApiService genericApiService,
			UserRepo tempUserRepository) {
		this.configRepository = configRepository;
		this.genericApiService = genericApiService;
		this.userRepo = userRepo;
	}

	public List<TempUsers> fetchUsers(String token) throws Exception {
		ApiConfig cfg = configRepository.findByAppName("calendly")
				.orElseThrow(() -> new RuntimeException("Calendly config not found in DB"));

		String endpoint = cfg.getEndpoint();
		String method = cfg.getMethod();

		String headersJson = cfg.getHeaders();
		if (headersJson != null && headersJson.contains("{{token}}")) {
			headersJson = headersJson.replace("{{token}}", token);
		}

		String resp = genericApiService.callApi(endpoint, method, headersJson);

		JsonNode root = mapper.readTree(resp);
		List<TempUsers> saved = new ArrayList<>();

		JsonNode arr = root.path("collection");
		if (!arr.isArray() || arr.size() == 0) {
			arr = root.path("data");
		}

		if (arr.isArray()) {
			for (JsonNode u : arr) {

				TempUsers tu = new TempUsers();
				tu.setAppName("calendly");
				tu.setUserId(u.path("uri").asText(u.path("id").asText(null)));
				tu.setName(u.path("name").asText(null));
				tu.setEmail(u.path("email").asText(null));
				tu.setRawJson(u.toString());
				saved.add(userRepo.save(tu));
			}
		} else {

			TempUsers tu = new TempUsers();
			tu.setAppName("calendly");
			tu.setUserId(root.path("uri").asText(root.path("id").asText(null)));
			tu.setName(root.path("name").asText(null));
			tu.setEmail(root.path("email").asText(null));
			tu.setRawJson(root.toString());
			saved.add(userRepo.save(tu));
		}

		return saved;
	}

}
