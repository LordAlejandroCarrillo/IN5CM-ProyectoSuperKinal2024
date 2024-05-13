package org.alejandrocarrillo.model;

import java.sql.Date;
import java.sql.Time;

public class Factura {
    private int facturaId;
    private Date fecha;
    private Time hora;
    private String cliente;
    private int clienteId;
    private String empleado;
    private int empleadoId;
    private double total;
    
    public Factura(){
        
    }
    //Agregar
    public Factura(int facturaId, Date fecha, Time hora, int clienteId, int empleadoId, double total) {
        this.facturaId = facturaId;
        this.fecha = fecha;
        this.hora = hora;
        this.clienteId = clienteId;
        this.empleadoId = empleadoId;
        this.total = total;
    }
    //Listar

    public Factura(int facturaId, Date fecha, Time hora, String cliente, String empleado, double total) {
        this.facturaId = facturaId;
        this.fecha = fecha;
        this.hora = hora;
        this.cliente = cliente;
        this.empleado = empleado;
        this.total = total;
    }
    
    public int getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(int facturaId) {
        this.facturaId = facturaId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(int empleadoId) {
        this.empleadoId = empleadoId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }
    
    @Override
    public String toString() {
        return  "Id: " + facturaId + " | " + fecha;
    }
}
