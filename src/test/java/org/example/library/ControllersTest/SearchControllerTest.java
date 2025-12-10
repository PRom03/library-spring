package org.example.library.ControllersTest;

import org.example.library.Controllers.SearchController;
import org.example.library.Services.SearchService;
import org.example.library.Services.ValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.mockito.Mockito.mock;

@WebMvcTest(SearchController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SearchControllerTest {
    @MockitoBean
    private SearchService searchService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSearchRequest() throws Exception {
        String query="test";
        var result=this.mockMvc.perform(get("/api/search?q="+query)).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        verify(searchService).search(query);
    }
}
