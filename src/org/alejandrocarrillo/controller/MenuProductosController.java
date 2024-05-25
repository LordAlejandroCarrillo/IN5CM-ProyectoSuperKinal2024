package org.alejandrocarrillo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import org.alejandrocarrillo.dao.Conexion;
import org.alejandrocarrillo.model.CategoriaProducto;
import org.alejandrocarrillo.model.Distribuidor;
import org.alejandrocarrillo.model.Producto;
import org.alejandrocarrillo.system.Main;
import org.alejandrocarrillo.utils.SuperKinalAlert;

public class MenuProductosController implements Initializable {
    private Main stage;
    private Producto pr;
    Image cargarImg = new Image("/org/alejandrocarrillo/image/SubirImagen.png");
    
    ArrayList<Image> imgProductos = new ArrayList<>();

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;
    private List<File> files = null;
    @FXML
    Button btnListar, btnBuscar, btnCargar, btnGuardar, btnVaciar, btnBack, btnEliminar;
    @FXML
    TextField tfNombre, tfBuscar, tfMayor, tfUnitario, tfCompra, tfCantidad, tfID;
    @FXML
    Label lblNombreProducto;
    @FXML
    ImageView imgCargar;
    @FXML
    ComboBox cmbDistribuidor, cmbCategoria;
    @FXML
    TableView tblProductos;
    @FXML
    TextArea txtDescripcion;
    @FXML
    TableColumn colId, colNombre, colDescripcion, colStock, colUni, colMay, colCompra, colDistribuidor, colCategoria;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbCategoria.setItems(listarCategoriaProducto());
        cmbDistribuidor.setItems(listarDistribuidor());
        cargarDatos();
    }    
    
    public void vaciarCampos(){
        txtDescripcion.clear();
        tfMayor.clear();
        tfUnitario.clear();
        tfCompra.clear();
        tfCantidad.clear();
        tfID.clear();
        imgCargar.setImage(cargarImg);
        cmbDistribuidor.getSelectionModel().clearSelection();
        cmbCategoria.getSelectionModel().clearSelection();
    }
    
    @FXML
    public void cargarEditar(){
        pr = (Producto)tblProductos.getSelectionModel().getSelectedItem();
        if(pr !=  null){
            lblNombreProducto.setText(pr.getNombreProducto());
            txtDescripcion.setText(pr.getDescripcionProducto());
            tfMayor.setText(Double.toString(pr.getPrecioVentaMayor()));
            tfUnitario.setText(Double.toString(pr.getPrecioVentaUnitario()));
            tfCompra.setText(Double.toString(pr.getPrecioCompra()));
            tfCantidad.setText(Integer.toString(pr.getCantidadStock()));
            tfID.setText(Integer.toString(pr.getProductoId()));
            cmbDistribuidor.getSelectionModel().select(obtenerIndexDistribuidor());
            cmbCategoria.getSelectionModel().select(obtenerIndexCategoriaProducto());
        }
    }
    
    @FXML
    public void handleOnDrag(DragEvent event){
        if(event.getDragboard().hasFiles()){
            event.acceptTransferModes(TransferMode.ANY);
        }
    }
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        try{
            boolean token;
            boolean con = true;
            if(event.getSource() == btnBack){
                stage.menuModulosView();
            } else if(event.getSource() == btnBuscar){
                imgCargar.setImage(cargarImg);
                Producto producto = buscarProductos();
                buscarDatos();
                if(producto != null){
                    lblNombreProducto.setText(producto.getNombreProducto());
                    InputStream file = producto.getImagenProducto().getBinaryStream();
                    Image image = new Image(file);
                    if(file != null){
                        imgCargar.setImage(image);
                    }
                }
            } else if(event.getSource() == btnEliminar){
                if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(404).get() == ButtonType.OK){
                    eliminarProductos(pr.getProductoId());
                    cargarDatos();
                    vaciarCampos();
                }
            } else if(event.getSource() == btnListar){
                cargarDatos();
            } else if(event.getSource() == btnGuardar){
                if((imgCargar.getImage()!= null || imgCargar.getImage() != new Image("/org/alejandrocarrillo/image/SubirImagen.png")) && cmbDistribuidor.getSelectionModel().getSelectedItem() != null&&cmbCategoria.getSelectionModel().getSelectedItem() != null && !tfCantidad.getText().equals("") && !tfCompra.getText().equals("") && !tfUnitario.getText().equals("") && !tfMayor.getText().equals("") && !txtDescripcion.getText().equals("") && !tfNombre.getText().equals("")){
                   token = true; 
                } else{
                    token = false;
                }
                if(tfID.getText().equals("")){
                    con = true;
                } else{
                    con = false;
                }
                if(con){
                    if(token){
                        SuperKinalAlert.getInstance().mostraAlertaInformacion(400);
                        agregarProductos();
                        cargarDatos();
                        vaciarCampos();
                    } else{
                        SuperKinalAlert.getInstance().mostraAlertaInformacion(504);
                    }
                } else{
                 if(token){
                     if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(505).get() == ButtonType.OK){
                            if(token){
                                editarProductos();
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
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public int obtenerIndexCategoriaProducto(){
        int index = 0;
        for(int i = 0; i <= cmbCategoria.getItems().size(); i++){
            String categoriaProductoCmb = cmbCategoria.getItems().get(i).toString();
            String categoriaProductoTbl = (((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCategoriaProductos());
            if(categoriaProductoCmb.equals(categoriaProductoTbl)){
                index = i;
                break;
            }
        }
        return index;
    }
    
    public int obtenerIndexDistribuidor(){
        int index = 0;
        for(int i = 0; i <= cmbDistribuidor.getItems().size(); i++){
            String distribuidorCmb = cmbDistribuidor.getItems().get(i).toString();
            String distribuidorTbl = (((Producto)tblProductos.getSelectionModel().getSelectedItem()).getDistribuidor());
            if(distribuidorCmb.equals(distribuidorTbl)){
                index = i;
                break;
            }
        }
        return index;
    }
    
    public void cargarDatos(){
        tblProductos.setItems(listarProductos());
        colId.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("productoId"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Producto, String >("nombreProducto"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Producto, String>("descripcionProducto"));
        colStock.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("cantidadStock"));
        colUni.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioVentaUnitario"));
        colMay.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioVentaMayor"));
        colCompra.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioCompra"));
        colDistribuidor.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("distribuidor"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("categoriaProductos"));
        tblProductos.getSortOrder().add(colId);
    }
    
    public void buscarDatos(){
        tblProductos.setItems(buscarProductosTbl());
        colId.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("productoId"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Producto, String >("nombreProducto"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Producto, String>("descripcionProducto"));
        colStock.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("cantidadStock"));
        colUni.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioVentaUnitario"));
        colMay.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioVentaMayor"));
        colCompra.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioCompra"));
        colDistribuidor.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("distribuidor"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("categoriaProductos"));
        tblProductos.getSortOrder().add(colId);
        
    }
    
    @FXML
    public void handleOnDrop(DragEvent event) throws Exception{
        files = event.getDragboard().getFiles();
        FileInputStream file = new FileInputStream(files.get(0));
        Image image = new Image(file);
        imgCargar.setImage(image);
    }
    
    
    
    public void agregarProductos(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarProductos(?,?,?,?,?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tfNombre.getText());
            statement.setString(2, txtDescripcion.getText());
            statement.setInt(3, Integer.parseInt(tfCantidad.getText()));
            statement.setDouble(4, Double.parseDouble(tfUnitario.getText()));
            statement.setDouble(5,Double.parseDouble(tfMayor.getText()));
            statement.setDouble(6,Double.parseDouble(tfCompra.getText()));
            InputStream img = new FileInputStream(files.get(0));
            statement.setBinaryStream(7,img);
            statement.setInt(8,((Distribuidor)cmbDistribuidor.getSelectionModel().getSelectedItem()).getDistribuidorId());
            statement.setInt(9,((CategoriaProducto)cmbCategoria.getSelectionModel().getSelectedItem()).getCategoriaProductosId());
            statement.execute();
        } catch(Exception e){
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
    
    public void editarProductos(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarProductos(?,?,?,?,?,?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfID.getText()));
            statement.setString(2, tfNombre.getText());
            statement.setString(3, txtDescripcion.getText());
            statement.setInt(4, Integer.parseInt(tfCantidad.getText()));
            statement.setDouble(5, Double.parseDouble(tfUnitario.getText()));
            statement.setDouble(6,Double.parseDouble(tfMayor.getText()));
            statement.setDouble(7,Double.parseDouble(tfCompra.getText()));
            InputStream img = new FileInputStream(files.get(0));
            if(imgCargar.getImage() == cargarImg){
                img = null;
            }
            statement.setBinaryStream(8,img);
            statement.setInt(9,((Distribuidor)cmbDistribuidor.getSelectionModel().getSelectedItem()).getDistribuidorId());
            statement.setInt(10,((CategoriaProducto)cmbCategoria.getSelectionModel().getSelectedItem()).getCategoriaProductosId());
            statement.execute();
        } catch(Exception e){
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
    
    public Producto buscarProductos(){
        Producto producto = null;
        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_buscarProductos(?);";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfBuscar.getText()));
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
                producto = new Producto(productoId, nombreProducto, descripcionProducto, cantidadStock, precioVentaUnitario, precioVentaMayor, precioCompra, imagenProducto, distribuidor, categoriaProductos);
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
        
        return producto;
    }
    
        public ObservableList<Producto> buscarProductosTbl(){
        ArrayList<Producto> productos = new ArrayList<>();
        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_buscarProductos(?);";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfBuscar.getText()));
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
    
    public ObservableList<Distribuidor> listarDistribuidor(){
        
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
                distribuidores.add(new Distribuidor(distribuidorId, nombreDistribuidor, direccionDistribuidor, nitDistribuidor, telefonoDistribuidor,web));
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
    
        public void eliminarProductos(int id){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_eliminarProductos(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, id);
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
    
    public ObservableList<CategoriaProducto> listarCategoriaProducto(){
        
        ArrayList<CategoriaProducto> categoriaProductos = new ArrayList<>();

        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_listarCategoriaProductos();";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                int categoriaProductosId = resultSet.getInt("categoriaProductosId");
                String nombreCategoria = resultSet.getString("nombreCategoria");
                String descripcionCategoria = resultSet.getString("descripcionCategoria");
                categoriaProductos.add(new CategoriaProducto(categoriaProductosId, nombreCategoria, descripcionCategoria));
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
        
        return FXCollections.observableList(categoriaProductos);
    }
}