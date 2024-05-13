package org.alejandrocarrillo.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.alejandrocarrillo.dao.Conexion;
import org.alejandrocarrillo.model.Cliente;
import org.alejandrocarrillo.model.Empleado;
import org.alejandrocarrillo.model.Factura;
import org.alejandrocarrillo.system.Main;
import org.alejandrocarrillo.utils.SuperKinalAlert;

public class MenuFacturasController implements Initializable {
    private Main stage;
    
    @FXML
    Button btnBack, btnVaciar, btnGuardar, btnBuscar, btnListar;
    @FXML
    TextField tfBuscar, tfFactura, tfTotal;
    @FXML
    TableView tblFacturas;
    @FXML
    ComboBox cmbEmpleado, cmbCliente;
    @FXML
    TableColumn colEmpleado, colCliente, colHora, colFecha, colFactura, colTotal;
    
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
        cmbCliente.setItems(listarClientes());
        cmbEmpleado.setItems(listarEmpleados());
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
            if(cmbEmpleado.getSelectionModel().getSelectedItem() != null && cmbCliente.getSelectionModel().getSelectedItem() != null&& !tfTotal.getText().equals("")){
               token = true; 
            } else{
                token = false;
            }
            if(tfFactura.getText().equals("")){
                con = true;
            } else{
                con = false;
            }
            if(con){
                if(token){
                    SuperKinalAlert.getInstance().mostraAlertaInformacion(400);
                    agregarFactura();
                    cargarDatos();
                    vaciarCampos();
                } else{
                    SuperKinalAlert.getInstance().mostraAlertaInformacion(504);
                }
            } else{
                if(token){
                    if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(505).get() == ButtonType.OK){
                        if(token){
                            editarFactura();
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
        tfFactura.clear();
        tfTotal.clear();
        cmbCliente.getSelectionModel().clearSelection();
        cmbEmpleado.getSelectionModel().clearSelection();
    }
    
    public void cargarEditar(){
        Factura fa = (Factura)tblFacturas.getSelectionModel().getSelectedItem();
        if(fa !=  null){
            tfFactura.setText(Integer.toString(fa.getFacturaId()));
            tfTotal.setText(Double.toString(fa.getTotal()));
            cmbCliente.getSelectionModel().select(obtenerIndexCliente());
            cmbEmpleado.getSelectionModel().select(obtenerIndexEmpleado());
        }
    }
    
    public int obtenerIndexCliente(){
        int index = 0;
        for(int i = 0; i <= cmbCliente.getItems().size(); i++){
            String clienteCmb = cmbCliente.getItems().get(i).toString();
            String clienteTbl = (((Factura)tblFacturas.getSelectionModel().getSelectedItem()).getCliente());
            if(clienteCmb.equals(clienteTbl)){
                index = i;
                break;
            }
        }
        return index;
    }
    
    public int obtenerIndexEmpleado(){
        int index = 0;
        for(int i = 0; i <= cmbEmpleado.getItems().size(); i++){
            String empleadoCmb = cmbEmpleado.getItems().get(i).toString();
            String empleadoTbl = (((Factura)tblFacturas.getSelectionModel().getSelectedItem()).getEmpleado());
            if(empleadoCmb.equals(empleadoTbl)){
                index = i;
                break;
            }
        }
        return index;
    }
    
    public void cargarDatos(){
        tblFacturas.setItems(listarFactura());
        colFactura.setCellValueFactory(new PropertyValueFactory<Factura, Integer>("facturaId"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Factura, Date>("fecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<Factura, Time>("hora"));
        colCliente.setCellValueFactory(new PropertyValueFactory<Factura, String>("cliente"));
        colEmpleado.setCellValueFactory(new PropertyValueFactory<Factura, String>("empleado"));
        colTotal.setCellValueFactory(new PropertyValueFactory<Factura, Double>("total"));
        tblFacturas.getSortOrder().add(colFactura);
    }
    
    public void buscarDatos(){
        tblFacturas.setItems(buscarFactura());
        colFactura.setCellValueFactory(new PropertyValueFactory<Factura, Integer>("facturaId"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Factura, Date>("fecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<Factura, Time>("hora"));
        colCliente.setCellValueFactory(new PropertyValueFactory<Factura, Integer>("cliente"));
        colEmpleado.setCellValueFactory(new PropertyValueFactory<Factura, Integer>("empleado"));
        colTotal.setCellValueFactory(new PropertyValueFactory<Factura, Double>("total"));
        tblFacturas.getSortOrder().add(colFactura);
    }
    
    public void agregarFactura(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarFacturas(?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, ((Cliente)cmbCliente.getSelectionModel().getSelectedItem()).getClienteId());
            statement.setInt(2, ((Empleado)cmbEmpleado.getSelectionModel().getSelectedItem()).getEmpleadoId());
            statement.setDouble(3, Double.parseDouble(tfTotal.getText()));
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
    
    public void editarFactura(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarFacturas(?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfFactura.getText()));
            statement.setInt(2, ((Cliente)cmbCliente.getSelectionModel().getSelectedItem()).getClienteId());
            statement.setInt(3, ((Empleado)cmbEmpleado.getSelectionModel().getSelectedItem()).getEmpleadoId());
            statement.setDouble(4, Double.parseDouble(tfTotal.getText()));
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
    
    public ObservableList<Factura> buscarFactura(){
        ArrayList<Factura> facturas = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_buscarFacturas(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfBuscar.getText()));
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
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
    
    public ObservableList<Factura> listarFactura(){
        ArrayList<Factura> facturas = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_listarFacturas()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
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
    
    public ObservableList<Empleado> listarEmpleados(){
        
        ArrayList<Empleado> empleados = new ArrayList<>();

        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_listarEmpleados2();";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                int empleadoId = resultSet.getInt("empleadoId");
                String nombreEmpleado = resultSet.getString("nombreEmpleado");
                String apellidoEmpleado = resultSet.getString("apellidoEmpleado");
                empleados.add(new Empleado(empleadoId, nombreEmpleado,apellidoEmpleado));
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
    
    public ObservableList<Cliente> listarClientes(){
        
        ArrayList<Cliente> clientes = new ArrayList<>();

        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_listarClientes();";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                int clienteId = resultSet.getInt("clienteId");
                String nit = resultSet.getString("nit");
                String nombre = resultSet.getString("nombre");
                String apellido = resultSet.getString("apellido");
                String telefono = resultSet.getString("telefono");
                String direccion = resultSet.getString("direccion");
                clientes.add(new Cliente(clienteId, nit,nombre, apellido, telefono, direccion));
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
        
        return FXCollections.observableList(clientes);
    }
}
