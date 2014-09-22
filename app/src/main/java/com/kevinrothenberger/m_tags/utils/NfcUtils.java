package com.kevinrothenberger.m_tags.utils;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;

import java.io.IOException;

/**
 * Created by kevinrothenberger on 9/21/14.
 */

public class NfcUtils {

    public static NdefMessage createNdefMessage(String text) {
        byte[] textBytes = text.getBytes();
        byte[] textPayload = new byte[textBytes.length + 3];
        textPayload[0] = 0x02; // 0x02 = UTF8
        textPayload[1] = 'e'; // Language = en
        textPayload[2] = 'n';
        System.arraycopy(textBytes, 0, textPayload, 3, textBytes.length);
        NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], textPayload);
        return new NdefMessage(new NdefRecord[] { record, NdefRecord.createApplicationRecord("com.kevinrothenberger.m_tags") });
    }

    public static boolean writeTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;

        try {
            Ndef ndef = Ndef.get(tag);
            if(ndef != null) {
                ndef.connect();
                if(!ndef.isWritable()) {
                    return false;
                }
                if(ndef.getMaxSize() < size) {
                    return false;
                }
                ndef.writeNdefMessage(message);
                return true;
            } else {
                NdefFormatable formatable = NdefFormatable.get(tag);
                if(formatable != null) {
                    try {
                        formatable.connect();
                        formatable.format(message);
                        return true;
                    } catch (IOException e) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }

}
