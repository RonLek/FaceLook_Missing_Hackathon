package org.tensorflow.lite.examples.detection;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class CardDetailActivity extends AppCompatActivity {

    public TextView name, age, gender, city, condition, description;
    public ImageView thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        name = (TextView)findViewById(R.id.namedialog);
        age = (TextView)findViewById(R.id.agedialog);
        gender = (TextView)findViewById(R.id.genderdialog);
        city = (TextView)findViewById(R.id.citydialog);
        condition = (TextView)findViewById(R.id.conditiondialog);
        description = (TextView)findViewById(R.id.descriptiondialog);
        thumbnail = (ImageView)findViewById(R.id.thumbnaildialog);

        Person person = (Person)getIntent().getSerializableExtra("Person");

        name.setText(person.name);
        age.setText("" + person.age);
        gender.setText(person.gender);
        city.setText(person.city);
        condition.setText(person.condition);
        description.setText(person.description);
        Glide.with(getBaseContext()).load(person.thumbnail).into(thumbnail);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
