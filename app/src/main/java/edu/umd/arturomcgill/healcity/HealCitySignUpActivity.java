package edu.umd.arturomcgill.healcity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;


public class HealCitySignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    EditText firstNameText;
    EditText lastNameText;
    EditText emailText;
    EditText mobileText;
    EditText passwordText;
    EditText reEnterPasswordText;
    Button signupButton;
    TextView loginLink;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heal_city_signup);
        //ButterKnife.bind(this);
        firstNameText = (EditText) findViewById(R.id.input_first_name);
        lastNameText = (EditText) findViewById(R.id.input_last_name);
        emailText = (EditText) findViewById(R.id.input_email);
        mobileText = (EditText) findViewById(R.id.input_mobile);
        passwordText = (EditText) findViewById(R.id.input_password);
        reEnterPasswordText = (EditText) findViewById(R.id.input_reEnterPassword);
        signupButton = (Button) findViewById(R.id.btn_signup);
        loginLink = (TextView) findViewById(R.id.link_login);

        mAuth = FirebaseAuth.getInstance();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup(firstNameText.getText().toString(), lastNameText.getText().toString(),
                        emailText.getText().toString(), passwordText.getText().toString());
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
//                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
//                startActivity(intent);
                finish();
                //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup(final String firstName, final String lastName, final String email, final String password) {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        signupButton.setEnabled(false);

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(HealCitySignUpActivity.this, "Registration successful.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();
                            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                            User userAccount = new User(email, userId, firstName, lastName);
                            mRef.child("users").child(userId).setValue(userAccount);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(HealCitySignUpActivity.this, "Registration failed.",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]


        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        // progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Intent intent = new Intent(HealCitySignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();

        signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String firstName = firstNameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        String email = emailText.getText().toString();
        String mobile = mobileText.getText().toString();
        String password = passwordText.getText().toString();
        String reEnterPassword = reEnterPasswordText.getText().toString();
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}";



        if(firstName.isEmpty()){
            firstNameText.setError("first name is required");
            valid = false;
        }
        else{
            firstNameText.setError(null);
        }

        if(lastName.isEmpty()){
            lastNameText.setError("last name is required");
            valid = false;
        }
        else{
            lastNameText.setError(null);
        }
        if (email.isEmpty()) {
            emailText.setError("email is required");
            valid = false;
        }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                emailText.setError("enter a valid email address");
                valid = false;
        } else {
            emailText.setError(null);
        }

        if(!mobile.isEmpty() && mobile.length() !=10){
            mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        }
        else {
            mobileText.setError(null);
        }

        if (password.isEmpty()) {
            passwordText.setError("password is required");
            valid = false;
        }
        else if (password.length() < 8 || !password.matches(pattern)){
            passwordText.setError("not a valid password");
            valid = false;
        }
        else{
            passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || !(reEnterPassword.equals(password))) {
            reEnterPasswordText.setError("password is required");
            valid = false;
        }
        else if (!(reEnterPassword.equals(password))){
            reEnterPasswordText.setError("Passwords Do not match");
            valid = false;
        }
        else {
            reEnterPasswordText.setError(null);
        }

        return valid;
    }
}

