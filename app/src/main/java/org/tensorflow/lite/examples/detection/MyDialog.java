package org.tensorflow.lite.examples.detection;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MyDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public ImageButton close;
    public TextView name, age, gender, city, condition, description;
    public ImageView thumbnail;

    public MyDialog(Context a) {
        super(a);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.person_dialog);

        close = (ImageButton)findViewById(R.id.closebutton);
        close.setOnClickListener(this);

        name = (TextView)findViewById(R.id.namedialog);
        age = (TextView)findViewById(R.id.agedialog);
        gender = (TextView)findViewById(R.id.genderdialog);
        city = (TextView)findViewById(R.id.citydialog);
        condition = (TextView)findViewById(R.id.conditiondialog);
        description = (TextView)findViewById(R.id.descriptiondialog);
        thumbnail = (ImageView)findViewById(R.id.thumbnaildialog);

    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    public void setValues(String Name, String Age, String Gender, String City, String Condition,
                          String Description, String URL)
    {
        name.setText(Name);
        age.setText(Age);
        gender.setText(Gender);
        city.setText(City);
        condition.setText(Condition);
        description.setText(Description);
        Glide.with(getContext()).load(URL).into(thumbnail);
    }
}
