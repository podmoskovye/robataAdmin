package com.example.dcamo.splash.support;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.dcamo.splash.R;
import com.example.dcamo.splash.main.BookingActivity;
import com.example.dcamo.splash.main.MainActivity;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.example.dcamo.splash.main.MainActivity.ip;

public class SimpleScannerActivity extends  Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_simple_scanner);


        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(final Result rawResult) {
        final String result;
        result = rawResult.getText();
        Toast.makeText(this, "Contents = " + rawResult.getText() + ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();

        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isOnline()){
                    String myUrl = ip + "/admin/booking/check?userId=" + result.substring(0, 16) + "&id=" + result.substring(20, result.length());
                    //String to place our result in
                    String result;
                    //Instantiate new instance of our class
                    HttpGetRequest getRequest = new HttpGetRequest();
                    try {
                        result = getRequest.execute(myUrl).get();
                        if(result == null){
                            Toast.makeText(getBaseContext(), R.string.no_server_connection, Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getBaseContext(), "Ok", Toast.LENGTH_SHORT).show();
//                            JSONObject dataJsonObj = new JSONObject(result);
//                            JSONArray arr = dataJsonObj.getJSONArray("groupsAvailable");
//                            ArrayList arrA = new ArrayList();
//                            for (int i = 0; i <= arr.length() - 1; i++) {
//                                arrA.add((String) arr.get(i));
//                            }
//                            editText = (AutoCompleteTextView) findViewById(R.id.editGroupName);
//                            editText.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, arrA));
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        Log.e("meeesex", "here");
                    }}

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
//                intent.putExtra("data", result);
                Log.e("result", result);
                Log.e("lol", ip + "/admin/booking/check?userId=" + result.substring(0, 16) + "&id=" + result.substring(20, result.length()));
                finish();
                startActivity(intent);
                mScannerView.resumeCameraPreview(SimpleScannerActivity.this);
            }
        }, 0);
    }
    public class HttpGetRequest extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;
        @Override
        protected String doInBackground(String... params){
            String stringUrl = params[0];
            String result;
            String inputLine;
            try {
                //Create a URL object holding our url
                URL myUrl = new URL(stringUrl);
                //Create a connection
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();
                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url
                connection.connect();
                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
                Log.d("lol", result);
                Log.d("lol", "lolol");
            }
            catch(IOException e){
                e.printStackTrace();
                result = null;
            }
            return result;
        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}