package my.edu.unikl.icdsystemfirebase;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import my.edu.unikl.icdsystemfirebase.recycler_view.EventRVadapter;

/**
 * Created by MirolHusni95 on 13/4/2017.
 */

public class Lvl25Fragment extends Fragment implements View.OnClickListener {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseRefrence;

    private List<AdminEvent> list_users = new ArrayList<>();

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    private Button bttnSend;
    private TextView tvLvl25Counter, tvLvl25LastMsg;
    private EditText etComment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_lvl25, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Level 25");

        bttnSend = (Button) view.findViewById(R.id.buttonSendComment);
        tvLvl25Counter = (TextView) view.findViewById(R.id.tvLvl25Counter);
        tvLvl25LastMsg = (TextView) view.findViewById(R.id.tvCommentLvl25);
        etComment = (EditText) view.findViewById(R.id.etInsertComment);

        bttnSend.setOnClickListener(this);

        // Calling the RecyclerView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_lvl25event);
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
            startActivity(new Intent(getContext(),Lvl25Chatroom.class));
        }

    }

    private void getEventDetails(){

        mDatabaseRefrence.child("Events").child("Level25Events").addValueEventListener(new ValueEventListener() {
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
                    tvLvl25Counter.setText(child.child("PeopleCounter").getValue().toString());
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
        Query lastQuery = databaseReference.child("Chatroom").child("Level25Chat").orderByKey().limitToLast(1);
        lastQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Log.d("User key", child.getKey());
                    Log.d("User val", child.child("messageText").getValue().toString());
                    tvLvl25LastMsg.setText(child.child("messageUser").getValue().toString()+" : "+child.child("messageText").getValue().toString());
                }


                //Toast.makeText(getActivity().getApplicationContext() , message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
