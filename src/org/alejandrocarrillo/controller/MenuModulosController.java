/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author Alejandro
 */
public class MenuModulosController implements Initializable {
    private Main stage;
    @FXML
    Button btnClientes, btnTicket, btnCargos, btnEmpleados, btnCompras, btnCategoriaProductos, btnFacturas, btnDistribuidores, btnProductos, btnDetalleCompra, btnPromociones, btnDetalleFactura, modulo;
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnClientes){
            stage.menuClienteView();
        } else if(event.getSource() == btnTicket){
            stage.menuTicketSoporte();
        } else if(event.getSource() == btnCargos){
            stage.menuCargosView();
        } else if(event.getSource() == btnEmpleados){
            stage.menuEmpleadosView();
        } else if(event.getSource() == btnCategoriaProductos){
            stage.menuCategoriaProductosView();
        } else if(event.getSource() == btnFacturas){
            stage.menuFacturasView();
        } else if(event.getSource() == btnDistribuidores){
            stage.menuDistribuidoresView();
        } else if(event.getSource() == btnProductos){
            stage.menuProductosView();
        } else if(event.getSource() == btnDetalleCompra){
            stage.menuDetalleCompraView();
        } else if(event.getSource() == btnPromociones){
            stage.menuPromocionesView();
        } else if(event.getSource() == modulo){
            stage.menuPrincipalView();
            
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
