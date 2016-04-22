package com.example.tomsdeath.message;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.tomsdeath.message.Adapter.MessageRecyclerViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.message_list_view) RecyclerView rv;
    @Bind(R.id.button) FloatingActionButton button;
    @Bind(R.id.toolbar) Toolbar toolbar;
    public static final Uri SMS_URI = Uri.parse("content://sms/");
    public static final String EXTRA_PHONE_NUMBER = "phone_number";
    public static final String NUMBER = "number";
    public static final String _ID = "_id";
    public static final String ADDRESS = "address";
    public static final String PERSON = "person";
    public static final String BODY = "body";
    public static final String DATE = "date";
    public static final String TYPE = "type";
    private static final String[] ALL_PROJECTS = new String[] {_ID, ADDRESS, PERSON, BODY, DATE, TYPE};
    String address;
    String body;
    MessageRecyclerViewAdapter smsAdapter;
    List<Map<String, Object>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initRV();
        initSMSAdapter();
        button.setOnClickListener(this);
    }

    private void initSMSAdapter() {
        smsAdapter = new MessageRecyclerViewAdapter(getPhoneData(), MainActivity.this);
        smsAdapter.notifyDataSetChanged();
        smsAdapter.setOnItemClickListener(getItemClickListener());
    }

    private void initRV() {
        rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        rv.setAdapter(smsAdapter);
        rv.setItemAnimator(new DefaultItemAnimator());
    }

    @NonNull
    private MessageRecyclerViewAdapter.ItemClickListener getItemClickListener() {
        return new MessageRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this, SendActivity.class);
                intent.putExtra(EXTRA_PHONE_NUMBER, list.get(position).get(NUMBER).toString());
                startActivity(intent);
            }
        };
    }

    public List<Map<String, Object>> getPhoneData() {
        list = new ArrayList<>();
        Cursor cursor = getContentResolver().query(SMS_URI, ALL_PROJECTS, null, null, "date desc");
        try {
            while (cursor.moveToNext()) {
                Map<String, Object> map = new HashMap<>();
                int index_address = cursor.getColumnIndex(ADDRESS);
                int index_person = cursor.getColumnIndex(PERSON);
                int index_body = cursor.getColumnIndex(BODY);
                int index_state = cursor.getColumnIndex(TYPE);
                int index_date = cursor.getColumnIndex(DATE);

                address = cursor.getString(index_address);
                cursor.getInt(index_person);
                body = cursor.getString(index_body);
                int status = cursor.getInt(index_state);
                long data = cursor.getLong(index_date);
                Date date = new Date(data);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd hh:mm:ss");
                String str_Date = simpleDateFormat.format(date);

                map.put(NUMBER, address);
                map.put(BODY ,body);
                map.put(DATE, str_Date);
                if (status == 1) {
                    map.put(TYPE, "received");
                } else {
                    map.put(TYPE, "send");
                }
                list.add(map);
            }
            if(!cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                startActivity(new Intent(this, SendActivity.class));
                break;
            default:
                break;
        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            return false;
        }
    }
}
