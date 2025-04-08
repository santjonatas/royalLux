package jonatasSantos.royalLux.core.domain.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "roles")
public class Role{
    public Role(String name, String detail) {
        this.setName(name);
        this.setDetail(detail);
        this.createdAt = LocalDateTime.now();
    }

    public Role() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected int id;

    @Column(name = "name", nullable = false, length = 50)
    protected String name;

    @Column(name = "detail", length = 3000)
    protected String detail;

    @Column(name = "createdAt", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @Column(name = "updatedAt")
    protected LocalDateTime updatedAt;

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if(name == null)
            throw new IllegalArgumentException("Nome não pode ser nulo");

        if (name.isEmpty()){
            throw new IllegalArgumentException("Nome do cargo não pode ser vazio");
        }
        if (name.length() > 50){
            throw new IllegalArgumentException("Nome do cargo não deve conter mais que 50 caracteres");
        }
        this.name = name;
    }

    public String getDetail() {
        return this.detail;
    }

    public void setDetail(String detail) {
        if (detail.length() > 3000){
            throw new IllegalArgumentException("Detalhes não deve conter mais que 3000 caracteres");
        }
        this.detail = detail;
    }

    public LocalDateTime getCreatedAt() { return this.createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return this.updatedAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
