package jonatasSantos.royalLux.core.domain.entities;

import jakarta.persistence.*;
import jonatasSantos.royalLux.core.domain.enums.CustomerServiceStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "customerService")
public class CustomerService{
    public CustomerService(User createdByUser, Client client, CustomerServiceStatus status, LocalDateTime startTime, LocalDateTime estimatedFinishingTime, LocalDateTime finishingTime, BigDecimal totalValue, String details) {
        this.setCreatedByUser(createdByUser);
        this.setClient(client);
        this.setStatus(status);
        this.setStartTime(startTime);
        this.estimatedFinishingTime = estimatedFinishingTime;
        this.finishingTime = finishingTime;
        this.setTotalValue(totalValue);
        this.setDetails(details);
        this.createdAt = LocalDateTime.now();
    }

    public CustomerService() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected int id;

    @ManyToOne
    @JoinColumn(name = "createdByUserId", nullable = false, updatable = false)
    protected User createdByUser;

    @ManyToOne
    @JoinColumn(name = "clientId", nullable = false, updatable = false)
    protected Client client;

    @Column(name = "status", nullable = false, length = 30)
    protected CustomerServiceStatus status;

    @Column(name = "startTime", nullable = false)
    protected LocalDateTime startTime;

    @Column(name = "estimatedFinishingTime")
    protected LocalDateTime estimatedFinishingTime;

    @Column(name = "finishingTime")
    protected LocalDateTime finishingTime;

    @Column(name = "totalValue", nullable = false)
    protected BigDecimal totalValue;

    @Column(name = "details", length = 750)
    protected String details;

    @Column(name = "createdAt", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @Column(name = "updatedAt")
    protected LocalDateTime updatedAt;

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    public User getCreatedByUser() {
        return this.createdByUser;
    }

    public void setCreatedByUser(User createdByUser) {
        if(createdByUser == null)
            throw new IllegalArgumentException("Usuário criador de atendimento não pode ser nulo");

        this.createdByUser = createdByUser;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        if(client == null)
            throw new IllegalArgumentException("Cliente não pode ser nulo");

        this.client = client;
    }

    public CustomerServiceStatus getStatus() {
        return this.status;
    }

    public void setStatus(CustomerServiceStatus status) { this.status = status; }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        if(startTime == null)
            throw new IllegalArgumentException("Horário de início não pode ser nulo");

        this.startTime = startTime;
    }

    public LocalDateTime getEstimatedFinishingTime() {
        return this.estimatedFinishingTime;
    }

    public void setEstimatedFinishingTime(LocalDateTime estimatedFinishingTime) {
        this.estimatedFinishingTime = estimatedFinishingTime;
    }

    public void incrementEstimatedFinishingTime(LocalTime estimatedTime){
        if(this.estimatedFinishingTime == null)
            this.estimatedFinishingTime = this.startTime;

        this.estimatedFinishingTime = this.estimatedFinishingTime
                .plusHours(estimatedTime.getHour())
                .plusMinutes(estimatedTime.getMinute());
    }

    public LocalDateTime getFinishingTime() {
        return this.finishingTime;
    }

    public void setFinishingTime(LocalDateTime finishingTime) {
        this.finishingTime = finishingTime;
    }

    public BigDecimal getTotalValue() {
        return this.totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        if(totalValue == null)
            throw new IllegalArgumentException("Valor total não pode ser nulo");

        this.totalValue = totalValue;
    }

    public void incrementTotalValue(BigDecimal value){
        this.totalValue = this.totalValue.add(value);
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String details) {
        if (details.length() > 750){
            throw new IllegalArgumentException("Detalhes do atendimento não deve conter mais que 750 caracteres");
        }

        this.details = details;
    }

    public LocalDateTime getCreatedAt() { return this.createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return this.updatedAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
