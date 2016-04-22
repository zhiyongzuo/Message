package com.example.tomsdeath.message;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SendActivity extends Activity implements View.OnClickListener {

    @Bind(R.id.number) EditText number;
    @Bind(R.id.text_edit) EditText text;
    @Bind(R.id.floating_action_button_send) FloatingActionButton send;
    @Bind(R.id.add_contacts) ImageButton add_contacts;
    @Bind(R.id.name) TextView name;
    private static final int REQUEST_CODE = 1;
    String snumber = "";
    String stext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        ButterKnife.bind(this);

        add_contacts.setOnClickListener(this);
        snumber = getIntent().getStringExtra(MainActivity.EXTRA_PHONE_NUMBER);
        if (snumber!=null) {
            number.setText(snumber);
            add_contacts.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floating_action_button_send:
                stext = text.getText().toString();
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(snumber, null, stext, null, null);
                ContentValues values = new ContentValues();
                values.put(MainActivity.ADDRESS, snumber);
                values.put(MainActivity.BODY, stext);
                values.put(MainActivity.TYPE, 1);
                values.put(MainActivity.DATE, System.currentTimeMillis());
                getContentResolver().insert(MainActivity.SMS_URI, values);
                text.setText("");
                break;
            case R.id.add_contacts:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode) {
            case REQUEST_CODE:
                if(data!=null) {
                    Uri uri = data.getData();
                    CursorLoader cl = new CursorLoader(SendActivity.this, uri, null, null, null, null);
                    Cursor cursor = cl.loadInBackground();
                    if(cursor.moveToFirst()) {
                        int _id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        String nameStr = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        name.setText(nameStr);
                        Cursor cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.Contacts._ID + "=", new String[]{String.valueOf(_id)}, null);
                        while (cursor2.moveToNext()) {
                            number.setText(cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                        }
                        cursor2.close();
                    }
                    cursor.close();
                }
                break;
            default:
                break;
        }
    }
}
