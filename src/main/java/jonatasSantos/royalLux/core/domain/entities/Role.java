package jonatasSantos.royalLux.core.domain.entities;

import jonatasSantos.royalLux.core.domain.entities.common.Base;

import java.math.BigDecimal;

public class Role extends Base {
    protected String Name;
    protected String Detail;

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
}
