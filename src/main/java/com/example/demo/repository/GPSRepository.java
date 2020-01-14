package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.GPS;

@Repository
public interface GPSRepository extends JpaRepository<GPS, Long> {
	
	Page<GPS> findAllByOrderByUpdatedAtDesc(Pageable pageable);
}
