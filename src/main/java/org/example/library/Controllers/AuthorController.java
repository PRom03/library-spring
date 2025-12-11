package org.example.library.Controllers;

import org.example.library.Entities.Author;
import org.example.library.Services.AuthorService;
import org.example.library.Services.JwtService;
import org.example.library.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    @Autowired
    private AuthorService authorService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;


    @GetMapping(value = "/", produces = "application/json")
    public List<Author> findAll() {
        return authorService.findAll();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public Author findAuthorById(@PathVariable Long id) {
        return authorService.findAuthorById(id).orElseThrow();
    }

    @PostMapping(value = "/add", produces = "application/json")
    public ResponseEntity<?> addAuthor(@RequestBody AuthorService.AuthorDTO authorDto, @RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer")) token = token.replace("Bearer ", "");
        else return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        if (jwtService.isExpired(token)) {
            return new ResponseEntity<>("Token is expired", HttpStatus.UNAUTHORIZED);
        }
        if (!userService.getUserByEmail(jwtService.extractEmail(token)).orElseThrow().getRole().toString().equals("admin")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(authorService.save(authorDto), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}/update", produces = "application/json")
    public ResponseEntity<?> updateAuthor(@PathVariable Integer id, @RequestBody AuthorService.AuthorDTO authorDto,
                                          @RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer")) token = token.replace("Bearer ", "");
        else return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        if (jwtService.isExpired(token)) {
            return new ResponseEntity<>("Token is expired", HttpStatus.UNAUTHORIZED);
        }
        if (!userService.getUserByEmail(jwtService.extractEmail(token)).orElseThrow().getRole().toString().equals("admin")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Author author = authorService.findAuthorById(Long.valueOf(id)).orElse(null);
        if (author == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(authorService.update(author, authorDto), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}/delete", produces = "application/json")
    public ResponseEntity<?> deleteAuthor(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer")) token = token.replace("Bearer ", "");
        else return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        if (jwtService.isExpired(token)) {
            return new ResponseEntity<>("Token is expired", HttpStatus.UNAUTHORIZED);
        }
        if (!userService.getUserByEmail(jwtService.extractEmail(token)).orElseThrow().getRole().toString().equals("admin")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Author author = authorService.findAuthorById(id).orElse(null);
        if (author == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        authorService.delete(author);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
