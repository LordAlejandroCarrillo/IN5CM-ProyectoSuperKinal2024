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
import org.alejandrocarrillo.dto.CategoriaProductoDTO;
import org.alejandrocarrillo.model.CategoriaProducto;
import org.alejandrocarrillo.system.Main;
import org.alejandrocarrillo.utils.SuperKinalAlert;
public class FormCategoriaProductosController implements Initializable {
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
        if(CategoriaProductoDTO.getCategoriaProductoDTO().getCategoriaProducto() != null){
            cargarDatos(CategoriaProductoDTO.getCategoriaProductoDTO().getCategoriaProducto());
        }
    }
    
    public void cargarDatos(CategoriaProducto cap){
        tfID.setText(Integer.toString(cap.getCategoriaProductosId()));
        tfNombre.setText(cap.getNombreCategoria());
        txaDescripcion.setText(cap.getDescripcionCategoria());
    }
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        boolean token;
        if(event.getSource() == btnCancelar){
            CategoriaProductoDTO.getCategoriaProductoDTO().setCategoriaProducto(null);
            stage.menuCategoriaProductosView();
        } else if(event.getSource() == btnGuardar){
            if(!tfNombre.getText().equals("") && !txaDescripcion.getText().equals("")){
               token = true; 
            } else{
                token = false;
            }
            if(MenuCategoriaProductosController.opcion == 1){
                if(token){
                    agregarCategoriaProducto(tfNombre.getText(),txaDescripcion.getText());
                    SuperKinalAlert.getInstance().mostraAlertaInformacion(400);
                    stage.menuCategoriaProductosView();
                } else{
                    SuperKinalAlert.getInstance().mostraAlertaInformacion(504);
                }
            } else if(MenuCategoriaProductosController.opcion == 2){
                if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(505).get() == ButtonType.OK){
                    if(token){
                        editarCategoriaProducto();
                        CategoriaProductoDTO.getCategoriaProductoDTO().setCategoriaProducto(null);
                        SuperKinalAlert.getInstance().mostraAlertaInformacion(500);
                        stage.menuCategoriaProductosView();   
                    } else{
                        SuperKinalAlert.getInstance().mostraAlertaInformacion(504);
                    }
                } else{
                    stage.menuCategoriaProductosView();
                }
            }
        }
    }
    
    public void agregarCategoriaProducto(String nom, String des){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_agregarCategoriaProductos(?,?)";
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

    public void editarCategoriaProducto(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarCategoriaProductos(?,?,?)";
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
