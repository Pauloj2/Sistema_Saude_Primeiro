package com.example.demo.service;

import java.util.List;
import com.example.demo.model.Medico;

public interface MedicoService {

    List<Medico> getAllMedicos();
    Medico getMedicoById(Long id);
    void saveMedico(Medico medico);
    void deleteMedicoById(Long id);
    boolean existsByCrm(String crm);

    long count();
    default List<Medico> findAll() {
        return getAllMedicos();
    }
    default Medico findById(Long id) {
        return getMedicoById(id);
    }
}