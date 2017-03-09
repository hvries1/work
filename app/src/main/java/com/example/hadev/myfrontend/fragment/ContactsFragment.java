package com.example.hadev.myfrontend.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.data.ContactProvider;
import com.example.data.SimpleDBAdapter;
import com.example.hadev.myfrontend.R;
import com.example.hadev.myfrontend.domain.Contact;

public class ContactsFragment extends Fragment {

   /**
    * Called when the activity is first created.
    */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_contacts, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        LinearLayout contactView = (LinearLayout) this.getView().findViewById(R.id.contactview);

        Cursor cursor = getStoredContacts();
        while (cursor.moveToNext()) {
            String displayName = cursor.getString(cursor.getColumnIndex(ContactProvider.NAME));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactProvider.PHONE));
            contactView.addView(new Contact(displayName, phoneNumber, "").getListView(this.getContext()));
            //contactView.addView(new Contact("auth", this.getContext().getText(R.string.auth).toString()).getListView(this.getContext()));
        }

        try {
           new AsyncTask<String, Integer, Contact[]>() {
                @Override
                protected void onPreExecute(){
                    //Setup is done here
                }

                @Override
                protected Contact[] doInBackground(String... params) {
                    return SimpleDBAdapter.getStoredContacts();
                }

                @Override
                protected void onProgressUpdate(Integer... params){
                    //Update a progress bar here, or ignore it, it's up to you
                }

                @Override
                protected void onPostExecute(Contact[] contacts){
                    if (contacts != null) {
                        LinearLayout contactView = (LinearLayout) ContactsFragment.this.getView().findViewById(R.id.contactview);
                        for (Contact contact : contacts) {
                            contactView.addView(contact.getListView(ContactsFragment.this.getContext()));
                        }
                    }
                }

                @Override
                protected void onCancelled(){
                    // Handle what you want to do if you cancel this task
                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Cursor getContacts() {
        // Retrieve contacts from ContactsContract
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[] { ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME };
        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '" + ("1") + "'";
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        return getActivity().getContentResolver().query(uri, projection,
                selection, selectionArgs, sortOrder);
    }

    private Cursor getStoredContacts() {
        // Retrieve contacts from ContactsContract
        Uri uri = ContactProvider.CONTENT_URI;
        String[] projection = new String[] { ContactProvider._ID, ContactProvider.NAME, ContactProvider.PHONE };
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = ContactProvider.NAME + " COLLATE LOCALIZED ASC";

        return getActivity().getContentResolver().query(uri, projection,
                selection, selectionArgs, sortOrder);
    }
}
