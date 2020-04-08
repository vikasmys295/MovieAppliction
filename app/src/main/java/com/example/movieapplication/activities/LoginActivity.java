package com.example.movieapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.movieapplication.MainActivity;
import com.example.movieapplication.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth auth;
    int RC_SIGN_IN = 0;
    EditText username,userpassword;
    Button btn_login;
    String PREF_NAME = "Login_shared_preferences";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //sharedPreferences to store values
        sharedPreferences = getSharedPreferences(PREF_NAME,0);
        editor = sharedPreferences.edit();

        // getting references
        username = (EditText)findViewById(R.id.name);
        userpassword = (EditText)findViewById(R.id.password);
        btn_login = (Button)findViewById(R.id.login);
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        //adding clickListeners for butttons
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation();
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });

        FirebaseApp.initializeApp(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("312988066319-go7glptah9rkpkglqrktjke2938da4pa.apps.googleusercontent.com")
                .requestEmail()
                .build();
        auth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    //purpose only once login is performed
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if(user!=null || sharedPreferences.getBoolean("loginned",false)){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            // Signed in successfully, show authenticated UI.
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("firebase", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signin", "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("signin_error", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    //validation for username and password
    private void Validation(){
        String name = username.getText().toString();
        String pass = userpassword.getText().toString();
        if(name.contains("vikas")&&pass.contains("123")){
            editor.putBoolean("loginned",true);
            editor.apply();
            editor.commit();
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }else if(name.contains("varun")&&pass.contains("789")){
            editor.putBoolean("loginned",true);
            editor.apply();
            editor.commit();
            Intent login_intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(login_intent);
        }else{
            Toast.makeText(getApplicationContext(), "invalid username and password", Toast.LENGTH_SHORT).show();
        }
    }
}
