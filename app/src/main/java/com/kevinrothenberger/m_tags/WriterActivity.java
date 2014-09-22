package com.kevinrothenberger.m_tags;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kevinrothenberger.m_tags.utils.NfcUtils;


public class WriterActivity extends Activity {

    EditText nfc_text;
    Button write_nfc_button;

    boolean writeMode = false;
    IntentFilter[] writeTagFilters;
    NfcAdapter nfcAdapter;
    PendingIntent nfcPendingIntent;
    AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writer);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        nfc_text = (EditText) findViewById(R.id.nfc_text);
        write_nfc_button = (Button) findViewById(R.id.write_nfc_button);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        write_nfc_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            if(NfcUtils.writeTag(NfcUtils.createNdefMessage(nfc_text.getText().toString()), detectedTag)) {
                Toast.makeText(this, "Wrote NFC tag", Toast.LENGTH_LONG).show();
                //disableNFCWriteMode();
                alertDialog.dismiss();
            }
            else {
                Toast.makeText(this, "Failure", Toast.LENGTH_LONG).show();
            }
        }
    }

}
