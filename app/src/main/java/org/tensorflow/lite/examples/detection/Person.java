package  org.tensorflow.lite.examples.detection;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class Person implements Serializable {

        public int relationid;
        public int age;
        public String assistance;
        public String city;
        public String condition;
        public String description;
        public String gender;
        public float latitude;
        public float longitude;
        public String name;
        public String thumbnail;


        public Person() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public Person(int relationid, int age, String assistance, String city, String condition, String description, String gender,
                      float latitude, float longitude, String name, String thumbnail) {
            this.relationid = relationid;
            this.age = age;
            this.assistance = assistance;
            this.city = city;
            this.condition = condition;
            this.description = description;
            this.gender = gender;
            this.latitude = latitude;
            this.longitude = longitude;
            this.name = name;
            this.thumbnail = thumbnail;
        }

        public String getName()
        {
            return name;
        }

        public int getAge()
        {
            return age;
        }

        public String getThumbnail(){
            return thumbnail;
        }

        public void newPerson(String title){
            try {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("person").child(title);
                databaseReference.child("name").setValue(this.name);
                databaseReference.child("relationid").setValue(this.relationid);
                databaseReference.child("age").setValue(this.age);
                databaseReference.child("assistance").setValue(this.assistance);
                databaseReference.child("city").setValue(this.city);
                databaseReference.child("condition").setValue(this.condition);
                databaseReference.child("description").setValue(this.description);
                databaseReference.child("gender").setValue(this.gender);
                databaseReference.child("latitude").setValue(this.latitude);
                databaseReference.child("longitude").setValue(this.longitude);
                databaseReference.child("thumbnail").setValue(this.thumbnail);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


        }

}
