package jonatasSantos.royalLux.core.domain.entities;

import jonatasSantos.royalLux.core.domain.entities.common.Base;

public class Address extends Base {
    protected int UserId;
    protected String Street;
    protected String Number;
    protected String Complement;
    protected String Neighborhood;
    protected String City;
    protected String UF;
    protected String CEP;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        if (street.length() < 2){
            throw new IllegalArgumentException("Rua deve conter pelo menos 2 caracteres");
        }
        if (street.length() > 255){
            throw new IllegalArgumentException("Rua não deve conter mais que 255 caracteres");
        }

        Street = street;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        if (number.length() < 1){
            throw new IllegalArgumentException("Número deve conter pelo menos 1 caractere");
        }
        if (number.length() > 15){
            throw new IllegalArgumentException("Número não deve conter mais que 15 caracteres");
        }

        Number = number;
    }

    public String getComplement() {
        return Complement;
    }

    public void setComplement(String complement) {
        if (complement.length() > 50){
            throw new IllegalArgumentException("Complemento não deve conter mais que 50 caracteres");
        }

        Complement = complement;
    }

    public String getNeighborhood() {
        return Neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        if (neighborhood.length() < 2){
            throw new IllegalArgumentException("Bairro deve conter pelo menos 2 caracteres");
        }
        if (neighborhood.length() > 100){
            throw new IllegalArgumentException("Bairro não deve conter mais que 100 caracteres");
        }

        Neighborhood = neighborhood;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        if (city.length() < 2){
            throw new IllegalArgumentException("Cidade deve conter pelo menos 2 caracteres");
        }
        if (city.length() > 100){
            throw new IllegalArgumentException("Cidade não deve conter mais que 100 caracteres");
        }

        City = city;
    }

    public String getUF() {
        return UF;
    }

    public void setUF(String UF) {
        if (UF.length() != 2){
            throw new IllegalArgumentException("UF deve conter 2 caracteres");
        }

        this.UF = UF;
    }

    public String getCEP() {
        return CEP;
    }

    public void setCEP(String CEP) {
        if (CEP.length() != 8){
            throw new IllegalArgumentException("CEP deve conter 8 caracteres");
        }

        this.CEP = CEP;
    }
}
