package jonatasSantos.royalLux.core.domain.entities;

import jonatasSantos.royalLux.core.domain.entities.common.Base;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CustomerService extends Base {
    protected int ClientId;
    protected String Status;
    protected LocalDateTime StartTime;
    protected LocalDateTime EstimatedFinishingTime;
    protected LocalDateTime FinishingTime;
    protected BigDecimal TotalValue;
    protected String Details;
    protected boolean Completed = false;

    public int getClientId() {
        return ClientId;
    }

    public void setClientId(int clientId) {
        ClientId = clientId;
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

    public LocalDateTime getStartTime() {
        return StartTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        StartTime = startTime;
    }

    public LocalDateTime getEstimatedFinishingTime() {
        return EstimatedFinishingTime;
    }

    public void setEstimatedFinishingTime(LocalDateTime estimatedFinishingTime) {
        EstimatedFinishingTime = estimatedFinishingTime;
    }

    public LocalDateTime getFinishingTime() {
        return FinishingTime;
    }

    public void setFinishingTime(LocalDateTime finishingTime) {
        FinishingTime = finishingTime;
    }

    public BigDecimal getTotalValue() {
        return TotalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        TotalValue = totalValue;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        if (details.length() > 255){
            throw new IllegalArgumentException("Detalhes do atendimento não deve conter mais que 255 caracteres");
        }
        Details = details;
    }

    public boolean isCompleted() { return Completed; }

    public void setCompleted(boolean completed) { Completed = completed; }
}
