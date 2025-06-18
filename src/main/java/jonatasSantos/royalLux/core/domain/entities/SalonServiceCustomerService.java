package jonatasSantos.royalLux.core.domain.entities;

import jakarta.persistence.*;
import jonatasSantos.royalLux.core.domain.entities.common.Base;

import java.time.LocalDateTime;

@Entity
@Table(name = "salonServicesCustomerService")
public class SalonServiceCustomerService {

    public SalonServiceCustomerService(CustomerService customerService, SalonService salonService, Employee employee, LocalDateTime startTime, LocalDateTime finishingTime, Boolean completed) {
        this.setCustomerService(customerService);
        this.setSalonService(salonService);
        this.setEmployee(employee);
        this.startTime = startTime;
        this.finishingTime = finishingTime;
        this.setCompleted(completed);
        this.createdAt = LocalDateTime.now();
    }

    public SalonServiceCustomerService() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Integer id;

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
    protected Boolean completed;

    @Column(name = "createdAt", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @Column(name = "updatedAt")
    protected LocalDateTime updatedAt;

    public Integer getId() { return this.id; }

    public void setId(Integer id) { this.id = id; }

    public CustomerService getCustomerService() {
        return this.customerService;
    }

    public void setCustomerService(CustomerService customerService) {
        if(customerService == null)
            throw new IllegalArgumentException("Atendimento não pode ser nulo");

        this.customerService = customerService;
    }

    public SalonService getSalonService() {
        return this.salonService;
    }

    public void setSalonService(SalonService salonService) {
        if(salonService == null)
            throw new IllegalArgumentException("Serviço não pode ser nulo");

        this.salonService = salonService;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        if(employee == null)
            throw new IllegalArgumentException("Funcionário não pode ser nulo");

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

    public Boolean isCompleted() {
        return this.completed;
    }

    public void setCompleted(Boolean completed) {
        if(completed == null)
            throw new IllegalArgumentException("Completo não pode ser nulo");

        this.completed = completed;
    }

    public LocalDateTime getCreatedAt() { return this.createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return this.updatedAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
