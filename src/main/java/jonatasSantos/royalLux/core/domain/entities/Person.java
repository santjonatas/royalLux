package jonatasSantos.royalLux.core.domain.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.regex.Pattern;

@Entity
@Table(name = "persons")
public class Person{

    public Person(User user, String name, LocalDate dateBirth, String cpf, String phone, String email) {
        this.user = user;
        this.setName(name);
        this.setDateBirth(dateBirth);
        this.setCpf(cpf);
        this.setPhone(phone);
        this.setEmail(email);
        this.createdAt = LocalDateTime.now();
    }

    public Person(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected int id;

    @OneToOne
    @JoinColumn(name = "userId", nullable = false, updatable = false)
    protected User user;

    @Column(name = "name", nullable = false, length = 255)
    protected String name;

    @Column(name = "dateBirth")
    protected LocalDate dateBirth;

    @Column(name = "cpf", nullable = false, length = 11)
    protected String cpf;

    @Column(name = "phone", length = 11)
    protected String phone;

    @Column(name = "email", nullable = false)
    protected String email;

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
        this.user = user;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        if (name.length() < 3) {
            throw new IllegalArgumentException("Nome deve conter pelo menos 3 caracteres");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Nome não deve conter mais que 255 caracteres");
        }
        if (!name.matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
            throw new IllegalArgumentException("Nome deve conter apenas letras e espaços");
        }
        this.name = name;
    }

    public LocalDate getDateBirth() {
        return this.dateBirth;
    }

    public void setDateBirth(LocalDate dateBirth) {
        if (dateBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de nascimento não pode ser no futuro");
        }
        if (Period.between(dateBirth, LocalDate.now()).getYears() > 120) {
            throw new IllegalArgumentException("Data de nascimento inválida. Idade não pode ser superior a 120 anos");
        }

        this.dateBirth = dateBirth;
    }

    public String getCpf() {
        return this.cpf;
    }

    public void setCpf(String cpf) {
        if (!this.isValidCpf(cpf)) {
            throw new IllegalArgumentException("CPF inválido");
        }
        this.cpf = cpf;
    }

    private boolean isValidCpf(String cpf) {
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^0-9]", "");

        // Verifica se tem 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }

        // Verifica se todos os dígitos são iguais (ex: 111.111.111-11, que é inválido)
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        // Cálculo dos dígitos verificadores
        int soma = 0, resto;

        // Calcula o primeiro dígito verificador
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * (10 - i);
        }
        resto = soma % 11;
        int digito1 = (resto < 2) ? 0 : (11 - resto);

        // Calcula o segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * (11 - i);
        }
        resto = soma % 11;
        int digito2 = (resto < 2) ? 0 : (11 - resto);

        // Verifica se os dígitos calculados batem com os do CPF informado
        return (digito1 == cpf.charAt(9) - '0') && (digito2 == cpf.charAt(10) - '0');
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        if (phone.length() != 11){
            throw new IllegalArgumentException("Telefone deve conter 11 caracteres");
        }
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        if (email.length() > 255){
            throw new IllegalArgumentException("Email não deve conter mais que 255 caracteres");
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);

        if (!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Email inválido");
        }

        this.email = email;
    }

    public LocalDateTime getCreatedAt() { return this.createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return this.updatedAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}