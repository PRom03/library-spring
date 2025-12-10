package org.example.library.Services;

import org.example.library.Entities.Book;
import org.example.library.Entities.Category;
import org.example.library.Entities.Loan;
import org.example.library.Repositories.BookRepository;
import org.example.library.Repositories.LoanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RecommendationsService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;

    public RecommendationsService(LoanRepository loanRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
    }


    public List<Book> getRecommendations(Integer userId) {

        var userLoans = loanRepository.findByUserId(userId);

        var loanedBooks = userLoans.stream()
                .map(Loan::getBook)
                .filter(Objects::nonNull)
                .toList();

        var loanedBookIsbns = loanedBooks.stream()
                .map(Book::getIsbn)
                .toList();

        var categoryIds = loanedBooks.stream()
                .map(Book::getCategory)
                .map(Category::getId)
                .distinct()
                .toList();

        return bookRepository.findByCategoryIdInAndIdNotIn(categoryIds, loanedBookIsbns);
    }
}
