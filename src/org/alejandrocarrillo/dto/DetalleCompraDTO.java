package org.alejandrocarrillo.dto;

import org.alejandrocarrillo.model.DetalleCompra;

public class DetalleCompraDTO {
    private static DetalleCompraDTO instance;
    private DetalleCompra detCompra;
    
    private DetalleCompraDTO(){
        
    }
    
    public static DetalleCompraDTO getDetalleCompraDTO(){
        if(instance == null){
            instance = new DetalleCompraDTO();
        }
        return instance;
    }

    public DetalleCompra getDetalleCompra() {
        return detCompra;
    }

    public void setDetalleCompra(DetalleCompra detCompra) {
        this.detCompra = detCompra;
    }    
}