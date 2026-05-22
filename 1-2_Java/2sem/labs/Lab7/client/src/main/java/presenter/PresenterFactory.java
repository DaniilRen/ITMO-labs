package presenter;

import model.Model;
import view.View;

public class PresenterFactory {
    public static Presenter providePresenter(View view) {
        return new Presenter(new Model(view));
    }
}
