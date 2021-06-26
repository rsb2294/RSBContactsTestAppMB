package com.nt.rsbcontactstestappmb.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nt.rsbcontactstestappmb.R;
import com.nt.rsbcontactstestappmb.dbhandler.Contact;
import com.nt.rsbcontactstestappmb.dbhandler.MyDbHandler;

import java.util.List;

public class RVFavouritesAdapter extends RecyclerView.Adapter<RVContactsAdapter.ViewHolder> {
    private List<Contact> listdata;
    Context context;
    MyDbHandler db;

    // RecyclerView recyclerView;
    public RVFavouritesAdapter(List<Contact> listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
        db = new MyDbHandler(context);
    }

    @Override
    public RVContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.contactlayout, parent, false);
        RVContactsAdapter.ViewHolder viewHolder = new RVContactsAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RVContactsAdapter.ViewHolder holder, int position) {
        String name = listdata.get(position).getName();
        holder.tv_name.setText(name);
        holder.tv_number.setText(listdata.get(position).getPhone());

        if (listdata.get(position).getByteBuffer() == null) {
            Log.v("null", "yes");
            holder.iv_contact.setVisibility(View.GONE);
            String s = String.valueOf(name.charAt(0));
            holder.tv_initial.setText(s);
            holder.tv_initial.setVisibility(View.VISIBLE);

        } else {
            Log.v("null", "No");
            Bitmap photo = getImage(listdata.get(position).getByteBuffer());
//            b1 = BitmapFactory.decodeByteArray(photo, 0, listdata.get(position).getPhoto().length());
            holder.iv_contact.setImageBitmap(photo);
            holder.tv_initial.setVisibility(View.GONE);
        }
        if (listdata.get(position).getFav() != 0) {
            holder.iv_fav.setImageResource(R.drawable.ic_fav_fill);
        }

        holder.iv_deleted.setVisibility(View.GONE);

        holder.iv_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact contact = new Contact();
                Log.v("fav", "0 " + position);
                holder.iv_fav.setImageResource(R.drawable.ic_fav);
                if (listdata.size() > 1){
                    upadateFav(listdata, 0, contact, position);
                    listdata.remove(listdata.get(position).getFav());
                }else {
                    upadateFav(listdata, 0, contact, position);
                   listdata.clear();
                }


            }
        });
    }

    private void upadateFav(List<Contact> listdata, int check, Contact contact, int position) {
        contact.setId(listdata.get(position).getId());
        contact.setName(listdata.get(position).getName());
        contact.setPhone(listdata.get(position).getPhone());
        contact.setByteBuffer(listdata.get(position).getByteBuffer());
        contact.setFav(check);
        contact.setDeleted(listdata.get(position).getDeleted());
        int i = db.updateContact(contact);
        Log.v("updateFav", " " + i);
        RVFavouritesAdapter.this.notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_contact, iv_deleted, iv_fav;
        public TextView tv_name, tv_number, tv_initial;


        public ViewHolder(View itemView) {
            super(itemView);
            this.iv_contact = (ImageView) itemView.findViewById(R.id.iv_contact);
            this.iv_fav = (ImageView) itemView.findViewById(R.id.iv_fav);
            this.iv_deleted = (ImageView) itemView.findViewById(R.id.iv_delete);
            this.tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            this.tv_number = (TextView) itemView.findViewById(R.id.tv_number);
            this.tv_initial = itemView.findViewById(R.id.tv_initial);

        }
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}