package main.models;

import java.util.List;

public class Links {
    private List<LinkModel> links;

    public Links(List<LinkModel> links) {
        this.links = links;
    }

    public Links() {}

    public List<LinkModel> getLinks() {
        return links;
    }

    public void setLinks(List<LinkModel> links) {
        this.links = links;
    }
}
