package org.example.library.Controllers;

import org.example.library.Entities.Book;
import org.example.library.Services.JwtService;
import org.example.library.Services.RecommendationsService;
import org.example.library.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationsController {

    private final RecommendationsService recommendationsService;
    private final UserService userService;
    private final JwtService jwtService;

    public RecommendationsController(RecommendationsService recommendationsService, UserService userService, JwtService jwtService) {
        this.recommendationsService = recommendationsService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Book>> getUserRecommendations(@RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(recommendationsService.getRecommendations(userService.getUserByEmail(jwtService.extractEmail(token.replace("Bearer ",""))).orElse(null).getId()), HttpStatus.OK);
    }
}
