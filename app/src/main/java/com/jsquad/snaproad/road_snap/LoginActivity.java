package com.jsquad.snaproad.road_snap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LoginActivity extends AppCompatActivity implements OnCompleteListener<AuthResult> {

    private EditText txtEmail;
    private EditText txtPassword;
    private Button btnLogin;
    private Button btnSignUp;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        //locationTest();
        init();
    }


    private void init(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //authenticateUser(txtEmail.getText().toString(), txtPassword.getText().toString());
                //test
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("uID", "Jsu7Javg0ySQQY4LocnpJk9GsLU2");
                startActivity(intent);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void authenticateUser(String email, String password){
        Toast.makeText(this, "Please wait...", Toast.LENGTH_LONG);
        btnLogin.setEnabled(false);
        btnSignUp.setEnabled(false);
        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if(task.isSuccessful()){
            FirebaseUser user = auth.getCurrentUser();
            String uID = user.getUid();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("uID", uID);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Authentication failed", Toast.LENGTH_LONG);
            btnLogin.setEnabled(true);
            btnSignUp.setEnabled(true);
        }
    }
}
