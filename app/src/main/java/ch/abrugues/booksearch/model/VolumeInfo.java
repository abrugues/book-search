package ch.abrugues.booksearch.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class VolumeInfo implements Serializable {

    @SerializedName("title")
    private String title;

    @SerializedName("authors")
    private List<String> authors = null;

    @SerializedName("publisher")
    private String publisher;

    @SerializedName("description")
    private String description;

    @SerializedName("pageCount")
    private String pageCount;

    @SerializedName("imageLinks")
    private ImageLinks imageLinks;

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getDescription() {
        return description;
    }

    public String getPageCount() {
        return pageCount;
    }

    public ImageLinks getImageLinks() {
        return imageLinks;
    }

}
