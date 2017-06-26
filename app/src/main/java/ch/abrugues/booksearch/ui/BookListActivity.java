package ch.abrugues.booksearch.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.abrugues.booksearch.R;
import ch.abrugues.booksearch.model.BookList;
import ch.abrugues.booksearch.network.BookSvcApi;
import ch.abrugues.booksearch.utils.EndlessRecyclerViewScrollListener;
import ch.abrugues.booksearch.utils.RecyclerViewBookAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookListActivity extends AppCompatActivity {

    private static final int MAX_RESULTS = 20;

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    private RecyclerViewBookAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private EndlessRecyclerViewScrollListener mScrollListener;

    private String mQuery = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        ButterKnife.bind(this);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mScrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                fetchBooks(page);
            }
        };
        mRecyclerView.addOnScrollListener(mScrollListener);

        mAdapter = new RecyclerViewBookAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Ask for the books to the remote server
                mQuery = query;
                fetchBooks(0);
                // Reset the SearchView
                searchView.clearFocus();
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchItem.collapseActionView();
                // Set the Activity title as the query
                BookListActivity.this.setTitle(mQuery);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    private void fetchBooks(final int page) {
        if (page == 0) {
            progressBar.setVisibility(ProgressBar.VISIBLE);
            mAdapter.clear();
            mScrollListener.resetState();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BookSvcApi.GOOGLE_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BookSvcApi bookSvcApi = retrofit.create(BookSvcApi.class);
        String startIndex = String.valueOf(page * MAX_RESULTS);
        String maxResults = String.valueOf(MAX_RESULTS);
        Call<BookList> bookListCall = bookSvcApi.searchBooks(mQuery, startIndex, maxResults);
        bookListCall.enqueue(new Callback<BookList>() {
            @Override
            public void onResponse(Call<BookList> call, Response<BookList> response) {
                if (page == 0) {
                    progressBar.setVisibility(ProgressBar.GONE);
                }
                mAdapter.addAll(response.body().getBooks());
            }

            @Override
            public void onFailure(Call<BookList> call, Throwable t) {

            }
        });

    }

    public String getQuery() {
        return mQuery;
    }
}
