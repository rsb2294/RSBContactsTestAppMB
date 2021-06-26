package com.nt.rsbcontactstestappmb;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nt.rsbcontactstestappmb.adapters.RVContactsAdapter;
import com.nt.rsbcontactstestappmb.adapters.RVDeletedAdapter;
import com.nt.rsbcontactstestappmb.adapters.RVFavouritesAdapter;
import com.nt.rsbcontactstestappmb.dbhandler.Contact;
import com.nt.rsbcontactstestappmb.dbhandler.MyDbHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactsFragment extends Fragment {

    RecyclerView rv_contacts;
    String strtext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        MainActivity activity = (MainActivity) getActivity();

        Bundle results = activity.getMyData();
        strtext = results.getString("strtxt");
        Log.v("strtext", ""+strtext);

        MyDbHandler db = new MyDbHandler(getActivity());



        switch (strtext) {
            case "CONTACTS":
                List<Contact> contactList = db.getAllContacts();
                rv_contacts = view.findViewById(R.id.rv_contacts);
                RVContactsAdapter contactsAdapter = new RVContactsAdapter(contactList, getActivity());
                rv_contacts.setHasFixedSize(true);
                rv_contacts.setLayoutManager(new LinearLayoutManager(getActivity()));
                rv_contacts.setAdapter(contactsAdapter);
                break;

            case "FAVOURITES":
                List<Contact> favContactList = db.getFavContacts();
                rv_contacts = view.findViewById(R.id.rv_contacts);
                RVFavouritesAdapter favouritesAdapter = new RVFavouritesAdapter(favContactList, getActivity());
                rv_contacts.setHasFixedSize(true);
                rv_contacts.setLayoutManager(new LinearLayoutManager(getActivity()));
                rv_contacts.setAdapter(favouritesAdapter);
                break;

            case "DELETED":
                List<Contact> deletedContactList = db.getDeletedContacts();
                rv_contacts = view.findViewById(R.id.rv_contacts);
                RVDeletedAdapter deletedAdapter = new RVDeletedAdapter(deletedContactList, getActivity());
                rv_contacts.setHasFixedSize(true);
                rv_contacts.setLayoutManager(new LinearLayoutManager(getActivity()));
                rv_contacts.setAdapter(deletedAdapter);
                break;
        }

        return view;
    }
}