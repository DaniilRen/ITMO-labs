package model.local.localActions;

import view.View;

public class ExitAction implements LocalAction {
    public void apply(View view) {
        view.onDestroy();
    }
}
