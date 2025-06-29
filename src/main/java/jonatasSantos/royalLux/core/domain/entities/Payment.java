package jonatasSantos.royalLux.core.domain.entities;

import jakarta.persistence.*;
import jonatasSantos.royalLux.core.domain.enums.PaymentMethod;
import jonatasSantos.royalLux.core.domain.enums.PaymentStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    public Payment(CustomerService customerService, PaymentStatus status, LocalDateTime time, PaymentMethod method, String description, String transactionId, String paymentToken, String paymentUrl, String payerName) {
        this.customerService = customerService;
        this.status = status;
        this.time = time;
        this.method = method;
        this.description = description;
        this.transactionId = transactionId;
        this.paymentToken = paymentToken;
        this.paymentUrl = paymentUrl;
        this.payerName = payerName;
        this.createdAt = LocalDateTime.now();
    }

    public Payment() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Integer id;

    @OneToOne
    @JoinColumn(name = "customerServiceId", nullable = false)
    protected CustomerService customerService;

    @Column(name = "status", nullable = false, length = 50)
    protected PaymentStatus status;

    @Column(name = "time", nullable = false)
    protected LocalDateTime time;

    @Column(name = "method", nullable = false, length = 15)
    protected PaymentMethod method;

    @Column(name = "description", length = 500)
    protected String description;

    @Column(name = "transactionId", nullable = true)
    protected String transactionId;

    @Column(name = "paymentToken", nullable = true)
    protected String paymentToken;

    @Column(name = "paymentUrl", nullable = true)
    protected String paymentUrl;

    @Column(name = "payerName", length = 255, nullable = true)
    protected String payerName;

    @Column(name = "createdAt", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @Column(name = "updatedAt")
    protected LocalDateTime updatedAt;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CustomerService getCustomerService() {
        return this.customerService;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public PaymentStatus getStatus() {
        return this.status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getTime() {
        return this.time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public PaymentMethod getMethod() {
        return this.method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
