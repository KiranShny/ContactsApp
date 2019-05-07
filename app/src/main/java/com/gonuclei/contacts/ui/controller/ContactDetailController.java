package com.gonuclei.contacts.ui.controller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bluelinelabs.conductor.Controller;
import com.gonuclei.contacts.R;
import com.gonuclei.contacts.utils.BundleBuilder;
import com.google.android.material.snackbar.Snackbar;

public class ContactDetailController extends Controller {
    private static final String KEY_NAME = "CityDetailController.title";
    private static final String KEY_PHONE = "CityDetailController.phone";
    private static final int REQUEST_READ_CONTACTS = 222;
    private TextView name, phone;
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
        displayName= getArgs().getString(KEY_NAME);
        phoneNumber= getArgs().getString(KEY_PHONE);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_contact_detail, container, false);
        name = view.findViewById(R.id.tv_contact_detail_name);
        mContactPicture = view.findViewById(R.id.img_contact_detail_picture);
        phone = view.findViewById(R.id.tv_contact_detail_number);
        mCallLayout = view.findViewById(R.id.layout_call_button);
        requestPermission();
        ColorGenerator generator = ColorGenerator.MATERIAL;
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(displayName.substring(0,1), generator.getRandomColor());
        mContactPicture.setImageDrawable(drawable);

        TextView name = view.findViewById(R.id.tv_contact_detail_name);
        TextView phone = view.findViewById(R.id.tv_contact_detail_number);
        backImage=view.findViewById(R.id.img_back_button);
        name.setText(displayName);
        phone.setText(phoneNumber);

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


}
