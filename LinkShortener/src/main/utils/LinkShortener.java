package main.utils;

import main.models.Config;
import main.models.LinkModel;

import java.awt.*;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;

public class LinkShortener {

    private final List<LinkModel> existingUrls;
    private final Random random;
    private final Config config;
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_URL_LENGTH = 8;
    private static final String URL_BASE = "clck.ru/";

    public LinkShortener() {
        existingUrls = LinksManager.getLinks();
        config = main.utils.ConfigLoader.loadConfig();
        random = new Random();
    }

    public String getShortLink(String userId, String link, int clicksLimit, int hoursAlive) {
        String shortLink;
        do {
            shortLink = generateShortKey();
        } while (main.utils.LinksManager.checkIfLinkExistInStorage(shortLink, existingUrls));
        int resultClicksLimit = config.getClickLimit();
        int resultHoursAlive = config.getLinkHours();
        if (clicksLimit != 0) {
            resultClicksLimit = clicksLimit;
        }
        if (hoursAlive != 0) {
            resultHoursAlive = hoursAlive;
        }
        Instant expirationTime = LocalDateTime.now().plusHours(resultHoursAlive).atZone(ZoneId.systemDefault()).toInstant();
        LinkModel resultLink = new LinkModel(link, shortLink, resultClicksLimit, expirationTime, userId);
        existingUrls.add(resultLink);
        main.utils.LinksManager.saveLink(resultLink);
        return shortLink;
    }

    public void linkRedirect(LinkModel link) {
        try {
            if (link.getClicks() >= link.getClicksLimit()) {
                System.out.println("Достигнут лимит по переходам по ссылке");
                LinksManager.removeLink(link);
            } else {
                Instant now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();
                Instant expirationTime = link.getExpirationTime();
                if (now.isBefore(expirationTime)) {
                    LinksManager.incrementClicksForLink(link);
                    Desktop.getDesktop().browse(new URI(link.getOriginalUrl()));
                } else {
                    System.out.println("Ссылка больше недействительна");
                    LinksManager.removeLink(link);
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка при открытии ссылки");
        }
    }

    public void updateLinkClicksLimit(LinkModel link, int clicksLimit) {
        LinksManager.updateClicksLimit(link, clicksLimit);
    }

    private String generateShortKey() {
        StringBuilder shortKey = new StringBuilder();
        for (int i = 0; i < SHORT_URL_LENGTH; i++) {
            shortKey.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return URL_BASE + shortKey;
    }
}
