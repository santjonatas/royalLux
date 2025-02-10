package jonatas.santos.royal.lux.core.domain.entities;

import jonatas.santos.royal.lux.core.domain.entities.common.Base;

import java.time.LocalDateTime;

public class ServiceCustomerService extends Base {
    protected int CustomerServiceId;
    protected int ServiceId;
    protected LocalDateTime StartTime;
    protected LocalDateTime FinishingTime;

    public int getCustomerServiceId() {
        return CustomerServiceId;
    }

    public void setCustomerServiceId(int customerServiceId) {
        CustomerServiceId = customerServiceId;
    }

    public int getServiceId() {
        return ServiceId;
    }

    public void setServiceId(int serviceId) {
        ServiceId = serviceId;
    }

    public LocalDateTime getStartTime() {
        return StartTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        StartTime = startTime;
    }

    public LocalDateTime getFinishingTime() {
        return FinishingTime;
    }

    public void setFinishingTime(LocalDateTime finishingTime) {
        FinishingTime = finishingTime;
    }
}
