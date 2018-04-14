package com.example.dcamo.splash.support;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.dcamo.splash.R;
import com.example.dcamo.splash.main.BookingActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.dcamo.splash.main.MainActivity.ip;

@SuppressLint("ValidFragment")
public class DatePicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    public String finaltime;
    public List<Booking> arrayList;
    public TextView time;
    public TextView timeSorry;
    public ListView listView;
    public ArrayAdapter<Booking> adapter;
    @SuppressLint("ValidFragment")

    public DatePicker(TextView textView, List<Booking> arrayList, ListView listView, ArrayAdapter<Booking> adapter){
        this.time = textView;
        this.arrayList = arrayList;
        this.listView = listView;
        this.adapter = adapter;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // определяем текущую дату
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // создаем DatePickerDialog и возвращаем его
        DatePickerDialog picker = new DatePickerDialog(getActivity(), this,
                year, month, day);

        picker.setTitle(getResources().getString(R.string.choose_date));
        picker.getDatePicker().setMinDate(c.getTimeInMillis());
        return picker;
    }
    @Override
    public void onStart() {
        super.onStart();
        // добавляем кастомный текст для кнопки
        Button nButton =  ((AlertDialog) getDialog())
                .getButton(DialogInterface.BUTTON_POSITIVE);
        nButton.setText("OK");

    }

    public List<Booking> getArrayList() {
        return arrayList;
    }

    public String returnOfTheJedi(){
        return this.finaltime;
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year,
                          int month, int day) {
//        String formattedString = day + "." + (month + 1) + "." + year;

        try {
            String formattedString = new SimpleDateFormat("MM.dd.yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(year + "-" + (month + 1) + "-" + day));

        this.time.setText(formattedString);

//        String myUrl = ip + "/admin/booking?date=" + month + "." + day + "." + year;
        String myUrl = ip + "/admin/booking?date=" + formattedString;

        //String to place our result in


        String result;
        //Instantiate new instance of our class

        HttpGetRequest getRequest = new HttpGetRequest();
        try {
            result = getRequest.execute(myUrl).get();
            if(result == null){
                Toast.makeText(getActivity().getBaseContext(), R.string.no_server_connection, Toast.LENGTH_SHORT).show();
            }else {
                try {
                    arrayList.clear();
                    JSONArray teachersArray = new JSONArray(result);
                    for(int i = 0; i<= teachersArray.length()-1;i++){
                        JSONObject dataObject = teachersArray.getJSONObject(i);
//                        bookings.add(new Offers(file.getString("text"), file.getString("title"),file.getString("image")));
                        arrayList.add(new Booking(dataObject.getString("__v"),dataObject.getString("customerName"),dataObject.getString("customerNumber"),dataObject.getString("tableNumber"),dataObject.getString("timeStart"),dataObject.getString("timeEnd"),dataObject.getString("quantity"),dataObject.getString("callback"),dataObject.getString("bookingDate"),dataObject.getString("_id"),dataObject.getString("createdDate"),dataObject.getString("userId"), dataObject.getString("status")));
                        listView.setAdapter(adapter);
                        listView.destroyDrawingCache();
                        listView.setVisibility(ListView.INVISIBLE);
                        listView.setVisibility(ListView.VISIBLE);
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
//            this.timeSorry.setText(day + "." + month + "." + year);
//            this.finaltime = day + "." + month + "." + year;


        } catch (ParseException e) {
            e.printStackTrace();
        }}

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

}