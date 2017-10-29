package my.edu.unikl.icdsystemfirebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Admin_ListEvent extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private int LEVEL;
    private ListView list_data;
    private Spinner spinnerEvent;

    private ProgressBar circular_progress;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private List<AdminEvent> list_users = new ArrayList<>();

    private AdminEvent selectedUser; // hold user when we select item in listview

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__list_event);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Event List");

        circular_progress = (ProgressBar)findViewById(R.id.circular_progress);
        list_data = (ListView) findViewById(R.id.list_data);

        spinnerEvent = (Spinner) findViewById(R.id.spinnerEvents);
        spinnerEvent.setOnItemSelectedListener(this);

        //Firebase
        initFirebase();

    }

    private void initFirebase() {

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference  = mFirebaseDatabase.getReference();
    }

    private void levelSelection(){

        String lvlSelect = "level";
        switch (LEVEL){

            case 0 : lvlSelect = "Level3Events";
                break;
            case 1 : lvlSelect = "SurauEvents";
                break;
            case 2 : lvlSelect = "LpsEvents";
                break;
            case 3 : lvlSelect = "Level25Events";
                break;

        }
        showListEvents(lvlSelect);
    }

      private void showListEvents(String SelectedLevel) {
             //Progressing
             circular_progress.setVisibility(View.VISIBLE);
             list_data.setVisibility(View.INVISIBLE);

             mDatabaseReference.child("Events").child(SelectedLevel).addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {
                     if(list_users.size() > 0)
                         list_users.clear();
                     for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                         AdminEvent user = postSnapshot.getValue(AdminEvent.class);
                         list_users.add(user);
                     }
                     EventListViewAdapter adapter = new EventListViewAdapter(Admin_ListEvent.this,list_users);
                     list_data.setAdapter(adapter);

                     circular_progress.setVisibility(View.INVISIBLE);
                     list_data.setVisibility(View.VISIBLE);
                 }

                 @Override
                 public void onCancelled(DatabaseError databaseError) {

                 }
             });
         }


    @Override
    public boolean onSupportNavigateUp() {

       finish();
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        LEVEL = parent.getSelectedItemPosition();
        levelSelection();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
