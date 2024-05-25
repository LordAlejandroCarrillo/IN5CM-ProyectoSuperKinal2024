package org.alejandrocarrillo.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.alejandrocarrillo.dao.Conexion;
import org.alejandrocarrillo.model.Cargo;
import org.alejandrocarrillo.model.Empleado;
import org.alejandrocarrillo.system.Main;
import org.alejandrocarrillo.utils.SuperKinalAlert;

public class MenuEmpleadosController implements Initializable {
    private Main stage;
    private Empleado em;
    @FXML
    Button btnBack, btnVaciar, btnGuardar, btnBuscar, btnListar, btnAsignar, btnEliminar;
    @FXML
    TextField tfBuscar, tfSueldo, tfSalida, tfEntrada, tfApellido, tfNombre, tfID;
    @FXML
    TableView tblEmpleados;
    @FXML
    ComboBox cmbCargo, cmbEncargado;
    @FXML
    TableColumn colEmpleado, colNombre, colApellido, colSueldo, colEntrada, colSalida, colCargo, colEncargado;
    
    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
    
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
        cmbEncargado.setItems(listarEmpleado());
        cmbCargo.setItems(listarCargo());
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
                eliminarEmpleados(em.getEmpleadoId());
                cargarDatos();
                vaciarCampos();
            }
        } else if(event.getSource() == btnListar){
            cargarDatos();
        } else if(event.getSource() == btnGuardar){
            if(cmbCargo.getSelectionModel().getSelectedItem() != null && !tfSueldo.getText().equals("") && !tfSalida.getText().equals("") && !tfEntrada.getText().equals("") && !tfApellido.getText().equals("") && !tfNombre.getText().equals("")){
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
                    agregarEmpleado();
                    cargarDatos();
                    vaciarCampos();
                    cmbEncargado.setItems(listarEmpleado());
                } else{
                    SuperKinalAlert.getInstance().mostraAlertaInformacion(504);
                }
            } else{
                if(token){
                    if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(505).get() == ButtonType.OK){
                        if(token){
                            editarEmpleado();
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
        } else if(event.getSource() == btnAsignar){
            asignarEncargado();
            cargarDatos();
        }
    }
    
    public void vaciarCampos(){
        tfID.clear();
        tfNombre.clear();
        tfApellido.clear();
        tfEntrada.clear();
        tfSalida.clear();
        tfSueldo.clear();        
        cmbEncargado.getSelectionModel().clearSelection();
        cmbCargo.getSelectionModel().clearSelection();
    }
    
    public void cargarEditar(){
        em = (Empleado)tblEmpleados.getSelectionModel().getSelectedItem();
        if(em !=  null){
            tfID.setText(Integer.toString(em.getEmpleadoId()));
            tfNombre.setText(em.getNombreEmpleado());
            tfApellido.setText(em.getApellidoEmpleado());
            String entrada = formatoHora.format(em.getHoraEntrada());
            tfEntrada.setText(entrada);
            String salida = formatoHora.format(em.getHoraSalida());
            tfSalida.setText(salida);
            tfSueldo.setText(Double.toString(em.getSueldo()));
            cmbCargo.getSelectionModel().select(obtenerIndexCargo());
        }
    }
    
    public int obtenerIndexCargo(){
        int index = 0;
        for(int i = 0; i <= cmbCargo.getItems().size(); i++){
            String cargoCmb = cmbCargo.getItems().get(i).toString();
            String cargoTbl = (((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCargo());
            if(cargoCmb.equals(cargoTbl)){
                index = i;
                break;
            }
        }
        return index;
    }
    
    public void cargarDatos(){
        tblEmpleados.setItems(listarEmpleado());
        colEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("empleadoId"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombreEmpleado"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidoEmpleado"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<Empleado, Double>("sueldo"));
        colEntrada.setCellValueFactory(new PropertyValueFactory<Empleado, Time>("horaEntrada"));
        colSalida.setCellValueFactory(new PropertyValueFactory<Empleado, Time>("horaSalida"));
        colCargo.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("cargo"));
        colEncargado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("encargadoId"));
        tblEmpleados.getSortOrder().add(colEmpleado);
    }
    
    public void buscarDatos(){
        tblEmpleados.setItems(buscarEmpleado());
        colEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("empleadoId"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombreEmpleado"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidoEmpleado"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<Empleado, Double>("sueldo"));
        colEntrada.setCellValueFactory(new PropertyValueFactory<Empleado, Time>("horaEntrada"));
        colSalida.setCellValueFactory(new PropertyValueFactory<Empleado, Time>("horaSalida"));
        colCargo.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("cargo"));
        colEncargado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("encargadoId"));
        tblEmpleados.getSortOrder().add(colEmpleado);
    }
    
    public void agregarEmpleado(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarEmpleados(?,?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tfNombre.getText());
            statement.setString(2, tfApellido.getText());
            statement.setDouble(3, Double.parseDouble(tfSueldo.getText()));
            statement.setString(4, tfEntrada.getText());
            statement.setString(5, tfSalida.getText());
            statement.setInt(6, ((Cargo)cmbCargo.getSelectionModel().getSelectedItem()).getCargoId());
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
    
    public void asignarEncargado(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_asignarEncargado(?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, ((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getEmpleadoId());
            statement.setInt(2, ((Empleado)cmbEncargado.getSelectionModel().getSelectedItem()).getEmpleadoId());
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
    
    public void editarEmpleado(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarEmpleados(?,?,?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfID.getText()));
            statement.setString(2, tfNombre.getText());
            statement.setString(3, tfApellido.getText());
            statement.setDouble(4, Double.parseDouble(tfSueldo.getText()));
            statement.setString(5, tfEntrada.getText());
            statement.setString(6, tfSalida.getText());
            statement.setInt(7, ((Cargo)cmbCargo.getSelectionModel().getSelectedItem()).getCargoId());
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
    
    public ObservableList<Empleado> buscarEmpleado(){
        ArrayList<Empleado> empleados = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_buscarEmpleados(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfID.getText()));
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int empleadoId = resultSet.getInt("empleadoId");
                String nombreEmpleado = resultSet.getString("nombreEmpleado");
                String apellidoEmpleado = resultSet.getString("apellidoEmpleado");
                double sueldo = resultSet.getDouble("sueldo");
                Time horaEntrada = resultSet.getTime("horaEntrada");
                Time horaSalida = resultSet.getTime("horaSalida");
                String cargo = resultSet.getString("cargo");
                int encargadoId = resultSet.getInt("encargadoId");
                
                empleados.add(new Empleado(empleadoId, nombreEmpleado, apellidoEmpleado, sueldo, horaEntrada, horaSalida, cargo, encargadoId));
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
        
        return FXCollections.observableList(empleados);
    }
    
    public void eliminarEmpleados(int id){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_eliminarEmpleados(?)";
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
    
    public ObservableList<Empleado> listarEmpleado(){
        ArrayList<Empleado> empleados = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_listarEmpleados()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int empleadoId = resultSet.getInt("empleadoId");
                String nombreEmpleado = resultSet.getString("nombreEmpleado");
                String apellidoEmpleado = resultSet.getString("apellidoEmpleado");
                double sueldo = resultSet.getDouble("sueldo");
                Time horaEntrada = resultSet.getTime("horaEntrada");
                Time horaSalida = resultSet.getTime("horaSalida");
                String cargo = resultSet.getString("cargo");
                int encargadoId = resultSet.getInt("encargadoId");
                
                empleados.add(new Empleado(empleadoId, nombreEmpleado, apellidoEmpleado, sueldo, horaEntrada, horaSalida, cargo, encargadoId));
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
        
        return FXCollections.observableList(empleados);
    }
    
    public ObservableList<Cargo> listarCargo(){
        
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
}
