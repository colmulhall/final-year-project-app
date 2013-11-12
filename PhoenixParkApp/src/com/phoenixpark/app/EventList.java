package com.phoenixpark.app;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
 
public class EventList extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventlist_layout);
        JSONArray jArray = null;

        String result = null;

        StringBuilder sb = null;

        InputStream is = null;
        
        TextView tv = (TextView) findViewById(R.id.textView1);


        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        //http post
        try{
             HttpClient httpclient = new DefaultHttpClient();

             //Why to use 10.0.2.2
             HttpPost httppost = new HttpPost("http://10.0.2.2/FYP-Web-Coding/android_connect.php");
             httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
             HttpResponse response = httpclient.execute(httppost);
             HttpEntity entity = response.getEntity();
             is = entity.getContent();
             }catch(Exception e){
                 Log.e("log_tag", "Error in http connection"+e.toString());
            }
        //convert response to string
        try{
              BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
               sb = new StringBuilder();
               sb.append(reader.readLine() + "\n");

               String line="0";
               while ((line = reader.readLine()) != null) {
                              sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
                }catch(Exception e){
                      Log.e("log_tag", "Error converting result "+e.toString());
                }

        String name;
        try{
              jArray = new JSONArray(result);
              JSONObject json_data=null;
              for(int i=0;i<jArray.length();i++)
              {
                     json_data = jArray.getJSONObject(i);
                     name = json_data.getString("title");    //"title" is the column name in the database
                     tv.setText(name);  //set the text of the textview to the title
              }
            }
              catch(JSONException e1){
               Toast.makeText(getBaseContext(), "No Data Found" ,Toast.LENGTH_LONG).show();
              } catch (ParseException e1) {
           e1.printStackTrace();
         }
      }
}