package main.utils;

import com.google.gson.Gson;
import main.models.User;
import main.models.Users;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsersManager {
    private static final String PATH = "users.json";

    public static Users loadUsers() {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(PATH)) {
            return gson.fromJson(reader, Users.class);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                File file = new File(PATH);
                file.createNewFile();
            } catch (IOException exc) {
                System.out.println("Ошибка при получении списка пользователей");
            }
        }

        return new Users(Collections.emptyList());
    }

    public static void saveUser(User user, Users users){
        try (FileWriter fileWriter = new FileWriter(PATH)) {
            Gson gson = new Gson();
            if (users == null) {
                users = new Users(new ArrayList<>(Collections.emptyList()));
            }
            List<User> innerUsers = users.getUsers();
            innerUsers.add(user);

            String json = gson.toJson(new Users(innerUsers));
            fileWriter.write(json);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении пользователя");
        }
    }
}
