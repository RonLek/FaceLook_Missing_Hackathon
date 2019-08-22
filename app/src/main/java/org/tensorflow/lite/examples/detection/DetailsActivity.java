package org.tensorflow.lite.examples.detection;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {

    private String title;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private TextView name, age, latitude, longitude, city, gender;
    private EditText description;

    private Button saveDetails;

    private Spinner conditionsSpinner;

    private Switch assistance;

    Person person = new Person();

    //Firebase
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        name = (TextView)findViewById(R.id.nameinput);
        age =  (TextView)findViewById(R.id.ageinput);
        latitude = (TextView)findViewById(R.id.latitudeinput);
        longitude = (TextView)findViewById(R.id.longitudeinput);
        city = (TextView)findViewById(R.id.cityinput);
        gender = (TextView)findViewById(R.id.genderinput);
        description = (EditText)findViewById(R.id.descriptioninput);
        assistance = (Switch)findViewById(R.id.assistanceinput);

        saveDetails = (Button)findViewById(R.id.savedetailsbutton);

        conditionsSpinner = (Spinner)findViewById(R.id.conditionspinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.condtion_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        conditionsSpinner.setAdapter(adapter);

        conditionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("Healthy"))
                {
                    person.condition = "Healthy";
                }
                else if(selectedItem.equals("Injured/Sick"))
                {
                    person.condition = "Injured/Sick";
                }
                else if(selectedItem.equals("Dead"))
                {
                    person.condition = "Dead";
                }
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {
                person.condition = "Healthy";
            }
        });

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude.setText(" " + location.getLatitude());
                longitude.setText(" " + location.getLongitude());

                person.latitude = (float)location.getLatitude();
                person.longitude = (float)location.getLongitude();

                Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                List<Address> addresses;
                try {
                    addresses = gcd.getFromLocation(location.getLatitude(), location
                            .getLongitude(), 1);
                    if (addresses.size() > 0)
                    {
                        System.out.println(addresses.get(0).getLocality());
                        System.out.println(addresses.get(0).getAdminArea());
                        System.out.println(addresses.get(0).getPostalCode());
                        System.out.println(addresses.get(0).getFeatureName());
                    }

                    city.setText(" " + addresses.get(0).getLocality());

                    person.city = (String)city.getText();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, locationListener);

        title = (String)getIntent().getStringExtra("Title");
        Log.d("Details Activity", "Title = "+ title);

        databaseReference.child("person").child(title).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                person.name = title;
                person.age = 77;
                person.gender = "M";
                if((String)dataSnapshot.child("thumbnail").getValue() == "")
                {
                    person.thumbnail = "https://visualpharm.com/assets/66/Person-595b40b75ba036ed117d82de.svg";
                    person.relationid = 101;
                }



                //Uncomment on getting real data
//                person.name = (String)dataSnapshot.child("name").getValue();
//                person.age = ((Long)dataSnapshot.child("age").getValue()).intValue();
//                person.gender = (String)dataSnapshot.child("gender").getValue();
//                person.thumbnail = (String)dataSnapshot.child("thumbnail").getValue();
//                person.relationid = ((Long)dataSnapshot.child("relationid").getValue()).intValue();
                name.setText((String)dataSnapshot.child("name").getValue());
                age.setText(String.valueOf(dataSnapshot.child("age").getValue()));
                gender.setText("" +(String)dataSnapshot.child("gender").getValue());
                name.setText(person.name);
                age.setText("" + person.age);
                gender.setText(person.gender);
                Log.d("Details Activity", "" + dataSnapshot.getValue());
                Log.d("Details Activity", "" +dataSnapshot.child("name").exists());
                System.out.println("Details Activity data change listener");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Save to Firebase
                person.description = description.getText().toString();
                if(assistance.isChecked())
                {
                    person.assistance = "Yes";
                }
                else
                {
                    person.assistance = "No";
                }
                person.newPerson(title);
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Toast.makeText(getBaseContext(), "Data Updated Successfully!", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });



    }
}
