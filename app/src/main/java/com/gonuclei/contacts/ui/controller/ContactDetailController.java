package com.gonuclei.contacts.ui.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bluelinelabs.conductor.Controller;
import com.gonuclei.contacts.R;
import com.gonuclei.contacts.utils.BundleBuilder;

public class ContactDetailController extends Controller {
    private static final String KEY_NAME = "CityDetailController.title";
    private static final String KEY_PHONE = "CityDetailController.phone";
    private TextView name, phone;
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
        displayName= getArgs().getString(KEY_PHONE);
        phoneNumber= getArgs().getString(KEY_NAME);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_contact_detail, container, false);
        name = view.findViewById(R.id.tv_contact_detail_name);
        phone = view.findViewById(R.id.tv_contact_detail_number);
        name.setText(displayName);
        phone.setText(phoneNumber);
        return view;
    }

}
