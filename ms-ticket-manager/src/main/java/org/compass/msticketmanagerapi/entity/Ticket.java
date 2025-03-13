package org.compass.msticketmanagerapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "tickets")
public class Ticket {
    @Id
    private String id;
    private String customerName;
    private String cpf;
    private String customerMail;
    private String eventId;
    private String brlAmount;
    private String usdAmount;
    private String status;
}