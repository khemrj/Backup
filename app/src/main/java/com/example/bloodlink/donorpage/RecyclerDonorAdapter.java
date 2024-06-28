package com.example.bloodlink.donorpage;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodlink.MapsActivity;
import com.example.bloodlink.R;
import com.example.bloodlink.googleMaps.Maps;

import java.util.ArrayList;

public class RecyclerDonorAdapter extends RecyclerView.Adapter<RecyclerDonorAdapter.ViewHolder> {
    @NonNull
    Context context;
    String imageButton;//EX09april
    Button call;
    ArrayList<DonorModel> arrDonor;
    RecyclerDonorAdapter(@NonNull Context context, ArrayList<DonorModel> arrDonor) {
        this.context = context;
        this.arrDonor = arrDonor;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.donor_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //here problem is showning don't treat position as fixed ;only use immedately and call 'holder.getAdapterPosition()' to look it up later in this fun public void onBindViewHolder(@NonNull ViewHolder holder, int position)
        int adapterPosition = holder.getAdapterPosition();


        holder.imgContact.setImageResource(arrDonor.get(position).img);
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {

                    arrDonor.get(adapterPosition);
//                    Intent intent=new Intent(v.getContext(),location.class);
//                    notifyDataSetChanged();
                }
            }
        });

        holder.txtName.setText(arrDonor.get(position).name);
        holder.txtAge.setText(arrDonor.get(position).age);
        holder.txtBloodGroup.setText(arrDonor.get(position).bloodgroup);
        holder.txtPints.setText(arrDonor.get(position).pints);
        holder.txtLocation.setText(arrDonor.get(position).location);

        // Bind other data as before

        // Set the visibility of the button based on the model's property
        //---yo condition hamro button visible garauna lagi ho
//        if (arrDonor.get(position).isAcceptButtonVisible()) {
//            holder.acceptButton.setVisibility(View.VISIBLE);
//        } else {
//            holder.acceptButton.setVisibility(View.GONE);
//        }



        // Set the text of the button based on the model's property
        holder.acceptButton.setText(arrDonor.get(position).getAcceptButtonText());
        if ("Accepted".equals(arrDonor.get(adapterPosition).getAcceptButtonText())) {
            // Change the background color when "Accepted" is displayed
            //ContextCompat mean
            holder.acceptButton.setBackgroundColor(ContextCompat.getColor(context, R.color.orange));
        } else {
            // Set the default background color for other cases
            holder.acceptButton.setBackgroundColor(ContextCompat.getColor(context, com.google.android.material.R.color.design_default_color_primary));
        }
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Retrieve the donor object at the current position
                    DonorModel donor = arrDonor.get(position);
                    // Get the location text from the TextView
                    String location = holder.txtLocation.getText().toString();
                    // Create an intent to launch the LocationActivity.class
                    //  Intent intent = new Intent(v.getContext(), MapsActivity.class);
                    // Pass any necessary data to the LocationActivity
                    // Pass the location text to the LocationActivity
                    //intent.putExtra("location", location);
                    //  intent.putExtra("donor_id", donor.getId(txtLocation)); // Example: Passing donor ID
                    // Start the LocationActivity
                    // v.getContext().startActivity(intent);

                    openGoogleMaps();
                }

            }

            private void openGoogleMaps() {
                // Latitude and Longitude of the location of user
                //make latlong dynamic from database
                double latitude = 28.733350;
                double longitude = 80.572492;

                Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude + "(Marker+Title)");

                // Create an Intent with action VIEW and the Uri
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                // Set the package of the application to be opened (Google Maps)
                mapIntent.setPackage("com.google.android.apps.maps");

                // Verify if there's an application available to handle the Intent
                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    // Start the activity
                    context.startActivity(mapIntent);
                } else {
                    Toast.makeText(context, "maps not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Handle the "Accept" button click
        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    // Update the model to hide the button when clicked
                    // arrDonor.get(adapterPosition).setAcceptButtonVisible(false);
                    // Notify the adapter that the data has changed to refresh the UI

                    // Set the text of the button based on the model's property
                    //  holder.acceptButton.setText(arrDonor.get(position).getAcceptButtonText());
                    arrDonor.get(adapterPosition).setAcceptButtonText("Call");


                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrDonor.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtAge, txtBloodGroup, txtPints, txtLocation;
        ImageView imgContact;
        Button acceptButton;
        ImageButton imageButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgContact = itemView.findViewById(R.id.imageContact);
            txtName = itemView.findViewById(R.id.txtName);
            txtAge = itemView.findViewById(R.id.txtAge);
            txtBloodGroup = itemView.findViewById(R.id.txtBloodGroup);
            txtPints = itemView.findViewById(R.id.txtPints);
            txtLocation = itemView.findViewById(R.id.txtLocation);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            imageButton = itemView.findViewById(R.id.locationPin);

            // Set OnClickListener for the ImageButton
        }
    }
}
