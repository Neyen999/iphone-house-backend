package com.personal.iphonehouse.services;

import com.personal.iphonehouse.dtos.PruebaFechaDto;
import com.personal.iphonehouse.models.PruebaFecha;
import com.personal.iphonehouse.repositories.PruebaFechaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PruebaFechaService {

    @Autowired
    private PruebaFechaRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    public List<PruebaFecha> findAll() {
        return repository.findAll();
    }

    public PruebaFecha findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public PruebaFechaDto save() {
        PruebaFecha pruebaFecha = repository.save(new PruebaFecha());
        return modelMapper.map(pruebaFecha, PruebaFechaDto.class);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<PruebaFechaDto> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<PruebaFecha> pruebaFechas = repository.findByDateCreatedBetween(startDate, endDate);
        return pruebaFechas.stream()
                .map(pruebaFecha -> modelMapper.map(pruebaFecha, PruebaFechaDto.class))
                .collect(Collectors.toList());
    }
}
