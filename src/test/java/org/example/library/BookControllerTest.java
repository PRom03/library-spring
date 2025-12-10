package org.example.library;

import org.example.library.Controllers.BookController;
import org.example.library.Controllers.LoanController;
import org.example.library.Entities.Book;
import org.example.library.Services.BookService;
import org.example.library.Services.JwtService;
import org.example.library.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private UserService userService;
    @Test
    void shouldReturnRecommendedBooks() throws Exception {
        List<Book> books = List.of(new Book(), new Book());
        when(bookService.findAll()).thenReturn(books);

        var result = mockMvc.perform(
                get("/api/books/")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        verify(bookService).findAll();
    }
}