package org.alejandrocarrillo.model;

public class Usuario {
    private int usuarioId;
    private String usuario;
    private String contra;
    private int nivelAccesoId;
    private int empleadoId;

    public Usuario() {
    }

    public Usuario(int usuarioId, String usuario, String contra, int nivelAccesoId, int empleadoId) {
        this.usuarioId = usuarioId;
        this.usuario = usuario;
        this.contra = contra;
        this.nivelAccesoId = nivelAccesoId;
        this.empleadoId = empleadoId;
    }
    
    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public int getNivelAccesoId() {
        return nivelAccesoId;
    }

    public void setNivelAccesoId(int nivelAccesoId) {
        this.nivelAccesoId = nivelAccesoId;
    }

    public int getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(int empleadoId) {
        this.empleadoId = empleadoId;
    }
    
}
