package org.alejandrocarrillo.dto;

import org.alejandrocarrillo.model.CategoriaProducto;

public class CategoriaProductoDTO {
    private static CategoriaProductoDTO instance;
    private CategoriaProducto categoria;
    
    private CategoriaProductoDTO(){
        
    }
    
    public static CategoriaProductoDTO getCategoriaProductoDTO(){
        if(instance == null){
            instance = new CategoriaProductoDTO();
        }
        return instance;
    }

    public CategoriaProducto getCategoriaProducto() {
        return categoria;
    }

    public void setCategoriaProducto(CategoriaProducto categoria) {
        this.categoria = categoria;
    }    
}
