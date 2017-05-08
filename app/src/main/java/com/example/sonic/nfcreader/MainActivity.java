package com.example.sonic.nfcreader;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    TextView textViewInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewInfo = (TextView)findViewById(R.id.textView_explanation);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this,
                    "NFC NOT supported on this devices!",
                    Toast.LENGTH_LONG).show();
            finish();
        } else if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this,
                    "NFC NOT Enabled!",
                    Toast.LENGTH_LONG).show();
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String action = intent.getAction();

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            Toast.makeText(this,
                    "onResume() - ACTION_TAG_DISCOVERED",
                    Toast.LENGTH_SHORT).show();

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (tag == null) {
                textViewInfo.setText("tag == null");
            } else {
//                String tagInfo = tag.toString() + "\n";

                String tagInfo = "";
                tagInfo += "\nTag Id: \n";
                // this code to get Tag ID length and Tag ID
                byte[] tagId = tag.getId();
//                tagInfo += "length = " + tagId.length + "\n";
                for (int i = 0; i < tagId.length; i++) {
                    tagInfo += Integer.toHexString(tagId[i] & 0xFF) + " ";
                }

//                Log.i("nfc", tagInfo);
//                tagInfo += "\n";

//                String[] techList = tag.getTechList();
//                tagInfo += "\nTech List\n";
//                tagInfo += "length = " + techList.length + "\n";
//                for (int i = 0; i < techList.length; i++) {
//                    tagInfo += techList[i] + "\n ";
//                }

//                textViewInfo.setText(tagInfo);
                textViewInfo.setText(ByteArrayToHexString(tagId));
            }
        } else {
            Toast.makeText(this,
                    "onResume() : " + action,
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        // disabling foreground dispatch:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            ((TextView) findViewById(R.id.textView_explanation)).setText(
                    "NFC Tag\n" +
                            ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)));
        }
        Log.i("nfc", "ID : " + intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
//        Log.i("nfc", "Tag : " + intent.getByteArrayExtra(NfcAdapter.EXTRA_TAG));
//        Log.i("nfc", "Msg : " + intent.getByteArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES));
    }

    private String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String out = "";
        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

}
