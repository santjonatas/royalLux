package jonatasSantos.royalLux.core.domain.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "salonServices")
public class SalonService {

    public SalonService(String name, String description, LocalTime estimatedTime, BigDecimal value) {
        this.setName(name);
        this.setDescription(description);
        this.estimatedTime = estimatedTime;
        this.value = value;
        this.createdAt = LocalDateTime.now();
    }

    public SalonService() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected int id;

    @Column(name = "name", nullable = false, length = 50)
    protected String name;

    @Column(name = "description", length = 1000)
    protected String description;

    @Column(name = "estimatedTime", nullable = false)
    protected LocalTime estimatedTime;

    @Column(name = "value", nullable = false)
    protected BigDecimal value;

    @Column(name = "createdAt", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @Column(name = "updatedAt")
    protected LocalDateTime updatedAt;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name.isEmpty()){
            throw new IllegalArgumentException("Nome do serviço não pode ser vazio");
        }
        if (name.length() > 50){
            throw new IllegalArgumentException("Nome do serviço não deve conter mais que 50 caracteres");
        }

        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        if (description.length() > 1000){
            throw new IllegalArgumentException("Descrição não deve conter mais que 1000 caracteres");
        }

        this.description = description;
    }

    public LocalTime getEstimatedTime() {
        return this.estimatedTime;
    }

    public void setEstimatedTime(LocalTime estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
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
