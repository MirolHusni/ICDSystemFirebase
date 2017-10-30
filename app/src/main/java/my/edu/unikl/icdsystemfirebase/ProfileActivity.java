package my.edu.unikl.icdsystemfirebase;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import my.edu.unikl.icdsystemfirebase.Firebase.UserInformation;
import my.edu.unikl.icdsystemfirebase.other.CircleTransform;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    //Firebase storage path
    public static final String FB_STORAGE_PATH = "images/";

    private TextView tvUsername, tvUserEmail;
    private EditText etUsername, etPassword, etPhone;
    private ImageView imgProfile;

    private Button btnUpdate;

    private String username,userPass, userPhone, userPhotoUrl;

    private Uri filepath;
    private final int PICK_IMAGE_REQUEST =1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvUserEmail = (TextView) findViewById(R.id.tvUserEmail);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPhone = (EditText) findViewById(R.id.etPhone);

        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        imgProfile.setOnClickListener(this);

        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);


        initFirebase();
        getUserInformation();


    }

    public void initFirebase(){

        //Firebase database init
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        //FireBase user init
        user = FirebaseAuth.getInstance().getCurrentUser();
        //FireBase Storage init
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



    }


    public void getUserInformation(){

        loadUserPhoto();

        String userEmail = user.getEmail();
        tvUserEmail.setText(userEmail);
        tvUsername.setText(user.getDisplayName());
        etUsername.setText(user.getDisplayName());




    }

    private void loadUserPhoto(){

        try {

            DatabaseReference databaseReference = database.getInstance().getReference();
            Query queryUser = databaseReference.child("Users").orderByChild("uid").equalTo(user.getUid());
            queryUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        // Log.d("User key", child.getKey());
                        Log.d("Profile activity: ", child.child("uPhotoUrl").getValue().toString());

                        Glide.with(ProfileActivity.this).load(child.child("uPhotoUrl").getValue().toString())
                                .crossFade()
                                .thumbnail(0.5f)
                                .bitmapTransform(new CircleTransform(ProfileActivity.this))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imgProfile);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }catch (Exception e){
            Log.d("Error loaduserPhoto", e.getMessage());
        }
    }


    public void chooseProfileImg(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent, "Select picture"), PICK_IMAGE_REQUEST);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null && data.getData() != null){

            filepath = data.getData();
            try {

                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                //imgProfile.setImageBitmap(bitmap);

                Glide.with(this).load(filepath)
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(this))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgProfile);


            }catch(Exception e){

                e.printStackTrace();
            }
        }
    }


    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onClick(View v) {

        if( v == btnUpdate){


            if (filepath != null) {
                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setTitle("Uploading image");
                dialog.show();

                //Get the storage reference
                StorageReference ref = storageReference.child(FB_STORAGE_PATH + user.getUid() + "." + getImageExt(filepath));

                //Add file to reference

                ref.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        //Dimiss dialog when success
                        dialog.dismiss();
                        //Display success toast msg
                        Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_SHORT).show();
                        UserInformation userUpload = new UserInformation(user.getUid(), etUsername.getText().toString().trim(),
                                etPassword.getText().toString().trim(), etPhone.getText().toString().trim() ,taskSnapshot.getDownloadUrl().toString());


                        //Save image info in to firebase database
                        String uploadId = user.getUid();
                        myRef.child("Users").child(user.getUid()).setValue(userUpload);

                        clearEditText();

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                //Dimiss dialog when error
                                dialog.dismiss();
                                //Display err toast msg
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                //Show upload progress

                                double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                dialog.setMessage("Uploaded " + (int) progress + "%");
                            }
                        });
            } else {
                Toast.makeText(getApplicationContext(), "Please select image", Toast.LENGTH_SHORT).show();
            }

        }

        if( v == imgProfile){

            chooseProfileImg();
        }

    }



    private void clearEditText() {

        etUsername.setText(null);
        etPassword.setText(null);
        etPhone.setText(null);

    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return true;
    }
}
