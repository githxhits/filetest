package com.example.demo.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.domain.GPS;

public interface GPSService {

	public void uploadFile(MultipartFile file);

	public Page<GPS> getLatestTrack(Pageable pageable);
	
	public Resource viewFile(Long id, HttpServletRequest request);
}
