package org.fhict.fhictcompanion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OptionActivity extends AppCompatActivity implements TokenFragment.OnFragmentInteractionListener {
    Button calendar;
    Button ppl;
    FragmentManager fragMgr = getSupportFragmentManager();
    FragmentTransaction fragTrans = fragMgr.beginTransaction();
    private List<Person> people = new ArrayList<>();
    List<String> lwValues = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        calendar = findViewById(R.id.btnCalendar);
        ppl = findViewById(R.id.btnPeople);
        TokenFragment tokenfrg = new TokenFragment();
        fragTrans.add(R.id.fragment_container, tokenfrg, "Token");
        fragTrans.commit();

        ppl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPeople();
            }
        });
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendar();
            }
        });
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
    private void openCalendar() {
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
    }
    private void openPeople() {
        PersonFragment personfrg = new PersonFragment();
        fragMgr.beginTransaction().add(R.id.fragment_container, personfrg).commit();
    }
    public void onFragmentInteraction(String token) {
        new OptionActivity.JSONTask().execute(token);
    }

    public class JSONTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {

            URL url = null;
            String s = null;
            try
            {
                url = new URL("https://api.fhict.nl/people");
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
                JSONArray jsonArray = new JSONArray(s);
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject personObject = jsonArray.getJSONObject(i);
                    String id = personObject.getString("id");
                    String firstName = personObject.getString("givenName");
                    String lastName = personObject.getString("surName");
                    String initials=null;
                    if(personObject.has("personalTitle"))
                    {
                        initials = personObject.getString("personalTitle");
                    }
                    else
                    {
                        initials = "";
                    }
                    String mail = personObject.getString("mail");
                    String office = personObject.getString("office");
                    String phone = personObject.getString("telephoneNumber");
                    String department = personObject.getString("department");
                    String title = personObject.getString("title");
                    addToList(id,firstName,lastName,initials,mail,office,phone,department,title);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(int i=0;i<people.size();i++)
            {
                lwValues.add(people.get(i).getName() + " " + people.get(i).getOffice());
            }
            //displayToList();
        }
    }
    private void addToList(String Id, String FirstName, String LastName, String Initials, String Mail, String Office, String Phone,
                           String Department, String Title)
    {
        Person p = new Person(Id, FirstName, LastName, Initials, Mail, Office, Phone, Department, Title);
        people.add(p);
    }

}
