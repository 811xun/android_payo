package com.home.glx.uwallet.tools;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Parcelable;
import android.util.Log;

import com.yzq.zxinglibrary.android.CaptureActivity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class NfcUtils {

    /**
     * 构造函数，用于初始化nfc
     */
    public NfcUtils(Activity activity) {
        CaptureActivity.mNfcAdapter = NfcCheck(activity);
        NfcInit(activity);
    }

    /**
     * 检查NFC是否打开
     */
    public static NfcAdapter NfcCheck(Activity activity) {
        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(activity);
        if (mNfcAdapter == null) {
            Log.d("22222", "NfcCheck: ");
            return null;
        } else {
            Log.d("3333", "NfcCheck: " + mNfcAdapter.isEnabled());
            if (!mNfcAdapter.isEnabled()) {
                Log.d("44444", "NfcCheck: " + mNfcAdapter.isEnabled());
//                Intent setNfc = new Intent(Settings.ACTION_NFC_SETTINGS);
//                activity.startActivity(setNfc);
            }
        }
        return mNfcAdapter;
    }

    /**
     * 初始化nfc设置
     */
    public static void NfcInit(Activity activity) {
        CaptureActivity.mPendingIntent = PendingIntent.getActivity(activity, 0,
                new Intent(activity, activity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
      /*  if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            CaptureActivity.mPendingIntent = PendingIntent.getActivity(activity, 0,
                    new Intent(activity, activity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_IMMUTABLE);
        } else {
            CaptureActivity.mPendingIntent = PendingIntent.getActivity(activity, 0,
                    new Intent(activity, activity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_ONE_SHOT);
        }*/
        IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter filter2 = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        try {
            filter.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }
        CaptureActivity.mIntentFilter = new IntentFilter[]{filter, filter2};
        CaptureActivity.mTechList = null;
    }

    /**
     * 读取NFC的数据
     */
    public static String readNFCFromTag(Intent intent) throws UnsupportedEncodingException {
        Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawArray != null) {
            NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
            NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
            if (mNdefRecord != null) {
                String readResult = new String(mNdefRecord.getPayload(), "UTF-8");
                return readResult;
            }
        }
        return "";
    }


    /**
     * 往nfc写入数据
     */
    public static void writeNFCToTag(String data, Intent intent) throws IOException, FormatException {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Ndef ndef = Ndef.get(tag);
        ndef.connect();
        NdefRecord ndefRecord = NdefRecord.createTextRecord(null, data);
//        NdefRecord[] records = {ndefRecord};
        NdefRecord[] records = new NdefRecord[]{NdefRecord
                .createApplicationRecord("com.home.glx.uwallet")};
        NdefMessage ndefMessage = new NdefMessage(records);
        ndef.writeNdefMessage(ndefMessage);
    }

    /**
     * 读取nfcID
     */
    public static String readNFCId(Intent intent) throws UnsupportedEncodingException {
        try {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String id = ByteArrayToHexString(tag.getId());
            return id;
        } catch (Exception e) {
            return "ID读取失败";
        }
    }

    /**
     * 将字节数组转换为字符串
     */
    public static String ByteArrayToHexString(byte[] inarray) {
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


    /**
     * 检查手机是否支持NFC
     *
     * @param context
     * @return
     */
    public static boolean nfcPhone(Context context) {
        PackageManager packageManager = context.getPackageManager();
        boolean b1 = packageManager
                .hasSystemFeature(PackageManager.FEATURE_NFC);
//        Toast.makeText(context, "是否支持nfc===" + b1, Toast.LENGTH_SHORT).show();
        return b1;
    }

}
