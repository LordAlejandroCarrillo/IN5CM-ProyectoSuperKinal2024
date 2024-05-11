package org.alejandrocarrillo.dto;

import org.alejandrocarrillo.model.Producto;

public class ProductoDTO {
    private static ProductoDTO instance;
    private Producto producto;
    
    private ProductoDTO(){
        
    }
    
    public static ProductoDTO getProductoDTO(){
        if(instance == null){
            instance = new ProductoDTO();
        }
        return instance;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }    
}
