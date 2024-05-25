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
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.alejandrocarrillo.dao.Conexion;
import org.alejandrocarrillo.dto.CargoDTO;
import org.alejandrocarrillo.model.Cargo;
import org.alejandrocarrillo.system.Main;
import org.alejandrocarrillo.utils.SuperKinalAlert;

public class FormCargosController implements Initializable {
    private Main stage;
    private int opcion;
    
    @FXML
    TextField tfID, tfNombre;
    @FXML
    Button btnGuardar, btnCancelar;
    @FXML
    TextArea txaDescripcion;
    
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
        if(CargoDTO.getCargoDTO().getCargo() != null){
              cargarDatos(CargoDTO.getCargoDTO().getCargo());
        }
    }
    
    public void cargarDatos(Cargo cargo){
        tfID.setText(Integer.toString(cargo.getCargoId()));
        tfNombre.setText(cargo.getNombre());
        txaDescripcion.setText(cargo.getDescripcionCargo());
    }
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        boolean token;
        if(event.getSource() == btnCancelar){
            CargoDTO.getCargoDTO().setCargo(null);
            stage.menuCargosView();
        } else if(event.getSource() == btnGuardar){
            if(!tfNombre.getText().equals("") && !txaDescripcion.getText().equals("")){
               token = true; 
            } else{
                token = false;
            }
            if(MenuCargosController.opcion == 1){
                if(token){
                    agregarCargos(tfNombre.getText(),txaDescripcion.getText());
                    SuperKinalAlert.getInstance().mostraAlertaInformacion(400);
                    stage.menuCargosView();
                } else{
                    SuperKinalAlert.getInstance().mostraAlertaInformacion(504);
                }
            }else if(MenuCargosController.opcion == 2){
                if(token){
                    if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(505).get() == ButtonType.OK){
                        editarCargo();
                        CargoDTO.getCargoDTO().setCargo(null);
                        SuperKinalAlert.getInstance().mostraAlertaInformacion(500);
                        stage.menuCargosView();  
                    } else{
                        stage.menuCargosView();
                    }
                } else{
                    SuperKinalAlert.getInstance().mostraAlertaInformacion(504);
                }
            }
        }
    }
    
    public void agregarCargos(String nom, String des){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_agregarCargos(?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, nom);
            statement.setString(2, des);
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
    
    public void editarCargo(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarCargos(?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tfID.getText());
            statement.setString(2, tfNombre.getText());
            statement.setString(3, txaDescripcion.getText());
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
