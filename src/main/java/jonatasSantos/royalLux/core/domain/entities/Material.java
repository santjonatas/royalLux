package jonatasSantos.royalLux.core.domain.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "materials")
public class Material {

    public Material(String name, String description, BigDecimal value, Integer quantity) {
        this.setName(name);
        this.setDescription(description);
        this.value = value;
        this.setAvailableQuantity(quantity);
        this.createdAt = LocalDateTime.now();
    }

    public Material() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Integer id;

    @Column(name = "name", nullable = false, length = 50)
    protected String name;

    @Column(name = "description", length = 3000)
    protected String description;

    @Column(name = "value")
    protected BigDecimal value;

    @Column(name = "availableQuantity", nullable = false)
    protected Integer availableQuantity;

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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if(name == null)
            throw new IllegalArgumentException("Nome não pode ser nulo");

        if (name.isEmpty())
            throw new IllegalArgumentException("Nome do material não pode ser vazio");

        if (name.length() > 50)
            throw new IllegalArgumentException("Nome do material não deve conter mais que 50 caracteres");

        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        if (description.length() > 3000)
            throw new IllegalArgumentException("Descrição não deve conter mais que 3000 caracteres");

        this.description = description;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Integer getAvailableQuantity() {
        return this.availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        if(availableQuantity == null)
            throw new IllegalArgumentException("Quantidade disponível não pode ser nula");

        this.availableQuantity = availableQuantity;
    }

    public void incrementQuantity(Integer quantity){
        this.availableQuantity += quantity;
    }

    public void decrementQuantity(Integer quantity){
        if(quantity > this.availableQuantity)
            throw new IllegalArgumentException("Quantidade a ser removida não pode ser maior que a atual");

        this.availableQuantity -= quantity;
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
