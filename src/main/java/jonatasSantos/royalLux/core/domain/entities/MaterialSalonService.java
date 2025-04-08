package jonatasSantos.royalLux.core.domain.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "materialsSalonServices")
public class MaterialSalonService {

    public MaterialSalonService(SalonService salonService, Material material, Integer quantityMaterial) {
        this.setSalonService(salonService);
        this.setMaterial(material);
        this.setQuantityMaterial(quantityMaterial);
        this.createdAt = LocalDateTime.now();
    }

    public MaterialSalonService() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected int id;

    @ManyToOne
    @JoinColumn(name = "salonServiceId", nullable = false)
    protected SalonService salonService;

    @ManyToOne
    @JoinColumn(name = "materialId", nullable = false)
    protected Material material;

    @Column(name = "quantityMaterial", nullable = false)
    protected Integer quantityMaterial;

    @Column(name = "createdAt", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @Column(name = "updatedAt")
    protected LocalDateTime updatedAt;

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    public SalonService getSalonService() {
        return this.salonService;
    }

    public void setSalonService(SalonService salonService) {
        if(salonService == null)
            throw new IllegalArgumentException("Salão não pode ser nulo");

        this.salonService = salonService;
    }

    public Material getMaterial() {
        return this.material;
    }

    public void setMaterial(Material material) {
        if(material == null)
            throw new IllegalArgumentException("Material não pode ser nulo");

        this.material = material;
    }

    public Integer getQuantityMaterial() {
        return this.quantityMaterial;
    }

    public void setQuantityMaterial(Integer quantityMaterial) {
        if(quantityMaterial == null)
            throw new IllegalArgumentException("Quantidade de materiais não pode ser nula");

        this.quantityMaterial = quantityMaterial;
    }

    public LocalDateTime getCreatedAt() { return this.createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return this.updatedAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
