package com.example.dcamo.splash.main;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.dcamo.splash.R;
import com.example.dcamo.splash.support.Booking;
import com.example.dcamo.splash.support.DatePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.dcamo.splash.main.MainActivity.ip;

import com.example.dcamo.splash.R;

public class BookingActivity extends Activity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    SharedPreferences mSettings;
    public List<Booking> bookings = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_activity);
        final ListView listView1 = (ListView) this.findViewById(R.id.listViewOffers);
        final ArrayAdapter<Booking> adapter = new BookingAdapter(this.getBaseContext());
        Date now = new Date();



        final TextView dateText = findViewById(R.id.dateText);
        dateText.setText(new SimpleDateFormat("MM.dd.yyyy").format(now));
        String myUrl = ip + "/admin/booking?date=" + dateText.getText();

        //String to place our result in


        String result;
        //Instantiate new instance of our class

        HttpGetRequest getRequest = new HttpGetRequest();
        try {
            result = getRequest.execute(myUrl).get();
            if(result == null){
                Toast.makeText(this.getBaseContext(), R.string.no_server_connection, Toast.LENGTH_SHORT).show();
            }else {
                try {
                    bookings.clear();
                    JSONArray teachersArray = new JSONArray(result);
                    for(int i = 0; i<= teachersArray.length()-1;i++){
                        JSONObject dataObject = teachersArray.getJSONObject(i);
//                        bookings.add(new Offers(file.getString("text"), file.getString("title"),file.getString("image")));
                        bookings.add(new Booking(dataObject.getString("__v"),dataObject.getString("customerName"),dataObject.getString("customerNumber"),dataObject.getString("tableNumber"),dataObject.getString("timeStart"),dataObject.getString("timeEnd"),dataObject.getString("quantity"),dataObject.getString("callback"),dataObject.getString("bookingDate"),dataObject.getString("_id"),dataObject.getString("createdDate"),dataObject.getString("userId"), dataObject.getString("status")));

                        Log.e("test1", dataObject.toString());

                    }
//                        SharedPreferences.Editor editor = mSettings.edit();
//                        editor.putString(savedTeachers, result);
//                        editor.apply();
                    Log.e("teachersNotSaved", result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        listView1.setAdapter(adapter);


        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                DatePicker datePicker = new DatePicker(dateText, bookings, listView1, adapter);
                datePicker.show(getFragmentManager(), ")");
                bookings = datePicker.getArrayList();
//                Log.e("bookings", bookings.get(0).customerNumber);

//                String myUrl = ip + "/admin/booking?date=" + dateText.getText();

            }
        });




    }
    //    private static final List<Lessons> lessonses = new ArrayList<>();
    private class BookingAdapter extends ArrayAdapter<Booking> {

        public BookingAdapter(Context context) {
            super(context, R.layout.booking_item, bookings);
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            Booking offer = getItem(position);
//            Log.e(position + "", teacher.name);



                convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.booking_item, null);
                ((TextView) convertView.findViewById(R.id.tableText)).setText(offer.tableNumber);
                ((TextView) convertView.findViewById(R.id.numberText)).setText(offer.customerNumber);
                ((TextView) convertView.findViewById(R.id.nameText)).setText(offer.customerName);
                ((TextView) convertView.findViewById(R.id.timeText)).setText(offer.timeStart);
                return convertView;


//                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null);
        }

    }








    public class HttpGetRequest extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = params[0];
            String result;
            String inputLine;
            try {
                //Create a URL object holding our url
                URL myUrl = new URL(stringUrl);
                //Create a connection
                HttpURLConnection connection = (HttpURLConnection)
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
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
                Log.d("lol", result);
                Log.d("lol", "lolol");
            } catch (IOException e) {
                e.printStackTrace();
                result = null;
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
