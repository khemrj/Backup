package com.example.bloodlink.myprofile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bloodlink.R;
import com.example.bloodlink.becomeadonor.becomeadonor;

public class myprofile extends AppCompatActivity {
TextView textView, txtName,txtAge,txtBloodGroup,txtLocation,txtType;
Button button2;//declaration for button2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

}