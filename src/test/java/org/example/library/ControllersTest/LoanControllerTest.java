package org.example.library.ControllersTest;

import io.jsonwebtoken.JwtException;
import org.example.library.Controllers.LoanController;
import org.example.library.Entities.Book;
import org.example.library.Entities.Loan;
import org.example.library.Entities.User;
import org.example.library.Entities.UserRole;
import org.example.library.Repositories.BookRepository;
import org.example.library.Repositories.LoanRepository;
import org.example.library.Repositories.UserRepository;
import org.example.library.Services.JwtService;
import org.example.library.Services.LoanService;
import org.example.library.Services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LoanController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LoanControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private LoanService loanService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private BookRepository bookRepository;
    @MockitoBean
    private LoanRepository loanRepository;
    @MockitoBean
    private UserRepository userRepository;

    @Test
    void testLoanCreation() throws Exception {
        String isbn = "9788301214575";

        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setEmail("jkowalski@mail.pl");
        mockUser.setPassword("jan123");
        mockUser.setRole(UserRole.client);
        when(jwtService.extractEmail(anyString())).thenReturn("jkowalski@mail.pl");
        when(jwtService.isExpired(anyString())).thenReturn(false);
        when(userService.getUserByEmail("jkowalski@mail.pl"))
                .thenReturn(Optional.of(mockUser));

        Book book = new Book();
        book.setIsbn(isbn);
        book.setAvailable(1);

        when(bookRepository.findBookByIsbn(isbn))
                .thenReturn(Optional.of(book));

        when(loanRepository.findByUserIdAndBookIdAndStatusIn(anyLong(), eq(isbn), anyList()))
                .thenReturn(Optional.empty());

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));

        when(loanRepository.save(any(Loan.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        String json = "{\"isbn\":\"" + isbn + "\"}";

        mvc.perform(post("/api/loans")
                        .header("Authorization", "Bearer faktyczny-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void testLoanCreationUnauthorized() throws Exception {

        when(jwtService.extractEmail(anyString()))
                .thenThrow(new JwtException("Invalid token"));

        when(userService.getUserByEmail(anyString()))
                .thenReturn(Optional.empty());

        String json = "{\"isbn\":\"1234567890\"}";
        String invalidAuth = "Bearer invalid-token";

        mvc.perform(post("/api/loans")
                        .header("Authorization", invalidAuth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is4xxClientError());
    }

}
