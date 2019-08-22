package org.tensorflow.lite.examples.detection;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String[] imageurl = new String[3];

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private RecyclerView recyclerView;
    private PersonAdapter adapter;
    private List<Person> personList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent facescan = new Intent(getBaseContext(), DetectorActivity.class);
                startActivity(facescan);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        personList = new ArrayList<>();

        adapter = new PersonAdapter(getBaseContext(), personList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


//        preparePersons();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imageurl[0] = (String)dataSnapshot.child("carouselimages").child("0").getValue();
                imageurl[1] = (String)dataSnapshot.child("carouselimages").child("1").getValue();
                imageurl[2] = (String)dataSnapshot.child("carouselimages").child("2").getValue();

                ImageListener imageListener = new ImageListener() {
                    @Override
                    public void setImageForPosition(int position, ImageView imageView) {
                        System.out.println(imageurl[position]);
                        Glide.with(getBaseContext()).load(imageurl[position]).into(imageView);

                    }
                };

                CarouselView carouselView = (CarouselView) findViewById(R.id.carouselView);
                carouselView.setImageListener(imageListener);
                carouselView.setPageCount(3);

                System.out.println(imageurl[0]);
                System.out.println(imageurl[1]);
                System.out.println(imageurl[2]);


                for (DataSnapshot postSnapshot: dataSnapshot.child("person").getChildren()) {
                    //To limit the number of victims shown per area - Remove below comment when
                    // actual missing person data with location given.
                    if(personList.size() < 6) {
                        Person person = new Person();
                        person.name = (String) postSnapshot.child("name").getValue();
                        if (postSnapshot.child("relationid").getValue() != null)
                            person.relationid = ((Long) postSnapshot.child("relationid").getValue()).intValue();
                        if (postSnapshot.child("age").getValue() != null)
                            person.age = ((Long) postSnapshot.child("age").getValue()).intValue();
                        person.assistance = (String) postSnapshot.child("assistance").getValue();
                        person.city = (String) postSnapshot.child("city").getValue();
                        person.condition = (String) postSnapshot.child("condition").getValue();
                        person.description = (String) postSnapshot.child("description").getValue();
                        person.gender = (String) postSnapshot.child("gender").getValue();
                        if (postSnapshot.child("latitude").getValue() != null)
                            person.latitude = ((Double) postSnapshot.child("latitude").getValue()).floatValue();
                        if (postSnapshot.child("longitude").getValue() != null)
                            person.longitude = ((Double) postSnapshot.child("longitude").getValue()).floatValue();
                        person.thumbnail = (String) postSnapshot.child("thumbnail").getValue();
                        personList.add(person);
                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), "Couldn't load images. Check Internet Connection!", Toast.LENGTH_LONG);
            }
        });



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            new MaterialStyledDialog.Builder(this)
                    .setTitle("FaceLook")
                    .setDescription("Face Look is a face recognition app that can recognize faces of potential " +
                            "victims using state-of-art computer vision algorithms and can id " +
                            "missing persons. The user, through the app would act as a medium to " +
                            "connect missing persons with the main authorities.")
                    .setIcon(R.drawable.facelook)
                    .show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(this, DetectorActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_add_person) {
            Intent intent = new Intent(this, AddPersonActivity.class);
            startActivity(intent);



        } else if (id == R.id.nav_about) {
            new MaterialStyledDialog.Builder(this)
                    .setTitle("FaceLook")
                    .setDescription("Face Look is a face recognition app that can recognize faces of potential " +
                                    "victims using state-of-art computer vision algorithms and can id " +
                                    "missing persons. The user, through the app would act as a medium to " +
                                    "connect missing persons with the main authorities.")
                    .setIcon(R.drawable.facelook)
                    .show();

        }else if (id == R.id.nav_share) {
            Toast.makeText(getBaseContext(), "To be implemented after publishing on Play Store", Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
