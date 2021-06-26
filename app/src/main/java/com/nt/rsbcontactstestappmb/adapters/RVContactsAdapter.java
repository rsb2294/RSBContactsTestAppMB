package com.nt.rsbcontactstestappmb.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.nt.rsbcontactstestappmb.R;
import com.nt.rsbcontactstestappmb.dbhandler.Contact;
import com.nt.rsbcontactstestappmb.dbhandler.MyDbHandler;
import com.nt.rsbcontactstestappmb.utils.CustomDialogClass;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

public class RVContactsAdapter
        extends RecyclerView.Adapter<RVContactsAdapter.ViewHolder> {
    private List<Contact> listdata;
    Context context;
    MyDbHandler db;

    // RecyclerView recyclerView;
    public RVContactsAdapter(List<Contact> listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
        db = new MyDbHandler(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.contactlayout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.v("ContactList(" + position + "): ", "" + listdata.get(position));
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
            holder.iv_contact.setImageBitmap(photo);
            holder.tv_initial.setVisibility(View.GONE);
        }

        if (listdata.get(position).getFav() == 1) {
            holder.iv_fav.setImageResource(R.drawable.ic_fav_fill);
        } else {
            holder.iv_fav.setImageResource(R.drawable.ic_fav);
        }

        if (listdata.get(position).getDeleted() != 0) {
            holder.iv_deleted.setVisibility(View.GONE);
        } else {
            holder.iv_deleted.setVisibility(View.VISIBLE);
        }

        holder.iv_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact contact = new Contact();
                if (listdata.get(position).getFav() == 0) {
                    Log.v("fav", "1 " + position);
                    upadateFav(listdata, 1, contact, position);
                    Toast.makeText(context, "Added as favourite", Toast.LENGTH_SHORT).show();
                } else {
                    Log.v("fav", "0 " + position);
                    upadateFav(listdata, 0, contact, position);
                    Toast.makeText(context, "Removed from favourites", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.iv_deleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setMessage("Do you want to delete contact ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Contact contact = new Contact();
                                if (listdata.get(position).getFav() == 0) {
                                    holder.iv_deleted.setVisibility(View.GONE);
                                    listdata.remove(listdata.get(position).getFav());
                                    upadateDeleted(listdata, 1, contact, position);
                                    Toast.makeText(context, "Contact deleted.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Delete");
                alert.show();
            }
        });

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                CustomDialogClass cdd=new CustomDialogClass(context, listdata.get(position).getPhone());
                cdd.show();
            }
        });

//        if (photoList.containsKey(String.valueOf(listdata.get(position).getId()))){
//            String id = String.valueOf(listdata.get(position).getId());
//            Log.v("BITMAP", ""+photoList.get(id));
//            Bitmap photo = getImage(getBytes(photoList.get(id)));
////            b1 = BitmapFactory.decodeByteArray(photo, 0, listdata.get(position).getPhoto().length());
//            holder.iv_contact.setImageBitmap(photo);
//            holder.tv_initial.setVisibility(View.GONE);
//            updatePhoto(listdata, photo, position);
//        }

    }

    private void updatePhoto(List<Contact> listdata, Bitmap photo, int position) {
        Contact contact = new Contact();
        contact.setId(listdata.get(position).getId());
        contact.setName(listdata.get(position).getName());
        contact.setPhone(listdata.get(position).getPhone());
        contact.setByteBuffer(getBytes(photo));
        contact.setFav(listdata.get(position).getFav());
        contact.setDeleted(listdata.get(position).getDeleted());
        listdata.set(position, contact);
        int i = db.updateContact(contact);

        Log.v("updateFav", " " + i);
        Log.v("Updated: ", "" + contact);
        RVContactsAdapter.this.notifyItemChanged(position);
    }

    private void upadateFav(List<Contact> listdata, int check, Contact contact, int position) {
        contact.setId(listdata.get(position).getId());
        contact.setName(listdata.get(position).getName());
        contact.setPhone(listdata.get(position).getPhone());
        contact.setByteBuffer(listdata.get(position).getByteBuffer());
        contact.setFav(check);
        contact.setDeleted(listdata.get(position).getDeleted());
        listdata.set(position, contact);
        int i = db.updateContact(contact);

        Log.v("updateFav", " " + i);
        Log.v("Updated: ", "" + contact);
        RVContactsAdapter.this.notifyItemChanged(position);
    }

    private void upadateDeleted(List<Contact> listdata, int check, Contact contact, int position) {
        contact.setId(listdata.get(position).getId());
        contact.setName(listdata.get(position).getName());
        contact.setPhone(listdata.get(position).getPhone());
        contact.setByteBuffer(listdata.get(position).getByteBuffer());
        contact.setFav(listdata.get(position).getFav());
        contact.setDeleted(check);
        int i = db.updateContact(contact);
        Log.v("updateDeleted", " " + i);
        RVContactsAdapter.this.notifyItemRemoved(position);
        RVContactsAdapter.this.notifyItemRangeChanged(0, listdata.size());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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
        ConstraintLayout root;


        public ViewHolder(View itemView) {
            super(itemView);
            this.root = itemView.findViewById(R.id.root);
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

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        return stream.toByteArray();
    }
}