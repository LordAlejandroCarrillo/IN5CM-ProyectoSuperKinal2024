package org.alejandrocarrillo.report;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.alejandrocarrillo.dao.Conexion;
import win.zqxu.jrviewer.JRViewerFX;

public class GenerarReporte {
    private static GenerarReporte instance;
    
    private static Connection conexion;
    
    private GenerarReporte(){
    
    }

    public static GenerarReporte getInstance() {
        if(instance == null){
            instance = new GenerarReporte();
        }
        return instance;
    }
    
    public void generarFactura(int Id){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("factId", Id);
            Stage reportStage = new Stage();
            JasperPrint reporte = JasperFillManager.fillReport(GenerarReporte.class.getResourceAsStream("/org/alejandrocarrillo/report/Facturas.jasper"), parametros, conexion);
            JRViewerFX reportView = new JRViewerFX(reporte);
            Pane root = new Pane();
            root.getChildren().add(reportView);
            reportView.setPrefSize(600, 800);
            Scene scene = new Scene(root);
            reportStage.setScene(scene);
            reportStage.setTitle("Factura");
            reportStage.show();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void generarClientes(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            Map<String, Object> parametros = new HashMap<>();
            Stage reportStage = new Stage();
            JasperPrint reporte = JasperFillManager.fillReport(GenerarReporte.class.getResourceAsStream("/org/alejandrocarrillo/report/Clientes.jasper"), parametros, conexion);
            JRViewerFX reportView = new JRViewerFX(reporte);
            Pane root = new Pane();
            root.getChildren().add(reportView);
            reportView.setPrefSize(600, 800);
            Scene scene = new Scene(root);
            reportStage.setScene(scene);
            reportStage.setTitle("Cliente");
            reportStage.show();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void generarProductos(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            Map<String, Object> parametros = new HashMap<>();
            Stage reportStage = new Stage();
            JasperPrint reporte = JasperFillManager.fillReport(GenerarReporte.class.getResourceAsStream("/org/alejandrocarrillo/report/Productos.jasper"), parametros, conexion);
            JRViewerFX reportView = new JRViewerFX(reporte);
            Pane root = new Pane();
            root.getChildren().add(reportView);
            reportView.setPrefSize(850, 792);
            Scene scene = new Scene(root);
            reportStage.setScene(scene);
            reportStage.setTitle("Producto");
            reportStage.show();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
