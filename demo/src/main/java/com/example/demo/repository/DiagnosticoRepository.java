package com.example.demo.repository;

import com.example.demo.model.Diagnostico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {

    boolean existsByCodigo(String codigo);
    List<Diagnostico> findAllByIdIn(List<Long> ids);
}
