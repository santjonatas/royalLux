package jonatasSantos.royalLux.core.domain.entities;

import jonatasSantos.royalLux.core.domain.entities.common.Base;

import java.time.LocalDateTime;

public class ServiceCustomerService extends Base {
    protected int CustomerServiceId;
    protected int ServiceId;
    protected int EmployeeId;
    protected LocalDateTime StartTime;
    protected LocalDateTime FinishingTime;
    protected boolean Completed = false;

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

    public int getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(int employeeId) {
        EmployeeId = employeeId;
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

    public boolean isCompleted() { return Completed; }

    public void setCompleted(boolean completed) { Completed = completed; }
}
