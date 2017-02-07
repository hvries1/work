package com.example.hadev.myfrontend;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Hadev on 6-2-2017.
 */

public class Contact implements ViewObject {

    private String displayName;

    public Contact(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public View getListView(Context context) {
        TextView contactView = new TextView(context);
        contactView.append("Name: ");
        contactView.append(displayName);
        contactView.append("\n");
        return contactView;
    }

    @Override
    public View getDetailView() {
        return null;
    }
}
