package my.edu.unikl.icdsystemfirebase;

/**
 * Created by MirolHusni95 on 13/4/2017.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import my.edu.unikl.icdsystemfirebase.recycler_view.EventRVadapter;


public class Lvl3Fragment extends Fragment implements View.OnClickListener {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseRefrence;

    private Button bttnSend;
    private TextView tvLvl3Stat,lstComment;
    private EditText etComment;

    private List<AdminEvent> list_users = new ArrayList<>();

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;



    BarChart barChart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lvl3, container, false);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Level 3");

        bttnSend = (Button) view.findViewById(R.id.buttonSendComment);
        tvLvl3Stat = (TextView) view.findViewById(R.id.tvLvl3Counter);
        etComment = (EditText) view.findViewById(R.id.etInsertComment);
        lstComment = (TextView) view.findViewById(R.id.tvComment);

        barChart = (BarChart) view.findViewById(R.id.bargraph);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(5f,0));
        barEntries.add(new BarEntry(12f,1));
        barEntries.add(new BarEntry(14f,2));
        barEntries.add(new BarEntry(10f,3));
        barEntries.add(new BarEntry(21f,4));
        barEntries.add(new BarEntry(15f,5));

        BarDataSet bds = new BarDataSet(barEntries,"Number of people");

        ArrayList<String> theTimes = new ArrayList<>();
        theTimes.add("8 a.m");
        theTimes.add("9 a.m");
        theTimes.add("10 a.m");
        theTimes.add("11 a.m");
        theTimes.add("12 p.m");
        theTimes.add("1 p.m");

        BarData theData = new BarData(theTimes,bds);
        barChart.setData(theData);
        barChart.setTouchEnabled(true);

        bttnSend.setOnClickListener(this);


        // Calling the RecyclerView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_lvl3event);
        mRecyclerView.setHasFixedSize(true);
        initFirebase();
        getEventDetails();
        getPeopleCount();
        getLastMessage();

    }


    private void initFirebase(){

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRefrence = mFirebaseDatabase.getReference();
    }


    @Override
    public void onClick(View v) {
        if(v == bttnSend) {
            startActivity(new Intent(getContext(), Lvl3Chatroom.class));
        }
    }

    private void getEventDetails(){

        mDatabaseRefrence.child("Events").child("Level3Events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(list_users.size() > 0)
                    list_users.clear();
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    AdminEvent user = postSnapshot.getValue(AdminEvent.class);
                    list_users.add(user);
                }


                // The number of Columns
                mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                mRecyclerView.setLayoutManager(mLayoutManager);

                mAdapter = new EventRVadapter(getContext(), list_users);
                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    public void getPeopleCount() {

        DatabaseReference databaseReference = mFirebaseDatabase.getInstance().getReference();
        Query lastQuery = databaseReference.child("CountPeople").orderByKey().limitToLast(1);
        lastQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Log.d("User key", child.getKey());
                    Log.d("User val", child.child("PeopleCounter").getValue().toString());
                    tvLvl3Stat.setText(child.child("PeopleCounter").getValue().toString());
                }


                //Toast.makeText(getActivity().getApplicationContext() , message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


    public void getLastMessage(){

        DatabaseReference databaseReference = mFirebaseDatabase.getInstance().getReference();
        Query lastQuery = databaseReference.child("Chatroom").child("Level3Chat").orderByKey().limitToLast(1);
        lastQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Log.d("User key", child.getKey());
                    Log.d("User val", child.child("messageText").getValue().toString());
                    lstComment.setText(child.child("messageUser").getValue().toString()+" : "+child.child("messageText").getValue().toString());
                }


                //Toast.makeText(getActivity().getApplicationContext() , message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
