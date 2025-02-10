package jonatas.santos.royal.lux.core.domain.entities;

import jonatas.santos.royal.lux.core.domain.entities.common.Base;

public class User extends Base {
    protected String Username;
    protected String Password;
    protected boolean IsActive = false;

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
        if (password.length() > 50){
            throw new IllegalArgumentException("Senha não deve conter mais que 50 caracteres");
        }
        Password = password;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }
}
