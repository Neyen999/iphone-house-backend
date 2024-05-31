
package com.personal.iphonehouse.controllers;

import com.personal.iphonehouse.models.Store;
import com.personal.iphonehouse.services.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreService service;

    @GetMapping
    public List<Store> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Store getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Store create(@RequestBody Store entity) {
        return service.save(entity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}
