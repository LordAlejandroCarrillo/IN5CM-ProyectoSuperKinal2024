package org.alejandrocarrillo.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import org.alejandrocarrillo.system.Main;

/**
 * FXML Controller class
 *
 * @author informatica
 */
public class MenuPrincipalController implements Initializable {
    private Main stage;
    @FXML
    MenuItem mtClientes, mtTicket, mtCargos, mtEmpleados, mtCompras, mtCategoriaProductos, mtFacturas, mtDistribuidores, mtProductos, mtDetalleCompra, mtPromociones, mtDetalleFactura;
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == mtClientes){
            stage.menuClienteView();
        } else if(event.getSource() == mtTicket){
            stage.menuTicketSoporte();
        } else if(event.getSource() == mtCargos){
            stage.menuCargosView();
        } else if(event.getSource() == mtEmpleados){
            stage.menuEmpleadosView();
        } else if(event.getSource() == mtCompras){
            stage.menuComprasView();
        } else if(event.getSource() == mtCategoriaProductos){
            stage.menuCategoriaProductosView();
        } else if(event.getSource() == mtFacturas){
            stage.menuFacturasView();
        } else if(event.getSource() == mtDistribuidores){
            stage.menuDistribuidoresView();
        } else if(event.getSource() == mtProductos){
            stage.menuProductosView();
        } else if(event.getSource() == mtDetalleCompra){
            stage.menuDetalleCompraView();
        } else if(event.getSource() == mtPromociones){
            stage.menuPromocionesView();
        } else if(event.getSource() == mtDetalleFactura){
            stage.menuDetalleFacturaView();
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
