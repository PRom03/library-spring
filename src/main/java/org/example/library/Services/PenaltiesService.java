package org.example.library.Services;

import org.example.library.Entities.Loan;
import org.example.library.LoanStatus;
import org.example.library.Repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class PenaltiesService {
    @Autowired
    private LoanRepository loanRepository;

    public void calculatePenalties() {
        LocalDateTime now = LocalDateTime.now();
        var overdueLoans = loanRepository.findByStatusAndReturnDateBefore(LoanStatus.loaned.toString(), now);

        for (Loan loan : overdueLoans) {
            Duration overdue = Duration.between(loan.getReturnDate(), now);
            long daysOverdue = overdue.toDays();
            if(overdue.toSeconds()>0) loan.setPenalty(BigDecimal.valueOf(0.01));
            else if(daysOverdue>0)loan.setPenalty(BigDecimal.valueOf(daysOverdue * 0.10).setScale(2, RoundingMode.HALF_UP));
            loanRepository.save(loan);
        }
    }
}
