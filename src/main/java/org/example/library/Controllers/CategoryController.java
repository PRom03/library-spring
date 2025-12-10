package org.example.library.Controllers;

import org.example.library.Entities.Category;
import org.example.library.Services.CategoryService;
import org.example.library.Services.JwtService;
import org.example.library.Services.UserService;
import org.example.library.SimpleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private  CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    @GetMapping(value = "/",produces = "application/json")
    public List<Category> findAll() {
        return categoryService.findAll();
    }
    @GetMapping(value = "/{id}",produces = "application/json")
    public Category findCategoryById(@PathVariable Long id) {
        return categoryService.findCategoryById(id).orElseThrow();
    }
    @PostMapping(value = "/add",produces = "application/json")
    public ResponseEntity<?> addCategory(@RequestBody SimpleDto dto, @RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer")) token=token.replace("Bearer ", "");
        else return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        if(jwtService.isExpired(token)) {
            return new ResponseEntity<>("Token is expired", HttpStatus.UNAUTHORIZED);
        }
        if(jwtService.isExpired(token)) {
            return new ResponseEntity<>("Token is expired", HttpStatus.UNAUTHORIZED);
        }
        if(!userService.getUserByEmail(jwtService.extractEmail(token)).orElseThrow().getRole().toString().equals("admin"))
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(categoryService.save(dto), HttpStatus.CREATED);
    }
    @PatchMapping(value = "/{id}/update",produces = "application/json")
    public ResponseEntity<?> updateCategory(@PathVariable Integer id, @RequestBody SimpleDto dto,
                                          @RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer")) token=token.replace("Bearer ", "");
        else return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        if(jwtService.isExpired(token)) {
            return new ResponseEntity<>("Token is expired", HttpStatus.UNAUTHORIZED);
        }
        if(!userService.getUserByEmail(jwtService.extractEmail(token)).orElse(null).getRole().toString().equals("admin"))
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Category category=categoryService.findCategoryById(Long.valueOf(id)).orElse(null);
        if(category==null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(categoryService.update(category,dto), HttpStatus.OK);
    }
    @DeleteMapping(value = "/{id}/delete",produces = "application/json")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id,@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer")) token=token.replace("Bearer ", "");
        else return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        if(jwtService.isExpired(token)) {
            return new ResponseEntity<>("Token is expired", HttpStatus.UNAUTHORIZED);
        }
        Category category=categoryService.findCategoryById(Long.valueOf(id)).orElse(null);
        if(category==null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        categoryService.delete(category);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
