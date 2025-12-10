package org.example.library.Controllers;


import org.example.library.Entities.User;
import org.example.library.Services.JwtService;
import org.example.library.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public static class LoginRequest {
        private String email;
        private String password;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }


    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }
    @GetMapping("/me")
    public User getMe(@RequestHeader("Authorization") String token) {
        token=token.replace("Bearer ", "");
        return userService.getUserByEmail(jwtService.extractEmail(token)).orElseThrow();
    }
    @PostMapping(value = "/register",produces = "application/json")
    public User createUser(@RequestBody UserService.RegisterRequest registerRequest) {
        return userService.createUser(registerRequest);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }
    @PostMapping(value="/login",produces = "application/json")
    public ResponseEntity<Map<String,String>> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.getUserByEmail(loginRequest.getEmail())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message","Nieprawidłowe dane logowania."));
        }

        String token = jwtService.generateToken(user.getEmail());
        return ResponseEntity.ok(Map.of(
                "token", token,
                "email", user.getEmail(),
                "role", user.getRole().name()
        ));
    }
    @GetMapping(value = "/",produces = "application/json")
    public List<User> getUsers(){
        return userService.getAllUsers();

    }
    @GetMapping(value="/{email}/role")
    public ResponseEntity<?> findRole(@PathVariable String email) {
        User user = userService.getUserByEmail(email).orElseThrow();
        return new ResponseEntity<>(Map.of("role",user.getRole().toString()), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}

