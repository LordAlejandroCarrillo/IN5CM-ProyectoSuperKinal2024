package org.alejandrocarrillo.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.alejandrocarrillo.system.Main;

/**
 * FXML Controller class
 *
 * @author informatica
 */
public class MenuPrincipalController implements Initializable {
    private Main stage;
    @FXML
    Button modulos;
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == modulos){
            stage.menuModulosView();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
}
