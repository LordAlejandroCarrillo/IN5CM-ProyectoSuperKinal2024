package org.alejandrocarrillo.dto;

import org.alejandrocarrillo.model.DetalleFactura;

public class DetalleFacturaDTO {
    private static DetalleFacturaDTO instance;
    private DetalleFactura detFact;
    
    private DetalleFacturaDTO(){
        
    }
    
    public static DetalleFacturaDTO getDetalleFacturaDTO(){
        if(instance == null){
            instance = new DetalleFacturaDTO();
        }
        return instance;
    }

    public DetalleFactura getDetalleFactura() {
        return detFact;
    }

    public void setDetalleFactura(DetalleFactura detFact) {
        this.detFact = detFact;
    }    
}
