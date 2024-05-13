package org.alejandrocarrillo.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
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
import org.alejandrocarrillo.dto.CompraDTO;
import org.alejandrocarrillo.model.Compra;
import org.alejandrocarrillo.system.Main;
import org.alejandrocarrillo.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author Alejandro
 */
public class MenuComprasController implements Initializable {
    private Main stage;
    public static int opcion;
    
    @FXML
    TextField textField;
    @FXML
    Button btnBack, btnBuscar, btnEliminar, btnAgregar, btnEditar, btnListar;
    @FXML
    TableView tblCompras;
    @FXML
    TableColumn colCompraId, colFechaCompra, colTotalCompra;
    
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
            stage.menuPrincipalView();
        } else if(event.getSource() == btnAgregar){
            stage.formComprasView();
            opcion = 1;
        } else if(event.getSource() == btnEditar){
            CompraDTO.getCompraDTO().setCompra((Compra) tblCompras.getSelectionModel().getSelectedItem());
            if(CompraDTO.getCompraDTO().getCompra() != null){
                stage.formComprasView();
            }
            opcion = 2;
        } else if(event.getSource() == btnBuscar){
            buscarDatos(Integer.parseInt(textField.getText()));
        } else if(event.getSource() == btnListar){
            cargarDatos();
        } else if(event.getSource() == btnEliminar){
            if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(404).get() == ButtonType.OK){
                CompraDTO.getCompraDTO().setCompra((Compra) tblCompras.getSelectionModel().getSelectedItem());
                borrarDatos();
                CompraDTO.getCompraDTO().setCompra(null);
                cargarDatos();
            }
        }
    }
    
    public void borrarDatos(){
        eliminarCompra(CompraDTO.getCompraDTO().getCompra().getCompraId());
    }
    
    public void cargarDatos(){
        tblCompras.setItems(listarCompras());
        colCompraId.setCellValueFactory(new PropertyValueFactory<Compra, Integer>("compraId"));
        colFechaCompra.setCellValueFactory(new PropertyValueFactory<Compra, Date>("fechaCompra"));
        colTotalCompra.setCellValueFactory(new PropertyValueFactory<Compra, Double>("totalCompra"));
    }
    
    public void buscarDatos(int comId){
        tblCompras.setItems(buscarCompras(comId));
        colCompraId.setCellValueFactory(new PropertyValueFactory<Compra, Integer>("compraId"));
        colFechaCompra.setCellValueFactory(new PropertyValueFactory<Compra, Date>("fechaCompra"));
        colTotalCompra.setCellValueFactory(new PropertyValueFactory<Compra, Double>("totalCompra"));
    }
    
    public ObservableList<Compra> listarCompras(){
        
        ArrayList<Compra> compras = new ArrayList<>();

        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_listarCompras()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                int compraId = resultSet.getInt("compraId");
                Date fechaCompra = resultSet.getDate("fechaCompra");
                double totalCompra = resultSet.getDouble("totalCompra"); 
                compras.add(new Compra(compraId, fechaCompra, totalCompra));
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
        return FXCollections.observableList(compras);
    }
    
    public ObservableList<Compra> buscarCompras(int comId){
        
        ArrayList<Compra> compras = new ArrayList<>();

        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_buscarCompras(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1,comId);
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                int compraId = resultSet.getInt("compraId");
                Date fechaCompra = resultSet.getDate("fechaCompra");
                double totalCompra = resultSet.getDouble("totalCompra"); 
                compras.add(new Compra(compraId, fechaCompra, totalCompra));
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
        return FXCollections.observableList(compras);
    }
    
    public void eliminarCompra(int comId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_eliminarCompras(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, comId);
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
