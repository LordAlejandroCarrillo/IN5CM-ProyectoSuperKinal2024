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
import static org.alejandrocarrillo.controller.MenuCargosController.opcion;
import org.alejandrocarrillo.dao.Conexion;
import org.alejandrocarrillo.dto.CategoriaProductoDTO;
import org.alejandrocarrillo.model.CategoriaProducto;
import org.alejandrocarrillo.system.Main;
import org.alejandrocarrillo.utils.SuperKinalAlert;

public class MenuCategoriaProductosController implements Initializable {
    private Main stage;
    public static int opcion;
    
    @FXML
    TextField textField;
    @FXML
    Button btnBack, btnBuscar, btnEliminar, btnAgregar, btnEditar, btnListar;
    @FXML
    TableView tblCategoriaProductos;
    @FXML
    TableColumn colCategoriaProductosId, colNombreCategoria, colDescripcionCategoria;
    
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
            stage.formCategoriaProductosView();
            opcion = 1;
        } else if(event.getSource() == btnEditar){
            CategoriaProductoDTO.getCategoriaProductoDTO().setCategoriaProducto((CategoriaProducto) tblCategoriaProductos.getSelectionModel().getSelectedItem());
            if(CategoriaProductoDTO.getCategoriaProductoDTO().getCategoriaProducto() != null){
                stage.formCategoriaProductosView();
            }
            opcion = 2;
        } else if(event.getSource() == btnBuscar){
            buscarDatos(Integer.parseInt(textField.getText()));
        } else if(event.getSource() == btnListar){
            cargarDatos();
        } else if(event.getSource() == btnEliminar){
            if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(404).get() == ButtonType.OK){
                CategoriaProductoDTO.getCategoriaProductoDTO().setCategoriaProducto((CategoriaProducto) tblCategoriaProductos.getSelectionModel().getSelectedItem());
                borrarDatos();
                CategoriaProductoDTO.getCategoriaProductoDTO().setCategoriaProducto(null);
                cargarDatos();
            }
        }
    }
    
    public void cargarDatos(){
        tblCategoriaProductos.setItems(listarCategoriaProductos());
        colCategoriaProductosId.setCellValueFactory(new PropertyValueFactory<CategoriaProducto, Integer>("categoriaProductosId"));
        colNombreCategoria.setCellValueFactory(new PropertyValueFactory<CategoriaProducto, String>("nombreCategoria"));
        colDescripcionCategoria.setCellValueFactory(new PropertyValueFactory<CategoriaProducto, String>("descripcionCategoria"));
    }
    
    public void buscarDatos(int capId){
        tblCategoriaProductos.setItems(buscarCategoriaProductos(capId));
        colCategoriaProductosId.setCellValueFactory(new PropertyValueFactory<CategoriaProducto, Integer>("categoriaProductosId"));
        colNombreCategoria.setCellValueFactory(new PropertyValueFactory<CategoriaProducto, String>("nombreCategoria"));
        colDescripcionCategoria.setCellValueFactory(new PropertyValueFactory<CategoriaProducto, String>("descripcionCategoria"));
    }
    
    public void borrarDatos(){
        eliminarCategoriaProducto(CategoriaProductoDTO.getCategoriaProductoDTO().getCategoriaProducto().getCategoriaProductosId());
    }
    
    public ObservableList<CategoriaProducto> buscarCategoriaProductos(int capId){
        
        ArrayList<CategoriaProducto> categoria = new ArrayList<>();

        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_buscarCategoriaProductos(?);";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1,capId);
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                int categoriaProductosId = resultSet.getInt("categoriaProductosId");
                String nombreCategoria = resultSet.getString("nombreCategoria");
                String descripcionCategoria = resultSet.getString("descripcionCategoria");
                categoria.add(new CategoriaProducto(categoriaProductosId, nombreCategoria, descripcionCategoria));
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
        return FXCollections.observableList(categoria);
    }
    
    public ObservableList<CategoriaProducto> listarCategoriaProductos(){
        
        ArrayList<CategoriaProducto> categoria = new ArrayList<>();

        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_listarCategoriaProductos();";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                int categoriaProductosId = resultSet.getInt("categoriaProductosId");
                String nombreCategoria = resultSet.getString("nombreCategoria");
                String descripcionCategoria = resultSet.getString("descripcionCategoria");
                categoria.add(new CategoriaProducto(categoriaProductosId, nombreCategoria, descripcionCategoria));
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
        return FXCollections.observableList(categoria);
    }
    
    public void eliminarCategoriaProducto(int capId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_eliminarCategoriaProductos(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, capId);
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
}
