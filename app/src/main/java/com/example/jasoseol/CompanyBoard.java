package com.example.jasoseol;

public class CompanyBoard {
    private String imgURL;
    private String title;
    private String position;
    private String until;
    private boolean favorite;

    public CompanyBoard(String imgURL, String title, String position, String until) {
        this.imgURL = imgURL;
        this.title = title;
        this.position = position;
        this.until = until;
        this.favorite = false;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getUntil() {
        return until;
    }

    public void setUntil(String until) {
        this.until = until;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "CompanyBoard{" +
                "imgURL='" + imgURL + '\'' +
                ", title='" + title + '\'' +
                ", position='" + position + '\'' +
                ", until='" + until + '\'' +
                ", favorite=" + favorite +
                '}';
    }
}
