package org.alejandrocarrillo.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.alejandrocarrillo.dao.Conexion;
import org.alejandrocarrillo.dto.DistribuidorDTO;
import org.alejandrocarrillo.model.Distribuidor;
import org.alejandrocarrillo.system.Main;
import org.alejandrocarrillo.utils.SuperKinalAlert;

public class FormDistribuidoresController implements Initializable {
    private Main stage;
    private int opcion;
    
    @FXML
    TextField tfID, tfNombre, tfDireccion, tfNit, tfTelefono, tfWeb;
    @FXML
    Button btnGuardar, btnCancelar;
    
    private Connection conexion;
    private PreparedStatement statement;

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(DistribuidorDTO.getDistribuidorDTO().getDistribuidor() != null){
              cargarDatos(DistribuidorDTO.getDistribuidorDTO().getDistribuidor());
        }
    }
    
    public void cargarDatos(Distribuidor distribuidor){
        tfID.setText(Integer.toString(distribuidor.getDistribuidorId()));
        tfNombre.setText(distribuidor.getNombreDistribuidor());
        tfDireccion.setText(distribuidor.getDireccionDistribuidor());
        tfNit.setText(distribuidor.getNitDistribuidor());
        tfTelefono.setText(distribuidor.getTelefonoDistribuidor());
        tfWeb.setText(distribuidor.getWeb());
    }
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        boolean token;
        if(event.getSource() == btnCancelar){
            DistribuidorDTO.getDistribuidorDTO().setDistribuidor(null);
            stage.menuDistribuidoresView();
        } else if(event.getSource() == btnGuardar){
            if(!tfNombre.getText().equals("") && !tfDireccion.getText().equals("") && !tfNit.getText().equals("") && !tfTelefono.getText().equals("")){
               token = true; 
            } else{
                token = false;
            }
            if(MenuDistribuidoresController.opcion == 1){
                if(token){
                    agregarDistribuidor(tfNombre.getText(),tfDireccion.getText(),tfNit.getText(),tfTelefono.getText(),tfWeb.getText());
                    SuperKinalAlert.getInstance().mostraAlertaInformacion(400);
                    stage.menuDistribuidoresView();
                } else{
                    SuperKinalAlert.getInstance().mostraAlertaInformacion(504);
                }
            } else if(MenuDistribuidoresController.opcion == 2){
                if(token){
                    if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(505).get() == ButtonType.OK){
                        editarDistribuidor();
                        DistribuidorDTO.getDistribuidorDTO().setDistribuidor(null);
                        SuperKinalAlert.getInstance().mostraAlertaInformacion(500);
                        stage.menuDistribuidoresView();  
                    } else{
                        stage.menuDistribuidoresView();
                    }
                } else{
                    SuperKinalAlert.getInstance().mostraAlertaInformacion(504);
                }
            }
        }
    }
    
    public void agregarDistribuidor(String nom, String dir, String nit, String tel, String web){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_agregarDistribuidores(?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, nom);
            statement.setString(2, dir);
            statement.setString(3, nit);
            statement.setString(4, tel);
            statement.setString(5, web);
            statement.execute();
        } catch(SQLException e){
            System.out.println(e.getMessage());
        } finally{
            try{
                if(statement != null){
                    statement.close();
                }
                if(conexion != null){
                    conexion.close();
                } 
                
            } catch(SQLException e){
                
            }
        }
    }
    
    
    public void editarDistribuidor(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarDistribuidores(?,?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tfID.getText());
            statement.setString(2, tfNombre.getText());
            statement.setString(3, tfDireccion.getText());
            statement.setString(4, tfNit.getText());
            statement.setString(5, tfTelefono.getText());
            statement.setString(6, tfWeb.getText());
            statement.execute();
        } catch(SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally{
            try{
                if(statement != null){
                    statement.close();
                }
                
                if(conexion != null){
                    conexion.close();
                }
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
}
