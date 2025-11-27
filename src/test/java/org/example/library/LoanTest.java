package org.example.library;

import com.jayway.jsonpath.JsonPath;
import org.example.library.Controllers.LoanController;
import org.example.library.Entities.Loan;
import org.example.library.Services.JwtService;
import org.example.library.Services.LoanService;
import org.example.library.Services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LoanController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LoanTest {
    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private LoanService loanService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private JwtService jwtService;
    @Test
    void findAll() throws Exception {
//        when(loanService.findAll()!=null).then(new Answer<List<Loan>>() {
//            @Override
//            public List<Loan> answer(InvocationOnMock invocation) throws Throwable {
//                return loanService.findAll();
//            }
//        }
//        );
        this.mvc.perform(get("/api/loans")).andDo(print()).andExpect(status().isOk());

    }
    @ParameterizedTest
    @CsvSource(textBlock = """
'9788301214575'
            """)
    void testLoanCreation(String isbn) throws Exception{
        MvcResult login = this.mvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
            { "email": "jkowalski@mail.pl", "password": "jan123" }
        """)
        ).andReturn();

        String token = JsonPath.read(
                login.getResponse().getContentAsString(),
                "$.token"
        );
        String json="{\"isbn\":\""+isbn+"\"}";
        String authHeader="Bearer "+token;
        this.mvc.perform(post("/api/loans").header("Authorization",authHeader).contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated());
    }
}
