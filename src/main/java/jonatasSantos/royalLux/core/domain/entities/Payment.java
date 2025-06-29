package jonatasSantos.royalLux.core.domain.entities;

import jakarta.persistence.*;
import jonatasSantos.royalLux.core.domain.enums.PaymentMethod;
import jonatasSantos.royalLux.core.domain.enums.PaymentStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    public Payment(CustomerService customerService, User createdByUser, PaymentStatus status, PaymentMethod method, String description, String transactionId, String paymentToken, String paymentUrl, String payerName) {
        setCustomerService(customerService);
        setCreatedByUser(createdByUser);
        setStatus(status);
        setMethod(method);
        setDescription(description);
        setTransactionId(transactionId);
        setPaymentToken(paymentToken);
        setPaymentUrl(paymentUrl);
        setPayerName(payerName);
        this.createdAt = LocalDateTime.now();
    }

    public Payment(CustomerService customerService, User createdByUser, PaymentStatus status, PaymentMethod method, String description, String payerName) {
        setCustomerService(customerService);
        setCreatedByUser(createdByUser);
        setStatus(status);
        setMethod(method);
        setDescription(description);
        setPayerName(payerName);
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

    @OneToOne
    @JoinColumn(name = "createdByUserId", nullable = true)
    protected User createdByUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    protected PaymentStatus status;

    @Enumerated(EnumType.STRING)
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

    public CustomerService getCustomerService() { return this.customerService; }

    public void setCustomerService(CustomerService customerService) {
        if(customerService == null)
            throw new IllegalArgumentException("Atendimento não pode ser nulo");

        this.customerService = customerService;
    }

    public User getCreatedByUser() { return createdByUser; }

    public void setCreatedByUser(User createdByUser) {
        if(createdByUser == null)
            throw new IllegalArgumentException("Usuário criador de pagamento não pode ser nulo");

        this.createdByUser = createdByUser;
    }

    public PaymentStatus getStatus() {
        return this.status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
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
        if(description == null)
            throw new IllegalArgumentException("Descrição não pode ser nula");

        this.description = description;
    }

    public String getTransactionId() { return transactionId; }

    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getPaymentToken() { return paymentToken; }

    public void setPaymentToken(String paymentToken) { this.paymentToken = paymentToken; }

    public String getPaymentUrl() { return paymentUrl; }

    public void setPaymentUrl(String paymentUrl) { this.paymentUrl = paymentUrl; }

    public String getPayerName() { return payerName; }

    public void setPayerName(String payerName) {
        if(payerName == null)
            throw new IllegalArgumentException("Nome do pagador não pode ser nulo");

        this.payerName = payerName;
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
