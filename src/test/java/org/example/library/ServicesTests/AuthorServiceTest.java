package org.example.library.ServicesTests;

import org.example.library.Entities.Author;
import org.example.library.Repositories.AuthorRepository;
import org.example.library.Services.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthorServiceTest {

        private AuthorRepository authorRepository;
        private AuthorService authorService;

        @BeforeEach
        void setUp() {
            authorRepository = mock(AuthorRepository.class);
            authorService = new AuthorService(authorRepository);
        }

        @Test
        void shouldReturnAllAuthors() {
            List<Author> authors = List.of(new Author(), new Author());
            when(authorRepository.findAll()).thenReturn(authors);

            List<Author> result = authorService.findAll();

            assertThat(result).hasSize(2);
            verify(authorRepository).findAll();
        }

        @Test
        void shouldReturnAuthorById() {
            Author author = new Author();
            when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

            Optional<Author> result = authorService.findAuthorById(1L);

            assertThat(result).isPresent();
            verify(authorRepository).findById(1L);
        }

        @Test
        void shouldSaveNewAuthor() {
            AuthorService.AuthorDTO dto =
                    new AuthorService.AuthorDTO("Jan", "Kowalski", 1980, "Bio", "img.jpg");

            Author savedAuthor = new Author();
            savedAuthor.setId(authorRepository.findMaxId());

            when(authorRepository.save(any())).thenReturn(savedAuthor);

            Author result = authorService.save(dto);

            ArgumentCaptor<Author> captor = ArgumentCaptor.forClass(Author.class);
            verify(authorRepository).save(captor.capture());

            Author captured = captor.getValue();
            assertThat(captured.getFirstName()).isEqualTo("Jan");
            assertThat(captured.getLastName()).isEqualTo("Kowalski");
            assertThat(captured.getBirthyear()).isEqualTo(1980);
            assertThat(captured.getBriefBio()).isEqualTo("Bio");
            assertThat(captured.getImageSource()).isEqualTo("img.jpg");

            assertThat(result.getId()).isEqualTo(authorRepository.findMaxId());
        }

        @Test
        void shouldUpdateAuthor() {
            Author author = new Author();
            author.setId(authorRepository.findMaxId());

            AuthorService.AuthorDTO dto =
                    new AuthorService.AuthorDTO("Adam", "Nowak", 1975, "Updated bio", "updated.jpg");

            when(authorRepository.save(author)).thenReturn(author);

            Author result = authorService.update(author, dto);

            verify(authorRepository).save(author);

            assertThat(result.getFirstName()).isEqualTo("Adam");
            assertThat(result.getLastName()).isEqualTo("Nowak");
            assertThat(result.getBirthyear()).isEqualTo(1975);
            assertThat(result.getBriefBio()).isEqualTo("Updated bio");
            assertThat(result.getImageSource()).isEqualTo("updated.jpg");
        }

        @Test
        void shouldDeleteAuthor() {
            Author author = new Author();

            authorService.delete(author);

            verify(authorRepository).delete(author);
        }
    }

