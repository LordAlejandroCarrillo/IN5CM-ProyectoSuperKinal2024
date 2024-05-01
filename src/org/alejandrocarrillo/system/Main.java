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
import org.alejandrocarrillo.controller.MenuClienteController;
import org.alejandrocarrillo.controller.MenuPrincipalController;
import org.alejandrocarrillo.controller.MenuClienteDatosController;

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
    
    public void menuPrincipalView(){
        try{
            MenuPrincipalController menuPrincipalView = (MenuPrincipalController)switchScene("MenuPrincipal.fxml", 1000, 725);
            menuPrincipalView.setStage(this);
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
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
            MenuClienteDatosController menuClienteVDatos = (MenuClienteDatosController)switchScene("MenuClienteVDatos.fxml", 1000, 700);
            menuClienteVDatos.setStage(this);
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }    
    public static void main(String[] args) {
        launch(args);
    }
    
}
