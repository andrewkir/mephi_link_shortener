package main.utils;

import com.google.gson.Gson;
import main.models.LinkModel;
import main.models.Links;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class LinksManager {

    private static final String PATH = "links.json";

    public static void saveLink(LinkModel link) {
        List<LinkModel> existingLings = new ArrayList<>(getLinksForUser(link.getUserId()));

        try (FileWriter fileWriter = new FileWriter(PATH)) {
            Gson gson = new Gson();
            existingLings.add(link);
            Links links = new Links(existingLings);
            String json = gson.toJson(links);
            fileWriter.write(json);
            System.out.println("JSON data saved to " + PATH);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении ссылки");
        }
    }


    public static List<LinkModel> getLinksForUser(String userId) {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(PATH)) {
            Links result = gson.fromJson(reader, Links.class);
            if (result == null) {
                return Collections.emptyList();
            }
            List<LinkModel> resultLinks = new ArrayList<>(Collections.emptyList());
            for (LinkModel link : result.getLinks()) {
                if (Objects.equals(userId, link.getUserId())) {
                    resultLinks.add(link);
                }
            }

            return resultLinks;
        } catch (IOException e) {
            try {
                File file = new File(PATH);
                file.createNewFile();
            } catch (IOException exc) {
                exc.printStackTrace();
                System.out.println("Ошибка при получении списка ссылок");
            }
        }

        return new ArrayList<>(Collections.emptyList());
    }

    public static List<LinkModel> getLinks() {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(PATH)) {
            Links result = gson.fromJson(reader, Links.class);
            if (result == null) {
                return new ArrayList<>(Collections.emptyList());
            }
            return result.getLinks();
        } catch (IOException e) {
            try {
                File file = new File(PATH);
                file.createNewFile();
            } catch (IOException exc) {
                exc.printStackTrace();
                System.out.println("Ошибка при получении списка ссылок");
            }
        }

        return new ArrayList<>();
    }

    public static boolean checkIfLinkExistInStorage(String originalLink, List<LinkModel> links){
        for (LinkModel link: links){
            if (Objects.equals(originalLink, link.getOriginalUrl())) return true;
        }

        return false;
    }
}
