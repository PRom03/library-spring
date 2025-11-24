package org.example.library.Repositories;


import org.example.library.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.support.Repositories;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
}
