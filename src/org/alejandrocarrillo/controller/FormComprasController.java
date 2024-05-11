package org.alejandrocarrillo.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import org.alejandrocarrillo.dao.Conexion;
import org.alejandrocarrillo.dto.CompraDTO;
import org.alejandrocarrillo.model.Compra;
import org.alejandrocarrillo.system.Main;
import org.alejandrocarrillo.utils.SuperKinalAlert;

public class FormComprasController implements Initializable {
    private Main stage;
    private int opcion;
    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
    
    @FXML
    TextField tfID, tfFecha, tfTotal;
    @FXML
    Button btnGuardar, btnCancelar;
    
    private Connection conexion;
    private PreparedStatement statement;
    private ResultSet resultSet;

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(CompraDTO.getCompraDTO().getCompra() != null){
              cargarDatos(CompraDTO.getCompraDTO().getCompra());
        }
    }
    
    public void cargarDatos(Compra compra){
        tfID.setText(Integer.toString(compra.getCompraId()));
        String fecha = formato.format(compra.getFechaCompra());
        tfFecha.setText(fecha);
        tfTotal.setText(Double.toString(compra.getTotalCompra()));
    }
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        boolean token;
        if(event.getSource() == btnCancelar){
            CompraDTO.getCompraDTO().setCompra(null);
            stage.menuComprasView();
        } else if(event.getSource() == btnGuardar){
            if(!tfFecha.getText().equals("") && !tfTotal.getText().equals("")){
               token = true; 
            } else{
                token = false;
            }
            if(MenuComprasController.opcion == 1){
                if(token){
                    agregarCompra(tfFecha.getText(),tfTotal.getText());
                    SuperKinalAlert.getInstance().mostraAlertaInformacion(400);
                    stage.menuComprasView();
                } else{
                    SuperKinalAlert.getInstance().mostraAlertaInformacion(504);
                }
            } else if(MenuComprasController.opcion == 2){
                if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(505).get() == ButtonType.OK){
                    if(token){
                        editarCompra();
                        CompraDTO.getCompraDTO().setCompra(null);
                        SuperKinalAlert.getInstance().mostraAlertaInformacion(500);
                        stage.menuComprasView();   
                    } else{
                        SuperKinalAlert.getInstance().mostraAlertaInformacion(504);
                    }
                } else{
                    stage.menuComprasView();
                }
            }
        }
    }
    
    public void agregarCompra(String fec, String tot){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_agregarCompras(?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, fec);
            statement.setString(2, tot);
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
    
    public void editarCompra(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarCompras(?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tfID.getText());
            statement.setString(2, tfFecha.getText());
            statement.setString(3, tfTotal.getText());
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
