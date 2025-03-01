package jonatasSantos.royalLux.core.domain.entities;

import jakarta.persistence.*;
import jonatasSantos.royalLux.core.domain.entities.common.Base;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return super.getId();
    }

    @Column(name = "createdAt", nullable = false, updatable = false)
    public LocalDateTime getCreatedAt() {
        return super.getCreatedAt();
    }

    @Column(name = "updatedAt", nullable = true, updatable = true)
    public LocalDateTime getUpdatedAt() {
        return super.getUpdatedAt();
    }

    @Column(name = "username", nullable = false, length = 25)
    protected String Username;

    @Column(name = "password", nullable = false, length = 255)
    protected String Password;

    @Column(name = "active", columnDefinition = "BOOLEAN")
    protected boolean Active = false;

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        if (username.isEmpty()){
            throw new IllegalArgumentException("Username não pode ser vazio");
        }
        if (username.length() < 8){
            throw new IllegalArgumentException("Username deve conter pelo menos 8 caracteres");
        }
        if (username.length() > 25){
            throw new IllegalArgumentException("Username não deve conter mais que 25 caracteres");
        }
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        if (password.isEmpty()){
            throw new IllegalArgumentException("Senha não pode ser vazia");
        }
        if (password.length() < 8){
            throw new IllegalArgumentException("Senha deve conter pelo menos 8 caracteres");
        }
        if (password.length() > 255){
            throw new IllegalArgumentException("Senha não deve conter mais que 255 caracteres");
        }
        Password = password;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }
}
