module LinkShortener {
    requires com.google.gson;
    requires java.desktop;
    opens main.models to com.google.gson;
}
