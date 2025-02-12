package jonatasSantos.royalLux.core.domain.entities;

import jonatasSantos.royalLux.core.domain.entities.common.Base;

import java.math.BigDecimal;
import java.time.LocalTime;

public class Service extends Base {
    protected String Name;
    protected String Description;
    protected LocalTime EstimatedTime;
    protected BigDecimal Value;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        if (name.isEmpty()){
            throw new IllegalArgumentException("Nome do serviço não pode ser vazio");
        }
        if (name.length() > 50){
            throw new IllegalArgumentException("Nome do serviço não deve conter mais que 50 caracteres");
        }
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        if (description.length() > 255){
            throw new IllegalArgumentException("Descrição não deve conter mais que 255 caracteres");
        }
        Description = description;
    }

    public LocalTime getEstimatedTime() {
        return EstimatedTime;
    }

    public void setEstimatedTime(LocalTime estimatedTime) {
        EstimatedTime = estimatedTime;
    }

    public BigDecimal getValue() {
        return Value;
    }

    public void setValue(BigDecimal value) {
        Value = value;
    }
}
