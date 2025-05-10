package org.example.eventbookingsystem.Service;

import io.mailtrap.client.MailtrapClient;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapMail;
import io.mailtrap.model.response.emails.SendResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MailtrapEmailService {

    private final MailtrapClient mailtrapClient;

    public MailtrapEmailService(
            @Value("${mailtrap.api.token}") String token,
            @Value("${mailtrap.inbox.id}") Long inboxId,
            @Value("${mailtrap.sandbox.enabled:true}") boolean sandboxEnabled) {

        final MailtrapConfig config = new MailtrapConfig.Builder()
                .sandbox(sandboxEnabled)
                .inboxId(inboxId)
                .token(token)
                .build();

        this.mailtrapClient = MailtrapClientFactory.createMailtrapClient(config);
    }

    public void sendEmail(String to, String subject, String text) {
        final MailtrapMail mail = MailtrapMail.builder()
                .from(new Address("noreply@eventbooking.com", "Event Booking System"))
                .to(List.of(new Address(to)))
                .subject(subject)
                .text(text)
                .category("User Verification")
                .build();

        try {
            SendResponse response = mailtrapClient.send(mail);
            log.info("Email sent successfully via Mailtrap: {}", response);
        } catch (Exception e) {
            log.error("Failed to send email via Mailtrap: {}", e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
