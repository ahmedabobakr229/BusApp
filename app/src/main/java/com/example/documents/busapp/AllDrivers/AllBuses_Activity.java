package com.example.documents.busapp.AllDrivers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.documents.busapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllBuses_Activity extends AppCompatActivity implements UserClick {

    private Toolbar mToolbar;

    private RecyclerView mUsersList;

    private DatabaseReference mUsersDatabase;

    private LinearLayoutManager mLayoutManager;
    private UserAdapter userAdapter ;
    TextView name;

    SearchView searchView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_buses_);

//        mToolbar = (Toolbar) findViewById(R.id.users_appBar);
        searchView = (SearchView)findViewById(R.id.search_bar);
        //setSupportActionBar(mToolbar);

//       getSupportActionBar().setTitle("All Users");
//       getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLayoutManager = new LinearLayoutManager(this);

        mUsersList = (RecyclerView) findViewById(R.id.bus_list);
        name = (TextView)findViewById(R.id.driver_info);

        mUsersList.setLayoutManager(mLayoutManager);
        mUsersList.setHasFixedSize(true);

        fetchFeeds();


    }
    @Override
    public void asd (Buss buss){

    }

//    public static class AllBussInfo extends RecyclerView.ViewHolder{
//
//        TextView bus_number , ava_seats;
//        ImageView imageView;
//
//
//        public AllBussInfo(View itemView) {
//            super(itemView);
//        }
//    }

    public ArrayList<Buss> fetchFeeds() {

        final ArrayList<Buss> feeds = new ArrayList<>();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        database.child("Users").child("Drivers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {

                        Buss feed = new Buss(noteDataSnapshot.child("numBus").getValue().toString()
                                ,noteDataSnapshot.child("avaSeats").getValue().toString() );
                        feed.setId(noteDataSnapshot.getKey());
                        feeds.add(feed);
                    }
                    userAdapter = new UserAdapter(AllBuses_Activity.this);
                    userAdapter.setUsersData(feeds , AllBuses_Activity.this);
                    mUsersList.setAdapter(userAdapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return feeds;
    }
}
