package org.alejandrocarrillo.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static Conexion instance;
    
    private String jdbcurl = "jdbc:mysql://localhost:3306/acarrilloSK_2020412?serverTimezone=GMT-6";
    private String usuario = "root";
    private String contrasenia = "admin";

    private Conexion(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static Conexion getInstance(){
        if(instance == null){
            instance = new Conexion();
        }
        return instance;
    }

    public Connection obtenerConexion() throws SQLException{
        return DriverManager.getConnection(jdbcurl, usuario, contrasenia);
    }
}
