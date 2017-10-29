package my.edu.unikl.icdsystemfirebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminHome extends AppCompatActivity implements View.OnClickListener {

    private CardView cvAddEvent, cvManageUser, cvEventList, cvLogout;

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        cvAddEvent = (CardView) findViewById(R.id.cvaddevent);
        cvManageUser = (CardView) findViewById(R.id.cvmanageuser);
        cvEventList = (CardView) findViewById(R.id.cveventlist);
        cvLogout = (CardView) findViewById(R.id.cvlogout);

        cvAddEvent.setOnClickListener(this);
        cvManageUser.setOnClickListener(this);
        cvEventList.setOnClickListener(this);
        cvLogout.setOnClickListener(this);
    }

    private void initFirebase(){
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {

        if( v == cvAddEvent){

            Intent gAddEvent = new Intent(this, Admin_AddEvent.class);
            startActivity(gAddEvent);

        }

        if( v == cvEventList){

            Intent gListEvent = new Intent(this, Admin_ListEvent.class);
            startActivity(gListEvent);

        }

        if( v== cvLogout){

        }
    }
}
