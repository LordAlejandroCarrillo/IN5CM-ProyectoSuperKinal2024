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
import java.text.SimpleDateFormat;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.alejandrocarrillo.dao.Conexion;
import org.alejandrocarrillo.model.Producto;
import org.alejandrocarrillo.model.Promocion;
import org.alejandrocarrillo.system.Main;
import org.alejandrocarrillo.utils.SuperKinalAlert;

public class MenuPromocionesController implements Initializable {
    private Main stage;
    
    @FXML
    Button btnBack, btnGuardar, btnVaciar, btnListar, btnBuscar;
    @FXML
    TextField tfBuscar, tfPrecio, tfFin, tfPromocion;
    @FXML
    TableView tblPromociones;
    @FXML
    ComboBox cmbProducto;
    @FXML
    TableColumn colPromocion, colPrecio, colDescripcion, colInicio, colFin, colProducto;
    @FXML
    TextArea txtDescripcion;
    
    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
    
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
            if(cmbProducto.getSelectionModel().getSelectedItem() != null && !tfFin.getText().equals("") && !tfPrecio.getText().equals("") && !txtDescripcion.getText().equals("")){
               token = true; 
            } else{
                token = false;
            }
            if(tfPromocion.getText().equals("")){
                con = true;
            } else{
                con = false;
            }
            if(con){
                if(token){
                    SuperKinalAlert.getInstance().mostraAlertaInformacion(400);
                    agregarPromocion();
                    cargarDatos();
                    vaciarCampos();
                } else{
                    SuperKinalAlert.getInstance().mostraAlertaInformacion(504);
                }
            } else{
                if(token){
                    if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(505).get() == ButtonType.OK){
                        if(token){
                            editarPromocion();
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
        tfPromocion.clear();
        tfPrecio.clear();
        txtDescripcion.clear();
        tfFin.clear();
        cmbProducto.getSelectionModel().clearSelection();
    }
    
    public void cargarEditar(){
        Promocion pr = (Promocion)tblPromociones.getSelectionModel().getSelectedItem();
        if(pr !=  null){
            tfPromocion.setText(Integer.toString(pr.getPromocionId()));
            tfPrecio.setText(Double.toString(pr.getPrecioPromocion()));
            txtDescripcion.setText(pr.getDescripcionPromocion());
            String fecha = formato.format(pr.getFechaFinalizacion());
            tfFin.setText(fecha);
            cmbProducto.getSelectionModel().select(obtenerIndexProducto());
        }
    }
    
    public int obtenerIndexProducto(){
        int index = 0;
        for(int i = 0; i <= cmbProducto.getItems().size(); i++){
            String productoCmb = cmbProducto.getItems().get(i).toString();
            String productoTbl = (((Promocion)tblPromociones.getSelectionModel().getSelectedItem()).getProducto());
            if(productoCmb.equals(productoTbl)){
                index = i;
                break;
            }
        }
        return index;
    }
    
    public void cargarDatos(){
        tblPromociones.setItems(listarPromociones());
        colPromocion.setCellValueFactory(new PropertyValueFactory<Promocion, Integer>("promocionId"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<Promocion, Double>("precioPromocion"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Promocion, String>("descripcionPromocion"));
        colInicio.setCellValueFactory(new PropertyValueFactory<Promocion, Date>("fechaInicio"));
        colFin.setCellValueFactory(new PropertyValueFactory<Promocion, Date>("fechaFinalizacion"));
        colProducto.setCellValueFactory(new PropertyValueFactory<Promocion, Integer>("producto"));
        tblPromociones.getSortOrder().add(colPromocion);
    }
    
    public void buscarDatos(){
        tblPromociones.setItems(buscarPromociones());
        colPromocion.setCellValueFactory(new PropertyValueFactory<Promocion, Integer>("promocionId"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<Promocion, Double>("precioPromocion"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Promocion, String>("descripcionPromocion"));
        colInicio.setCellValueFactory(new PropertyValueFactory<Promocion, Date>("fechaInicio"));
        colFin.setCellValueFactory(new PropertyValueFactory<Promocion, Date>("fechaFinalizacion"));
        colProducto.setCellValueFactory(new PropertyValueFactory<Promocion, Integer>("producto"));
        tblPromociones.getSortOrder().add(colPromocion);
    }
    
    public void agregarPromocion(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_agregarPromociones(?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setDouble(1, Double.parseDouble(tfPrecio.getText()));
            statement.setString(2, txtDescripcion.getText());
            statement.setString(3, tfFin.getText());
            statement.setInt(4, ((Producto)cmbProducto.getSelectionModel().getSelectedItem()).getProductoId());
                                 
            
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
    
    public void editarPromocion(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarPromociones(?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfPromocion.getText()));
            statement.setDouble(2, Double.parseDouble(tfPrecio.getText()));
            statement.setString(3, txtDescripcion.getText());
            statement.setString(4, tfFin.getText());
            statement.setInt(5, ((Producto)cmbProducto.getSelectionModel().getSelectedItem()).getProductoId());
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
    
    public ObservableList<Promocion> buscarPromociones(){
        ArrayList<Promocion> promociones = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_buscarPromociones(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfBuscar.getText()));
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int promocionId = resultSet.getInt("promocionId");
                double precioPromocion = resultSet.getDouble("precioPromocion");
                String descripcionPromocion = resultSet.getString("descripcionPromocion");
                Date fechaInicio = resultSet.getDate("fechaInicio");
                Date fechaFinalizacion = resultSet.getDate("fechaFinalizacion");
                String producto = resultSet.getString("producto");
                
                promociones.add(new Promocion(promocionId, precioPromocion, descripcionPromocion, fechaInicio, fechaFinalizacion, producto));
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
        
        return FXCollections.observableList(promociones);
    }
    
    public ObservableList<Promocion> listarPromociones(){
        ArrayList<Promocion> promociones = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_listarPromociones()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int promocionId = resultSet.getInt("promocionId");
                double precioPromocion = resultSet.getDouble("precioPromocion");
                String descripcionPromocion = resultSet.getString("descripcionPromocion");
                Date fechaInicio = resultSet.getDate("fechaInicio");
                Date fechaFinalizacion = resultSet.getDate("fechaFinalizacion");
                String producto = resultSet.getString("producto");
                
                promociones.add(new Promocion(promocionId, precioPromocion, descripcionPromocion, fechaInicio, fechaFinalizacion, producto));
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
        
        return FXCollections.observableList(promociones);
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
}
