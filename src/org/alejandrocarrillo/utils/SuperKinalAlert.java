/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alejandrocarrillo.utils;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author informatica
 */
public class SuperKinalAlert {
    private static SuperKinalAlert instance;
    
    private SuperKinalAlert(){
        
    }
    
    public static SuperKinalAlert getInstance(){
        if(instance == null){
            instance = new SuperKinalAlert();
        }
        return instance;
    }
    
    public void mostraAlertaInformacion(int code){
        if(code == 400){ // Codigo 400, agregar registros.
            Alert alert =  new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmacion de Registro");
            alert.setHeaderText("Confirmacion de Registro");
            alert.setContentText("Registro realizado con exito!");
            alert.showAndWait();
        } else if(code == 500){ // Coidog 500, editar registros.
            Alert alert =  new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Edicion Registrada");
            alert.setHeaderText("Edicion Registrada");
            alert.setContentText("Edicion realizada con exito!");
            alert.showAndWait();
        } else if(code == 504){ // Codigo 504, campos pendientes.
            Alert alert =  new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campos Pendientes");
            alert.setHeaderText("Campos Pendientes");
            alert.setContentText("Algunos campos necesarios para el registro estan vacios!");
            alert.showAndWait();
        }
    }
    
    public Optional<ButtonType> mostrarAlertaConfirmacion(int code){
        Optional<ButtonType> action = null;
        if(code == 404){ // Codigo 404, eliminacion de registro.
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Eliminacion de Registro");
            alert.setHeaderText("Eliminacion de Registro");
            alert.setContentText("Desea confirmar la eliminacion de registro?");
            action = alert.showAndWait();
        }else if(code == 505){ // Codigo 505, confirmacion editar.
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmacion de Edicion");
            alert.setHeaderText("Confirmacion de Edicion");
            alert.setContentText("Desea editar este registro?");
            action = alert.showAndWait();
        }
        return action;
    }
}
