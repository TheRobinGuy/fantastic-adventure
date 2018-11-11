package com.example.API.Repository;

import com.example.API.Entity.hackerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface hackerRepository extends JpaRepository<hackerEntity, Integer> {
}
