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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.alejandrocarrillo.dao.Conexion;
import org.alejandrocarrillo.model.NivelAcceso;
import org.alejandrocarrillo.model.Usuario;
import org.alejandrocarrillo.system.Main;
import org.alejandrocarrillo.utils.PasswordUtils;
import org.alejandrocarrillo.utils.SuperKinalAlert;

public class MenuLogInController implements Initializable {
    private Main stage;
    private int op = 0;
    
    @FXML
    Button btnIniciar, btnRegistrar;
    @FXML
    TextField tfUsuario;
    @FXML
    PasswordField tfPass;
    
    private static Connection conexion;
    private static PreparedStatement statement;
    private static ResultSet resultSet;
    
    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnIniciar){
            Usuario us = buscarUsuarios();
            if(op == 0){
                if(us != null){
                    if(PasswordUtils.getInstance().checkPassword(tfPass.getText(), us.getContra())){
                        SuperKinalAlert.getInstance().alertaUsuario(us.getUsuario());
                        if(us.getNivelAccesoId() == 1){
                            btnRegistrar.setDisable(false);
                            btnIniciar.setText("Ir al menu");
                            op = 1;
                        } else if(us.getNivelAccesoId() == 2){
                            stage.menuPrincipalView();
                        }
                    } else{
                        SuperKinalAlert.getInstance().mostraAlertaInformacion(507);
                    }
                } else{
                    SuperKinalAlert.getInstance().mostraAlertaInformacion(506);
                }
            } else{
                stage.menuPrincipalView();
            }
        } else if(event.getSource() == btnRegistrar){
            stage.formLogIn();
        }
    }
    
    public Usuario buscarUsuarios(){
        Usuario usuario = null;
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "CALL sp_buscarUsuarios(?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tfUsuario.getText());
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int usuarioId = resultSet.getInt("usuarioId");
                String us = resultSet.getString("usuario");
                String contra = resultSet.getString("contra");
                int nivelAccesoId = resultSet.getInt("nivelAccesoId");
                int empleadoId = resultSet.getInt("empleadoId");
                usuario = new Usuario(usuarioId, us, contra, nivelAccesoId, empleadoId);
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
        
        return usuario;
    }
}
