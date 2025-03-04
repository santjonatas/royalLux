package jonatasSantos.royalLux.core.domain.entities;

import jonatasSantos.royalLux.core.domain.entities.common.Base;

import java.math.BigDecimal;

public class Employee extends Base {
    protected int UserId;
    protected String Title;
    protected BigDecimal Salary;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        if (title.length() < 5){
            throw new IllegalArgumentException("Título deve conter pelo menos 5 caracteres");
        }
        if (title.length() > 50){
            throw new IllegalArgumentException("Título não deve conter mais que 50 caracteres");
        }

        Title = title;
    }

    public BigDecimal getSalary() {
        return Salary;
    }

    public void setSalary(BigDecimal salary) {
        Salary = salary;
    }
}
