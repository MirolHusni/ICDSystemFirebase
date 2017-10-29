package my.edu.unikl.icdsystemfirebase;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;


public class TestActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView tvCounter,tvStat;
    Button btnUpdate, btnRandomNum;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference msgRef,countRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        tvCounter = (TextView) findViewById(R.id.tvCounter);
        tvStat = (TextView) findViewById(R.id.tvStat);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnRandomNum = (Button) findViewById(R.id.btnRndomNum);
        btnRandomNum.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);


        Toast.makeText(this, "Test" , Toast.LENGTH_SHORT).show();


        //initFirebase
        initFirebase();

        msgRef.setValue("Hi there");
        msgRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String message = dataSnapshot.getValue().toString();
                //tvCounter.setText(message);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        getLastData();

    }

    public void initFirebase(){

       mFirebaseDatabase = FirebaseDatabase.getInstance();
       msgRef = mFirebaseDatabase.getReference("message").child("name");
       countRef = mFirebaseDatabase.getReference();

    }


    public void getLastData() {


    }

    @Override
    public void onClick(View v) {

        if( v == btnUpdate){
           // getLastData();
        }

        if( v == btnRandomNum){

            Random rn = new Random();

           for(int i=0; i<10 ; i++){

              int rndom = rn.nextInt(10) +1;
               tvStat.setText(String.valueOf(rndom));

               if( rndom <4){
                   tvStat.setBackground(getResources().getDrawable(R.drawable.round_green));
                   tvStat.startAnimation(AnimationUtils.loadAnimation(TestActivity.this, android.R.anim.fade_in));
               }else  if( rndom >4){

                   tvStat.setBackground(getResources().getDrawable(R.drawable.round_red));
                   tvStat.startAnimation(AnimationUtils.loadAnimation(TestActivity.this, android.R.anim.fade_in));
               }

           }
        }
    }



}
