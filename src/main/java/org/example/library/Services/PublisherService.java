package org.example.library.Services;

import org.example.library.Entities.Publisher;
import org.example.library.Repositories.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PublisherService {
    @Autowired
    private PublisherRepository publisherRepository;
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }
    public List<Publisher> findAll() {
        return publisherRepository.findAll();
    }
    public Optional<Publisher> findPublisherById(Long id) {
        return publisherRepository.findById(id);
    }
}
