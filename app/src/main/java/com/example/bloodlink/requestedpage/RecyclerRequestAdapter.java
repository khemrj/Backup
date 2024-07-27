package com.example.bloodlink.requestedpage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodlink.R;

import java.util.ArrayList;

public class RecyclerRequestAdapter extends RecyclerView.Adapter<RecyclerRequestAdapter.ViewHolder> {

    private Context context;
    private ArrayList<RequesterModel> arrRequest;

    public RecyclerRequestAdapter(Context context, ArrayList<RequesterModel> arrRequest) {
        this.context = context;
        this.arrRequest = arrRequest;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.requested_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RequesterModel requester = arrRequest.get(position);

        holder.txtName.setText(requester.getName());
        holder.txtBloodGroup.setText(requester.getBloodGroup());
        holder.txtPints.setText(String.valueOf(requester.getPints()));
        holder.txtLocation.setText(requester.getLatitude() + ", " + requester.getLongitude());

        // Set click listener for the image button
        holder.imageButton.setOnClickListener(v -> openGoogleMaps(requester.getLatitude(), requester.getLongitude()));
    }

    @Override
    public int getItemCount() {
        return arrRequest.size();
    }

    private void openGoogleMaps(double latitude, double longitude) {
        Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude + "(Location)");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtBloodGroup, txtPints, txtLocation;
        ImageView imgContact;
        ImageButton imageButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgContact = itemView.findViewById(R.id.imageContact);
            txtName = itemView.findViewById(R.id.txtName);
            txtBloodGroup = itemView.findViewById(R.id.txtBloodGroup);
            txtPints = itemView.findViewById(R.id.txtPints);
            txtLocation = itemView.findViewById(R.id.txtLocation);
            imageButton = itemView.findViewById(R.id.locationPin);
        }
    }
}
