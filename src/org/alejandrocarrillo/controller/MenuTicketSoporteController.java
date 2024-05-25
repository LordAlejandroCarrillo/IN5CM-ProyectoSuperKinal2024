package org.alejandrocarrillo.controller;

import java.net.URL;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.alejandrocarrillo.dao.Conexion;
import org.alejandrocarrillo.model.Cliente;
import org.alejandrocarrillo.model.Factura;
import org.alejandrocarrillo.model.TicketSoporte;
import org.alejandrocarrillo.system.Main;
import org.alejandrocarrillo.utils.SuperKinalAlert;

public class MenuTicketSoporteController implements Initializable {
    private Main stage;
    private TicketSoporte ts;
    @FXML
    Button btnBack, btnGuardar, btnVaciar, btnListar, btnBuscar, btnEliminar;
    @FXML
    TextField textField, tfBuscar;
    @FXML
    TableView tblTickets;
    @FXML
    ComboBox cmbEstatus, cmbFacturas, cmbClientes;
    @FXML
    TextArea txtDescripcion;
    @FXML
    TableColumn colTicketId, colDescripcion, colEstatus, colCliente, colFacturaId;
    
    private Connection conexion;
    private PreparedStatement statement;
    private ResultSet resultSet;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarCmbEstatus();
        cmbClientes.setItems(listarClientes());
        cmbFacturas.setItems(listarFacturas());
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
                eliminarTicketSoporte(ts.getTicketSoporteId());
                cargarDatos();
                vaciarCampos();
            }
        } else if(event.getSource() == btnListar){
            cargarDatos();
        } else if(event.getSource() == btnGuardar){
            if(!txtDescripcion.getText().equals("") && cmbClientes.getSelectionModel().getSelectedItem() != null && cmbFacturas.getSelectionModel().getSelectedItem() != null){
               token = true; 
            } else{
                token = false;
            }
            if(textField.getText().equals("")){
                con = true;
            } else{
                con = false;
            }
            if(con){
                if(token){
                    SuperKinalAlert.getInstance().mostraAlertaInformacion(400);
                    agregarTicket();
                    cargarDatos();
                    vaciarCampos();
                } else{
                    SuperKinalAlert.getInstance().mostraAlertaInformacion(504);
                }
            } else{
                if(token){
                    if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(505).get() == ButtonType.OK){
                        if(token){
                            editarTicket();
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
    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
    public void vaciarCampos(){
        textField.clear();
        txtDescripcion.clear();
        cmbEstatus.getSelectionModel().clearSelection();
        cmbFacturas.getSelectionModel().clearSelection();
        cmbClientes.getSelectionModel().clearSelection();
    }
    
    public void cargarCmbEstatus(){
        cmbEstatus.getItems().add("En Proceso");
        cmbEstatus.getItems().add("Finalizado");
    }
    
    public void cargarEditar(){
        ts = (TicketSoporte)tblTickets.getSelectionModel().getSelectedItem();
        if(ts !=  null){
            textField.setText(Integer.toString(ts.getTicketSoporteId()));
            txtDescripcion.setText(ts.getDescripcionTicket());
            cmbClientes.getSelectionModel().select(obtenerIndexCliente());
            cmbFacturas.getSelectionModel().select(obtenerIndexFactura());
        }
    }
    
    public int obtenerIndexCliente(){
        int index = 0;
        for(int i = 0; i <= cmbClientes.getItems().size(); i++){
            String clienteCmb = cmbClientes.getItems().get(i).toString();
            String clienteTbl = ((TicketSoporte)tblTickets.getSelectionModel().getSelectedItem()).getCliente();
            if(clienteCmb.equals(clienteTbl)){
                index = i;
                break;
            }
        }
        return index;
    }
    
    public int obtenerIndexFactura(){
        int index = 0;
        for(int i = 0; i <= cmbFacturas.getItems().size(); i++){
            String facturaCmb = cmbFacturas.getItems().get(i).toString();
            String facturaTbl = ((TicketSoporte)tblTickets.getSelectionModel().getSelectedItem()).getFactura();
            if(facturaCmb.equals(facturaTbl)){
                index = i;
                break;
            }
        }
        return index;
    }
    
    public void cargarDatos(){
        tblTickets.setItems(listarTickets());
        colTicketId.setCellValueFactory(new PropertyValueFactory<TicketSoporte, Integer>("ticketSoporteId"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TicketSoporte, String>("descripcionTicket"));
        colEstatus.setCellValueFactory(new PropertyValueFactory<TicketSoporte, String>("estatus"));
        colCliente.setCellValueFactory(new PropertyValueFactory<TicketSoporte, String>("cliente"));
        colFacturaId.setCellValueFactory(new PropertyValueFactory<TicketSoporte, Integer>("factura"));
        tblTickets.getSortOrder().add(colTicketId);
    }
    
    public void buscarDatos(){
        tblTickets.setItems(buscarTickets());
        colTicketId.setCellValueFactory(new PropertyValueFactory<TicketSoporte, Integer>("ticketSoporteId"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TicketSoporte, String>("descripcionTicket"));
        colEstatus.setCellValueFactory(new PropertyValueFactory<TicketSoporte, String>("estatus"));
        colCliente.setCellValueFactory(new PropertyValueFactory<TicketSoporte, String>("cliente"));
        colFacturaId.setCellValueFactory(new PropertyValueFactory<TicketSoporte, Integer>("factura"));
        tblTickets.getSortOrder().add(colTicketId);
    }
    
    public void agregarTicket(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_agregarTicketSoporte(?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, txtDescripcion.getText());
            statement.setInt(2, (((Cliente) cmbClientes.getSelectionModel().getSelectedItem()).getClienteId()));
            statement.setInt(3, (((Factura) cmbFacturas.getSelectionModel().getSelectedItem()).getFacturaId()));
            
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
    
    public void editarTicket(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarTicketSoporte(?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(textField.getText()));
            statement.setString(2, txtDescripcion.getText());
            statement.setString(3, cmbEstatus.getSelectionModel().getSelectedItem().toString());
            statement.setInt(4, ((Cliente)cmbClientes.getSelectionModel().getSelectedItem()).getClienteId());
            statement.setInt(5, ((Factura)cmbFacturas.getSelectionModel().getSelectedItem()).getFacturaId());
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
    
        public void eliminarTicketSoporte(int id){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_eliminarTicketSoporte(?)";
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
    
    public ObservableList<TicketSoporte> listarTickets(){
        ArrayList<TicketSoporte> tickets = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_listarTicketSoporte()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int ticketSoporteId = resultSet.getInt("ticketSoporteId");
                String descripcionTicket = resultSet.getString("descripcionTicket");
                String estatus = resultSet.getString("estatus");
                String cliente = resultSet.getString("cliente");
                String factura = resultSet.getString("factura");
                
                tickets.add(new TicketSoporte(ticketSoporteId, descripcionTicket, estatus, cliente, factura));
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
        
        return FXCollections.observableList(tickets);
    }
    
    public ObservableList<TicketSoporte> buscarTickets(){
        ArrayList<TicketSoporte> tickets = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_buscarTicketSoporte(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfBuscar.getText()));
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int ticketSoporteId = resultSet.getInt("ticketSoporteId");
                String descripcionTicket = resultSet.getString("descripcionTicket");
                String estatus = resultSet.getString("estatus");
                String cliente = resultSet.getString("cliente");
                String factura = resultSet.getString("factura");
                
                tickets.add(new TicketSoporte(ticketSoporteId, descripcionTicket, estatus, cliente, factura));
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
        
        return FXCollections.observableList(tickets);
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
