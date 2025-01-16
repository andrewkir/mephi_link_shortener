package main.models;

import java.time.LocalDateTime;

public class LinkModel {
    private String originalUrl;
    private String shortenedUrl;
    private int clicksLimit;
    private int clicks;
    private LocalDateTime expirationTime;
    private String userId;
    private boolean isActive;

    public LinkModel(String originalUrl, String shortenedUrl, int clicksLimit, LocalDateTime expirationTime, String userId){
        this.originalUrl = originalUrl;
        this.shortenedUrl = shortenedUrl;
        this.clicksLimit = clicksLimit;
        this.expirationTime = expirationTime;
        this.userId = userId;
        this.isActive = true;
        this.clicks = 0;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getShortenedUrl() {
        return shortenedUrl;
    }

    public void setShortenedUrl(String shortenedUrl) {
        this.shortenedUrl = shortenedUrl;
    }

    public int getClicksLimit() {
        return clicksLimit;
    }

    public void setClicksLimit(int clicksLimit) {
        this.clicksLimit = clicksLimit;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
