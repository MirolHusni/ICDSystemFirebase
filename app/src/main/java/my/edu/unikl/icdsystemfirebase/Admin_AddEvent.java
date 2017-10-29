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
import android.widget.AdapterView;
import android.widget.Button;
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
    private TextInputEditText input_evName, input_evDesc,etDate,input_Date;
    private TextInputLayout tilEvName, tilEvDesc, tilEvDate;
    private Spinner spinnerLvl;
    private ListView list_data;
    private Button btnDate;
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



        etDate = (TextInputEditText) findViewById(R.id.etDate);
        btnDate = (Button) findViewById(R.id.selectDate);
        btnDate.setOnClickListener(this);

        tilEvName = (TextInputLayout) findViewById(R.id.tilEvName);




    //  CircleImageView cirImage = (CircleImageView)findViewById(R.id.profile_image);
    //  cirImage.setOnClickListener(new View.OnClickListener() {
    //
    //      @Override
    //      public void onClick(View arg0) {
    //
    //          Intent i = new Intent(
    //                  Intent.ACTION_PICK,
    //                  android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    //
    //          startActivityForResult(i, RESULT_LOAD_IMAGE);
    //      }
    //  });

        //Control
        circular_progress = (ProgressBar)findViewById(R.id.circular_progress);
        input_evName = (TextInputEditText) findViewById(R.id.etEvName);
        input_evDesc = (TextInputEditText)findViewById(R.id.etEvDesc);
        input_Date = (TextInputEditText) findViewById(R.id.etDate);

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


        spinnerLvl = (Spinner) findViewById(R.id.spinner2);
        spinnerLvl.setOnItemSelectedListener(this);




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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_add)
        {
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
        else if(item.getItemId() == R.id.menu_save)
        {
            AdminEvent user = new AdminEvent(selectedUser.getUid(), input_evName.getText().toString(), input_evDesc.getText().toString(),
                    input_Date.getText().toString());
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
                    ,input_Date.getText().toString());
            mDatabaseReference.child("Events").child(lvlSelection).child(ev.getUid()).setValue(ev);
            Toast.makeText(this, "Event added to database", Toast.LENGTH_SHORT).show();
            clearEditText();

        }else {
            Toast.makeText(this, "Please enter event name", Toast.LENGTH_SHORT).show();
            tilEvName.setError("Name not entered");
        }


    }

    private void clearEditText() {
        input_evName.setText("");
        input_evDesc.setText("");
        input_Date.setText("");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            CircleImageView cirImg = (CircleImageView) findViewById(R.id.profile_image);
            cirImg.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }


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
    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        int itemNo = parent.getSelectedItemPosition();
        LEVELSELECTION = itemNo;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

