package ch.abrugues.booksearch.dagger;

import ch.abrugues.booksearch.ui.BookListActivity;
import dagger.Component;

@Component(modules = NetworkModule.class)
public interface ApplicationComponent {

    void inject(BookListActivity activity);

}
