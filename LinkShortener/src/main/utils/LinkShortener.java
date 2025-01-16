package main.utils;

import main.models.Config;
import main.models.LinkModel;

import java.time.LocalDateTime;
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
        } while (main.utils.LinksManager.checkIfLinkExistInStorage(link, existingUrls));
        int resultClicksLimit = config.getClickLimit();
        int resultHoursAlive = config.getLinkHours();
        if (clicksLimit != 0) {
            resultClicksLimit = clicksLimit;
        }
        if (hoursAlive != 0) {
            resultHoursAlive = hoursAlive;
        }
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(resultHoursAlive);
        LinkModel resultLink = new LinkModel(link, shortLink, resultClicksLimit, expirationTime, userId);
        existingUrls.add(resultLink);
        main.utils.LinksManager.saveLink(resultLink);
        return shortLink;
    }

    private String generateShortKey() {
        StringBuilder shortKey = new StringBuilder();
        for (int i = 0; i < SHORT_URL_LENGTH; i++) {
            shortKey.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return URL_BASE + shortKey;
    }
}
