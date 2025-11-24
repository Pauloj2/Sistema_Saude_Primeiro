package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.HorarioDisponivel;

@Repository
public interface  HorarioDisponivelRepository extends JpaRepository<HorarioDisponivel, Long>{
    
}
