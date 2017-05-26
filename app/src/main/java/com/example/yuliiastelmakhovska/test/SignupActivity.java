package com.example.yuliiastelmakhovska.test;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SignupActivity extends AppCompatActivity {
EditText input_name;
    EditText input_email;
    EditText input_password;
    Button btn_signup;
    TextView link_login;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        input_name=(EditText)findViewById(R.id.input_name);
        input_email=(EditText)findViewById(R.id.input_email);
        input_password=(EditText)findViewById(R.id.input_password);
        btn_signup=(Button)findViewById(R.id.btn_signup);
        link_login=(TextView)findViewById(R.id.link_login);
        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String name = input_name.getText().toString();
                String email = input_email.getText().toString();
                String password = input_password.getText().toString();
                if (!validate(name,email,password)) {
                    onSignupFailed();
                    return;
                }
                else{

UserSignupTask task= new UserSignupTask(name,email,password);
          task.execute();

                }
            }

        });

    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        btn_signup.setEnabled(true);
    }

    public boolean validate(String name, String email, String password) {
        boolean valid = true;

        if (name.isEmpty() || name.length() < 3) {
            input_name.setError("at least 3 characters");
            valid = false;
        } else {
            input_name.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.setError("enter a valid email address");
            valid = false;
        } else {
            input_email.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            input_password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            input_password.setError(null);
        }

        return valid;
    }

    public class UserSignupTask extends AsyncTask<Void, Void, String> {

        private  String mName;
        private  String mEmail;
        private  String mPassword;
String id;
        UserSignupTask(String name, String email, String password) {
            mName = name;
            mEmail = email;
            mPassword = password;
        }

//        @Override
//        protected void onPreExecute() {
//         //   super.onPreExecute();
//            try {
//                String s=verifyEmail(mEmail);
//                Log.i("onPreExecute",""+s);
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (LoginActivity.UserLoginTask.DownloadException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } catch (DownloadException e) {
//                e.printStackTrace();
//            }
//
//        }

        protected String verifyEmail(String email) throws IOException, LoginActivity.UserLoginTask.DownloadException, JSONException, DownloadException {
            String exist;


            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;

            String requestUrl = "http://"+MainActivity.ip+"/Users/count/"+email;
            URL url = null;
            try {
                url = new URL(requestUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("GET");
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                String response = convertInputStreamToString(inputStream);
                Log.i("response",""+response);
                exist = response;
            } else {
                throw new DownloadException("Failed to fetch data!!");
            }
            Log.i("exist", ""+exist);
            return exist;
        }

        protected String getID(String email) throws IOException, LoginActivity.UserLoginTask.DownloadException, JSONException, DownloadException {
            String userId="";
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            String elevationRequestUrl = "http://"+MainActivity.ip+"/Users/"+email;
            URL url = null;
            try {
                url = new URL(elevationRequestUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("GET");
            int statusCode = urlConnection.getResponseCode();
            Log.i("statusCode",""+statusCode);
            if (statusCode == 200) {
                Log.i("statusCode", "200");
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                String response = convertInputStreamToString(inputStream);
                userId = parseUser_id(response);
            } else {
                throw new DownloadException("Failed to fetch data!!");
            }
            Log.i("userId", ""+userId);
            return userId;
        }

        private String parseUser_id(String response) throws JSONException {

            String userId="";
            try {

                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has("_id")) {
                    userId = jsonObject.getString("_id");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("user_id",""+userId);
            return userId;
        }

        private String convertInputStreamToString(InputStream inputStream) throws IOException {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";

            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            /* Close Stream */
            if (null != inputStream) {
                inputStream.close();
            }
            return result;
        }

        class DownloadException extends Exception {

            public DownloadException(String message) {
                super(message);
                Log.d("DownloadException", message);
            }
        }


        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                if(verifyEmail(mEmail).equals("0")) {
                    postUser(mName, mEmail, mPassword);
                    id=getID(mEmail);
                }
                else{
                   finish();
                }//TODO info if email is not unik
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (DownloadException e) {
                e.printStackTrace();
            } catch (LoginActivity.UserLoginTask.DownloadException e) {
                e.printStackTrace();
            }

            return id;
        }
protected void postUser(String mName, String mEmail, String mPassword )throws IOException{
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL("http://"+MainActivity.ip+"/Users");
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            String urlParameters= "user_name=" + mName +
                    "&user_password=" + mPassword+
            "&user_email=" + mEmail;
            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.writeBytes (urlParameters);
            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }

}

        @Override
        protected void onPostExecute(String s) {
            if (s!=null){
//                Intent intent= new Intent(getApplicationContext(), MainActivity.class);
//                intent.putExtra("user_id",s);
//                startActivity(intent);
//                finish();
            }
        }
    }
}
