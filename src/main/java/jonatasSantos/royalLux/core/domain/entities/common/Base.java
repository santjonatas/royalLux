package jonatasSantos.royalLux.core.domain.entities.common;

import java.time.LocalDateTime;

public class Base<T> {

    protected int Id;
    protected LocalDateTime CreatedAt;
    protected LocalDateTime UpdatedAt;

    public Base() {
        this.CreatedAt = LocalDateTime.now(); // Sempre define a data ao criar um objeto
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public LocalDateTime getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        CreatedAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        UpdatedAt = updatedAt;
    }
}
