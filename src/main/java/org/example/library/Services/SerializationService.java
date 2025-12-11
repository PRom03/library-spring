package org.example.library.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.example.library.Entities.Book;
import org.example.library.Repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SerializationService {
    private final BookRepository bookrepository;

    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final XmlMapper xmlMapper = new XmlMapper();

    public SerializationService(BookRepository bookrepository) {
        this.bookrepository = bookrepository;
    }

    public String exportToJson() throws Exception {
        List<Book> books = bookrepository.findAll();
        return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(books);
    }

    public String exportToXml() throws Exception {
        List<Book> books = bookrepository.findAll();
        return xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(books);
    }

    public void importJson(String data) throws Exception {
        List<Book> books = jsonMapper.readValue(data, new TypeReference<>() {
        });
        bookrepository.saveAll(books);
    }

    public void importXml(String data) throws Exception {
        List<Book> books = xmlMapper.readValue(data, new TypeReference<>() {
        });
        bookrepository.saveAll(books);
    }
}
