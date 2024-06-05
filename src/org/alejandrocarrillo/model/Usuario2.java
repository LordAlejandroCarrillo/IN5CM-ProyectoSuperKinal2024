package org.alejandrocarrillo.model;

public class Usuario2 {
    private static Usuario2 instance;
    private int usuarioId;
    private String usuario;
    private String contra;
    private int nivelAccesoId;
    private int empleadoId;

    private Usuario2() {
    }
    
    public static Usuario2 getInstance(int us){
        if(us == 1){
            instance = null;
        }
        if(instance == null){
            instance = new Usuario2();
        }
        return instance;
    }

    public Usuario2(int usuarioId, String usuario, String contra, int nivelAccesoId, int empleadoId) {
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
