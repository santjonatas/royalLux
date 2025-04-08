package jonatasSantos.royalLux.core.domain.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "employeesRoles")
public class EmployeeRole{

    public EmployeeRole(Employee employee, Role role) {
        this.setEmployee(employee);
        this.setRole(role);
        this.createdAt = LocalDateTime.now();
    }

    public EmployeeRole() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected int id;

    @ManyToOne
    @JoinColumn(name = "employeeId", nullable = false)
    protected Employee employee;

    @ManyToOne
    @JoinColumn(name = "roleId", nullable = false)
    protected Role role;

    @Column(name = "createdAt", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @Column(name = "updatedAt")
    protected LocalDateTime updatedAt;

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        if(employee == null)
            throw new IllegalArgumentException("Funcionário não pode ser nulo");

        this.employee = employee;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        if(role == null)
            throw new IllegalArgumentException("Função não pode ser nula");

        this.role = role;
    }

    public LocalDateTime getCreatedAt() { return this.createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return this.updatedAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
