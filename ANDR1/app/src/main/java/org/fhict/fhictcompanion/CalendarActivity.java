package org.fhict.fhictcompanion;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class CalendarActivity extends AppCompatActivity implements TokenFragment.OnFragmentInteractionListener {
    CalendarView calView;
    TextView currDatetw;
    ListView listView ;
    List<Event> classes = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    Date selectedDate;
    List<String> lwValues = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calView = (CalendarView)findViewById(R.id.cv);
        currDatetw = (TextView)findViewById(R.id.twDate);
        listView = (ListView) findViewById(R.id.lv);

        selectedDate = new Date(calView.getDate());
        currDatetw.setText(sdf.format(selectedDate));
        calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                // TODO Auto-generated method stub
                String sel = Integer.toString(dayOfMonth) + "-"
                        + Integer.toString(month+1) + "-"
                        + Integer.toString(year);

                currDatetw.setText(sel);
                lwValues.clear();
                for(int i=0;i<classes.size();i++)
                {
                    if(sdf.format(classes.get(i).getStart()).equals("16-04-2020"))
                    {
                        lwValues.add(classes.get(i).getTitle() + " " + classes.get(i).getLocation());
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),
                                "No classes", Toast.LENGTH_LONG)
                                .show();
                    }
                }
                displayToList();
            }
        });
    }

    @Override
    public void onFragmentInteraction(String token) {
        new JSONTask().execute(token);
    }

    private class JSONTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {

            URL url = null;
            String s = null;
            try
            {
                url = new URL("https://api.fhict.nl/schedule/me");
                HttpURLConnection connection = null;
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + strings[0]);
                connection.connect();
                InputStream is = connection.getInputStream();
                Scanner scn = new Scanner(is);
                s = scn.useDelimiter("\\Z").next();

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject scheduleObject = jsonArray.getJSONObject(i);
                    String startDate= scheduleObject.getString("start");
                    String endDate= scheduleObject.getString("end");
                    String location= scheduleObject.getString("room");
                    String description= scheduleObject.getString("description");
                    String title= scheduleObject.getString("subject");
                    addToList("16-04-2020","16-04-2020",title,location,description,false);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private Date convertStringToDate(String s)
    {
        Date d = null;
        try
        {
            d = new SimpleDateFormat("dd-MM-yyyy").parse(s);
        }
        catch (ParseException e)
        {
            Toast.makeText(getApplicationContext(),"Data not inserted" + e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return d;
    }
    private void addToList(String start, String end, String title, String location, String description, boolean allDay)
    {
        Event e = new Event(convertStringToDate(start),convertStringToDate(end),title,location,description,allDay);
        classes.add(e);
    }
    private void displayToList()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, lwValues);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                Toast.makeText(getApplicationContext(),
                        classes.get(position).getDescription(), Toast.LENGTH_LONG)
                        .show();

            }

        });
    }
}
