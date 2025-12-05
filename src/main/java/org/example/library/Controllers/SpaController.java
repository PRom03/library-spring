package org.example.library.Controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class SpaController {

    @RequestMapping(value = "/{path:[^\\.]*}")
    public void forward(HttpServletResponse response) throws IOException {
        ClassPathResource indexHtml = new ClassPathResource("static/react/index.html");
        response.setContentType("text/html");
        StreamUtils.copy(indexHtml.getInputStream(), response.getOutputStream());
    }


}