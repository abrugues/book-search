package ch.abrugues.booksearch.network;


import android.os.AsyncTask;

import java.io.IOException;

import ch.abrugues.booksearch.model.BookList;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookListTask extends AsyncTask<Object, Void, BookList> {

    private static final int MAX_RESULTS = 20;

    private String query;
    private int page;

    public BookListTask(String query, int page) {
        this.query = query;
        this.page = page;
    }

    @Override
    protected BookList doInBackground(Object[] objects) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BookSvcApi.GOOGLE_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BookSvcApi bookSvcApi = retrofit.create(BookSvcApi.class);
        String startIndex = String.valueOf(page * MAX_RESULTS);
        String maxResults = String.valueOf(MAX_RESULTS);
        Call<BookList> bookListCall = bookSvcApi.searchBooks(query,startIndex,maxResults);
        try {
            Response<BookList> bookListResponse = bookListCall.execute();
            return bookListResponse.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
