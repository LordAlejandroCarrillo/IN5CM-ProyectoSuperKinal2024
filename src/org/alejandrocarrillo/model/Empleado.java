package org.alejandrocarrillo.model;

import java.sql.Time;

public class Empleado {
    private int empleadoId;
    private String nombreEmpleado;
    private String apellidoEmpleado;
    private double sueldo;
    private Time horaEntrada;
    private Time horaSalida;
    private String cargo;
    private int cargoId;
    private String encargado;
    private int encargadoId;
    
    public Empleado(){
        
    }

    public Empleado(int empleadoId, String nombreEmpleado, String apellidoEmpleado, double sueldo, Time horaEntrada, Time horaSalida, int cargoId, int encargadoId) {
        this.empleadoId = empleadoId;
        this.nombreEmpleado = nombreEmpleado;
        this.apellidoEmpleado = apellidoEmpleado;
        this.sueldo = sueldo;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.cargoId = cargoId;
        this.encargadoId = encargadoId;
    }

    public Empleado(int empleadoId, String nombreEmpleado, String apellidoEmpleado, double sueldo, Time horaEntrada, Time horaSalida, String cargo, String encargado) {
        this.empleadoId = empleadoId;
        this.nombreEmpleado = nombreEmpleado;
        this.apellidoEmpleado = apellidoEmpleado;
        this.sueldo = sueldo;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.cargo = cargo;
        this.encargado = encargado;
    }
    
    public Empleado(int empleadoId, String nombreEmpleado, String apellidoEmpleado, double sueldo, Time horaEntrada, Time horaSalida, String cargo, int encargadoId) {
        this.empleadoId = empleadoId;
        this.nombreEmpleado = nombreEmpleado;
        this.apellidoEmpleado = apellidoEmpleado;
        this.sueldo = sueldo;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.cargo = cargo;
        this.encargadoId = encargadoId;
    }
    
    

    public Empleado(int empleadoId, String nombreEmpleado, String apellidoEmpleado) {
        this.empleadoId = empleadoId;
        this.nombreEmpleado = nombreEmpleado;
        this.apellidoEmpleado = apellidoEmpleado;
    }

    public int getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(int empleadoId) {
        this.empleadoId = empleadoId;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public String getApellidoEmpleado() {
        return apellidoEmpleado;
    }

    public void setApellidoEmpleado(String apellidoEmpleado) {
        this.apellidoEmpleado = apellidoEmpleado;
    }

    public double getSueldo() {
        return sueldo;
    }

    public void setSueldo(double sueldo) {
        this.sueldo = sueldo;
    }

    public Time getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(Time horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public Time getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(Time horaSalida) {
        this.horaSalida = horaSalida;
    }

    public int getCargoId() {
        return cargoId;
    }

    public void setCargoId(int cargoId) {
        this.cargoId = cargoId;
    }

    public int getEncargadoId() {
        return encargadoId;
    }

    public void setEncargadoId(int encargadoId) {
        this.encargadoId = encargadoId;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getEncargado() {
        return encargado;
    }

    public void setEncargado(String encargado) {
        this.encargado = encargado;
    }
    
    @Override
    public String toString() {
        return "Id: " + empleadoId + " | " + nombreEmpleado + " " + apellidoEmpleado;
    }
}
