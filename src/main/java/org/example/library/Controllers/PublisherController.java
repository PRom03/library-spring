package org.example.library.Controllers;

import org.example.library.Entities.Publisher;
import org.example.library.Services.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {
    @Autowired
    private PublisherService publisherService;

    @GetMapping(value = "/",produces = "application/json")

    public List<Publisher> findAll() {
        return publisherService.findAll();
    }
    @GetMapping(value = "/{id}",produces = "application/json")

    public Optional<Publisher> findById(@PathVariable Long id) {
        return publisherService.findPublisherById(id);
    }
}
