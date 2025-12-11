package org.example.library.Services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {
    private final JwtService jwtService;
    private final UserService userService;

    public ValidationService(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    public ResponseEntity<?> validate(String token,String role){
        if(token.startsWith("Bearer ")) token=token.replace("Bearer ", "");
        else return new ResponseEntity<>("Token is expired", HttpStatus.UNAUTHORIZED);

        if(jwtService.isExpired(token)) {
            return new ResponseEntity<>("Token is expired", HttpStatus.UNAUTHORIZED);
        }
        if(jwtService.isExpired(token)) {
            return new ResponseEntity<>("Token is expired", HttpStatus.UNAUTHORIZED);
        }
        if(!userService.getUserByEmail(jwtService.extractEmail(token)).orElseThrow().getRole().toString().equals(role))
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return null;
    }
}
