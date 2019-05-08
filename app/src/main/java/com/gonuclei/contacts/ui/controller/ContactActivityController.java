package com.gonuclei.contacts.ui.controller;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler;
import com.bluelinelabs.conductor.changehandler.VerticalChangeHandler;
import com.gonuclei.contacts.R;
import com.gonuclei.contacts.adapter.ContactAdapter;
import com.gonuclei.contacts.model.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ContactActivityController extends Controller implements ContactAdapter.RecyclerViewClickListener {
    private static final int REQUEST_READ_CONTACTS = 79;
    private static List<Contact> mobileArray;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_main, container, false);
        RecyclerView contactsRecyclerView = view.findViewById(R.id.rv_contacts);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRouter().pushController(RouterTransaction.with(
                        new ContactAddController())
                        .pushChangeHandler(new VerticalChangeHandler())
                        .popChangeHandler(new VerticalChangeHandler()));
            }
        });

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    mobileArray = getAllContacts();
                }
            };
            runnable.run();
        } else {
            requestPermission();
        }

        ContactAdapter contactAdapter = new ContactAdapter(getActivity(), mobileArray, this);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        contactsRecyclerView.setAdapter(contactAdapter);
        return view;
    }


    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.READ_CONTACTS)) {

        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.READ_CONTACTS)) {
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            mobileArray = getAllContacts();
                        }
                    };
                    runnable.run();
                } else {
                    // permission denied,Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private static final String[] PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
            ContactsContract.CommonDataKinds.Email.DATA,
    };


    private List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<>();
        ContentResolver cr = getApplicationContext().getContentResolver();
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, null, null, null);
        if (cursor != null) {
            try {
                final int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                String name = "", number = "";

                while (cursor.moveToNext()) {
                    Contact contact = new Contact();
                    name = cursor.getString(nameIndex);
                    number = cursor.getString(numberIndex);
                    contact.setDisplayName(name);
                    contact.setPhoneNumber(number);
                    contactList.add(contact);
                }
            } finally {
                cursor.close();
            }
        }

        return contactList;
    }

    @Override
    public void onClick(View view, int position) {
        getRouter().pushController(RouterTransaction.with(
                new ContactDetailController(mobileArray.get(position).getDisplayName(),
                        mobileArray.get(position).getPhoneNumber()))
                .pushChangeHandler(new HorizontalChangeHandler())
                .popChangeHandler(new HorizontalChangeHandler()));
    }
}
