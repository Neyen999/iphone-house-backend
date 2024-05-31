
package com.personal.iphonehouse.services;

import com.personal.iphonehouse.models.Store;
import com.personal.iphonehouse.repositories.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {

    @Autowired
    private StoreRepository repository;

    public List<Store> findAll() {
        return repository.findAll();
    }

    public Store findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Store save(Store entity) {
        return repository.save(entity);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
