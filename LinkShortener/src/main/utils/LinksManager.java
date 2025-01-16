package main.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.models.LinkModel;
import main.models.Links;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LinksManager {

    private static final String PATH = "links.json";

    public static void saveLink(LinkModel link) {
        List<LinkModel> existingLings = new ArrayList<>(getLinksForUser(link.getUserId()));

        try (FileWriter fileWriter = new FileWriter(PATH)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
                    .create();
            existingLings.add(link);
            Links links = new Links(existingLings);
            String json = gson.toJson(links);
            fileWriter.write(json);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении ссылки");
        }
    }

    public static void saveLinks(List<LinkModel> links) {
        try (FileWriter fileWriter = new FileWriter(PATH)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
                    .create();
            String json = gson.toJson(new Links(links));
            fileWriter.write(json);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении ссылок");
        }
    }


    public static List<LinkModel> getLinksForUser(String userId) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
                .create();

        try (FileReader reader = new FileReader(PATH)) {
            Links result = gson.fromJson(reader, Links.class);
            if (result == null) {
                return Collections.emptyList();
            }
            List<LinkModel> resultLinks = new ArrayList<>(Collections.emptyList());
            for (LinkModel link : result.getLinks()) {
                if (userId.equals(link.getUserId())) {
                    resultLinks.add(link);
                }
            }

            return resultLinks;
        } catch (IOException e) {
            try {
                File file = new File(PATH);
                file.createNewFile();
            } catch (IOException exc) {
                System.out.println("Ошибка при получении списка ссылок");
            }
        }

        return new ArrayList<>(Collections.emptyList());
    }

    public static List<LinkModel> getLinks() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
                .create();

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
                System.out.println("Ошибка при получении списка ссылок");
            }
        }

        return new ArrayList<>();
    }

    public static boolean checkIfLinkExistInStorage(String originalLink, List<LinkModel> links) {
        if (links.isEmpty()) return false;
        for (LinkModel link : links) {
            if (originalLink.equals(link.getOriginalUrl())) return true;
        }

        return false;
    }

    public static void removeLink(LinkModel link){
        List<LinkModel> links = getLinks();
        links.removeIf(localLink -> localLink.getShortenedUrl().equals(link.getShortenedUrl()));
        saveLinks(links);
    }

    public static void incrementClicksForLink(LinkModel link) {
        List<LinkModel> links = getLinks();
        for (LinkModel localLink: links) {
            if (localLink.getShortenedUrl().equals(link.getShortenedUrl())) {
                localLink.setClicks(localLink.getClicks() + 1);
                saveLinks(links);
                return;
            }
        }
    }

    public static void updateClicksLimit(LinkModel link, int clicksLimit) {
        List<LinkModel> links = getLinks();
        for (LinkModel localLink: links) {
            if (localLink.getShortenedUrl().equals(link.getShortenedUrl())) {
                localLink.setClicksLimit(clicksLimit);
                saveLinks(links);
                return;
            }
        }
    }
}
