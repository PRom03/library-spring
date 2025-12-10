package org.example.library.ControllersTest;

import org.example.library.Controllers.LoanController;
import org.example.library.Entities.User;
import org.example.library.Services.JwtService;
import org.example.library.Services.LoanService;
import org.example.library.Services.UserService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
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

    @ParameterizedTest
    @CsvSource(textBlock = """
'9788301214575'
            """)
    void testLoanCreation(String isbn) throws Exception{
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setEmail("jkowalski@mail.pl");
        mockUser.setPassword("jan123");

        when(userService.getUserByEmail("jkowalski@mail.pl")).thenReturn(Optional.of(mockUser));
        when(jwtService.extractEmail(anyString())).thenReturn("jkowalski@mail.pl");

        String json = "{\"isbn\":\"" + isbn + "\"}";
        String authHeader = "Bearer faktyczny-token";

        mvc.perform(post("/api/loans")
                        .header("Authorization", authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

    }
}
