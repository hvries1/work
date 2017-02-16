package com.example.hadev.myfrontend.domain;

import android.content.Context;
import android.view.View;

/**
 * Created by Hadev on 6-2-2017.
 */

public interface ViewObject {
    View getListView(Context context);
    View getDetailView();
}
