package my.edu.unikl.icdsystemfirebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;

    //Progress dialog
    private ProgressDialog pDialog;

    //firebase auth object
    private FirebaseAuth firebaseAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        inputEmail = (EditText) findViewById(R.id.etEvDesc);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        inputEmail.setPadding(20, 0, 0, 0);

        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the objects getcurrentuser method is not null
        //means user is already logged in
        if(firebaseAuth.getCurrentUser() != null){
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        // Progress dialog
        pDialog = new ProgressDialog(this);

        //attaching click listener
        btnLinkToRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);


    }

    //method for user login
    private void userLogin(){

        String email = inputEmail.getText().toString().trim();
        String password  = inputPassword.getText().toString().trim();

        RegisterActivity validate = new RegisterActivity();



        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            inputEmail.setError("Please enter email");
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            inputPassword.setError("Password");
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        pDialog.setMessage("Loggin in Please Wait...");
        pDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pDialog.dismiss();
                        //if the task is successfull
                        if(task.isSuccessful()){
                            //start the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else
                            Toast.makeText(getApplicationContext(), "Incorrect email or password!", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if(view == btnLogin){
            userLogin();
        }

        if(view == btnLinkToRegister){
            finish();
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }


}
