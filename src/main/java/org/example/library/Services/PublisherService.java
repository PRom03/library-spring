package org.example.library.Services;

import org.example.library.Entities.Publisher;
import org.example.library.Repositories.PublisherRepository;
import org.example.library.Utilities.SimpleDto;
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
    public Publisher save(SimpleDto dto) {
        Publisher publisher = new Publisher();
        publisher.setName(dto.name());
        return publisherRepository.save(publisher);
    }
    public Publisher update(Publisher publisher, SimpleDto dto) {
        publisher.setName(dto.name());
        return publisherRepository.save(publisher);
    }
    public void delete(Publisher publisher) {
        publisherRepository.delete(publisher);
    }
}
