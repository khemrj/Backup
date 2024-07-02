package com.example.bloodlink.requestedpage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodlink.R;

import java.util.ArrayList;

public class RecyclerRequestAdapter extends RecyclerView.Adapter<RecyclerRequestAdapter.ViewHolder> {

    Context context;
    ArrayList<RequesterModel> arrRequest;

    public RecyclerRequestAdapter(Context context, ArrayList<RequesterModel> arrRequest) {
        this.context = context;
        this.arrRequest = arrRequest;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.requested_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RequesterModel requester = arrRequest.get(position);

        holder.txtName.setText(requester.getName());
        holder.txtBloodGroup.setText(requester.getBloodGroup());
        holder.txtPints.setText(String.valueOf(requester.getPints()));
        holder.txtLocation.setText(requester.getLatitude() + ", " + requester.getLongitude());

        // Handle other UI components and listeners as needed
    }

    @Override
    public int getItemCount() {
        return arrRequest.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtBloodGroup, txtPints, txtLocation;
        ImageView imgContact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgContact = itemView.findViewById(R.id.imageContact);
            txtName = itemView.findViewById(R.id.txtName);
            txtBloodGroup = itemView.findViewById(R.id.txtBloodGroup);
            txtPints = itemView.findViewById(R.id.txtPints);
            txtLocation = itemView.findViewById(R.id.txtLocation);
        }
    }
}
