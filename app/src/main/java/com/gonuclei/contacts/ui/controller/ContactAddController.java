package com.gonuclei.contacts.ui.controller;

import android.Manifest;
import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.provider.ContactsContract;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.bluelinelabs.conductor.Controller;
import com.gonuclei.contacts.R;

import java.util.ArrayList;

public class ContactAddController extends Controller {
    private static final int REQUEST_READ_CONTACTS = 763;
    private EditText mNameEditText;
    private EditText mNumberEditText;
    private ImageView mBackButton;
    private TextView mSaveTextView;
    private static final String TAG = "ContactAddController";

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_add_contacts, container, false);
        mNameEditText = view.findViewById(R.id.et_name);
        mNumberEditText = view.findViewById(R.id.et_number);
        mBackButton = view.findViewById(R.id.img_clear_button);
        mSaveTextView = view.findViewById(R.id.tv_save_btn);
        requestPermission();
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRouter().popToRoot();
            }
        });
        mSaveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable name = mNameEditText.getText();
                Editable phone = mNumberEditText.getText();

                if (name != null && phone != null && !phone.toString().equals("") && !name.toString().equals("")) {
                    ArrayList<ContentProviderOperation> ops = new ArrayList<>();
                    ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI).withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null).withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

                    ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,mNameEditText.getText().toString()).build());

                    ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,mNumberEditText.getText().toString()).withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());
                    try {
                        getActivity().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                        getRouter().popToRoot();
                    } catch (Exception e) {
                        Log.e(TAG, "onClick: "+"Error Occoured" );
                        e.printStackTrace();
                    }
                }
            }
        });

        return view;
    }
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_CONTACTS)) {

        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_CONTACTS)) {
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }
    }
}
