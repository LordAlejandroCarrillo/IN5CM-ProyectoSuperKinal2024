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
import org.alejandrocarrillo.dto.CargoDTO;
import org.alejandrocarrillo.model.Cargo;
import org.alejandrocarrillo.system.Main;
import org.alejandrocarrillo.utils.SuperKinalAlert;

public class MenuCargosController implements Initializable {
    private Main stage;
    public static int opcion;
    
    @FXML
    TextField textField;
    @FXML
    Button btnBack, btnBuscar, btnEliminar, btnAgregar, btnEditar, btnListar;
    @FXML
    TableView tblCargos;
    
    @FXML
    TableColumn colCargoId, colNombre, colDescripcion;
    
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
            stage.formCargosView();
            opcion = 1;
        } else if(event.getSource() == btnEditar){
            CargoDTO.getCargoDTO().setCargo((Cargo) tblCargos.getSelectionModel().getSelectedItem());
            if(CargoDTO.getCargoDTO().getCargo() != null){
                stage.formCargosView();
            }
            opcion = 2;
        } else if(event.getSource() == btnBuscar){
            buscarDatos(Integer.parseInt(textField.getText()));
        } else if(event.getSource() == btnListar){
            cargarDatos();
        } else if(event.getSource() == btnEliminar){
            if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(404).get() == ButtonType.OK){
                CargoDTO.getCargoDTO().setCargo((Cargo) tblCargos.getSelectionModel().getSelectedItem());
                borrarDatos();
                CargoDTO.getCargoDTO().setCargo(null);
                cargarDatos();
            }
        }
    }
    
    public void borrarDatos(){
        eliminarCargo(CargoDTO.getCargoDTO().getCargo().getCargoId());
    }
    
    public void cargarDatos(){
        tblCargos.setItems(listarCargos());
        colCargoId.setCellValueFactory(new PropertyValueFactory<Cargo, Integer>("cargoId"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Cargo, String>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Cargo, String>("descripcionCargo"));
    }
    
    public void buscarDatos(int carId){
        tblCargos.setItems(buscarCargos(carId));
        colCargoId.setCellValueFactory(new PropertyValueFactory<Cargo, Integer>("cargoId"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Cargo, String>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Cargo, String>("descripcionCargo"));
    }
    
    public ObservableList<Cargo> listarCargos(){
        
        ArrayList<Cargo> cargos = new ArrayList<>();

        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_listarCargos();";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                int cargoId = resultSet.getInt("cargoId");
                String nombre = resultSet.getString("nombre");
                String descripcionCargo = resultSet.getString("descripcionCargo");
                cargos.add(new Cargo(cargoId, nombre, descripcionCargo));
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
        return FXCollections.observableList(cargos);
    }
    
    public ObservableList<Cargo> buscarCargos(int carId){
        
        ArrayList<Cargo> cargos = new ArrayList<>();

        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_buscarCargos(?);";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1,carId);
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                int cargoId = resultSet.getInt("cargoId");
                String nombre = resultSet.getString("nombre");
                String descripcionCargo = resultSet.getString("descripcionCargo");
                cargos.add(new Cargo(cargoId, nombre, descripcionCargo));
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
        return FXCollections.observableList(cargos);
    }
    
    public void eliminarCargo(int carId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_eliminarCargos(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, carId);
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
