package main.utils;

import com.google.gson.Gson;
import main.models.Config;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigLoader {
    private static final String PATH = "config.json";

    public static Config loadConfig() {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(PATH)) {
            return gson.fromJson(reader, Config.class);
        } catch (IOException e) {
            try (FileWriter fileWriter = new FileWriter(PATH)) {
                Config config = new Config(100, 24);
                String json = gson.toJson(config);
                fileWriter.write(json);
            } catch (IOException ex) {
                System.out.println("Ошибка при получении и создании конфига");
            }
        }

        return new Config(100, 24);
    }
}
