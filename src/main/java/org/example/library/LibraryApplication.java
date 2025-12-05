package org.example.library;

import ch.qos.logback.classic.encoder.JsonEncoder;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Key;
import java.util.Arrays;

@SpringBootApplication
public class LibraryApplication {


	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
		Key key= Keys.secretKeyFor(SignatureAlgorithm.HS256);
		System.out.println(Encoders.BASE64.encode(key.getEncoded()));
		System.out.println(new BCryptPasswordEncoder().encode("admin123"));
	}

}
