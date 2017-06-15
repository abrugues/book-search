package ch.abrugues.booksearch.utils;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ch.abrugues.booksearch.R;
import ch.abrugues.booksearch.model.Book;
import ch.abrugues.booksearch.ui.BookDetailsActivity;
import ch.abrugues.booksearch.ui.BookListActivity;

public class RecyclerViewBookAdapter extends RecyclerView.Adapter<RecyclerViewBookAdapter.ViewHolder> {

    private Context mContext;
    private List<Book> bookList;

    public RecyclerViewBookAdapter(Context context) {
        mContext = context;
        bookList = new ArrayList<>();
    }

    public void addAll(List<Book> bookList) {
        this.bookList.addAll(bookList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.bookList = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Book book = bookList.get(position);
        holder.setBook(book);

        // Thumbnail
        String imageUrl = null;
        if (book.getVolumeInfo().getImageLinks() != null) {
            imageUrl = book.getVolumeInfo().getImageLinks().getThumbnail();
        }
        if (imageUrl != null) {
            Picasso.with(mContext).load(imageUrl).into(holder.mThumbnailImgView);
        }
        // Title
        holder.mTitleTxtView.setText(book.getVolumeInfo().getTitle());
        // Author
        if (book.getVolumeInfo().getAuthors() == null) {
            holder.mAuthorTxtView.setText(R.string.no_author);
        } else {
            holder.mAuthorTxtView.setText(TextUtils.join(", ", book.getVolumeInfo().getAuthors()));
        }
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mThumbnailImgView;
        private TextView mTitleTxtView, mAuthorTxtView;

        private Book book;

        private ViewHolder(View itemView) {
            super(itemView);
            mThumbnailImgView = (ImageView) itemView.findViewById(R.id.thumbnailImgView);
            mTitleTxtView = (TextView) itemView.findViewById(R.id.titleTxtView);
            mAuthorTxtView = (TextView) itemView.findViewById(R.id.authorTxtView);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    BookListActivity listActivity = (BookListActivity) mContext;
                    String query = listActivity.getQuery();
                    Intent i = BookDetailsActivity.createIntent(mContext, query, book);
                    // Shared element activity transition
                    Pair<View, String> p1 = Pair.create((View) mThumbnailImgView, "cover_shared");
                    Pair<View, String> p2 = Pair.create((View) mTitleTxtView, "title_shared");
                    Pair<View, String> p3 = Pair.create((View) mAuthorTxtView, "author_shared");
                    ActivityOptionsCompat options = ActivityOptionsCompat
                            .makeSceneTransitionAnimation(listActivity, p1, p2, p3);

                    mContext.startActivity(i, options.toBundle());
                }
            });
        }

        private void setBook(Book book) {
            this.book = book;
        }
    }
}
