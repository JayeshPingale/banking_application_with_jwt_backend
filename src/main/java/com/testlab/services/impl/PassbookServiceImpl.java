package com.testlab.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.testlab.DTO.PassbookResponseDTO;
import com.testlab.Exception.ResourceNotFoundException;
import com.testlab.Repository.AccountRepository;
import com.testlab.Repository.CustomerRepository;
import com.testlab.entities.Account;
import com.testlab.entities.Customer;
import com.testlab.entities.Passbook;
import com.testlab.entities.Transaction;
import com.testlab.mapper.PassbookMapper;
import com.testlab.services.PassbookService;
import com.testlab.services.PdfGenerationService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@Transactional
public class PassbookServiceImpl implements PassbookService {

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PassbookMapper passbookMapper;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PdfGenerationService pdfGenerationService;
    @Override
    public PassbookResponseDTO generateAndSendPassbook(String accountNumber) {
//        // Fetch customer
//        Customer customer = customerRepository.findById(requestDTO.getCustomerId())
//                .orElseThrow(() -> new RuntimeException("Customer not found"));
//
//        // Get account
//        Account account = customer.getAccounts().stream()
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("No account found for customer"));

        // 1️⃣ Find account by accountNumber
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found with number: " + accountNumber));

        // 2️⃣ Get customer
        Customer customer = account.getCustomer();

//        // Get latest transaction
//        List<Transaction> fromTxns = account.getFromTransactions() != null ? account.getFromTransactions() : List.of();
//        List<Transaction> toTxns = account.getToTransactions() != null ? account.getToTransactions() : List.of();
//        List<Transaction> allTransactions = new ArrayList<>();
//        allTransactions.addAll(fromTxns);
//        allTransactions.addAll(toTxns);
//        
//        if (allTransactions.isEmpty()) {
//            throw new ResourceNotFoundException("No transactions found for this account");
//        }
//        
//        allTransactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));
//
//        Transaction latestTxn = allTransactions.get(0);
        
        // 3️⃣ Get all transactions
        List<Transaction> allTransactions = new ArrayList<>();
        if (account.getFromTransactions() != null) allTransactions.addAll(account.getFromTransactions());
        if (account.getToTransactions() != null) allTransactions.addAll(account.getToTransactions());

        if (allTransactions.isEmpty()) {
            throw new ResourceNotFoundException("No transactions found for this account");
        }

        allTransactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));
        Transaction latestTxn = allTransactions.get(0);
//        Transaction latestTxn = null;
//        if (!fromTxns.isEmpty() && !toTxns.isEmpty()) {
//            Transaction lastFrom = fromTxns.get(fromTxns.size() - 1);
//            Transaction lastTo = toTxns.get(toTxns.size() - 1);
//            latestTxn = lastFrom.getDate().isAfter(lastTo.getDate()) ? lastFrom : lastTo;
//        } else if (!fromTxns.isEmpty()) {
//            latestTxn = fromTxns.get(fromTxns.size() - 1);
//        } else if (!toTxns.isEmpty()) {
//            latestTxn = toTxns.get(toTxns.size() - 1);
//        } else {
//            throw new RuntimeException("No transactions found");
//        }
//
//        // Create Passbook entity
//        Passbook passbook = new Passbook();
//        passbook.setAccountId(account.getAccountId());
//        passbook.setAccountNumber(account.getAccountNumber());
//        passbook.setBalance(account.getBalance());
//        passbook.setTransId(latestTxn.getTransId());
//        passbook.setTransType(latestTxn.getTransType());
//        passbook.setDate(latestTxn.getDate());
//
//        // Map to DTO
//        PassbookResponseDTO responseDTO = passbookMapper.toResponse(passbook, latestTxn);
//
//        // Send email
//        sendPassbookEmail(customer.getEmailid(), responseDTO,allTransactions);
//
//        return responseDTO;
        // 4️⃣ Create Passbook entity
        Passbook passbook = new Passbook();
        passbook.setAccountId(account.getAccountId());
        passbook.setAccountNumber(account.getAccountNumber());
        passbook.setBalance(account.getBalance());
        passbook.setTransId(latestTxn.getTransId());
        passbook.setTransType(latestTxn.getTransType());
        passbook.setDate(latestTxn.getDate());

        // 5️⃣ Map to DTO
        PassbookResponseDTO responseDTO = passbookMapper.toResponse(passbook, latestTxn);

        // 6️⃣ Send email
        sendPassbookEmail(customer.getEmailid(), responseDTO, allTransactions);
        // PDF generate karo
        byte[] pdfBytes = pdfGenerationService.generatePassbookPdf(
            account.getAccountNumber(), 
            account.getBalance(), 
            allTransactions
        );
        sendPassbookEmailWithAttachment(customer.getEmailid(), responseDTO, pdfBytes);
        return responseDTO;
    }

//    private void sendPassbookEmail(String toEmail, PassbookResponseDTO passbookDTO,) {
//        String subject = "Your Passbook Details";
//        StringBuilder body = new StringBuilder();
//        body.append("Dear Customer,\n\n");
//        body.append("Account Number: ").append(passbookDTO.getAccountNumber()).append("\n");
//        body.append("Current Balance: ₹").append(passbookDTO.getCurrentBalance()).append("\n");
//        body.append("Latest Transaction:\n");
//        body.append(" - Transaction ID: ").append(passbookDTO.getTransaction().getTransId()).append("\n");
//        body.append(" - Type: ").append(passbookDTO.getTransaction().getTransType()).append("\n");
//        body.append(" - Amount: ₹").append(passbookDTO.getTransaction().getAmount()).append("\n");
//        body.append(" - Date: ").append(passbookDTO.getTransaction().getDate()).append("\n\n");
//        body.append("Thank you for banking with us.");
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(toEmail);
//        message.setSubject(subject);
//        message.setText(body.toString());
//        mailSender.send(message);
//    }
    private void sendPassbookEmail(String toEmail, PassbookResponseDTO passbookDTO, List<Transaction> allTransactions) {
        String subject = "📑 Passbook Statement for Account: " + passbookDTO.getAccountNumber();

        StringBuilder body = new StringBuilder();
        body.append("<html><body style='font-family:Arial,sans-serif;'>");

        // Header
        body.append("<h2 style='color: #2E86C1;'>Welcome to Lenna Bank!</h2>");
        body.append("<p>Dear Customer,</p>");
        body.append("<p>Here is the latest statement for your account:</p>");
        
        // Account info
        body.append("<p><strong>Account Number:</strong> ").append(passbookDTO.getAccountNumber()).append("</p>");
        body.append("<p><strong>Current Balance:</strong> Rs.").append(passbookDTO.getCurrentBalance()).append("</p>");
        
        // Transaction table
        body.append("<h3>Recent Transactions:</h3>");
        body.append("<table border='1' cellpadding='5' cellspacing='0' style='border-collapse: collapse;'>");
        body.append("<tr style='background-color:#D6EAF8;'>")
            .append("<th>Transaction ID</th>")
            .append("<th>Type</th>")
            .append("<th>Amount (Rs.)</th>")
            .append("<th>Date</th>")
            .append("</tr>");

        for (Transaction transaction : allTransactions) {
            body.append("<tr>")
                .append("<td>").append(transaction.getTransId()).append("</td>")
                .append("<td>").append(transaction.getTransType()).append("</td>")
                .append("<td>").append(transaction.getAmount()).append("</td>")
                .append("<td>").append(transaction.getDate()).append("</td>")
                .append("</tr>");
        }

        body.append("</table>");
        body.append("<p style='margin-top:20px;'>Thank you for banking with us!</p>");
        body.append("<p><i>Lenna Bank Team</i></p>");
        body.append("</body></html>");

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body.toString(), true); // true = HTML
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    private void sendPassbookEmailWithAttachment(String toEmail, PassbookResponseDTO passbookDTO, byte[] pdfAttachment) {
        String subject = "📑 Passbook Statement for Account: " + passbookDTO.getAccountNumber();
        String body = "Dear Customer,\n\nPlease find your latest passbook statement attached as a PDF.\n\nThank you for banking with us!\nLenna Bank Team";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            
            // MimeMessageHelper attachments, HTML content etc. ke liye zaroori hai
            // 'true' ka matlab hai ki yeh ek multipart message hai (matlab text aur attachment dono ho sakte hain)
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body);

            // Sabse important step: PDF ko attach karna
            // "Passbook-Statement.pdf" woh naam hai jo user ko attachment mein dikhega
            helper.addAttachment("Passbook-Statement.pdf", new ByteArrayResource(pdfAttachment));

            mailSender.send(message);

        } catch (MessagingException e) {
            // Production code mein yahan proper logging honi chahiye
            throw new RuntimeException("Failed to send email with attachment", e);
        }
    }

}
