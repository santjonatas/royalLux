package jonatasSantos.royalLux.core.domain.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "schedulerConfig")
public class SchedulerConfig {

    public SchedulerConfig(String name, String description, LocalDate date, Boolean enabled) {
        setName(name);
        setDescription(description);
        this.date = date;
        this.enabled = enabled;
        this.createdAt = LocalDateTime.now();
    }

    public SchedulerConfig() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    protected String name;

    @Column(name = "description", nullable = false, length = 150)
    protected String description;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @Column(name = "createdAt", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @Column(name = "updatedAt")
    protected LocalDateTime updatedAt;

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if(name == null)
            throw new IllegalArgumentException("Nome não pode ser nulo");

        if (name.isEmpty()){
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        if (name.length() > 100){
            throw new IllegalArgumentException("Nome não deve conter mais que 100 caracteres");
        }
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        if(description == null)
            throw new IllegalArgumentException("Descrição não pode ser nula");

        if (description.isEmpty()){
            throw new IllegalArgumentException("Descrição não pode ser vazia");
        }
        if (description.length() > 150){
            throw new IllegalArgumentException("Descrição não deve conter mais que 150 caracteres");
        }
        this.description = description;
    }

    public LocalDate getDate() { return date; }

    public void setDate(LocalDate date) { this.date = date; }

    public Boolean getEnabled() { return enabled; }

    public void setEnabled(Boolean enabled) {
        if(enabled == null)
            throw new IllegalArgumentException("Habilitado não pode ser nulo");

        this.enabled = enabled;
    }

    public LocalDateTime getCreatedAt() { return this.createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return this.updatedAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
