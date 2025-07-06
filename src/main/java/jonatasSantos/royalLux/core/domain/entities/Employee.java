package jonatasSantos.royalLux.core.domain.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "employees")
public class Employee{
    public Employee(User user, String title, BigDecimal salary) {
        this.setUser(user);
        this.setTitle(title);
        this.setSalary(salary);
        this.createdAt = LocalDateTime.now();
    }

    public Employee() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected int id;

    @OneToOne
    @JoinColumn(name = "userId", nullable = false, updatable = false)
    protected User user;

    @Column(name = "title", nullable = false, length = 50)
    protected String title;

    @Column(name = "salary")
    protected BigDecimal salary;

    @Column(name = "createdAt", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @Column(name = "updatedAt")
    protected LocalDateTime updatedAt;

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        if(user == null)
            throw new IllegalArgumentException("Usuário não pode ser nulo");

        this.user = user;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        if(title == null)
            throw new IllegalArgumentException("Título não pode ser nulo");

        if (title.length() < 5)
            throw new IllegalArgumentException("Título deve conter pelo menos 5 caracteres");

        if (title.length() > 50)
            throw new IllegalArgumentException("Título não deve conter mais que 50 caracteres");

        this.title = title;
    }

    public BigDecimal getSalary() {
        return this.salary;
    }

    public void setSalary(BigDecimal salary) {
        if (salary != null) {
            if (salary.compareTo(BigDecimal.ZERO) < 0)
                throw new IllegalArgumentException("Salário não pode ser menor que zero");
        }

        this.salary = salary;
    }

    public LocalDateTime getCreatedAt() { return this.createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return this.updatedAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}