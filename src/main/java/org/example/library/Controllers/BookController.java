package org.example.library.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.util.Resource;
import org.example.library.Entities.Author;
import org.example.library.Entities.Book;
import org.example.library.Repositories.BookRepository;
import org.example.library.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private SerializationService serializationService;

    @GetMapping(value = "/",produces = "application/json")
    public List<Book> findAll() {
        return bookService.findAll();
    }
    @GetMapping(value = "/{isbn}",produces = "application/json")
    public Book findBookByIsbn(@PathVariable String isbn) {
        return bookService.findBookByIsbn(isbn).orElseThrow();
    }
    @PostMapping(value = "/add",produces = "application/json")
    public ResponseEntity<?> addBook(@RequestBody BookService.BookDTO bookDto, @RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer")) token=token.replace("Bearer ", "");
        else return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        if(jwtService.isExpired(token)) {
            return new ResponseEntity<>("Token is expired", HttpStatus.UNAUTHORIZED);
        }
        if(!userService.getUserByEmail(jwtService.extractEmail(token)).orElseThrow().getRole().toString().equals("admin"))
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(bookService.save(bookDto), HttpStatus.CREATED);
    }
    @PatchMapping(value = "/{isbn}/update",produces = "application/json")
    public ResponseEntity<?> updateBook(@PathVariable String isbn,@RequestBody BookService.BookDTO bookDto,
                                          @RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer")) token=token.replace("Bearer ", "");
        else return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        if(jwtService.isExpired(token)) {
            return new ResponseEntity<>("Token is expired", HttpStatus.UNAUTHORIZED);
        }
        if(!userService.getUserByEmail(jwtService.extractEmail(token)).orElseThrow().getRole().toString().equals("admin"))
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Book book=bookService.findBookByIsbn(isbn).orElse(null);
        if(book==null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(bookService.update(book,bookDto), HttpStatus.OK);
    }
    @DeleteMapping(value = "/{isbn}/delete")
    public ResponseEntity<?> deleteBook(@PathVariable String isbn,@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer")) token=token.replace("Bearer ", "");
        else return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        if(jwtService.isExpired(token)) {
            return new ResponseEntity<>("Token is expired", HttpStatus.UNAUTHORIZED);
        }
        if(!userService.getUserByEmail(jwtService.extractEmail(token)).orElseThrow().getRole().toString().equals("admin"))
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Book book=bookService.findBookByIsbn(isbn).orElse(null);
        if(book==null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        bookService.delete(book);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping(value = "/export", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> export(@RequestParam(defaultValue = "json") String format) throws Exception {
        if (format.equalsIgnoreCase("xml")) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_XML)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.xml")
                    .body(serializationService.exportToXml());
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.json")
                .body(serializationService.exportToJson());
    }
    @PostMapping(value = "/import", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<String> importData(@RequestBody String fileContent,
                                             @RequestHeader("Content-Type") String contentType,@RequestHeader("Authorization")String token)
            throws Exception {
        if (token.startsWith("Bearer")) token=token.replace("Bearer ", "");
        else return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        if(jwtService.isExpired(token)) {
            return new ResponseEntity<>("Token is expired", HttpStatus.UNAUTHORIZED);
        }
        if(!userService.getUserByEmail(jwtService.extractEmail(token)).orElseThrow().getRole().toString().equals("admin"))
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (contentType.contains("xml")) {
            serializationService.importXml(fileContent);
        } else {
            serializationService.importJson(fileContent);
        }

        return ResponseEntity.ok("Import zakończony.");
    }

}
