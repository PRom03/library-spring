package org.example.library.Controllers;

import org.example.library.Entities.Category;
import org.example.library.Entities.Publisher;
import org.example.library.Services.CategoryService;
import org.example.library.Services.JwtService;
import org.example.library.Services.PublisherService;
import org.example.library.Services.UserService;
import org.example.library.SimpleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {
    @Autowired
    private PublisherService publisherService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    @GetMapping(value = "/",produces = "application/json")

    public List<Publisher> findAll() {
        return publisherService.findAll();
    }
    @GetMapping(value = "/{id}",produces = "application/json")

    public Optional<Publisher> findById(@PathVariable Long id) {
        return publisherService.findPublisherById(id);
    }
    @PostMapping(value = "/add",produces = "application/json")
    public ResponseEntity<?> addPublisher(@RequestBody SimpleDto dto, @RequestHeader("Authorization") String token) {
        token=token.replace("Bearer ", "");
        if(!userService.getUserByEmail(jwtService.extractEmail(token)).orElse(null).getRole().toString().equals("admin"))
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(publisherService.save(dto), HttpStatus.CREATED);
    }
    @PatchMapping(
            value = "/{id}/update",produces = "application/json")
    public ResponseEntity<?> updatePublisher(@PathVariable Integer id, @RequestBody SimpleDto dto,
                                            @RequestHeader("Authorization") String token) {
        token=token.replace("Bearer ", "");
        if(!userService.getUserByEmail(jwtService.extractEmail(token)).orElse(null).getRole().toString().equals("admin"))
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Publisher publisher=publisherService.findPublisherById(Long.valueOf(id)).orElse(null);
        if(publisher==null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(publisherService.update(publisher,dto), HttpStatus.OK);
    }
    @DeleteMapping(value = "/{id}/delete",produces = "application/json")
    public ResponseEntity<?> deletePublisher(@PathVariable Long id,@RequestHeader("Authorization") String token) {
        Publisher publisher=publisherService.findPublisherById(Long.valueOf(id)).orElse(null);
        if(publisher==null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        publisherService.delete(publisher);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
