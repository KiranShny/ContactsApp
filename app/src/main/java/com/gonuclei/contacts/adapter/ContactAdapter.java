package com.gonuclei.contacts.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.gonuclei.contacts.R;
import com.gonuclei.contacts.model.Contact;

import java.util.List;


public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactsViewHolder> {

    private List<Contact> contactList;
    private Context context;
    ColorGenerator generator = ColorGenerator.MATERIAL;

    public ContactAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        holder.mContactNameView.setText(contactList.get(position).getDisplayName());
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(contactList.get(position).getDisplayName().substring(0,1), generator.getRandomColor());
        holder.mContactImageView.setImageDrawable(drawable);
        holder.mContactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //transition to detail Controller
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder {
        ImageView mContactImageView;
        TextView mContactNameView;
        LinearLayout mContactLayout;

        ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            mContactImageView = itemView.findViewById(R.id.img_contact_picture);
            mContactNameView = itemView.findViewById(R.id.tv_contact_name);
            mContactLayout = itemView.findViewById(R.id.layout_contact);
        }
    }
}
