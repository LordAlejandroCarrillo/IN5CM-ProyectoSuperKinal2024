/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alejandrocarrillo.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.alejandrocarrillo.dao.Conexion;
import org.alejandrocarrillo.dto.DistribuidorDTO;
import org.alejandrocarrillo.model.Distribuidor;
import org.alejandrocarrillo.system.Main;
import org.alejandrocarrillo.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author Alejandro
 */
public class MenuDistribuidoresController implements Initializable {
    private Main stage;
    public static int opcion;
    
    @FXML
    TextField textField;
    @FXML
    Button btnBack, btnBuscar, btnEliminar, btnAgregar, btnEditar, btnListar;
    @FXML
    TableView tblDistribuidores;
    @FXML
    TableColumn colDistribuidorId, colNombreDistribuidor, colDireccionDistribuidor, colNitDistribuidor, colTelefonoDistribuidor,colWeb;
    
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
        cargarDatos();
    }
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnBack){
            stage.menuModulosView();
        } else if(event.getSource() == btnAgregar){
            stage.formDistribuidoresView();
            opcion = 1;
        } else if(event.getSource() == btnEditar){
            DistribuidorDTO.getDistribuidorDTO().setDistribuidor((Distribuidor) tblDistribuidores.getSelectionModel().getSelectedItem());
            if(DistribuidorDTO.getDistribuidorDTO().getDistribuidor() != null){
                stage.formDistribuidoresView();
            }
            opcion = 2;
        } else if(event.getSource() == btnBuscar){
            buscarDatos(Integer.parseInt(textField.getText()));
        }  else if(event.getSource() == btnListar){
            cargarDatos();
        } else if(event.getSource() == btnEliminar){
            if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(404).get() == ButtonType.OK){
                DistribuidorDTO.getDistribuidorDTO().setDistribuidor((Distribuidor) tblDistribuidores.getSelectionModel().getSelectedItem());
                borrarDatos();
                DistribuidorDTO.getDistribuidorDTO().setDistribuidor(null);
                cargarDatos();
            }
        } 
    }
    
    public void borrarDatos(){
        eliminarDistribuidor(DistribuidorDTO.getDistribuidorDTO().getDistribuidor().getDistribuidorId());
    }
    
    public void buscarDatos(int disId){
        tblDistribuidores.setItems(buscarDistribuidores(disId));
        colDistribuidorId.setCellValueFactory(new PropertyValueFactory<Distribuidor, Integer>("distribuidorId"));
        colNombreDistribuidor.setCellValueFactory(new PropertyValueFactory<Distribuidor, String>("nombreDistribuidor"));
        colDireccionDistribuidor.setCellValueFactory(new PropertyValueFactory<Distribuidor, String>("direccionDistribuidor"));
        colNitDistribuidor.setCellValueFactory(new PropertyValueFactory<Distribuidor, String>("nitDistribuidor"));
        colTelefonoDistribuidor.setCellValueFactory(new PropertyValueFactory<Distribuidor, String>("telefonoDistribuidor"));
        colWeb.setCellValueFactory(new PropertyValueFactory<Distribuidor, String>("web"));
    }
    
    public void cargarDatos(){
        tblDistribuidores.setItems(listarDistribuidores());
        colDistribuidorId.setCellValueFactory(new PropertyValueFactory<Distribuidor, Integer>("distribuidorId"));
        colNombreDistribuidor.setCellValueFactory(new PropertyValueFactory<Distribuidor, String>("nombreDistribuidor"));
        colDireccionDistribuidor.setCellValueFactory(new PropertyValueFactory<Distribuidor, String>("direccionDistribuidor"));
        colNitDistribuidor.setCellValueFactory(new PropertyValueFactory<Distribuidor, String>("nitDistribuidor"));
        colTelefonoDistribuidor.setCellValueFactory(new PropertyValueFactory<Distribuidor, String>("telefonoDistribuidor"));
        colWeb.setCellValueFactory(new PropertyValueFactory<Distribuidor, String>("web"));
    }
    
    public ObservableList<Distribuidor> listarDistribuidores(){
        
        ArrayList<Distribuidor> distribuidores = new ArrayList<>();

        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_listarDistribuidores();";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                int distribuidorId = resultSet.getInt("distribuidorId");
                String nombreDistribuidor = resultSet.getString("nombreDistribuidor");
                String direccionDistribuidor = resultSet.getString("direccionDistribuidor");
                String nitDistribuidor = resultSet.getString("nitDistribuidor");
                String telefonoDistribuidor = resultSet.getString("telefonoDistribuidor");
                String web = resultSet.getString("web");
                distribuidores.add(new Distribuidor(distribuidorId,nombreDistribuidor, direccionDistribuidor, nitDistribuidor, telefonoDistribuidor, web));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            try {
                if(resultSet != null){
                    resultSet.close();
                }

                if(statement != null){
                    statement.close();
                }

                if(conexion != null){
                    conexion.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return FXCollections.observableList(distribuidores);
    }
    
    public ObservableList<Distribuidor> buscarDistribuidores(int disId){
        
        ArrayList<Distribuidor> distribuidores = new ArrayList<>();

        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_buscarDistribuidores(?);";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1,disId);
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                int distribuidorId = resultSet.getInt("distribuidorId");
                String nombreDistribuidor = resultSet.getString("nombreDistribuidor");
                String direccionDistribuidor = resultSet.getString("direccionDistribuidor");
                String nitDistribuidor = resultSet.getString("nitDistribuidor");
                String telefonoDistribuidor = resultSet.getString("telefonoDistribuidor");
                String web = resultSet.getString("web");
                distribuidores.add(new Distribuidor(distribuidorId,nombreDistribuidor, direccionDistribuidor, nitDistribuidor, telefonoDistribuidor, web));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            try {
                if(resultSet != null){
                    resultSet.close();
                }

                if(statement != null){
                    statement.close();
                }

                if(conexion != null){
                    conexion.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return FXCollections.observableList(distribuidores);
    }
    
    public void eliminarDistribuidor(int disId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_eliminarDistribuidores(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, disId);
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
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
