package my.edu.unikl.icdsystemfirebase;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;

    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private String email,password, username;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();


        inputFullName = (EditText) findViewById(R.id.etEvName);
        inputEmail = (EditText) findViewById(R.id.etEvDesc);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);



        pDialog = new ProgressDialog(this);

        //attaching listener to button
        btnRegister.setOnClickListener(this);
        btnLinkToLogin.setOnClickListener(this);

    }

    private void registerUser(){

        //getting email and password from edit texts
         username = inputFullName.getText().toString().trim();
         email = inputEmail.getText().toString().trim();
         password  = inputPassword.getText().toString().trim();

        //checking if email and passwords are empty


        if(TextUtils.isEmpty(username) && TextUtils.isEmpty(email) && TextUtils.isEmpty(password)){
            inputFullName.setError("Username cannot be empty!");
            inputEmail.setError("Email cannot be empty!");
            inputPassword.setError("Password cannot be empty!");
            return;
        }
        if (!isValidEmail(email)) {
            inputEmail.setError("Invalid Email");
            return;
        }

        if (!isValidPassword(password)) {
            inputPassword.setError("Password too short");
            return;
        }





        //if the email and password are not empty
        //displaying a progress dialog

        pDialog.setMessage("Registering Please Wait...");
        pDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            //display some message here
                            user = FirebaseAuth.getInstance().getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Log.d("Updated", username+ user.getDisplayName());
                                            }
                                        }

                                    });
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setTitle(email+", Congratulations!");
                            builder.setIcon(R.drawable.ic_applause);

                            builder.setMessage("Your new account has been created. Enjoy!");


// Set up the buttons
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    firebaseAuth.signOut();
                                    Intent intent = new Intent(
                                            RegisterActivity.this,
                                            LoginActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            });

                            builder.show();

                        }else{
                            //display some message here
                            Toast.makeText(RegisterActivity.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        pDialog.dismiss();
                    }
                });

    }

    @Override
    public void onClick(View view) {
        //calling register method on click
        if (view == btnRegister) {
            registerUser();
        }

        if (view == btnLinkToLogin) {

            Intent intent = new Intent(
                    RegisterActivity.this,
                    LoginActivity.class);
            startActivity(intent);
        }
    }

    // validating email id
    public boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    public boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 5) {
            return true;
        }
        return false;
    }

}

