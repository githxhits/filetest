package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.domain.GPS;
import com.example.demo.service.GPSService;

@RestController
public class GPSController {

	@Autowired
	private GPSService gpsService;

	@PostMapping("/uploadFile")
	public ResponseEntity<Void> uploadFile(@RequestParam("file") MultipartFile file) {
		gpsService.uploadFile(file);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@GetMapping("/downloadFile/{id}")
	public ResponseEntity<Resource> downloadFile(@PathVariable Long id, HttpServletRequest request) {
		Resource resource = gpsService.viewFile(id, request);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@GetMapping("/latest-track")
	public ResponseEntity<Page<GPS>> getLatestTrack(
			@PageableDefault(page = 0, size = 10, sort = "updatedAt", direction = Direction.DESC) Pageable pageable) {
		Page<GPS> trackResponses = gpsService.getLatestTrack(pageable);
		return new ResponseEntity<Page<GPS>>(trackResponses, HttpStatus.OK);
	}

}