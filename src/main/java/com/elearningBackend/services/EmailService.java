package com.elearningBackend.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Value("${ebudget.security.reset-token.expiration-hours}")
    private int tokenExpirationHours;

    public EmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Async // Exécute l'envoi d'email de manière asynchrone
    public void sendPasswordResetEmail(String recipientEmail, String userName, String resetUrl) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            Context context = new Context();
            context.setVariable("name", userName);
            context.setVariable("resetUrl", resetUrl);
            context.setVariable("expirationHours", this.tokenExpirationHours);

            String htmlContent = templateEngine.process("password-reset-email", context); // Nom du template HTML

            helper.setTo(recipientEmail);
            helper.setSubject("Réinitialisation de votre mot de passe eBudget");
            helper.setText(htmlContent, true); // true indique que le contenu est HTML
            helper.setFrom(mailFrom); // Spécifier l'expéditeur

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            // Loggez l'erreur de manière appropriée
            System.err.println("Failed to send password reset email to " + recipientEmail + ": " + e.getMessage());
            // Vous pourriez lancer une exception personnalisée ou gérer autrement
        }
    }

    // Ajoutez ici d'autres méthodes d'envoi d'email si nécessaire (ex: confirmation de changement)
    @Async
    public void sendPasswordChangeConfirmationEmail(String recipientEmail, String userName) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            Context context = new Context();
            context.setVariable("name", userName);
            // Vous pouvez ajouter d'autres variables si nécessaire

            String htmlContent = templateEngine.process("password-change-confirmation-email", context); // Nouveau template

            helper.setTo(recipientEmail);
            helper.setSubject("Confirmation de changement de mot de passe eBudget");
            helper.setText(htmlContent, true);
            helper.setFrom(mailFrom);

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            System.err.println("Failed to send password change confirmation email to " + recipientEmail + ": " + e.getMessage());
        }
    }
}