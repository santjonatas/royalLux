package jonatas.santos.royal.lux.core.domain.entities;

import jonatas.santos.royal.lux.core.domain.entities.common.Base;

import java.math.BigDecimal;

public class Role extends Base {
    protected String Name;
    protected String Detail;
    protected BigDecimal Salary;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        if (name.isEmpty()){
            throw new IllegalArgumentException("Nome do cargo não pode ser vazio");
        }
        if (name.length() > 50){
            throw new IllegalArgumentException("Nome do cargo não deve conter mais que 50 caracteres");
        }
        Name = name;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        if (detail.length() > 255){
            throw new IllegalArgumentException("Detalhes não deve conter mais que 255 caracteres");
        }
        Detail = detail;
    }

    public BigDecimal getSalary() {
        return Salary;
    }

    public void setSalary(BigDecimal salary) {
        Salary = salary;
    }
}
