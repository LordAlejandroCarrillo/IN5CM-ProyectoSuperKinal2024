package org.alejandrocarrillo.system;

import java.util.ArrayList;
import javafx.scene.image.Image;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.InputStream;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.layout.AnchorPane;
import org.alejandrocarrillo.controller.*;


/**
 *
 * @author informatica
 */
public class Main extends Application {
    private final String URLWIEV = "/org/alejandrocarrillo/view/";
    private Stage stage;
    private Scene scene;
    List<Image> icons = new ArrayList<>();
    
    @Override
    public void start(Stage stage) throws Exception {
        icons.add(new Image(getClass().getResourceAsStream("/org/alejandrocarrillo/image/LogoSuperKinal.png")));
        this.stage = stage;
        stage.getIcons().addAll(icons);
        stage.setTitle("SK APP");
        menuPrincipalView();
        stage.show();
        stage.setResizable(false);
    }
    
    public Initializable switchScene(String fxmlName, int width, int height) throws Exception{
        Initializable resultado = null;
        FXMLLoader loader = new FXMLLoader();
        
        InputStream file = Main.class.getResourceAsStream(URLWIEV + fxmlName);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(URLWIEV + fxmlName));
        
        scene = new Scene((AnchorPane) loader.load(file), width, height);
        stage.getIcons();
        stage.setScene(scene);
        stage.sizeToScene();
        
        resultado = (Initializable) loader.getController();
        
        return resultado;
    }
    //MENU PRINCIPAL
    public void menuPrincipalView(){
        try{
            MenuPrincipalController menuPrincipalView = (MenuPrincipalController)switchScene("MenuPrincipal.fxml", 1000, 725);
            menuPrincipalView.setStage(this);
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    //
    //MENU CLIENTE
    public void menuClienteView(){
        try{
            MenuClienteController menuClienteView = (MenuClienteController)switchScene("MenuClienteView.fxml", 1000, 725);
            menuClienteView.setStage(this);
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void menuClienteVDatos(){
        try{
            MenuClienteDatosController menuClienteVDatos = (MenuClienteDatosController)switchScene("MenuClienteVDatos.fxml", 1000, 725);
            menuClienteVDatos.setStage(this);
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    //
    //MENU TICKET SOPORTE
    public void menuTicketSoporte(){
        try{
            MenuTicketSoporteController menuTicketSoporte = (MenuTicketSoporteController)switchScene("MenuTicketSoporte.fxml", 1000, 725);
            menuTicketSoporte.setStage(this);
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    //
    //MENU CARGOS
    public void menuCargosView(){
        try{
            MenuCargosController menuCargosView = (MenuCargosController)switchScene("MenuCargos.fxml", 1000, 725);
            menuCargosView.setStage(this);
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void formCargosView(){
        try{
            FormCargosController formCargosView = (FormCargosController)switchScene("FormCargos.fxml", 1000, 725);
            formCargosView.setStage(this);
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    //
    //MENU EMPLEADOS
    public void menuEmpleadosView(){
        try{
            MenuEmpleadosController menuEmpleadosView = (MenuEmpleadosController)switchScene("MenuEmpleados.fxml", 1000, 725);
            menuEmpleadosView.setStage(this);
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    //
    //MENU COMPRAS
    public void menuComprasView(){
        try{
            MenuComprasController menuComprasView = (MenuComprasController)switchScene("MenuCompras.fxml", 1000, 725);
            menuComprasView.setStage(this);
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void formComprasView(){
        try{
            FormComprasController formComprasView = (FormComprasController)switchScene("FormCompras.fxml", 1000, 725);
            formComprasView.setStage(this);
        } catch(Exception e){
           System.out.println(e.getMessage());
        }
    }
    
    //MENU CATEGORIA PRODUCTOS
    public void menuCategoriaProductosView(){
        try{
            MenuCategoriaProductosController menuCategoriaProductosView = (MenuCategoriaProductosController)switchScene("MenuCategoriaProductos.fxml", 1000, 725);
            menuCategoriaProductosView.setStage(this);
        } catch(Exception e){
           System.out.println(e.getMessage());
        }
    }
    public void formCategoriaProductosView(){
        try{
            FormCategoriaProductosController formCategoriaProductosView = (FormCategoriaProductosController)switchScene("FormCategoriaProductos.fxml", 1000, 725);
            formCategoriaProductosView.setStage(this);
        } catch(Exception e){
           System.out.println(e.getMessage());
        }
    }
    
    //MENU FACTURAS
    public void menuFacturasView(){
        try{
            MenuFacturasController menuFacturasView = (MenuFacturasController)switchScene("MenuFacturas.fxml", 1000, 725);
            menuFacturasView.setStage(this);
        } catch(Exception e){
           System.out.println(e.getMessage());
        }  
    }
    
    //
    //MENU DISTRIBUIDORES
    public void menuDistribuidoresView(){
        try{
            MenuDistribuidoresController menuDistribuidoresView = (MenuDistribuidoresController)switchScene("MenuDistribuidores.fxml", 1000, 725);
            menuDistribuidoresView.setStage(this);
        } catch(Exception e){
           System.out.println(e.getMessage());
        }  
    }
    public void formDistribuidoresView(){
        try{
            FormDistribuidoresController formDistribuidoresView = (FormDistribuidoresController)switchScene("FormDistribuidores.fxml", 1000, 725);
            formDistribuidoresView.setStage(this);
        } catch(Exception e){
           System.out.println(e.getMessage());
        }  
    }
    
    //
    //MENU PRODUCTOS
    public void menuProductosView(){
        try{
            MenuProductosController menuProductosView = (MenuProductosController)switchScene("MenuProducto.fxml", 1000, 725);
            menuProductosView.setStage(this);
        } catch(Exception e){
           System.out.println(e.getMessage());
        }  
    }
    
    //
    //MENU DETALLE COMPRA
    public void menuDetalleCompraView(){
        try{
            MenuDetalleCompraController menuDetalleCompraView = (MenuDetalleCompraController)switchScene("MenuDetalleCompra.fxml", 1000, 725);
            menuDetalleCompraView.setStage(this);
        } catch(Exception e){
           System.out.println(e.getMessage());
        }  
    }
    
    //
    //MENU PROMOCIONES
    public void menuPromocionesView(){
        try{
            MenuPromocionesController menuPromocionesView = (MenuPromocionesController)switchScene("MenuPromociones.fxml", 1000, 725);
            menuPromocionesView.setStage(this);
        } catch(Exception e){
           System.out.println(e.getMessage());
        }  
    }
    
    //
    //MENU DETALLE FACTURA
    public void menuDetalleFacturaView(){
        try{
            MenuDetalleFacturaController menuDetalleFacturaView = (MenuDetalleFacturaController)switchScene("MenuDetalleFactura.fxml", 1000, 725);
            menuDetalleFacturaView.setStage(this);
        } catch(Exception e){
           System.out.println(e.getMessage());
        }  
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
