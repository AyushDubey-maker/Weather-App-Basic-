package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    EditText textView;
    Button search_but;
    TextView results;
    ProgressBar progressBar;





    class Weather extends AsyncTask<String,Void,String> {


            @Override
            protected String doInBackground (String...address){
                //String... means multiple address can be send. It acts as array
                try {
                    URL url = new URL(address[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //Establish connection with address
                    connection.connect();

                    //retrieve data from url
                    InputStream is = connection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);

                    //Retrieve data and return it as String
                    int data = isr.read();
                    String content = "";
                    char ch;
                    while (data != -1) {
                        ch = (char) data;
                        content = content + ch;
                        data = isr.read();
                    }
                    return content;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar=findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

    }

        public void search(View view) {
            textView = findViewById(R.id.editTextTextPersonName);
            search_but = findViewById(R.id.button);
            results=findViewById(R.id.textView);

            String enter_text = textView.getText().toString();
          progressBar.setVisibility(View.VISIBLE);

            String content;
            Weather weather = new Weather();
            try {
                //content = weather.execute("https://openweathermap.org/data/2.5/weather?q=London&appid=3f4e64ead800ab05ccb5c6201150dfca").get();
                content = weather.execute("https://api.openweathermap.org/data/2.5/weather?q=" + enter_text + "&units=metric&appid=3265874a2c77ae4a04bb96236a642d2f").get();
                //First we will check data is retrieve successfully or not
                //Log.i("contentData", content);
                //JSON
                JSONObject jsonObject = new JSONObject(content);
                String weatherData = jsonObject.getString("weather");
                String mainTemp=jsonObject.getString("main");
                double visibility;
               // double temperature;
               progressBar.setVisibility(View.GONE);
                //Log.i("weatherData", weatherData);
                //Weather Data is in Array
                JSONArray array = new JSONArray(weatherData);
                String main = "";
                String description = "";
                String temperature="";

                for (int i = 0; i < array.length(); i++) {
                    JSONObject weatherPart = array.getJSONObject(i);
                    main = weatherPart.getString("main");
                    description = weatherPart.getString("description");

                }
                JSONObject mainPart = new JSONObject(mainTemp);
                temperature = mainPart.getString("temp");
               // temperature=Double.parseDouble(jsonObject.getString("temp"));
               // double tempCel=(double) (temperature-273.14);

                visibility = Double.parseDouble(jsonObject.getString("visibility"));
                //By default visibility is in meter
                int visibiltyInKilometer = (int) visibility/1000;

                //Log.i("main", main);
                //Log.i("description", description);

                String resultText = "Main :"+main+
                        "\n\nDescription :"+description +
                        "\n\nTemperature :"+temperature+"\u2103"+
                        "\n\nVisibility :"+visibiltyInKilometer+" KM";
                results.setText(resultText);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

}