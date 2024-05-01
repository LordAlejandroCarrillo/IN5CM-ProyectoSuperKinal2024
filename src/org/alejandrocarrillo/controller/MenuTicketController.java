/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alejandrocarrillo.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.alejandrocarrillo.system.Main;

/**
 *
 * @author informatica
 */
public class MenuTicketController implements Initializable {
    private Main stage;
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
