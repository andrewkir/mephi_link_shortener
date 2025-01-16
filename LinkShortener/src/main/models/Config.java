package main.models;

public class Config {
    private int clickLimit;
    private int linkHours;

    public Config(int clickLimit, int linkHours) {
        this.clickLimit = clickLimit;
        this.linkHours = linkHours;
    }

    public int getClickLimit() {
        return clickLimit;
    }

    public int getLinkHours() {
        return linkHours;
    }
}
