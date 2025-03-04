package jonatasSantos.royalLux.core.domain.entities;

import jonatasSantos.royalLux.core.domain.entities.common.Base;

import java.util.Date;

public class Person extends Base {
    protected int UserId;
    protected String Name;
    protected Date DateBirth;
    protected String Cpf;
    protected String Phone;
    protected String Email;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        if (name.isEmpty()){
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        if (name.length() < 3){
            throw new IllegalArgumentException("Nome deve conter pelo menos 3 caracteres");
        }
        if (name.length() > 255){
            throw new IllegalArgumentException("Nome não deve conter mais que 255 caracteres");
        }
        Name = name;
    }

    public Date getDateBirth() {
        return DateBirth;
    }

    public void setDateBirth(Date dateBirth) {
        DateBirth = dateBirth;
    }

    public String getCpf() {
        return Cpf;
    }

    public void setCpf(String cpf){
        if (cpf.length() != 11){
            throw new IllegalArgumentException("CPF deve conter 11 caracteres");
        }
        Cpf = cpf;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        if (phone.length() != 11){
            throw new IllegalArgumentException("Telefone deve conter 11 caracteres");
        }
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        if (email.length() > 255){
            throw new IllegalArgumentException("Email não deve conter mais que 255 caracteres");
        }
        Email = email;
    }
}
