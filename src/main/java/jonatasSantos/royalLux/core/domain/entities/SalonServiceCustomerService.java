package jonatasSantos.royalLux.core.domain.entities;

import jakarta.persistence.*;
import jonatasSantos.royalLux.core.domain.entities.common.Base;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "salonServicesCustomerService")
public class SalonServiceCustomerService {

    public SalonServiceCustomerService(CustomerService customerService, SalonService salonService, Employee employee, LocalDate date, LocalTime startTime, LocalTime estimatedFinishingTime, Boolean completed) {
        this.setCustomerService(customerService);
        this.setSalonService(salonService);
        this.setEmployee(employee);
        this.setDate(date);
        this.startTime = startTime;
        this.estimatedFinishingTime = estimatedFinishingTime;
        this.setCompleted(completed);
        this.createdAt = LocalDateTime.now();
    }

    public SalonServiceCustomerService(CustomerService customerService, SalonService salonService, Employee employee, LocalDate date, LocalTime startTime, Boolean completed) {
        this.setCustomerService(customerService);
        this.setSalonService(salonService);
        this.setEmployee(employee);
        this.setDate(date);
        this.startTime = startTime;
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

    @Column(name = "date")
    protected LocalDate date;

    @Column(name = "startTime")
    protected LocalTime startTime;

    @Column(name = "estimatedFinishingTime")
    protected LocalTime estimatedFinishingTime;

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

    public LocalDate getDate() { return date; }

    public void setDate(LocalDate date) {

        if(date == null)
            throw new IllegalArgumentException("Data não pode ser nula");

        this.date = date;
    }

    public LocalTime getStartTime() {
        return this.startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEstimatedFinishingTime() {
        return this.estimatedFinishingTime;
    }

    public void setEstimatedFinishingTime(LocalTime finishingTime) {
        this.estimatedFinishingTime = finishingTime;
    }

    public void incrementEstimatedFinishingTime(LocalTime estimatedTime){
        if(this.estimatedFinishingTime == null)
            this.estimatedFinishingTime = this.startTime;

        this.estimatedFinishingTime = this.estimatedFinishingTime
                .plusHours(estimatedTime.getHour())
                .plusMinutes(estimatedTime.getMinute());
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
