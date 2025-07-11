package jonatasSantos.royalLux.core.domain.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "clients")
public class Client{
    public Client(User user) {
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }

    public Client() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Integer id;

    @OneToOne
    @JoinColumn(name = "userId", nullable = false, updatable = false)
    protected User user;

    @Column(name = "createdAt", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @Column(name = "updatedAt")
    protected LocalDateTime updatedAt;

    public Integer getId() { return this.id; }

    public void setId(Integer id) { this.id = id; }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        if(user == null)
            throw new IllegalArgumentException("Usuário não pode ser nulo");

        this.user = user;
    }

    public LocalDateTime getCreatedAt() { return this.createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return this.updatedAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}