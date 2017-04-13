package mree.cloud.music.player.app.views;

import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Created by eercan on 08.12.2016.
 */

public abstract class AbstractView {
    protected Context context;
    protected AlertDialog.Builder builder;
    protected AlertDialog dialog;

    public AbstractView(Context context) {
        this.context = context;
    }

    abstract void init();

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public AlertDialog.Builder getBuilder() {
        return builder;
    }

    public void setBuilder(AlertDialog.Builder builder) {
        this.builder = builder;
    }

    public AlertDialog getDialog() {
        return dialog;
    }

    public void setDialog(AlertDialog dialog) {
        this.dialog = dialog;
    }
}
