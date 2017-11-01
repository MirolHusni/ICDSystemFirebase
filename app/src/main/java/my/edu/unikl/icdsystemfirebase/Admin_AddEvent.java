package my.edu.unikl.icdsystemfirebase;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Admin_AddEvent extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    private int LEVELSELECTION;
    private String CrowdStatus;

    private EditText input_evName, input_evDesc,etDate,input_Date;
    private Spinner spinnerLvl, spinnerCS;
    private ListView list_data;
    private Button btnDate , btnAddEvent;
    private ImageView imgBackground;
    private ProgressBar circular_progress;


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;


    private AdminEvent selectedUser; // hold user when we select item in listview

    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminaddevent);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Add event");



        btnDate = (Button) findViewById(R.id.selectDate);
        btnDate.setOnClickListener(this);

        btnAddEvent = (Button) findViewById(R.id.btnAddEvent);
        btnAddEvent.setOnClickListener(this);

        //Spinner
        spinnerLvl = (Spinner) findViewById(R.id.spinner2);
        spinnerLvl.setOnItemSelectedListener(this);

        spinnerCS = (Spinner) findViewById(R.id.spinnerCrowd);
        spinnerCS.setOnItemSelectedListener(this);

        imgBackground = (ImageView) findViewById(R.id.imgAdminAddEvent);

        //Control
        circular_progress = (ProgressBar)findViewById(R.id.circular_progress);
        input_evName = (EditText) findViewById(R.id.etEvName);
        input_evDesc = (EditText)findViewById(R.id.etEvDesc);
        input_Date = (EditText) findViewById(R.id.etDate);

   //   list_data = (ListView)findViewById(R.id.list_data);
   //   list_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
   //       @Override
   //       public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
   //           AdminEvent user = (AdminEvent) adapterView.getItemAtPosition(i);
   //           selectedUser = user;
   //           input_evName.setText(user.getEvName());
   //           input_evDesc.setText(user.getEvDesc());
   //       }
   //   });




        //Firebase
        initFirebase();
       // addEventFirebaseListener();


    }

 //  private void addEventFirebaseListener() {
 //      //Progressing
 //      circular_progress.setVisibility(View.VISIBLE);
 //      list_data.setVisibility(View.INVISIBLE);
 //
 //      mDatabaseReference.child("events").addValueEventListener(new ValueEventListener() {
 //          @Override
 //          public void onDataChange(DataSnapshot dataSnapshot) {
 //              if(list_users.size() > 0)
 //                  list_users.clear();
 //              for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
 //                  AdminEvent user = postSnapshot.getValue(AdminEvent.class);
 //                  list_users.add(user);
 //              }
 //              EventListViewAdapter adapter = new EventListViewAdapter(Admin_AddEvent.this,list_users);
 //              list_data.setAdapter(adapter);
 //
 //              circular_progress.setVisibility(View.INVISIBLE);
 //              list_data.setVisibility(View.VISIBLE);
 //          }
 //
 //          @Override
 //          public void onCancelled(DatabaseError databaseError) {
 //
 //          }
 //      });
 //  }

    private void initFirebase() {

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference  = mFirebaseDatabase.getReference();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_add)
        {
         //  String lvlSelect = "level";
         //  switch (LEVELSELECTION){
         //
         //      case 0 : lvlSelect = "Level3Events";
         //          break;
         //      case 1 : lvlSelect = "SurauEvents";
         //          break;
         //      case 2 : lvlSelect = "LpsEvents";
         //          break;
         //      case 3 : lvlSelect = "Level25Events";
         //          break;
         //
         //  }
         //  createEvent(lvlSelect);
         //
        }
        else if(item.getItemId() == R.id.menu_save)
        {
            AdminEvent user = new AdminEvent(selectedUser.getUid(), input_evName.getText().toString(), input_evDesc.getText().toString(),
                    input_Date.getText().toString(), CrowdStatus);
            updateEvent(user);
        }
        else if(item.getItemId() == R.id.menu_remove){
            deleteEvent(selectedUser);
        }
        return true;
    }

    private void deleteEvent(AdminEvent selectedUser) {
        mDatabaseReference.child("Events").child(selectedUser.getUid()).removeValue();
        clearEditText();
    }


    private void updateEvent(AdminEvent user) {

        mDatabaseReference.child("Events").child(user.getUid()).child("eventName").setValue(user.getEvName());
        mDatabaseReference.child("Events").child(user.getUid()).child("eventDesc").setValue(user.getEvDesc());
        clearEditText();
    }



    private void createEvent(String lvlSelection) {

        if(!input_evName.getText().toString().equals("")){

            AdminEvent ev = new AdminEvent(UUID.randomUUID().toString(), input_evName.getText().toString(), input_evDesc.getText().toString()
                    ,input_Date.getText().toString(), CrowdStatus);
            mDatabaseReference.child("Events").child(lvlSelection).child(ev.getUid()).setValue(ev);
            Toast.makeText(this, "Event added to database", Toast.LENGTH_SHORT).show();
            clearEditText();

        }else {
            Toast.makeText(this, "Please enter event name", Toast.LENGTH_SHORT).show();
        }


    }

    private void clearEditText() {
        input_evName.setText("");
        input_evDesc.setText("");
        input_Date.setText("");
    }




    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onClick(View v) {

        if(v == btnDate){
            showDatePickerDialog();
        }

        if (v == btnAddEvent){

            String lvlSelect = "level";
            switch (LEVELSELECTION){

                case 0 : lvlSelect = "Level3Events";
                    break;
                case 1 : lvlSelect = "SurauEvents";
                    break;
                case 2 : lvlSelect = "LpsEvents";
                    break;
                case 3 : lvlSelect = "Level25Events";
                    break;

            }
            createEvent(lvlSelect);

        }
    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item

        Spinner spinLvl = (Spinner) parent;
        Spinner spinCS = (Spinner) parent;

        if(spinLvl.getId() == R.id.spinner2){
            LEVELSELECTION = parent.getSelectedItemPosition();

            if(LEVELSELECTION == 0){
                imgBackground.setImageResource(R.drawable.bg_lvl3);
                imgBackground.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
            }
            if(LEVELSELECTION == 1){
                imgBackground.setImageResource(R.drawable.bg_surau13);
                imgBackground.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
            }
        }

        if(spinCS.getId() == R.id.spinnerCrowd){
            CrowdStatus = parent.getSelectedItem().toString();
            //Toast.makeText(this, "Status "+CROWDSTATUS, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

