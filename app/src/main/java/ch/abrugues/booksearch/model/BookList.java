package ch.abrugues.booksearch.model;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BookList {

    @SerializedName("totalItems")
    private int totalItems;

    @SerializedName("items")
    private List<Book> books = new ArrayList<>();

    public int getTotalItems() {
        return totalItems;
    }

    public List<Book> getBooks() {
        return books;
    }
}
