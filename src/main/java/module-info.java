module fr.martinb.conservatoire {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens fr.martinb.conservatoire to javafx.fxml;
    opens fr.martinb.conservatoire.controller to javafx.fxml;
    exports fr.martinb.conservatoire;
    exports fr.martinb.conservatoire.controller;
    exports fr.martinb.conservatoire.modele;
    exports fr.martinb.conservatoire.sql;
}