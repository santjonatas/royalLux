package jonatasSantos.royalLux.core.domain.entities.common;

import java.time.LocalDateTime;

public class Base {

    protected int id;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    public Base() {
        this.createdAt = LocalDateTime.now(); // Sempre define a data ao criar um objeto
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
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
