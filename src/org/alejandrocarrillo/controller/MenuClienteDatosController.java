package org.alejandrocarrillo.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.alejandrocarrillo.dao.Conexion;
import org.alejandrocarrillo.model.Cliente;
import org.alejandrocarrillo.system.Main;
import java.sql.SQLException;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import org.alejandrocarrillo.controller.MenuClienteController;
import org.alejandrocarrillo.utils.SuperKinalAlert;
import org.alejandrocarrillo.dto.ClienteDTO;
public class MenuClienteDatosController implements Initializable {
    private int opcion;
    
    MenuClienteController clienteController = new MenuClienteController();
    private Main stage;
    
    @FXML
    TextField tfID, tfNombre, tfApellido, tfTelefono, tfDireccion, tfNit;
    @FXML
    Button btnGuardar, btnCancelar;
    @FXML
    TableView tblClientes;
    private Connection conexion;
    private PreparedStatement statement;
  
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(ClienteDTO.getClienteDTO().getCliente() != null){
              cargarDatos(ClienteDTO.getClienteDTO().getCliente());
        }
    }
    
    public void cargarDatos(Cliente cliente){
        tfID.setText(Integer.toString(cliente.getClienteId()));
        tfNit.setText(cliente.getNit());
        tfNombre.setText(cliente.getNombre());
        tfApellido.setText(cliente.getApellido());
        tfTelefono.setText(cliente.getTelefono());
        tfDireccion.setText(cliente.getDireccion());  
    }
    
    @FXML
    public void handleButtonAction (ActionEvent event){
        boolean token;
        if(event.getSource() == btnCancelar){
            ClienteDTO.getClienteDTO().setCliente(null);
            stage.menuClienteView();
        } else if(event.getSource() == btnGuardar){
            if(!tfNombre.getText().equals("") && !tfApellido.getText().equals("") && !tfDireccion.getText().equals("")){
               token = true; 
            } else{
                token = false;
            }
            if(MenuClienteController.opcion == 1){
                if(token){
                    agregarClientes(tfNit.getText(), tfNombre.getText(), tfApellido.getText(), tfTelefono.getText(), tfDireccion.getText());
                    SuperKinalAlert.getInstance().mostraAlertaInformacion(400);
                    stage.menuClienteView();
                } else{
                    SuperKinalAlert.getInstance().mostraAlertaInformacion(504);
                }
            } else if(MenuClienteController.opcion == 2){
                if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(505).get() == ButtonType.OK){
                    if(token){
                        editarCliente();
                        ClienteDTO.getClienteDTO().setCliente(null);
                        SuperKinalAlert.getInstance().mostraAlertaInformacion(500);
                        stage.menuClienteView();   
                    } else{
                        SuperKinalAlert.getInstance().mostraAlertaInformacion(504);
                    }
                } else{
                    stage.menuClienteView();
                }
            }
        }
    }
    
    public int opcion(int opcion){
        return opcion;
    }

    public void agregarClientes(String n17, String nom, String ape, String tel, String dir){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_agregarClientes(?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, n17);
            statement.setString(2, nom);
            statement.setString(3, ape);
            statement.setString(4, tel);
            statement.setString(5, dir);
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
    
    public void editarCliente(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarClientes(?,?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tfID.getText());
            statement.setString(2, tfNit.getText());
            statement.setString(3, tfNombre.getText());
            statement.setString(4, tfApellido.getText());
            statement.setString(5, tfTelefono.getText());
            statement.setString(6, tfDireccion.getText());
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

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
}
