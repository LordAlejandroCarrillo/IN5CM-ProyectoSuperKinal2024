package org.alejandrocarrillo.dto;

import org.alejandrocarrillo.model.Promocion;

public class PromocionDTO {
    private static PromocionDTO instance;
    private Promocion promocion;
    
    private PromocionDTO(){
        
    }
    
    public static PromocionDTO getPromocionDTO(){
        if(instance == null){
            instance = new PromocionDTO();
        }
        return instance;
    }

    public Promocion getPromocion() {
        return promocion;
    }

    public void setPromocion(Promocion promocion) {
        this.promocion = promocion;
    }    
}
