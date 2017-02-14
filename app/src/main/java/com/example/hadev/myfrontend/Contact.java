package com.example.hadev.myfrontend;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Hadev on 6-2-2017.
 */

public class Contact implements ViewObject {

    private String displayName;
    private String phoneNumber;

    public Contact(String displayName,String phoneNumber) {
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public View getListView(Context context) {
        View contactView= View.inflate(context, R.layout.contact_item, null);
        TextView namePlaceholder = (TextView) contactView.findViewById(R.id.name);
        namePlaceholder.setText(displayName);
        TextView phonePlaceholder = (TextView) contactView.findViewById(R.id.phone);
        phonePlaceholder.setText(phoneNumber);
        return contactView;
    }

    @Override
    public View getDetailView() {
        return null;
    }
}
