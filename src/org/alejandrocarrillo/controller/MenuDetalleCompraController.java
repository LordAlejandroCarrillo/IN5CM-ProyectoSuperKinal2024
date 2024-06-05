/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alejandrocarrillo.controller;

import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.alejandrocarrillo.dao.Conexion;
import org.alejandrocarrillo.model.Compra;
import org.alejandrocarrillo.model.DetalleCompra;
import org.alejandrocarrillo.model.Producto;
import org.alejandrocarrillo.system.Main;
import org.alejandrocarrillo.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author Alejandro
 */
public class MenuDetalleCompraController implements Initializable {
    private Main stage;
    private DetalleCompra dt;
    
    @FXML
    Button btnBack, btnVaciar, btnGuardar, btnBuscar, btnListar, btnMas, btnMenos, btnEliminar;
    @FXML
    TextField tfBuscar, tfDetalle, tfCantidad;
    @FXML
    TableView tblDetalle;
    @FXML
    ComboBox cmbProducto, cmbCompra;
    @FXML
    TableColumn colDetalle, colCantidad, colProducto, colCompra, colTotal;
    
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
        cmbCompra.setItems(listarCompra());
        cmbProducto.setItems(listarProductos());
        cargarDatos();
    }
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        boolean token;
        boolean con = true;
        if(event.getSource() == btnBack){
            stage.menuModulosView();
        } else if(event.getSource() == btnBuscar){
            buscarDatos();
        } else if(event.getSource() == btnEliminar){
            if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(404).get() == ButtonType.OK){
                eliminarDetalleCompra(dt.getDetalleCompraId());
                cargarDatos();
                vaciarCampos();
            }
        } else if(event.getSource() == btnMas){
            if(!tfCantidad.getText().equals("")){
                int cantidad = (Integer.parseInt(tfCantidad.getText()));
                cantidad++;
                tfCantidad.setText(Integer.toString(cantidad));
            } else{
                tfCantidad.setText("1");
            }
        } else if(event.getSource() == btnMenos){
            if(!tfCantidad.getText().equals("")){
                int cantidad = (Integer.parseInt(tfCantidad.getText()));
                cantidad--;
                if(cantidad < 0){
                    tfCantidad.setText("1");
                } else if(cantidad >= 1){
                    tfCantidad.setText(Integer.toString(cantidad));
                }
            } else{
                tfCantidad.setText("1");
            }
        } else if(event.getSource() == btnListar){
            cargarDatos();
        } else if(event.getSource() == btnGuardar){
            if(cmbCompra.getSelectionModel().getSelectedItem() != null && cmbProducto.getSelectionModel().getSelectedItem() != null){
               token = true; 
            } else{
                token = false;
            }
            if(tfDetalle.getText().equals("")){
                con = true;
            } else{
                con = false;
            }
            if(con){
                if(token){
                    SuperKinalAlert.getInstance().mostraAlertaInformacion(400);
                    agregarDetalle();
                    cargarDatos();
                    vaciarCampos();
                } else{
                    SuperKinalAlert.getInstance().mostraAlertaInformacion(504);
                }
            } else{
                if(token){
                    if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(505).get() == ButtonType.OK){
                        if(token){
                            editarDetalle();
                            cargarDatos();
                            vaciarCampos();
                        } else{
                        SuperKinalAlert.getInstance().mostraAlertaInformacion(504);
                        }
                    }
                } else{
                    SuperKinalAlert.getInstance().mostraAlertaInformacion(504);
                }
            }
        } else if(event.getSource() == btnVaciar){
            vaciarCampos();
        }
    }
    
    public void vaciarCampos(){
        tfDetalle.clear();
        tfCantidad.clear();
        cmbCompra.getSelectionModel().clearSelection();
        cmbProducto.getSelectionModel().clearSelection();
    }
    
    public void cargarEditar(){
        dt = (DetalleCompra)tblDetalle.getSelectionModel().getSelectedItem();
        if(dt !=  null){
            tfDetalle.setText(Integer.toString(dt.getDetalleCompraId()));
            tfCantidad.setText(Integer.toString(dt.getCantidadCompra()));
            cmbCompra.getSelectionModel().select(obtenerIndexCompra());
            cmbProducto.getSelectionModel().select(obtenerIndexProducto());
        }
    }
    
    public int obtenerIndexCompra(){
        int index = 0;
        for(int i = 0; i <= cmbCompra.getItems().size(); i++){
            String compraCmb = cmbCompra.getItems().get(i).toString();
            String compraTbl = (((DetalleCompra)tblDetalle.getSelectionModel().getSelectedItem()).getCompra());
            if(compraCmb.equals(compraTbl)){
                index = i;
                break;
            }
        }
        return index;
    }
    
    public int obtenerIndexProducto(){
        int index = 0;
        for(int i = 0; i <= cmbProducto.getItems().size(); i++){
            String productoCmb = cmbProducto.getItems().get(i).toString();
            String productoTbl = (((DetalleCompra)tblDetalle.getSelectionModel().getSelectedItem()).getProducto());
            if(productoCmb.equals(productoTbl)){
                index = i;
                break;
            }
        }
        return index;
    }
    
    public void cargarDatos(){
        tblDetalle.setItems(listarDetalle());
        colDetalle.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("detalleCompraId"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("cantidadCompra"));
        colProducto.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("producto"));
        colCompra.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("compra"));
        colTotal.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Double>("totalCompra"));
        tblDetalle.getSortOrder().add(colDetalle);
    }
    
    public void buscarDatos(){
        tblDetalle.setItems(buscarDetalle());
        colDetalle.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("detalleCompraId"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("cantidadCompra"));
        colProducto.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("producto"));
        colCompra.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("compra"));
        colTotal.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Double>("totalCompra"));
        tblDetalle.getSortOrder().add(colDetalle);
    }
    
    public void editarDetalle(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarDetalleCompra(?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfDetalle.getText()));
            int cantidad = 0;
            if(tfCantidad.getText().equals("")){
                cantidad = 1;
            } else{
                cantidad = Integer.parseInt(tfCantidad.getText());
            }
            statement.setInt(2, cantidad);
            statement.setInt(3, ((Producto)cmbProducto.getSelectionModel().getSelectedItem()).getProductoId());
            statement.setInt(4, ((Compra)cmbCompra.getSelectionModel().getSelectedItem()).getCompraId());
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
    
    public void agregarDetalle(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarDetalleCompra(?,?,?)";
            statement = conexion.prepareStatement(sql);
            int cantidad = 0;
            if(tfCantidad.getText().equals("")){
                cantidad = 1;
            } else{
                cantidad = Integer.parseInt(tfCantidad.getText());
            }
            statement.setInt(1, cantidad);
            statement.setInt(2, ((Producto)cmbProducto.getSelectionModel().getSelectedItem()).getProductoId());
            statement.setInt(3, ((Compra)cmbCompra.getSelectionModel().getSelectedItem()).getCompraId());
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
    
    public ObservableList<DetalleCompra> buscarDetalle(){
        
        ArrayList<DetalleCompra> detalles = new ArrayList<>();

        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_buscarDetalleCompra(?);";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfBuscar.getText()));
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                int detalleCompraId = resultSet.getInt("detalleCompraId");
                int cantidadCompra = resultSet.getInt("cantidadCompra");
                String producto = resultSet.getString("producto");
                String compra = resultSet.getString("compra");
                double totalCompra = resultSet.getDouble("totalCompra");
                detalles.add(new DetalleCompra(detalleCompraId, cantidadCompra, producto, compra, totalCompra));
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
        
        return FXCollections.observableList(detalles);
    }
    
    public void eliminarDetalleCompra(int id){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_eliminarDetalleCompra(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, id);
            statement.execute();
        } catch(SQLException e){
            System.out.println(e.getMessage());
            String texto = e.getMessage();
            String sbs = texto.substring(0, 36);
            if(sbs.equals("Cannot delete or update a parent row")){
                SuperKinalAlert.getInstance().mostraAlertaInformacion(510);
            }
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
    
    public ObservableList<DetalleCompra> listarDetalle(){
        
        ArrayList<DetalleCompra> detalles = new ArrayList<>();

        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_listarDetalleCompra();";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                int detalleCompraId = resultSet.getInt("detalleCompraId");
                int cantidadCompra = resultSet.getInt("cantidadCompra");
                String producto = resultSet.getString("producto");
                String compra = resultSet.getString("compra");
                double totalCompra = resultSet.getDouble("totalCompra");
                detalles.add(new DetalleCompra(detalleCompraId, cantidadCompra, producto, compra, totalCompra));
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
        
        return FXCollections.observableList(detalles);
    }
    
    public ObservableList<Producto> listarProductos(){
        
        ArrayList<Producto> productos = new ArrayList<>();

        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_listarProductos();";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                int productoId = resultSet.getInt("productoId");
                String nombreProducto = resultSet.getString("nombreProducto");
                String descripcionProducto = resultSet.getString("descripcionProducto");
                int cantidadStock = resultSet.getInt("cantidadStock");
                double precioVentaUnitario = resultSet.getDouble("precioVentaUnitario");
                double precioVentaMayor = resultSet.getDouble("precioVentaMayor");
                double precioCompra = resultSet.getDouble("precioCompra");
                Blob imagenProducto = resultSet.getBlob("imagenProducto");
                String distribuidor = resultSet.getString("distribuidor");
                String categoriaProductos = resultSet.getString("categoriaProductos");
                productos.add(new Producto(productoId, nombreProducto, descripcionProducto, cantidadStock, precioVentaUnitario, precioVentaMayor, precioCompra, imagenProducto, distribuidor, categoriaProductos));
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
        
        return FXCollections.observableList(productos);
    }
    
    public ObservableList<Compra> listarCompra(){
        
        ArrayList<Compra> compras = new ArrayList<>();

        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_listarCompras();";
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
}
