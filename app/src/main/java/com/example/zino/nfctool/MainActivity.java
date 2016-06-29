package com.example.zino.nfctool;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String TAG = this.getClass().getName();
    ViewPager viewPager;
    MyPagerAdapter myPagerAdapter;
    NfcAdapter nfcAdapter; //개발자가 하드웨어 칩을 직접 제어할 수 없으므로, NfcAdapter를 통해
    //간접 제어할 수 있다..

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(myPagerAdapter);

        /*
        시스템이 TAG를 읽어들이면, Intent에 담아서 브로드케스팅하게 되는데, 이때
         Intent를 받이들이는 설정을 Filter 로 등록한 앱의 액티비티를 호출하게 되어 있다...
         따라서 onCreate 에서 getIntent()를 이용하여, 시스템이 전달한 Intent안에 들어있는
         TAG정보를 얼마든지 얻어올 수는 있다...그런데 문제는???
        */

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "this device is not supported NFC!!!", Toast.LENGTH_SHORT).show();
        }

        Log.d(TAG, "onCreate에서의 MainActivity is " + this);
    }


    @Override
    protected void onResume() {
        super.onResume();

        //액티비티가 스택구조로 쌓이는 액티비티 Task에 이미 동일한 주소값의 액티비티가 존재하면
        //중복해서 인스턴스를 등록하지 말자!!
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        Log.d(TAG, "onResume에서의 MainActivity is " + this);

        //NfcAdapter의 메서드중, TAG를 읽어들이려는 액티비티로 등록하는 메서드가 있다.
        //이 메서드 호출 이후에는 현재의 액티비티로 시스템의 Intent 정보를 곧바로 최우선하여 받는다!!
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] filters = new IntentFilter[]{};

        //현재 화면에 활성화된 액티비티가 TAG 정보를 받을 수 있도록...
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, null);
    }

    /*
    * 현재 forground로 떠있는 액티비티가 시스템으로 부터 INtent를 전달받으려면,
    * 평소와는 틑리게 onNewIntent에 전달되는 인수를 이용해야 한다!!
    * */
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        //현재 사용자가 쓰기모드인지, 읽기모드인지부터 판단하자!!
        //왜?? 시스템은 태그를 Activity에 전달하므로...
        int currentItem = viewPager.getCurrentItem();

        if (currentItem == 0) {
            Toast.makeText(this, "읽기모드네요!!", Toast.LENGTH_SHORT).show();

            ReadFragment readFragment=(ReadFragment)myPagerAdapter.getItem(0);
            if(readFragment.isWaiting){
                readTag(intent);
           }
        } else if (currentItem == 1) {
            Toast.makeText(this, "쓰기모드네요!!", Toast.LENGTH_SHORT).show();
        }


    }

    //태그읽기 메서드
    public void readTag(Intent intent) {
        //Intent에는 일반적으로 자바의 기본자료형만을 담을수 있다. 따라서 객체를 담기위해서는
        //Parcelable로 담고, 얻어와야 한다..
        Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        for (int i = 0; i < parcelables.length; i++) {
            NdefMessage ndefMessage = (NdefMessage) parcelables[i];

            NdefRecord[] records = ndefMessage.getRecords();
            for (int a = 0; a < records.length; a++) {
                NdefRecord ndefRecord = records[a];

                String data = decode(ndefRecord.getPayload());
                Log.d(TAG, data);

                //ReadFragment에 들어있는 txt_data에 읽혀진 데이터를 출력하자!!
                ReadFragment readFragment=(ReadFragment)myPagerAdapter.getItem(0);
                readFragment.txt_data.setText(data);
                readFragment.isWaiting=false;
                readFragment.bar.setVisibility(View.GONE); //다시 않보이도록 전환
            }
        }
    }

    public String decode(byte[] buf) {
        String strText = "";
        String textEncoding = ((buf[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
        int langCodeLen = buf[0] & 0077;

        try {
            strText = new String(buf, langCodeLen + 1, buf.length - langCodeLen - 1, textEncoding);
        } catch (Exception e) {
            Log.d("tag1", e.toString());
        }
        return strText;
    }

    public void btnClick(View view) {
        if (view.getId() == R.id.bt_read) {
            viewPager.setCurrentItem(0); //읽기용 프레그먼트로 이동
        } else if (view.getId() == R.id.bt_write) {
            viewPager.setCurrentItem(1); //쓰기용 프레그먼트로 이동
        }
    }
}






