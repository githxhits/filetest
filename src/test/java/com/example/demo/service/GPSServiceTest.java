package com.example.demo.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.demo.constant.GPSConstant;
import com.example.demo.domain.GPS;
import com.example.demo.repository.GPSRepository;
import com.example.demo.service.impl.GPSServiceImpl;

@RunWith(JUnit4.class)
public class GPSServiceTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	private GPSRepository gpsRepository;

	@Mock
	private FileStorageService fileStorageService;

	private GPSServiceImpl gpsService;

	@Before
	public void setUp() {
		initMocks(this);
		gpsService = new GPSServiceImpl(gpsRepository, fileStorageService);
	}

	@Test
	public void uploadFileSuccess() {
		String context = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <gpx 	xmlns=\"http://www.topografix.com/GPX/1/1\" version=\"1.1\" creator=\"Exported from Strava via extension e-ivanov.ru\"> 	<metadata> 		<name>name</name> 		<desc>desc</desc> 		<author></author> 		<link href=\"http://e-ivanov.ru/projects/strava-export-gpx/\"> 			<text>Strava gpx export</text> 		</link> 		<time>2020-01-06T19:52:41Z</time> 	</metadata> </gpx>";
		MockMultipartFile file = new MockMultipartFile("user-file", "test.gpx", GPSConstant.FILE_CONTENT_TYPE, context.getBytes());

		doReturn("test.gpx").when(fileStorageService).storeFile(file);
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		gpsService.uploadFile(file);
		
	}

	@Test
	public void getLatestTrackTest() {
		Pageable pageable = new PageRequest(0, 10);
		List<GPS> entityList = new ArrayList<GPS>();
		GPS entity = new GPS();
		entity.setName("name");
		entityList.add(entity);
		Page<GPS> enties = new PageImpl<GPS>(entityList);
		doReturn(enties).when(gpsRepository).findAll(pageable);
		Page<GPS> actual = gpsService.getLatestTrack(pageable);
		assertNotNull(actual);
		assertEquals("name", actual.getContent().get(0).getName());
	}
	
	//TODO

}
