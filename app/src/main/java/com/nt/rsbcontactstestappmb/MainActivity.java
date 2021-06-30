package com.nt.rsbcontactstestappmb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nt.rsbcontactstestappmb.dbhandler.Contact;
import com.nt.rsbcontactstestappmb.dbhandler.MyDbHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;
    String contactNumber = null;
    String contactName = null;
    private static final int CALL_PERMISSION_CODE = 100;
    private static final int CONTACTS_PERMISSION_CODE = 101;

    private String strtxt;

    FrameLayout frameLayout;

    ProgressBar progressBar;
    TextView tv_progress;

//    HashMap<String, Bitmap> photoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.myProgress);
        tv_progress = findViewById(R.id.myTextProgress);
        frameLayout = findViewById(R.id.framelayout);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();
        }
    }

    public void favourites(View view) {

        frameLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        tv_progress.setVisibility(View.GONE);
        strtxt = "FAVOURITES";
        Fragment fragment = new ContactsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

    }

    public void deleted(View view) {

        frameLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        tv_progress.setVisibility(View.GONE);
        strtxt = "DELETED";
        Fragment fragment = new ContactsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
    }

    public boolean checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission, Manifest.permission.CALL_PHONE}, requestCode);
            return false;
        } else {
//            Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
            return true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == CONTACTS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Contacts Permission Granted", Toast.LENGTH_SHORT) .show();

            } else {
                Toast.makeText(MainActivity.this, "Please provide contacts permission to access feature", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == CALL_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Call Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Please provide permission to access feature", Toast.LENGTH_SHORT).show();
            }

        }


    }


    public void displayContacts(View view) {

        if (checkPermission(Manifest.permission.READ_CONTACTS, CONTACTS_PERMISSION_CODE)) {

                frameLayout.setVisibility(View.GONE);
                tv_progress.setText("0");
                progressBar.setVisibility(View.VISIBLE);
                tv_progress.setVisibility(View.VISIBLE);

                strtxt = "CONTACTS";
                int count = 0;
                MyDbHandler db = new MyDbHandler(MainActivity.this);
                Contact contact = new Contact();
                ContentResolver cr = getContentResolver();
                Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                        null, null, null, null);
                if (cur.getCount() > 0) {
                    while (cur.moveToNext()) {
                        contactID = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                        contactName = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        if (Integer.parseInt(cur.getString(
                                cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                            Cursor pCur = cr.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{contactID}, null);
                            try {
                                InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                                        ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactID)));
                                Bitmap b = BitmapFactory.decodeStream(inputStream);

                                Log.v("contactid :" + contactID, "bitmap: " + b);
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                                    while (pCur.moveToNext()) {
                                        contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                        if (!contactName.isEmpty()) {
                                            contact.setName(contactName);
                                        }

                                        if (!contactNumber.isEmpty()) {
                                            contact.setPhone(contactNumber);
                                        }

                                        if (b != null) {
                                            contact.setByteBuffer(getBytes(b));
                                            Log.v("contactid1 :" + contactID, "bitmap1: " + b);
                                        } else {
                                            contact.setByteBuffer(null);
                                        }
                                        contact.setFav(0);
                                        contact.setDeleted(0);
                                        db.addContact(contact);
                                        count++;

                                        tv_progress.setText("" + count);
                                    }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            pCur.close();
                        }
                    }
                }


                progressBar.setVisibility(View.GONE);
                tv_progress.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                Fragment fragment = new ContactsFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.framelayout, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        }

//    }


    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        return stream.toByteArray();
    }

    public Bundle getMyData() {
        Bundle hm = new Bundle();
        hm.putString("strtxt", strtxt);
//        hm.putSerializable("photolist", photoList);
        return hm;
    }

}