package jonatas.santos.royal.lux.core.domain.entities;

import jonatas.santos.royal.lux.core.domain.entities.common.Base;

public class MaterialService extends Base {
    protected int ServiceId;
    protected int MaterialId;
    protected int QuantityMaterial;

    public int getServiceId() {
        return ServiceId;
    }

    public void setServiceId(int serviceId) {
        ServiceId = serviceId;
    }

    public int getMaterialId() {
        return MaterialId;
    }

    public void setMaterialId(int materialId) {
        MaterialId = materialId;
    }

    public int getQuantityMaterial() {
        return QuantityMaterial;
    }

    public void setQuantityMaterial(int quantityMaterial) {
        if (quantityMaterial <= 0){
            throw new IllegalArgumentException("Quantidade de materiais deve ser maior que zero");
        }
        QuantityMaterial = quantityMaterial;
    }
}
