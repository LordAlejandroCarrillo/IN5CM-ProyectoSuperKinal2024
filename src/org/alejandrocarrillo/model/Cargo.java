package org.alejandrocarrillo.model;

public class Cargo {
    private int cargoId;
    private String nombre;
    private String descripcionCargo;
    
    public Cargo(){
        
    }

    public Cargo(int cargoId, String nombre, String descripcionCargo) {
        this.cargoId = cargoId;
        this.nombre = nombre;
        this.descripcionCargo = descripcionCargo;
    }

    public int getCargoId() {
        return cargoId;
    }

    public void setCargoId(int cargoId) {
        this.cargoId = cargoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcionCargo() {
        return descripcionCargo;
    }

    public void setDescripcionCargo(String descripcionCargo) {
        this.descripcionCargo = descripcionCargo;
    }
    
    @Override
    public String toString() {
        return "Id: " + cargoId + " | " + nombre;
    }
}
