module com.example.generaciondeinformes {
    requires javafx.controls;
    requires javafx.fxml;
    requires net.sf.jasperreports.core;

    requires org.xerial.sqlitejdbc;
    requires java.sql;
    requires org.slf4j;
    requires org.slf4j.simple;
    requires java.desktop;

    opens com.example.generaciondeinformes;
    exports com.example.generaciondeinformes;
}