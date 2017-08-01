package com.flexfare.android;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Blessyn on 7/31/2017.
 */

public class RegistrationPresenter {

    RegistrationValidation regValid;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public RegistrationPresenter(RegistrationValidation regValid){
        this.regValid = regValid;
    }

    public void doValidation(String firstname, String lastname, String username, String password,String email, String mobile){

        if(firstname.equals("") || lastname.equals("") || username.equals("") || password.equals("") || email.equals("")|| mobile.equals("")){
            regValid.showEmptyFieldsError();
        }
        else if(!email.matches(emailPattern)){
            regValid.showInvalidEmailError();
        }
        else if(mobile.length() < 8){
            regValid.showInvalidPhoneNumberError();
        }
        else{
            regValid.doRegistratio();
        }


    }

    public void registerUser(List<Registration> registration) {
        String f_name = registration.get(0).getFirstname();
        String l_name = registration.get(0).getLastname();
        String u_name = registration.get(0).getUsername();
        String p_word = registration.get(0).getPassword();
        String email = registration.get(0).getEmail();
        String mobile = registration.get(0).getMobile();

        Log.i("FlexApp", f_name + "\n" + l_name);

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        DataOutputStream dataout = null;

        // Will contain the raw JSON response as a string.
        String result = null;

        try {

            URL url = new URL("http://www.flexfare.org/signup.php");


            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            urlConnection.connect();

            Map<String, Object> params = new LinkedHashMap<>();
            params.put("firstname", f_name);
            params.put("lastname", l_name);
            params.put("username", u_name);
            params.put("email", email);
            params.put("password", p_word);
            params.put("phonenumber", mobile);

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0) {
                    postData.append('&');
                }
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            String urlParameters = postData.toString();

            dataout = new DataOutputStream(urlConnection.getOutputStream());
            // perform POST operation
            dataout.writeBytes(urlParameters);

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.

            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                Log.i("FlexApp", "Buffer Empty");
            }
            result = buffer.toString();

        } catch (Exception e) {
            Log.e("FlexApp", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("FlexApp", "Error closing stream", e);
                }
            }
        }
        Log.i("FlexApp", result);

        Gson jsonResponse = new Gson();
        Response convertedResponse = jsonResponse.fromJson(result, Response.class);

        Log.i("FlexApp", convertedResponse.getStatus() + "\n" + convertedResponse.getMessage());

        if(convertedResponse.getStatus() == "1"){
            regValid.showRegistrationSuccess();
        }

       // regValid.showRegistrationSuccess();
        /*try{

            JSONObject obj = new JSONObject(result);
            String status = obj.getString("status");

            Log.i("FlexApp", "The code :"+ status);
            if(status.equals("1")){
                regValid.showRegistrationSuccess();
                Log.i("FlexApp", "The code :"+ status);
            }

        }catch (JSONException je){
            Log.e("FlexApp")
        }*/

    }


}
