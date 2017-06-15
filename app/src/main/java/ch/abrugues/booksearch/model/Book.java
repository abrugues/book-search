package ch.abrugues.booksearch.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Book implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("volumeInfo")
    private VolumeInfo volumeInfo;

    public String getId() {
        return id;
    }

    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

}
