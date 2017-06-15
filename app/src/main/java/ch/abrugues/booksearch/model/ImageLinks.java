package ch.abrugues.booksearch.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ImageLinks implements Serializable {

    @SerializedName("thumbnail")
    private String thumbnail;

    public String getThumbnail() {
        return thumbnail;
    }

}
