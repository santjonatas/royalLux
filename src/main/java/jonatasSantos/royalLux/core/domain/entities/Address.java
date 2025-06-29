package jonatasSantos.royalLux.core.domain.entities;

import jakarta.persistence.*;
import jonatasSantos.royalLux.core.domain.enums.AddressState;
import java.time.LocalDateTime;

@Entity
@Table(name = "addresses")
public class Address{
    public Address(User user, String street, String houseNumber, String complement, String neighborhood, String city, AddressState state, String cep) {
        this.user = user;
        this.setStreet(street);
        this.setHouseNumber(houseNumber);
        this.setComplement(complement);
        this.setNeighborhood(neighborhood);
        this.setCity(city);
        this.setState(state);
        this.setCep(cep);
        this.createdAt = LocalDateTime.now();
    }

    public Address() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Integer id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false, updatable = false)
    protected User user;

    @Column(name = "street", length = 255)
    protected String street;

    @Column(name = "houseNumber", length = 15)
    protected String houseNumber;

    @Column(name = "complement", length = 50)
    protected String complement;

    @Column(name = "neighborhood", length = 100)
    protected String neighborhood;

    @Column(name = "city", length = 100)
    protected String city;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", length = 2)
    protected AddressState state;

    @Column(name = "cep", length = 9)
    protected String cep;

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

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street) {
        if (street.length() < 2){
            throw new IllegalArgumentException("Rua deve conter pelo menos 2 caracteres");
        }
        if (street.length() > 255){
            throw new IllegalArgumentException("Rua não deve conter mais que 255 caracteres");
        }

        this.street = street;
    }

    public String getHouseNumber() {
        return this.houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        if (houseNumber.length() < 1){
            throw new IllegalArgumentException("Número deve conter pelo menos 1 caractere");
        }
        if (houseNumber.length() > 15){
            throw new IllegalArgumentException("Número não deve conter mais que 15 caracteres");
        }

        this.houseNumber = houseNumber;
    }

    public String getComplement() {
        return this.complement;
    }

    public void setComplement(String complement) {
        if (complement.length() > 50){
            throw new IllegalArgumentException("Complemento não deve conter mais que 50 caracteres");
        }

        this.complement = complement;
    }

    public String getNeighborhood() {
        return this.neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        if (neighborhood.length() < 2){
            throw new IllegalArgumentException("Bairro deve conter pelo menos 2 caracteres");
        }
        if (neighborhood.length() > 100){
            throw new IllegalArgumentException("Bairro não deve conter mais que 100 caracteres");
        }

        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        if (city.length() < 2){
            throw new IllegalArgumentException("Cidade deve conter pelo menos 2 caracteres");
        }
        if (city.length() > 100){
            throw new IllegalArgumentException("Cidade não deve conter mais que 100 caracteres");
        }

        this.city = city;
    }

    public AddressState getState() {
        return this.state;
    }

    public void setState(AddressState state) { this.state = state; }

    public String getCep() {
        return this.cep;
    }

    public void setCep(String cep) {
        if (cep.length() != 9){
            throw new IllegalArgumentException("CEP deve conter 9 caracteres");
        }

        if (!cep.matches("\\d{5}-\\d{3}")) {
            throw new IllegalArgumentException("CEP com formato inválido");
        }

        this.cep = cep;
    }

    public LocalDateTime getCreatedAt() { return this.createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return this.updatedAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}