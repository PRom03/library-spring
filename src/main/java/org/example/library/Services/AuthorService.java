package org.example.library.Services;

import org.example.library.Controllers.AuthorController;
import org.example.library.Entities.Author;
import org.example.library.Repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public record AuthorDTO(String firstName, String lastName,Integer birthyear, String briefBio,String imageSource) {}

    public List<Author> findAll() {
        return authorRepository.findAll();
    }
    public Optional<Author> findAuthorById(Long id) {
        return authorRepository.findById(id);
    }
    public Author save(AuthorDTO authorDto) {
        Author author = new Author();
        author.setFirstName(authorDto.firstName);
        author.setLastName(authorDto.lastName);
        author.setBirthyear(authorDto.birthyear);
        author.setBriefBio(authorDto.briefBio);
        author.setImageSource(authorDto.imageSource);
        return authorRepository.save(author);
    }
    public Author update(Author author,AuthorDTO authorDto) {
        author.setFirstName(authorDto.firstName);
        author.setLastName(authorDto.lastName);
        author.setBirthyear(authorDto.birthyear);
        author.setBriefBio(authorDto.briefBio);
        author.setImageSource(authorDto.imageSource);
        return authorRepository.save(author);
    }
    public void delete(Author author) {
        authorRepository.delete(author);
    }
}

