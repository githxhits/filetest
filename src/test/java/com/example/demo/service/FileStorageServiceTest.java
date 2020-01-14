package com.example.demo.service;

import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import com.example.demo.config.FileStorageProperties;
import com.example.demo.exception.FileStorageException;
import com.example.demo.service.impl.FileStorageServiceImpl;

@RunWith(JUnit4.class)
public class FileStorageServiceTest {

	private FileStorageServiceImpl fileStorageService;

	@Before
	public void setUp() {
		initMocks(this);
		FileStorageProperties fileStorageProperties = new FileStorageProperties();
		fileStorageProperties.setUploadDir("resources/files");
		fileStorageService = new FileStorageServiceImpl(fileStorageProperties);
	}

	@Test
	public void storeFileSuccess() {
		MockMultipartFile file = new MockMultipartFile("user-file", "test.gpx", null, "test data".getBytes());
		String actual = fileStorageService.storeFile(file);
		Assert.assertNotNull(actual);
	}

	@Test(expected = FileStorageException.class)
	public void storeFileFail() {
		MockMultipartFile file = new MockMultipartFile("user-file", "test..gpx", null, "test data".getBytes());
		String actual = fileStorageService.storeFile(file);
		Assert.assertEquals("test.gpx", actual);
	}

	@Test
	public void loadFileAsResourceSuccess() {
		Resource actual = fileStorageService.loadFileAsResource("test.gpx");
		Assert.assertNotNull(actual);
		Assert.assertEquals("test.gpx", actual.getFilename());
	}

	// TODO
}
