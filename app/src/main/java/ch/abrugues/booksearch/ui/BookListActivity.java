package ch.abrugues.booksearch.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import ch.abrugues.booksearch.R;
import ch.abrugues.booksearch.model.BookList;
import ch.abrugues.booksearch.network.BookListTask;
import ch.abrugues.booksearch.utils.EndlessRecyclerViewScrollListener;
import ch.abrugues.booksearch.utils.RecyclerViewBookAdapter;

public class BookListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerViewBookAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private EndlessRecyclerViewScrollListener mScrollListener;

    private ProgressBar progressBar;

    private String mQuery = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mScrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextData(page);
            }
        };
        mRecyclerView.addOnScrollListener(mScrollListener);

        mAdapter = new RecyclerViewBookAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

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
                queryBooks();
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

    private void queryBooks() {
        new BookListTask(mQuery, 0) {
            @Override
            protected void onPreExecute() {
                progressBar.setVisibility(ProgressBar.VISIBLE);
                mAdapter.clear();
                mScrollListener.resetState();
            }
            @Override
            protected void onPostExecute(BookList bookList) {
                progressBar.setVisibility(ProgressBar.GONE);
                mAdapter.addAll(bookList.getBooks());
            }
        }.execute();

    }

    private void loadNextData(int page) {
        new BookListTask(mQuery, page) {
            @Override
            protected void onPostExecute(BookList bookList) {
                mAdapter.addAll(bookList.getBooks());
            }
        }.execute();
    }

    public String getQuery() {
        return mQuery;
    }
}
