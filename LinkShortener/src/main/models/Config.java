package main.models;

public class Config {
    private int clickLimit;
    private int linkHours;

    public Config(int clickLimit, int linkHours) {
        this.clickLimit = clickLimit;
        this.linkHours = linkHours;
    }

    public Config(){
        this.clickLimit = 100;
        this.linkHours = 24;
    }

    public int getClickLimit() {
        return clickLimit;
    }

    public void setClickLimit(int clickLimit) {
        this.clickLimit = clickLimit;
    }

    public int getLinkHours() {
        return linkHours;
    }

    public void setLinkHours(int linkHours) {
        this.linkHours = linkHours;
    }
}
