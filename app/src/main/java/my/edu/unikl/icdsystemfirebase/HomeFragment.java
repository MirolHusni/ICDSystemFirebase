package my.edu.unikl.icdsystemfirebase;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


public class HomeFragment extends Fragment implements View.OnClickListener {

    Button btnLvl3, btnSurau, btnLps, btnLvl25;

    TextView lvl3Status, surauStatus, lpsStatus, lvl25Status;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments

        //initialize firebase
        initFirebase();
        return inflater.inflate(R.layout.fragment_home, container, false);


    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Home");

        btnLvl3 = (Button) view.findViewById(R.id.bttnLvl3);
        btnSurau = (Button) view.findViewById(R.id.bttnSurau);
        btnLps = (Button) view.findViewById(R.id.bttnLps);
        btnLvl25 = (Button) view.findViewById(R.id.bttnLvl25);

        lvl3Status = (TextView) view.findViewById(R.id.lvl3status);
        surauStatus = (TextView) view.findViewById(R.id.surauStatus);
        lpsStatus = (TextView) view.findViewById(R.id.lpsStatus);
        lvl25Status = (TextView) view.findViewById(R.id.lvl25Status);

        btnLvl3.setOnClickListener(this);
        btnSurau.setOnClickListener(this);
        btnLps.setOnClickListener(this);
        btnLvl25.setOnClickListener(this);




       // FirebaseUser user = mFirebaseAuth.getCurrentUser();

        getPeopleCount();
    }

    private void initFirebase() {

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
    }

    @Override
    public void onClick(View v) {

        if (v == btnLvl3) {

            FragmentTransaction t = this.getFragmentManager().beginTransaction();
            t.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            Fragment mFrag = new Lvl3Fragment();
            t.replace(R.id.content_main, mFrag);
            t.commit();
            // Toast.makeText(getActivity().getApplicationContext(), "Button is clicked!", Toast.LENGTH_SHORT).show();
        }

        if (v == btnSurau) {

            FragmentTransaction t = this.getFragmentManager().beginTransaction();
            t.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            Fragment mFrag = new SurauFragment();
            t.replace(R.id.content_main, mFrag);
            t.commit();

        }

        if (v == btnLps) {

            FragmentTransaction t = this.getFragmentManager().beginTransaction();
            t.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            Fragment mFrag = new LpsFragment();
            t.replace(R.id.content_main, mFrag);
            t.commit();

        }

        if (v == btnLvl25) {

            FragmentTransaction t = this.getFragmentManager().beginTransaction();
            t.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            Fragment mFrag = new Lvl25Fragment();
            t.replace(R.id.content_main, mFrag);
            t.commit();

        }
    }


    public void getPeopleCount() {

        //Load people counter lvl3
            DatabaseReference databaseReference = mFirebaseDatabase.getInstance().getReference();
            Query lastQuery = databaseReference.child("CountPeople").child("Level3Counter").orderByKey().limitToLast(1);
            lastQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        Log.d("User key", child.getKey());
                        Log.d("User val", child.child("PeopleCounter").getValue().toString());
                        lvl3Status.setText(child.child("PeopleCounter").getValue().toString());

                        crowdStatus(Integer.parseInt(child.child("PeopleCounter").getValue().toString()));

                    }


                    //Toast.makeText(getActivity().getApplicationContext() , message, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        //Load people counter Surau
        DatabaseReference dbRefSurau = mFirebaseDatabase.getInstance().getReference();
        Query lastQuerySurau = dbRefSurau.child("CountPeople").child("SurauCounter").orderByKey().limitToLast(1);
        lastQuerySurau.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Log.d("User key", child.getKey());
                    Log.d("User val", child.child("PeopleCounter").getValue().toString());
                    surauStatus.setText(child.child("PeopleCounter").getValue().toString());

                    crowdStatusSurau(Integer.parseInt(child.child("PeopleCounter").getValue().toString()));

                }


                //Toast.makeText(getActivity().getApplicationContext() , message, Toast.LENGTH_LONG).show();
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Load people counter Lps
        DatabaseReference dbRefLps = mFirebaseDatabase.getInstance().getReference();
        Query lastQueryLps = dbRefLps.child("CountPeople").child("LpsCounter").orderByKey().limitToLast(1);
        lastQueryLps.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Log.d("User key", child.getKey());
                    Log.d("User val", child.child("PeopleCounter").getValue().toString());
                    surauStatus.setText(child.child("PeopleCounter").getValue().toString());

                    crowdStatusLps(Integer.parseInt(child.child("PeopleCounter").getValue().toString()));

                }


                //Toast.makeText(getActivity().getApplicationContext() , message, Toast.LENGTH_LONG).show();
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Load people counter Lvl25
        DatabaseReference dbRefLvl25 = mFirebaseDatabase.getInstance().getReference();
        Query lastQueryLvl25 = dbRefLvl25.child("CountPeople").child("Level25Counter").orderByKey().limitToLast(1);
        lastQueryLvl25.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Log.d("User key", child.getKey());
                    Log.d("User val", child.child("PeopleCounter").getValue().toString());
                    lvl25Status.setText(child.child("PeopleCounter").getValue().toString());

                    crowdStatusLvl25(Integer.parseInt(child.child("PeopleCounter").getValue().toString()));

                }


                //Toast.makeText(getActivity().getApplicationContext() , message, Toast.LENGTH_LONG).show();
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }


    public void crowdStatus(int status){

        if(status >=0){
            lvl3Status.setBackground(getResources().getDrawable(R.drawable.round_green));
            lvl3Status.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
        }else  if( status <=3){
            lvl3Status.setBackground(getResources().getDrawable(R.drawable.round_orange));
            lvl3Status.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
        }else  if( status >3){
            lvl3Status.setBackground(getResources().getDrawable(R.drawable.round_red));
            lvl3Status.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
        }

    }

    public void crowdStatusSurau(int status){

        if(status >=0){
            surauStatus.setBackground(getResources().getDrawable(R.drawable.round_green));
            surauStatus.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
        }else  if(  status <=3){
            surauStatus.setBackground(getResources().getDrawable(R.drawable.round_orange));
            surauStatus.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
        }else  if( status >3){
            surauStatus.setBackground(getResources().getDrawable(R.drawable.round_red));
            surauStatus.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
        }

    }

    public void crowdStatusLps(int status){

        if(status >=0){
            lpsStatus.setBackground(getResources().getDrawable(R.drawable.round_green));
            lpsStatus.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
        }else  if(  status <=3){
            lpsStatus.setBackground(getResources().getDrawable(R.drawable.round_orange));
            lpsStatus.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
        }else  if( status >3){
            lpsStatus.setBackground(getResources().getDrawable(R.drawable.round_red));
            lpsStatus.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
        }

    }

    public void crowdStatusLvl25(int status){

        if(status >=0){
            lvl25Status.setBackground(getResources().getDrawable(R.drawable.round_green));
            lvl25Status.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
        }else  if(  status <=3){
            lvl25Status.setBackground(getResources().getDrawable(R.drawable.round_orange));
            lvl25Status.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
        }else  if( status >3){
            lvl25Status.setBackground(getResources().getDrawable(R.drawable.round_red));
            lvl25Status.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
        }

    }

}

