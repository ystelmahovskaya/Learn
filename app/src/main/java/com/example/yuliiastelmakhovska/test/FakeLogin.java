package com.example.yuliiastelmakhovska.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class FakeLogin extends AppCompatActivity {
    private CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_fake_login);
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

        Log.i("isLoggedIn",""+isLoggedIn());
        //////////
        //loginButton.setReadPermissions("email");
            /*loginButton.*/
            if(isLoggedIn()){
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("user_id", accessToken.getUserId());
                startActivity(intent);
            }
            else {
                FacebookCallback callback = new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.e("onCancel", "LoginButton");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("user_id", loginResult.getAccessToken().getUserId());
                        startActivity(intent);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.e("onCancel", "LoginButton");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.e("onError", "LoginButton");
                    }
                };
                loginButton.registerCallback(callbackManager, callback);
            }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
}
