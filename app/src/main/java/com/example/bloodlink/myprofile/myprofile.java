package com.example.bloodlink.myprofile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bloodlink.R;
import com.example.bloodlink.becomeadonor.becomeadonor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class myprofile extends AppCompatActivity {
TextView textView, txtName,txtAge,txtBloodGroup,txtLocation,txtType;
Button button2;//declaration for button2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        getMemberById();
        setContentView(R.layout.activity_myprofile);
        txtName=findViewById(R.id.txtName);
        txtAge=findViewById(R.id.txtAge);
        txtBloodGroup=findViewById(R.id.txtBloodGroup);
       // txtPints=findViewById(R.id.txtPints);
        txtLocation=findViewById(R.id.txtLocation);
        txtType=findViewById(R.id.txtType);
        textView=findViewById(R.id.textView);
        button2=findViewById(R.id.button2);//find and assigning the value button2
        txtName.setText("Suman Shah");
        txtAge.setText("23");
        txtBloodGroup.setText("B+");
        txtLocation.setText("Dhangadhi");
        txtType.setText("Receiver");
String s=txtName.getText().toString();
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(myprofile.this, s+" has  Beome A Donor", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(myprofile.this, becomeadonor.class);
                startActivity(intent);
                txtName.setText("");
                txtAge.setText("");
                txtBloodGroup.setText("");
                txtLocation.setText("");
                txtType.setText("");
            }
        });
    }
    public void getMemberById(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        SharedPreferences sharedPreferencesurl = getSharedPreferences("url_prefs", Context.MODE_PRIVATE);
        String URL = sharedPreferencesurl.getString("URL", null);
        SharedPreferences sharedPreferences = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId",null);
        String url = URL +"/api/v1/members";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    String memberName = null,bloodGroup =null,dob=null;
                    Log.d("rsponse is",response.toString());
                    int flag = 0;
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                       JSONObject User =  jsonObject.getJSONObject("userInfo");
                       if( userId.equals((User.getString("id")))){
                           flag = 1;
                            memberName = jsonObject.getString("firstname") + jsonObject.getString("lastname");
                            bloodGroup = jsonObject.getString("bloodGroup");
                            dob = jsonObject.getString("dateOfBirth");

                           break;
                       }

                    }
                    if(flag == 1){
                        Toast.makeText(myprofile.this, memberName + bloodGroup + dob, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(myprofile.this, "Please become donor to help people", Toast.LENGTH_SHORT).show();
                    }

//                    Log.d("myprofileOnsesponse","onresonse called");
//                    String id = response.getString("id").toString();
//                    String name = response.getString("firstname").toString();
//                    if(id.isEmpty()){
//                        Toast.makeText(myprofile.this, "please become a donor and help people", Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//
//                    }
                }
                catch (Exception e){
                    Log.d("exceptionInMyprofile",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("FindMemberByUseriderror",error.toString());
                Toast.makeText(myprofile.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
//                Intent i = getIntent();
//                String Token = i.getStringExtra("Token");
                SharedPreferences sharedPreferences = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
                String Token = sharedPreferences.getString("AuthToken", null);
                Log.d("BeDonorTokeninheader",Token);
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer "+Token);

                return headers;
            }
        };;

        requestQueue.add(jsonArrayRequest);

    }

}