package jonatasSantos.royalLux.core.domain.entities;

import jakarta.persistence.*;
import jonatasSantos.royalLux.core.domain.entities.common.Base;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "customerService")
public class CustomerService{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected int id;

    @OneToOne
    @JoinColumn(name = "clientId", nullable = false, updatable = false)
    protected Client client;

    @Column(name = "status", nullable = false, length = 25)
    protected String status;

    @Column(name = "startTime", nullable = false)
    protected LocalDateTime startTime;

    @Column(name = "estimatedFinishingTime", nullable = false)
    protected LocalDateTime estimatedFinishingTime;

    @Column(name = "finishingTime")
    protected LocalDateTime finishingTime;

    @Column(name = "totalValue", nullable = false)
    protected BigDecimal totalValue;

    @Column(name = "details", length = 750)
    protected String details;

    @Column(name = "completed")
    protected boolean completed;

    @Column(name = "createdAt", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @Column(name = "updatedAt")
    protected LocalDateTime updatedAt;

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        if (status.isEmpty()){
            throw new IllegalArgumentException("Status não pode ser vazio");
        }
        if (status.length() > 20){
            throw new IllegalArgumentException("Status não deve conter mais que 20 caracteres");
        }

        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEstimatedFinishingTime() {
        return this.estimatedFinishingTime;
    }

    public void setEstimatedFinishingTime(LocalDateTime estimatedFinishingTime) {
        this.estimatedFinishingTime = estimatedFinishingTime;
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
        this.totalValue = totalValue;
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

    public boolean isCompleted() {
        return this.completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getCreatedAt() { return this.createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return this.updatedAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
