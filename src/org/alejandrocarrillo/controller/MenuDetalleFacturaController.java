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
import org.alejandrocarrillo.model.DetalleFactura;
import org.alejandrocarrillo.model.Factura;
import org.alejandrocarrillo.model.Producto;
import org.alejandrocarrillo.system.Main;
import org.alejandrocarrillo.utils.SuperKinalAlert;

public class MenuDetalleFacturaController implements Initializable {
    private Main stage;

    @FXML
    Button btnBack, btnVaciar, btnGuardar, btnBuscar, btnListar;
    @FXML
    TextField tfBuscar, tfDetalle;
    @FXML
    TableView tblDetalle;
    @FXML
    ComboBox cmbProducto, cmbFactura;
    @FXML
    TableColumn colDetalle, colFactura, colProducto;
    
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
        cmbFactura.setItems(listarFacturas());
        cmbProducto.setItems(listarProductos());
        cargarDatos();
    }
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        boolean token;
        boolean con = true;
        if(event.getSource() == btnBack){
            stage.menuPrincipalView();
        } else if(event.getSource() == btnBuscar){
            buscarDatos();
        } else if(event.getSource() == btnListar){
            cargarDatos();
        } else if(event.getSource() == btnGuardar){
            if(cmbFactura.getSelectionModel().getSelectedItem() != null && cmbProducto.getSelectionModel().getSelectedItem() != null){
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
        cmbFactura.getSelectionModel().clearSelection();
        cmbProducto.getSelectionModel().clearSelection();
    }
    
    public void cargarEditar(){
        DetalleFactura dt = (DetalleFactura)tblDetalle.getSelectionModel().getSelectedItem();
        if(dt !=  null){
            tfDetalle.setText(Integer.toString(dt.getDetalleFacturaId()));
            cmbFactura.getSelectionModel().select(obtenerIndexFactura());
            cmbProducto.getSelectionModel().select(obtenerIndexProducto());
        }
    }
    
    public int obtenerIndexFactura(){
        int index = 0;
        for(int i = 0; i <= cmbFactura.getItems().size(); i++){
            String facturaCmb = cmbFactura.getItems().get(i).toString();
            String facturaTbl = (((DetalleFactura)tblDetalle.getSelectionModel().getSelectedItem()).getFactura());
            if(facturaCmb.equals(facturaTbl)){
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
            String productoTbl = (((DetalleFactura)tblDetalle.getSelectionModel().getSelectedItem()).getProducto());
            if(productoCmb.equals(productoTbl)){
                index = i;
                break;
            }
        }
        return index;
    }
    
    public void cargarDatos(){
        tblDetalle.setItems(listarDetalle());
        colDetalle.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("detalleFacturaId"));
        colFactura.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("factura"));
        colProducto.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("producto"));
        tblDetalle.getSortOrder().add(colDetalle);
    }
    
    public void buscarDatos(){
        tblDetalle.setItems(buscarDetalle());
        colDetalle.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("detalleFacturaId"));
        colFactura.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("factura"));
        colProducto.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("producto"));
        tblDetalle.getSortOrder().add(colDetalle);
    }
    
    public void editarDetalle(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarDetalleFactura(?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfDetalle.getText()));
            statement.setInt(2, ((Factura)cmbFactura.getSelectionModel().getSelectedItem()).getFacturaId());
            statement.setInt(3, ((Producto)cmbProducto.getSelectionModel().getSelectedItem()).getProductoId());
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
            String sql = "call sp_agregarDetalleFactura(?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, ((Factura)cmbFactura.getSelectionModel().getSelectedItem()).getFacturaId());
            statement.setInt(2, ((Producto)cmbProducto.getSelectionModel().getSelectedItem()).getProductoId());
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
    
    public ObservableList<DetalleFactura> buscarDetalle(){
        
        ArrayList<DetalleFactura> detalles = new ArrayList<>();

        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_buscarDetalleFactura(?);";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfBuscar.getText()));
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                int detalleFacturaId = resultSet.getInt("detalleFacturaId");
                String factura = resultSet.getString("factura");
                String producto = resultSet.getString("producto");
                detalles.add(new DetalleFactura(detalleFacturaId, factura, producto));
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
    
    public ObservableList<DetalleFactura> listarDetalle(){
        
        ArrayList<DetalleFactura> detalles = new ArrayList<>();

        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_listarDetalleFactura();";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                int detalleFacturaId = resultSet.getInt("detalleFacturaId");
                String factura = resultSet.getString("factura");
                String producto = resultSet.getString("producto");
                detalles.add(new DetalleFactura(detalleFacturaId, factura, producto));
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
                InputStream imagenProducto = resultSet.getBinaryStream("imagenProducto");
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
    
    public ObservableList<Factura> listarFacturas(){
        
        ArrayList<Factura> facturas = new ArrayList<>();

        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_listarFacturas();";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                int facturaId = resultSet.getInt("facturaId");
                Date fecha = resultSet.getDate("fecha");
                Time hora = resultSet.getTime("hora");
                String cliente = resultSet.getString("cliente");
                String empleado = resultSet.getString("empleado");
                double total = resultSet.getDouble("total");
                facturas.add(new Factura(facturaId, fecha, hora, cliente, empleado, total));
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
        
        return FXCollections.observableList(facturas);
    }
}
