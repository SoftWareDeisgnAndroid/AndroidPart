package com.example.mylectureroom;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URL;

import java.security.MessageDigest;


public class SignUpActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "155.230.52.54:9900";
    private static String TAG = "phptest";

    private EditText mID;
    private EditText mPassword;
    private EditText mPassword2;
    private TextView mTextViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mID = (EditText)findViewById(R.id.editText_signupID);
        mPassword = (EditText)findViewById(R.id.editText_signupPassword);
        mPassword2 = (EditText)findViewById(R.id.editText_signupPassword2);
        mTextViewResult = (TextView)findViewById(R.id.textView_signup_result);
        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());



        Button buttonInsert = (Button)findViewById(R.id.button_signup_insert);
        Button button_goto = (Button)findViewById(R.id.button_goto_login);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = mID.getText().toString();
                String password = mPassword.getText().toString();
                String password2  = mPassword2.getText().toString();


                GetData task = new GetData();
                task.execute("http://" + IP_ADDRESS + "/dupcheck.php", id, password, password2);


                mID.setText("");
                mPassword.setText("");
                mPassword2.setText("");
            }
        });

        button_goto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;
        String gID;
        String gPassword;
        String gPassword2;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SignUpActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);

            if(result == null) {
                mTextViewResult.setText(errorString);
            }
            else if(gID.equals("") || gPassword.equals("")){
                mTextViewResult.setText("ID나 Password는 공란으로 할 수 없습니다.");
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setMessage("ID나 Password는 공란으로 할 수 없습니다.");
                builder.setTitle("Error")
                        .setCancelable(false)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int i){
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Error");
                alert.show();


            }
            else if (!result.equals("fail")){
                mTextViewResult.setText("Duplicated ID. Please Enter Another ID.");
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setMessage("Duplicated ID");
                builder.setTitle("Error")
                        .setCancelable(false)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int i){
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Error");
                alert.show();
            }
            else if (!gPassword.equals(gPassword2)){
                mTextViewResult.setText("Passwords are not same. Please enter correctly.");
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setMessage("Passwords are not same");
                builder.setTitle("Error")
                        .setCancelable(false)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int i){
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Error");
                alert.show();
            }
            else{

                InsertData task2 = new InsertData();
                task2.execute("http://" + IP_ADDRESS + "/insert.php", gID, gPassword);
            }
        }


        @Override
        protected String doInBackground(String... params) {

            gID = (String)params[1];
            gPassword = (String)params[2];
            gPassword2 = (String)params[3];
            String serverURL = (String)params[0];
            String postParameters = "ID=" + gID;



            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

    class InsertData extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SignUpActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String id = (String)params[1];
            String password = (String)params[2];
            String serverURL = (String)params[0];
            String SHAPassword = SHAEncode(password);
            String postParameters = "id=" + id + "&password=" + SHAPassword;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
            }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
        protected String SHAEncode(String password) {
            try {

                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(password.getBytes("UTF-8"));
                StringBuffer hexString = new StringBuffer();

                for (int i = 0; i < hash.length; i++) {
                    String hex = Integer.toHexString(0xff & hash[i]);
                    if (hex.length() == 1) hexString.append('0');
                    hexString.append(hex);
                }

                return hexString.toString();

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

}
