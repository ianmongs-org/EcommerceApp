package com.java.EcomerceApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne(mappedBy = "payment", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Order order;
    @NotBlank
    @Size(min = 3, message = "payment method must have a min of 5 characters")
    private String paymentMethod;
    private String pgPaymentId;
    private String pgPaymentStatus;
    private String pgResponseMessage;
    private String pgName;
    private Double amount;

    public Payment(String pgPaymentId, String paymentMethod, String pgPaymentStatus, String pgResponseMessage, String pgName) {
        this.pgPaymentId = pgPaymentId;
        this.paymentMethod = paymentMethod;
        this.pgPaymentStatus = pgPaymentStatus;
        this.pgResponseMessage = pgResponseMessage;
        this.pgName = pgName;
    }
}
