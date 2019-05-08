package com.gonuclei.contacts.ui.controller;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bluelinelabs.conductor.Controller;
import com.gonuclei.contacts.R;
import com.gonuclei.contacts.utils.BundleBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.io.InputStream;

public class ContactDetailController extends Controller {
    private static final String KEY_NAME = "CityDetailController.title";
    private static final String KEY_PHONE = "CityDetailController.phone";
    private static final int REQUEST_READ_CONTACTS = 222;
    private TextView name, phone, email;
    private ImageView mContactPicture;
    private RelativeLayout mCallLayout;
    private ImageView backImage;
    private String displayName;
    private String phoneNumber;


    public ContactDetailController(String displayName, String phoneNumber) {
        this(new BundleBuilder(new Bundle())
                .putString(KEY_NAME, displayName)
                .putString(KEY_PHONE, phoneNumber)
                .build());
    }

    public ContactDetailController(Bundle args) {
        super(args);
        displayName = getArgs().getString(KEY_NAME);
        phoneNumber = getArgs().getString(KEY_PHONE);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_contact_detail, container, false);
        name = view.findViewById(R.id.tv_contact_detail_name);
        mContactPicture = view.findViewById(R.id.img_contact_detail_picture);
        phone = view.findViewById(R.id.tv_contact_detail_number);
        email = view.findViewById(R.id.contact_detail_email);
        mCallLayout = view.findViewById(R.id.layout_call_button);
        requestPermission();

        Bitmap bitmap = retrieveContactPhoto(getApplicationContext(), phoneNumber);
        if (bitmap != null) {
            RoundedBitmapDrawable drawable =
                    RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), bitmap);
            drawable.setCircular(true);
            mContactPicture.setImageDrawable(drawable);
        } else {
            mContactPicture.setImageDrawable(getDrawable());
        }

        TextView name = view.findViewById(R.id.tv_contact_detail_name);
        TextView phone = view.findViewById(R.id.tv_contact_detail_number);
        backImage = view.findViewById(R.id.img_back_button);
        name.setText(displayName);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareContact();
            }
        });
        phone.setText(phoneNumber);
        email.setText(getNameEmailDetails(phoneNumber));

        mCallLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(callIntent);
            }
        });

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRouter().popToRoot();
            }
        });
        return view;
    }

    public String getNameEmailDetails(String number) {
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        String contactId = null;
        String email="";
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID};

        Cursor cursor =
                contentResolver.query(
                        uri,
                        projection,
                        null,
                        null,
                        null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
            }
            cursor.close();
        }
        String filter = ContactsContract.CommonDataKinds.Email.DATA + " NOT LIKE ''";
        ContentResolver cr = getApplicationContext().getContentResolver();
        Cursor cur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
        if(cur!=null) {
            while (cur.moveToNext()) {
                 email =cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            }
            cur.close();
        }
        return email;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {

        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CALL_PHONE},
                    REQUEST_READ_CONTACTS);
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.CALL_PHONE)) {
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CALL_PHONE},
                    REQUEST_READ_CONTACTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull
            String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                mCallLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(v, "Call Function wont work as Permissions were denied", Snackbar.LENGTH_LONG);
                        requestPermission();
                    }
                });
            }
        }
    }

    private Bitmap retrieveContactPhoto(Context context, String number) {
        ContentResolver contentResolver = context.getContentResolver();
        String contactId = null;
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID};

        Cursor cursor =
                contentResolver.query(
                        uri,
                        projection,
                        null,
                        null,
                        null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
            }
            cursor.close();
        }

        Bitmap photo = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ic_launcher_background);

        InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
                ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(contactId)));

        if (inputStream != null) {
            photo = BitmapFactory.decodeStream(inputStream);
        }

        return photo;
    }

    private TextDrawable getDrawable() {
        ColorGenerator generator = ColorGenerator.MATERIAL;
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(displayName.substring(0, 1), generator.getRandomColor());
        return drawable;
    }


    public void shareContact() {
        Cursor cur = getApplicationContext().getContentResolver().
                query(ContactsContract.Contacts.CONTENT_URI,
                        new String[]{ContactsContract.Contacts.LOOKUP_KEY},
                        ContactsContract.Contacts._ID, null, null);
        if (cur.moveToFirst()) {
            String lookupKey = cur.getString(0);
            Uri vcardUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType(ContactsContract.Contacts.CONTENT_VCARD_TYPE);
            intent.putExtra(Intent.EXTRA_STREAM, vcardUri);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Bob Dylan"); // put the name of the contact here
            startActivity(intent);
        }
    }

}
