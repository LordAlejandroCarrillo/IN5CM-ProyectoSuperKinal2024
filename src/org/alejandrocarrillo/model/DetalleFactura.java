package org.alejandrocarrillo.model;

public class DetalleFactura {
    private int detalleFacturaId;
    private int facturaId;
    private int productoId;
    
    public DetalleFactura(){
        
    }

    public DetalleFactura(int detalleFacturaId, int facturaId, int productoId) {
        this.detalleFacturaId = detalleFacturaId;
        this.facturaId = facturaId;
        this.productoId = productoId;
    }

    public int getDetalleFacturaId() {
        return detalleFacturaId;
    }

    public void setDetalleFacturaId(int detalleFacturaId) {
        this.detalleFacturaId = detalleFacturaId;
    }

    public int getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(int facturaId) {
        this.facturaId = facturaId;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }
    
    @Override
    public String toString() {
        return "DetalleFactura";
    }
}
