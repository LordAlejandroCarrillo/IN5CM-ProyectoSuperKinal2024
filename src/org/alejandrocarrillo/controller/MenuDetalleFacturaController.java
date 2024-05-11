package org.alejandrocarrillo.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.alejandrocarrillo.system.Main;

/**
 * FXML Controller class
 *
 * @author Alejandro
 */
public class MenuDetalleFacturaController implements Initializable {
    private Main stage;

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
        
    }
}
