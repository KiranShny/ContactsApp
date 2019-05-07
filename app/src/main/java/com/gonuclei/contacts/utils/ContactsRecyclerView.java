package com.gonuclei.contacts.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gonuclei.contacts.adapter.ContactAdapter;
import com.gonuclei.contacts.model.Contact;

import java.util.List;

@SuppressLint("ViewConstructor")
public class ContactsRecyclerView extends RecyclerView {
    public ContactsRecyclerView(@NonNull Context context, List<Contact> contactList) {
        super(context);
        this.setLayoutManager(new LinearLayoutManager(context));
        this.setAdapter(new ContactAdapter(context,contactList));
    }

    public ContactsRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, List<Contact> contactList) {
        super(context, attrs);
        this.setLayoutManager(new LinearLayoutManager(context));
        this.setAdapter(new ContactAdapter(context,contactList));
    }

    public ContactsRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle, List<Contact> contactList) {
        super(context, attrs, defStyle);
        this.setLayoutManager(new LinearLayoutManager(context));
        this.setAdapter(new ContactAdapter(context,contactList));
    }

    public void notifyDatasetChanged(){
        Adapter adapter = this.getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
