package com.kosnik.listener;

import com.kosnik.event.OrderPlacedEvent;
import jakarta.ws.rs.core.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
//TODO handle exception
public class NotificationListener {
    private final JavaMailSender mailSender;
    @KafkaListener(topics = "notification")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("green.marker2@mail.ru");
        message.setTo(SecurityContextHolder.getContext().getAuthentication().getName());
        message.setSubject("Order reservation");
        message.setText(System.out.printf(
                "Order date = %tf\n, Arrival date = %tf\n, Departure date = %tf",
                orderPlacedEvent.getOrderDate(),
                orderPlacedEvent.getArrivalDate(),
                orderPlacedEvent.getDepartureDate()
                ).toString());
        mailSender.send(message);
    }
}
