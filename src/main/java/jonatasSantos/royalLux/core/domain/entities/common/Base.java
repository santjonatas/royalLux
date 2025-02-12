package jonatasSantos.royalLux.core.domain.entities.common;

import java.time.LocalDateTime;

public class Base {

    protected int Id;
    protected LocalDateTime CreatedAt;
    protected LocalDateTime UpdatedAt;

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
