package jonatasSantos.royalLux.core.domain.entities;

import jonatasSantos.royalLux.core.domain.entities.common.Base;

import java.time.LocalDateTime;

public class Payment extends Base {
    protected int CustomerServiceId;
    protected String Status;
    protected LocalDateTime Time;
    protected String Method;

    public int getCustomerServiceId() {
        return CustomerServiceId;
    }

    public void setCustomerServiceId(int customerServiceId) {
        CustomerServiceId = customerServiceId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        if (status.isEmpty()){
            throw new IllegalArgumentException("Status não pode ser vazio");
        }
        if (status.length() > 20){
            throw new IllegalArgumentException("Status não deve conter mais que 20 caracteres");
        }
        Status = status;
    }

    public LocalDateTime getTime() {
        return Time;
    }

    public void setTime(LocalDateTime time) {
        Time = time;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        if (method.isEmpty()){
            throw new IllegalArgumentException("Método de pagamento não pode ser vazio");
        }
        if (method.length() > 50){
            throw new IllegalArgumentException("Método de pagamento não deve conter mais que 50 caracteres");
        }
        Method = method;
    }
}
