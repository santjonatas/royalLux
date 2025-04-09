package jonatasSantos.royalLux.core.domain.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected int id;

    @OneToOne
    @JoinColumn(name = "customerServiceId", nullable = false)
    protected CustomerService customerService;

    @Column(name = "status", nullable = false, length = 50)
    protected String status;

    @Column(name = "time", nullable = false)
    protected LocalDateTime time;

    @Column(name = "method", nullable = false)
    protected String method;

    @Column(name = "description", length = 500)
    protected String description;

    @Column(name = "createdAt", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @Column(name = "updatedAt")
    protected LocalDateTime updatedAt;
}
