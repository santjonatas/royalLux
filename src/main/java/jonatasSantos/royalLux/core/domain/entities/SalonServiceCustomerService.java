package jonatasSantos.royalLux.core.domain.entities;

import jakarta.persistence.*;
import jonatasSantos.royalLux.core.domain.entities.common.Base;

import java.time.LocalDateTime;

@Entity
@Table(name = "salonServicesCustomerService")
public class SalonServiceCustomerService {

    public SalonServiceCustomerService(CustomerService customerService, SalonService salonService, Employee employee, LocalDateTime startTime, LocalDateTime finishingTime, boolean completed) {
        this.customerService = customerService;
        this.salonService = salonService;
        this.employee = employee;
        this.startTime = startTime;
        this.finishingTime = finishingTime;
        this.completed = completed;
        this.createdAt = LocalDateTime.now();
    }

    public SalonServiceCustomerService() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected int id;

    @ManyToOne
    @JoinColumn(name = "customerServiceId", nullable = false, updatable = false)
    protected CustomerService customerService;

    @ManyToOne
    @JoinColumn(name = "salonServiceId", nullable = false, updatable = false)
    protected SalonService salonService;

    @ManyToOne
    @JoinColumn(name = "employeeId", nullable = false, updatable = false)
    protected Employee employee;

    @Column(name = "startTime")
    protected LocalDateTime startTime;

    @Column(name = "finishingTime")
    protected LocalDateTime finishingTime;

    @Column(name = "completed", nullable = false)
    protected boolean completed;

    @Column(name = "createdAt", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @Column(name = "updatedAt")
    protected LocalDateTime updatedAt;

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    public CustomerService getCustomerService() {
        return this.customerService;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public SalonService getSalonService() {
        return this.salonService;
    }

    public void setSalonService(SalonService salonService) {
        this.salonService = salonService;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getFinishingTime() {
        return this.finishingTime;
    }

    public void setFinishingTime(LocalDateTime finishingTime) {
        this.finishingTime = finishingTime;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getCreatedAt() { return this.createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return this.updatedAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
