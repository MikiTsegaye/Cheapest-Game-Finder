module org.hit {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    opens org.hit.client to javafx.fxml;
    opens org.hit.client.model to com.google.gson;
    exports org.hit.client;
}