package com.jsquad.snaproad.road_snap;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity implements OnClickListener,
OnCompleteListener<AuthResult>{

    private EditText txtFirstName;
    private EditText txtLastName;
    private EditText txtUserName;
    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtConfirmPassword;
    private Button btnRegister;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();

        txtFirstName = (EditText)findViewById(R.id.first_name);
        txtLastName = (EditText)findViewById(R.id.last_name);
        txtUserName = (EditText)findViewById(R.id.user_name);
        txtEmail = (EditText)findViewById(R.id.email);
        txtPassword = (EditText)findViewById(R.id.password);
        txtConfirmPassword = (EditText)findViewById(R.id.confirm_password);
        btnRegister = (Button)findViewById(R.id.register);

        btnRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Log.d("JOCAS", "GOES HERE");
        if(txtFirstName.getText().toString().trim().equals("")){
            Toast.makeText(this, "First name is required", Toast.LENGTH_SHORT).show();
            Log.d("JOCAS","ASD");
            return;
        }
        else if(txtLastName.getText().toString().trim().equals("")){
            Toast.makeText(this, "Last name is required", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(txtUserName.getText().toString().trim().equals("")){
            Toast.makeText(this, "User name is required", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(txtEmail.getText().toString().trim().equals("")){
            Toast.makeText(this, "Email address is required", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(txtPassword.getText().toString().trim().equals("")){
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(txtConfirmPassword.getText().toString().trim().equals("")){
            Toast.makeText(this, "Please confirm password", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).matches()){
            Toast.makeText(this, "Please check you email address", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!txtPassword.getText().toString().equals(txtConfirmPassword.getText().toString())){
            Toast.makeText(this, "Passwords does not match", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            btnRegister.setEnabled(false);
            Toast.makeText(this, "Please wait", Toast.LENGTH_LONG);
            auth.createUserWithEmailAndPassword(txtEmail.getText().toString(), txtPassword.getText()
                    .toString()).addOnCompleteListener(this);
        }

    }



    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if(task.isSuccessful()){
            FirebaseUser user = auth.getCurrentUser();
            String uID = user.getUid();
            FirebaseController fbController = new FirebaseController();
            fbController.addUser(uID,
                    new User(txtLastName.getText().toString(),
                            txtFirstName.getText().toString(),
                            txtUserName.getText().toString()));

            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT);
            Toast.makeText(this, "Please verify your email address", Toast.LENGTH_LONG);
            Log.d("JOCAS","REGISTRATION SUCCESS");
            btnRegister.setEnabled(true);

            finish();
            return;
        }
        else{
            Toast.makeText(this, "Email address already in use", Toast.LENGTH_SHORT).show();
            txtEmail.getText().clear();
            btnRegister.setEnabled(true);
        }
    }
}

