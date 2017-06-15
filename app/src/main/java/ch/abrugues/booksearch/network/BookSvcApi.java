package ch.abrugues.booksearch.network;


import ch.abrugues.booksearch.model.BookList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BookSvcApi {

    String GOOGLE_API_BASE_URL = "https://www.googleapis.com/books/v1/";
    String BOOK_SVC = "volumes";

    @GET(BOOK_SVC)
    Call<BookList> searchBooks(@Query("q") String searchTerms, @Query("startIndex") String startIndex,
                               @Query("maxResults") String maxResults);

}
