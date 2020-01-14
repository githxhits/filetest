package com.example.demo.service.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.example.demo.constant.GPSConstant;
import com.example.demo.domain.GPS;
import com.example.demo.exception.FileStorageException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.GPSRepository;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.GPSService;
import com.example.demo.utils.DateUtils;

@Service
public class GPSServiceImpl implements GPSService {

	private static final Logger logger = LoggerFactory.getLogger(GPSServiceImpl.class);

	private GPSRepository gpsRepository;

	private FileStorageService fileStorageService;

	@Autowired
	public GPSServiceImpl(GPSRepository gpsRepository, FileStorageService fileStorageService) {
		this.gpsRepository = gpsRepository;
		this.fileStorageService = fileStorageService;
	}

	@Override
	public void uploadFile(MultipartFile file) {
		// TODO Auto-generated method stub
		try {
			validation(file);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file.getInputStream());
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName(GPSConstant.GPS_METADATA);
			if (nList.getLength() < 1) {
				throw new FileStorageException("Must have metadata tag for description gpx file");
			}
			
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					GPS gps = new GPS();
					String name = getValue(eElement, GPSConstant.GPS_METADATA_NAME);
					if (StringUtils.isEmpty(name)) {
						throw new FileStorageException("Name tag in metadata can not be null");
					}
					gps.setName(getValue(eElement, GPSConstant.GPS_METADATA_NAME));
					gps.setDescription(getValue(eElement, GPSConstant.GPS_METADATA_DESC));
					gps.setAuthor(getValue(eElement, GPSConstant.GPS_METADATA_AUTHOR));
					String time = getValue(eElement, GPSConstant.GPS_METADATA_TIME);
					if (!StringUtils.isEmpty(time)) {
						gps.setTime(DateUtils.getTimestamp(time));
					}
					gps.setCreatedAt(new Timestamp(new Date().getTime()));
					gps.setUpdatedAt(new Timestamp(new Date().getTime()));

					String fileName = fileStorageService.storeFile(file);
					if (!StringUtils.isEmpty(fileName)) {
						gps.setFileName(fileName);
						gps.setFileSize(file.getSize());
						gpsRepository.save(gps);
					}
				}
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Parser Configuration Exception ", e);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("SAX Exception ", e);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("IO Exception ", e);
		}
	}

	@Override
	public Page<GPS> getLatestTrack(Pageable pageable) {
		// TODO Auto-generated method stub
		Page<GPS> entities = gpsRepository.findAll(pageable);
		return entities;
	}

	@Override
	public Resource viewFile(Long id, HttpServletRequest request) {
		// TODO Auto-generated method stub
		GPS gps = gpsRepository.findOne(id);
		if (gps == null) {
			throw new ResourceNotFoundException("Resource not found by ID " + id);
		}
		String fileName = gps.getFileName();
		Resource resource = fileStorageService.loadFileAsResource(fileName);
		return resource;
	}

	private String getValue(Element eElement, String elementTagName) {
		NodeList nodeList = eElement.getElementsByTagName(elementTagName);
		if (null != nodeList.item(0)) {
			return nodeList.item(0).getTextContent();
		}
		return null;
	}
	
	private void validation(MultipartFile file) {
		String contentType = file.getContentType();
		if (file.isEmpty()) {
			throw new FileStorageException("File is not empty");
		} else if (!contentType.contains(GPSConstant.FILE_CONTENT_TYPE)) {
			throw new FileStorageException("Only accept *.GPX");
		}
		// TODO
	}
}
