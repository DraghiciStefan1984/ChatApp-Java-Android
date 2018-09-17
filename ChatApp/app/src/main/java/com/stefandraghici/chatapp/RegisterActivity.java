package com.stefandraghici.chatapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.signin.internal.AuthAccountResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity
{
    // Constants
    public static final String CHAT_PREFS = "ChatPrefs";
    public static final String DISPLAY_NAME_KEY = "username";

    private AutoCompleteTextView emailView;
    private AutoCompleteTextView usernameView;
    private EditText passwordView;
    private EditText confirmPasswordView;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailView = (AutoCompleteTextView) findViewById(R.id.register_email);
        passwordView = (EditText) findViewById(R.id.register_password);
        confirmPasswordView = (EditText) findViewById(R.id.register_confirm_password);
        usernameView = (AutoCompleteTextView) findViewById(R.id.register_username);

        // Keyboard sign in action
        confirmPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
            {
                if (id == R.integer.register_form_finished || id == EditorInfo.IME_NULL)
                {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });

        firebaseAuth=FirebaseAuth.getInstance();
    }

    // Executed when Sign Up button is pressed.
    public void signUp(View v)
    {
        attemptRegistration();
    }

    private void attemptRegistration()
    {
        // Reset errors displayed in the form.
        emailView.setError(null);
        passwordView.setError(null);

        // Store values at the time of the login attempt.
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password))
        {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email))
        {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        }
        else if (!isEmailValid(email))
        {
            emailView.setError(getString(R.string.error_invalid_email));
            focusView = emailView;
            cancel = true;
        }

        if (cancel)
        {
            focusView.requestFocus();
        }
        else
        {
            createFirebaseUser();
        }
    }

    private boolean isEmailValid(String email)
    {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password)
    {
        String confirmedPassword=confirmPasswordView.getText().toString();
        return confirmedPassword.equals(password) && password.length()>4;
    }

    // TODO: Create a Firebase user
    private void createFirebaseUser()
    {
        String email=emailView.getText().toString();
        String password=passwordView.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                Log.d("Chatapp", "create user; "+task.isSuccessful());

                if(!task.isSuccessful())
                {
                    Log.d("Chatapp", "create user failed; ");
                }
            }
        });
    }

    // TODO: Save the display name to Shared Preferences


    // TODO: Create an alert dialog to show in case registration failed
}