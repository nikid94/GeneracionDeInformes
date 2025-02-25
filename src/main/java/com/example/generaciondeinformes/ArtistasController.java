package com.example.generaciondeinformes;

import com.example.generaciondeinformes.Clases.Artista;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ArtistasController {
    @FXML
    private ListView<Artista> lvArtistas;
    private String urlDB = "jdbc:sqlite:db/chinook.db";

    public void initialize() {
        lvArtistas.setItems(getArtistas());
        System.out.println(lvArtistas.getItems());
        lvArtistas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmación");
                alert.setHeaderText("¿Realizar informe de " + newValue + "?");
                alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                if(alert.showAndWait().get() == ButtonType.YES) {
                    try{
                        String artistaPath = "informes/informeArtista.jrxml";
                        InputStream clienteStream = InformesApplication.class.getResourceAsStream(artistaPath);
                        System.out.println("Compilando informe: " + artistaPath);
                        JasperReport jasperReport = JasperCompileManager.compileReport(clienteStream);

                        Connection conn = DriverManager.getConnection(urlDB);

                        Map<String, Object> params = new HashMap<>();
                        params.put("ArtistaID", newValue.getId());

                        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params,  conn);

                        JasperViewer.viewReport(jasperPrint, false);

                    } catch (JRException | SQLException e) {
                        e.printStackTrace();
                    }
                }else alert.close();
            }
        });
    }

    public void onCancelarClick(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("¿Seguro que quieres cancelar el informe?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        if(alert.showAndWait().get() == ButtonType.YES) {
            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        }else alert.close();
    }

    public ObservableList<Artista> getArtistas() {
        ObservableList<Artista> artistasList = FXCollections.observableArrayList();
        try {
            Connection conn = DriverManager.getConnection(urlDB);
            String consulta = "SELECT artists.ArtistId, artists.Name FROM artists ORDER BY artists.Name";
            PreparedStatement ps = conn.prepareStatement(consulta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                artistasList.add(new Artista(rs.getInt("ArtistId"), rs.getString("Name")));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return artistasList;
    }
}
