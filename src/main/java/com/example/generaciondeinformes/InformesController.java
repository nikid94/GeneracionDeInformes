package com.example.generaciondeinformes;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class InformesController {

    private String urlDB = "jdbc:sqlite:db/chinook.db";

    public void OnClientesClick(ActionEvent actionEvent) {
        try{
            String clientesPath = "informes/informeClientes.jrxml";
            InputStream clienteStream = InformesApplication.class.getResourceAsStream(clientesPath);
            System.out.println("Compilando informe: " + clientesPath);
            JasperReport jasperReport = JasperCompileManager.compileReport(clienteStream);

            Connection conn = DriverManager.getConnection(urlDB);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(),  conn);

            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException | SQLException e) {
            e.printStackTrace();
        }

    }

    public void OnArtistaClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(InformesApplication.class.getResource("artistas-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 537, 477);
        stage.setMinHeight(scene.getHeight());
        stage.setMinWidth(scene.getWidth());
        stage.setTitle("Generación de Informes");
        stage.setScene(scene);
        stage.show();


    }

    public void OnCerrarClick(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("¿Seguro que quieres cerrar la aplicación");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        if(alert.showAndWait().get() == ButtonType.YES) {
            System.exit(0);
        }else alert.close();

    }
}