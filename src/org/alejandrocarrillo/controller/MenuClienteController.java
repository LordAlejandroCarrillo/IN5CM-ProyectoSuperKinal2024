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
import org.alejandrocarrillo.model.Cliente;
import org.alejandrocarrillo.system.Main;
import org.alejandrocarrillo.dto.ClienteDTO;
import org.alejandrocarrillo.utils.SuperKinalAlert;
public class MenuClienteController implements Initializable {

    private Main stage;
    public static int opcion;
    
    @FXML
    TextField textField;
    @FXML
    Button btnBack, btnBuscar, btnEliminar, btnAgregar, btnEditar, btnListar;
    @FXML
            
    TableView tblClientes;
    @FXML
    TableColumn colClienteId, colNit, colNombre, colApellido, colTelefono, colDireccion, colClienteId1, colNit1, colNombre1, colApellido1, colTelefono1, colDireccion1;
    private Connection conexion;
    private PreparedStatement statement;
    private ResultSet resultSet;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        
    }   
    
    @FXML
    public void handleButtonAction (ActionEvent event){
        if(event.getSource() == btnBack){
            stage.menuPrincipalView();
        } else if(event.getSource() == btnBuscar){
            buscarDatos(Integer.parseInt(textField.getText()));
        } else if(event.getSource() == btnAgregar){
            stage.menuClienteVDatos();
            opcion = 1;
        } else if(event.getSource() == btnEditar){
            ClienteDTO.getClienteDTO().setCliente((Cliente) tblClientes.getSelectionModel().getSelectedItem());
            if(ClienteDTO.getClienteDTO().getCliente() != null){
                stage.menuClienteVDatos();
            }
            opcion = 2;
        } else if(event.getSource() == btnListar){
            cargarDatos();
            textField.setText("");
        } else if(event.getSource() == btnEliminar){
            if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(404).get() == ButtonType.OK){
                ClienteDTO.getClienteDTO().setCliente((Cliente) tblClientes.getSelectionModel().getSelectedItem());
                borrarDatos();
                ClienteDTO.getClienteDTO().setCliente(null);
                cargarDatos();
            }
        }
    }
    
    public void cargarDatos(){
        tblClientes.setItems(listarClientes());
        colClienteId.setCellValueFactory(new PropertyValueFactory<Cliente, Integer>("clienteId"));
        colNit.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nit"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Cliente, String>("apellido"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Cliente, String>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Cliente, String>("direccion"));
    }
    
    public void borrarDatos(){
        eliminarCliente(ClienteDTO.getClienteDTO().getCliente().getClienteId());
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
    
    public void buscarDatos(int cliId){
        tblClientes.setItems(buscarCliente(cliId));
        colClienteId.setCellValueFactory(new PropertyValueFactory<Cliente, Integer>("clienteId"));
        colNit.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nit"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Cliente, String>("apellido"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Cliente, String>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Cliente, String>("direccion"));
    }
    
    public ObservableList<Cliente> buscarCliente(int cliId){
        ArrayList<Cliente> clientes = new ArrayList<>();

        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_buscarClientes(?);";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1,cliId);
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
    
    public void eliminarCliente(int cliId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_eliminarClientes(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, cliId);
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

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
}
