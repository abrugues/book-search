package ch.abrugues.booksearch.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.abrugues.booksearch.R;
import ch.abrugues.booksearch.model.Book;

public class BookDetailsActivity extends AppCompatActivity {

    private static final String ACTIVITY_TITLE = "activityTitle";
    private static final String BOOK_DETAILS = "bookDetails";

    @BindView(R.id.coverImgView) ImageView coverImgView;
    @BindView(R.id.titleTxtView) TextView titleTxtView;
    @BindView(R.id.authorTxtView) TextView authorTxtView;
    @BindView(R.id.publisherTxtView) TextView publisherTxtView;
    @BindView(R.id.pagesTxtView) TextView pagesTxtView;
    @BindView(R.id.descriptionExpTxtView) ExpandableTextView descriptionExpTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        ButterKnife.bind(this);

        setTitle(getIntent().getExtras().getString(ACTIVITY_TITLE));

        // Get the book from the intent
        Book book = (Book) getIntent().getExtras().getSerializable(BOOK_DETAILS);

        // Populate UI elements
        // Image
        String imageUrl = null;
        if (book.getVolumeInfo().getImageLinks() != null) {
            imageUrl = book.getVolumeInfo().getImageLinks().getThumbnail();
        }
        if (imageUrl != null) {
            Picasso.with(this)
                    .load(imageUrl)
                    .into(coverImgView);
        }
        // Title
        titleTxtView.setText(book.getVolumeInfo().getTitle());
        // Author
        if (book.getVolumeInfo().getAuthors() == null) {
            authorTxtView.setText(R.string.no_author);
        } else {
            String by = getString(R.string.by);
            authorTxtView.setText(by + " " + TextUtils.join(", ", book.getVolumeInfo().getAuthors()));
        }
        // Publisher
        String publisherKey = getString(R.string.publisher);
        String publisherValue = book.getVolumeInfo().getPublisher();
        if (publisherValue == null) {
            publisherValue = getString(R.string.unknown);
        }
        publisherTxtView.setText(publisherKey + " " + publisherValue);
        // Pages
        String pagesKey = getString(R.string.pages);
        String pagesValue = book.getVolumeInfo().getPageCount();
        if (pagesValue == null) {
            pagesValue = getString(R.string.not_available);
        }
        pagesTxtView.setText(pagesKey + " " + pagesValue);
        // Description
        String description = book.getVolumeInfo().getDescription();
        if (description == null) {
            description = getString(R.string.not_available);
        }
        descriptionExpTxtView.setText(description);
    }

    public static Intent createIntent(Context context, String query, Book book) {
        Intent i = new Intent(context, BookDetailsActivity.class);
        i.putExtra(ACTIVITY_TITLE, query);
        i.putExtra(BOOK_DETAILS, book);
        return i;
    }
}
