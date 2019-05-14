package com.example.kamilzemczak.mgrandroid.backgroundworker;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class RegisterBackgroundWorker extends AsyncTask<String, Void, String> {
    Context context;

    public RegisterBackgroundWorker(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String register_url = "http://10.0.2.2:8080/registrationa";
        if(type.equals("register")) {
            try {
                String username = params[1];
                String password = params[2];
                String passwordConfirm = params[3];;
                String name = params[4];
                String surname = params[5];
                String stringDate = params[6];
                String gender = params[7];
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username", "UTF-8")+"="+ URLEncoder.encode(username, "UTF-8")+"&"
                        + URLEncoder.encode("password", "UTF-8")+"="+ URLEncoder.encode(password, "UTF-8")+"&"
                        + URLEncoder.encode("passwordConfirm", "UTF-8")+"="+ URLEncoder.encode(passwordConfirm, "UTF-8")+"&"
                        + URLEncoder.encode("name", "UTF-8")+"="+ URLEncoder.encode(name, "UTF-8")+"&"
                        + URLEncoder.encode("surname", "UTF-8")+"="+ URLEncoder.encode(surname, "UTF-8")+"&"
                        + URLEncoder.encode("stringDate", "UTF-8")+"="+ URLEncoder.encode(stringDate, "UTF-8")+"&"
                        + URLEncoder.encode("gender", "UTF-8")+"="+ URLEncoder.encode(gender, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String result = "";
                String line = "";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(String result) {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
