package com.kevinrothenberger.m_tags;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kevinrothenberger.m_tags.utils.API;
import com.kevinrothenberger.m_tags.utils.NfcUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class WriterActivity extends Activity {

    boolean writeMode = false;
    IntentFilter[] writeTagFilters;
    NfcAdapter nfcAdapter;
    PendingIntent nfcPendingIntent;
    AlertDialog alertDialog;

    LinearLayout linearLayout;

    int item_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writer);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        linearLayout = (LinearLayout) findViewById(R.id.itemsLinearLayout);
        loadItems();

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    private void enableNFCWriteMode() {
        writeMode = true;
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        writeTagFilters = new IntentFilter[] { tagDetected };
        nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, writeTagFilters, null);
    }

    private void disableNFCWriteMode() {
        writeMode = false;
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if(writeMode && NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if(NfcUtils.writeTag(NfcUtils.createNdefMessage(Integer.toString(item_id)), detectedTag)) {
                Toast.makeText(this, "Wrote NFC tag", Toast.LENGTH_LONG).show();
                //disableNFCWriteMode();
                alertDialog.dismiss();
            }
            else {
                Toast.makeText(this, "Failure", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void loadItems() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    JSONArray items = API.getMuseumItems(WriterActivity.this);

                    Message message = Message.obtain();
                    message.obj = items;

                    itemStreamHandler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }

    Handler itemStreamHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            JSONArray items = (JSONArray) msg.obj;

            if(items != null) {
                linearLayout.removeAllViews();

                for(int i = 0; i < items.length(); i++) {
                    try {
                        addViewToStream(getItemView(items.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            return false;
        }
    });

    private View getItemView(final JSONObject itemObject) {
        final LinearLayout layout = new LinearLayout(this);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(600, 100);
        layout.setLayoutParams(layoutParams);

        TextView itemName = new TextView(this);
        try {
            itemName.setText(itemObject.getString("Item_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        layout.addView(itemName);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    item_id = itemObject.getInt("Item_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                enableNFCWriteMode();
                alertDialog = new AlertDialog.Builder(WriterActivity.this).setTitle("Touch tag to write")
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                disableNFCWriteMode();
                            }
                        }).create();
                alertDialog.show();
            }
        });

        return layout;

    }

    private void addViewToStream(View view){
        linearLayout.addView(view);


        View seperatorLine = new View(this);
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        layoutParams.setMargins(30,30,30,30);
        seperatorLine.setLayoutParams(layoutParams);
        seperatorLine.setBackgroundColor(Color.rgb(180, 180, 180));
        linearLayout.addView(seperatorLine);
    }

}
