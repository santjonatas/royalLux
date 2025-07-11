package jonatasSantos.royalLux.core.domain.entities;

import jakarta.persistence.*;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails{

    public User(String username, UserRole role, boolean active) {
        this.setUsername(username);
        this.setRole(role);
        this.active = active;
        this.createdAt = LocalDateTime.now();
    }

    public User(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Integer id;

    @Column(name = "username", nullable = false, length = 25)
    protected String username;

    @Column(name = "password", nullable = false)
    protected String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 15)
    protected UserRole role;

    @Column(name = "active", columnDefinition = "BOOLEAN")
    protected boolean active = false;

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

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        if(username == null)
            throw new IllegalArgumentException("Username não pode ser nulo");

        if (username.isEmpty())
            throw new IllegalArgumentException("Username não pode ser vazio");

        if (username.length() < 5)
            throw new IllegalArgumentException("Username deve conter pelo menos 5 caracteres");

        if (username.length() > 25)
            throw new IllegalArgumentException("Username não deve conter mais que 25 caracteres");

        if (!username.matches("^[a-z0-9_.]+$"))
            throw new IllegalArgumentException("O nome de usuário só pode conter letras minúsculas, números, '.' e '_'");

        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) { this.password = password; }

    public Boolean validatePassword(String password){
        if (password == null)
            throw new IllegalArgumentException("Senha não pode ser nula");

        if (password.isEmpty())
            throw new IllegalArgumentException("Senha não pode ser vazia");

        if (password.length() < 8)
            throw new IllegalArgumentException("Senha deve conter pelo menos 8 caracteres");

        if (password.length() > 50)
            throw new IllegalArgumentException("Senha não deve conter mais que 50 caracteres");

        if (!password.matches(".*[A-Z].*"))
            throw new IllegalArgumentException("Senha deve conter pelo menos uma letra maiúscula");

        if (!password.matches(".*[a-z].*"))
            throw new IllegalArgumentException("Senha deve conter pelo menos uma letra minúscula");

        if (!password.matches(".*\\d.*"))
            throw new IllegalArgumentException("Senha deve conter pelo menos um número");

        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>?/].*"))
            throw new IllegalArgumentException("Senha deve conter pelo menos um caractere especial");

        if (!password.matches("^[A-Za-z0-9!@#$%^&*()_+\\-=\\[\\]{};':\",.<>?/]+$"))
            throw new IllegalArgumentException("Senha contém caracteres inválidos");

        return true;
    }

    public UserRole getRole() { return this.role; }

    public void setRole(UserRole role) {
        if(role == null)
            throw new IllegalArgumentException("Permissão não pode ser nula");

        this.role = role;
    }

    public boolean isActive() { return this.active; }

    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return this.createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return this.updatedAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Implementação de UserDetails
    @Override
    public boolean isEnabled() { return this.active; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> this.role.name());
    }

    // Não utilizados
    @Override
    public boolean isAccountNonExpired() { return UserDetails.super.isAccountNonExpired(); }

    @Override
    public boolean isAccountNonLocked() { return UserDetails.super.isAccountNonLocked(); }

    @Override
    public boolean isCredentialsNonExpired() { return UserDetails.super.isCredentialsNonExpired(); }
}