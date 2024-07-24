package com.example.bloodlink.becomeadonor;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bloodlink.R;
import com.example.bloodlink.dashboard.dashboard;
import com.example.bloodlink.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class becomeadonor extends AppCompatActivity {
    private String latLong;
    private String id;
    private String Address;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private final int FINE_PERMISSION_CODE = 1;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;

    DatePickerDialog.OnDateSetListener setListener;


    EditText firstName,middleName,lastName, address,lastdonatedtime,dob,lastDate;
    Button updatebtn, cancelbtn,update,cancel;
    TextView DOB;
    AutoCompleteTextView bloodGroup, gender;


    ArrayList<String> arblood = new ArrayList<>();
    ArrayList<String> argender = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_becomeadonor);
        dob = findViewById(R.id.dob);
        firstName= findViewById(R.id.firstName);
        middleName=findViewById(R.id.middleName);
        lastName=findViewById(R.id.lastName);
        // bloodGroup = findViewById(R.id.bloodGroup);
        address = findViewById(R.id.address);
        lastdonatedtime = findViewById(R.id.lastDate);
        gender = findViewById(R.id.gender);


        updatebtn = findViewById(R.id.update);
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        cancelbtn = findViewById(R.id.cancel);
        bloodGroup = findViewById(R.id.bloodGroup);

        dob.setInputType(InputType.TYPE_CLASS_DATETIME);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
               int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        becomeadonor.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        // on below line we are setting date to our edit text.
                        // dob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        dob.setText(year + "/" + monthOfYear + "/" + dayOfMonth);
                    }
                },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
                // After the DatePickerDialog is dismissed, set the input type to TYPE_NULL
                dob.setInputType(InputType.TYPE_NULL);
            }
        });
        lastdonatedtime.setInputType(InputType.TYPE_CLASS_DATETIME);
        lastdonatedtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        becomeadonor.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        // on below line we are setting date to our edit text.
                        // dob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        lastdonatedtime.setText(year + "/" + monthOfYear + "/" + dayOfMonth);
                    }
                },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
                lastdonatedtime.setInputType(InputType.TYPE_NULL);
            }
        });


        //checkbox ko lagi
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Address = address.getText().toString();
                becomeDonor();

                // Check if the checkbox is checked
//                if (checkBox.isChecked()) {
                    // Check if all EditText fields are filled
                    //  int dayOfMonth = 0;
                    // int monthOfYear=0;
                    // int year = 0;
                    //String D= dob.getText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year).toString();
                    // int s=Integer.parseInt(D);
                    String dobText = dob.getText().toString().trim();// Trim to remove leading/trailing spaces
                    if (isEditTextFilled(firstName) &&
                            isEditTextFilled(lastName)&&
                            isEditTextFilled(bloodGroup) &&
                            isEditTextFilled(address) &&
                            !dobText.isEmpty() &&
                            isEditTextFilled(lastdonatedtime)&&
                            isEditTextFilled(gender)) {
                        // ------All fields are filled, show success message OR DATABASE HALNU

                        // Clear all EditText fields
                        firstName.getText().clear();
                        middleName.getText().clear();
                        lastName.getText().clear();
                        bloodGroup.getText().clear();
                        address.getText().clear();

                        gender.getText().clear();
                        dob.setText("");
                        lastdonatedtime.setText("");



                    } else {
                        // At least one field is empty, show an error message
                        Toast.makeText(becomeadonor.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                    }
//                } else {
//                    // Checkbox is not checked, show a message
//                    Toast.makeText(becomeadonor.this, "Please check the 'Become a Donor' checkbox.", Toast.LENGTH_SHORT).show();
//                }
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(becomeadonor.this, dashboard.class);
                startActivity(intent);
            }
        });

        arblood.add("A+");
        arblood.add("AB+");
        arblood.add("AB-");
        arblood.add("B+");
        arblood.add("B-");
        arblood.add("O-");
        arblood.add("O+");

        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arblood);
        bloodGroup.setAdapter(bloodAdapter);

        //---------------------gender
        argender.add("Male");
        argender.add("Female");
        argender.add("Other");
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, argender);
        gender.setAdapter(genderAdapter);
    }

    // Helper function to check if an EditText is filled
    private boolean isEditTextFilled(EditText editText) {
        return editText.getText() != null && !editText.getText().toString().isEmpty();
    }

    public void becomeDonor() {
        SharedPreferences sharedPreferences = getSharedPreferences("url_prefs", Context.MODE_PRIVATE);
        String URL = sharedPreferences.getString("URL", null);
        String url = URL +"/api/v1/members";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("firstname",firstName.getText() );
            jsonRequest.put("middlename", middleName.getText());
            jsonRequest.put("lastname", lastName.getText());
            jsonRequest.put("dateOfBirth", dob.getText());
            jsonRequest.put("bloodGroup", bloodGroup.getText());
            jsonRequest.put("gender", gender.getText());
            jsonRequest.put("lastTimeOfDonation", lastdonatedtime.getText());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                jsonRequest.put("registrationDate", LocalDate.now());
            }

        } catch (JSONException e) {
           e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    id = response.getString("id");
                    Log.d("memberResponse","id is"+id);
                    updateMemberLocation(id);
                } catch (JSONException e) {
                    Log.d("exceptionInResponse",e.toString());
                }

                Intent intent = new Intent(becomeadonor.this, dashboard.class);
                // This Token has null value but why??

                //startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                Log.d("volleyError", error.toString());
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
        };

        requestQueue.add(jsonObjectRequest);
    }
    public void updateMemberLocation(String id){
        //for lat long generation from Address


        GeoCodeLocation locationAddress = new GeoCodeLocation();
        locationAddress.getAddressFromLocation(Address, getApplicationContext(), new GeoCoderHandler());

        //fetching URL
        SharedPreferences sharedPreferences = getSharedPreferences("url_prefs", Context.MODE_PRIVATE);
        String URL = sharedPreferences.getString("URL", null);
        String latitude = sharedPreferences.getString("latitude",null);
        String longitude = sharedPreferences.getString("longitude",null);
        String url = URL + "/api/v1/member-locations";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JSONObject jsonRequest = new JSONObject();
        try {
            Log.d("Id2","id in memlocationfunctio is"+ id);
            jsonRequest.put("memberLocation",id);
            jsonRequest.put("latitude",latitude);
            jsonRequest.put("longitude",longitude);

        } catch (JSONException e) {
           Log.d("ExceptionJsonbeco",e.toString());
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,jsonRequest,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(becomeadonor.this, "Data updated successfully!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("volleyerrorUpdareMember", error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
                String Token = sharedPreferences.getString("AuthToken", null);
                Log.d("BeDonorTokeninheader",Token);
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer "+Token);

                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);



    }
    private class GeoCoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            SharedPreferences sharedPreferences = getSharedPreferences("url_prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String[] parts = locationAddress.split(" ");
            editor.putString("latitude",parts[0]);
            editor.putString("longitude",parts[1]);
            editor.apply();



            //chandan chat bhandar
            Log.d("Location1",locationAddress);
        }


    }


}








