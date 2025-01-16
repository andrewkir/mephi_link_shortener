module LinkShortener {
    requires com.google.gson;
    opens main.models to com.google.gson;
}
