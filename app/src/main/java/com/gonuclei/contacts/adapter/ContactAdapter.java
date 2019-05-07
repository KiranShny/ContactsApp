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
    private RecyclerViewClickListener mlisner;

    public ContactAdapter(Context context, List<Contact> contactList, RecyclerViewClickListener recyclerViewClickListener) {
        this.context = context;
        this.mlisner = recyclerViewClickListener;
        this.contactList = contactList;
    }

    public interface RecyclerViewClickListener {

        void onClick(View view, int position);
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactsViewHolder(itemView, mlisner);
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactsViewHolder holder, final int position) {
        holder.mContactNameView.setText(contactList.get(position).getDisplayName());
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(contactList.get(position).getDisplayName().substring(0,1), generator.getRandomColor());
        holder.mContactImageView.setImageDrawable(drawable);
        holder.mContactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlisner.onClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RecyclerViewClickListener mListener;
        ImageView mContactImageView;
        TextView mContactNameView;
        LinearLayout mContactLayout;

        ContactsViewHolder(@NonNull View itemView, RecyclerViewClickListener recyclerViewClickListener) {
            super(itemView);
            mListener = recyclerViewClickListener;
            mContactImageView = itemView.findViewById(R.id.img_contact_picture);
            mContactNameView = itemView.findViewById(R.id.tv_contact_name);
            mContactLayout = itemView.findViewById(R.id.layout_contact);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }
}
