package com.example.bloodlink.donorpage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import java.util.ArrayList;

public class RecyclerDonorAdapter extends RecyclerView.Adapter<RecyclerDonorAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DonorModel> arrDonor;

    RecyclerDonorAdapter(Context context, ArrayList<DonorModel> arrDonor) {
        this.context = context;
        this.arrDonor = arrDonor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.donor_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DonorModel donor = arrDonor.get(position);

        holder.txtName.setText(donor.getName());
        holder.txtAge.setText(donor.getAge());
        holder.txtBloodGroup.setText(donor.getBloodgroup());
        holder.txtPints.setText(String.valueOf(donor.getPints()));
        holder.txtLocation.setText(donor.getLocation());

        holder.acceptButton.setText(donor.getAcceptButtonText());
        if ("Accepted".equals(donor.getAcceptButtonText())) {
            holder.acceptButton.setBackgroundColor(ContextCompat.getColor(context, R.color.orange));
        } else {
            holder.acceptButton.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
        }

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGoogleMaps(donor.getLocation());
            }

            private void openGoogleMaps(String location) {
                // Extract latitude and longitude from location string
                // Example: "Lat: 28.7041, Long: 77.1025"
                String[] parts = location.split(",");
                double latitude = Double.parseDouble(parts[0].substring(parts[0].indexOf(":") + 1).trim());
                double longitude = Double.parseDouble(parts[1].substring(parts[1].indexOf(":") + 1).trim());

                Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude + "(Marker+Title)");

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(mapIntent);
                } else {
                    Toast.makeText(context, "Google Maps app not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement button click logic as needed
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrDonor.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
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
        }
    }
}
