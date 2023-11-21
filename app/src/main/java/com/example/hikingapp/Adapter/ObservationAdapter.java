package com.example.hikingapp.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hikingapp.Database.DatabaseHelper;
import com.example.hikingapp.Model.ObservationModel;
import com.example.hikingapp.R;
import com.example.hikingapp.UpdateObservationActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ObservationVH> {
    ArrayList<ObservationModel> observations;

    Context context;

    public ObservationAdapter(ArrayList<ObservationModel> observations, Context context){
        this.observations = observations;
        this.context = context;
    }

    @NonNull
    @Override
    public ObservationAdapter.ObservationVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.observationdata, parent, false);
        return new ObservationVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObservationVH holder, int position) {
        ObservationModel observationModel = observations.get(position);
        holder.observationName.setText(String.valueOf(observationModel.getObservationName()));
        holder.observationComment.setText(String.valueOf(observationModel.getObservationComment()));
        holder.observationImage.setImageBitmap(observationModel.getObservationImage());

        holder.observationCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.bottomsheetdialoglayout);

                LinearLayout updateButton = dialog.findViewById(R.id.dialogUpdate);
                LinearLayout deleteButton = dialog.findViewById(R.id.dialogDelete);

                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, UpdateObservationActivity.class);
                        if (context != null && observationModel != null) {
                            intent.putExtra("Observation_ID", observationModel.getObservationID());
    //                        intent.putExtra("Observation_Name", observationModel.getObservationName());
    //                        intent.putExtra("Observation_Comment", observationModel.getObservationComment());
    //                        intent.putExtra("Observation_Time", observationModel.getObservationTime());
    //
    //                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
    //                        observationModel.getObservationImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
    //                        byte[] imageBytes = stream.toByteArray();
    //                        intent.putExtra("Observation_Image", imageBytes);

                            context.startActivity(intent);
                            dialog.dismiss();
                            Toast.makeText(context, "Edit is clicked", Toast.LENGTH_SHORT).show();
                        }else {
                            Log.e("UpdateObservation", "ObservationModel is null");
                        }
                    }
                });

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Toast.makeText(context, "Delete is successfully", Toast.LENGTH_SHORT).show();
                        if (context != null && observationModel != null) {
                            DatabaseHelper dbHelper = new DatabaseHelper(context);
                            dbHelper.deleteObservation(observationModel.getObservationID());

                        } else {
                            if (context == null) {
                                Log.e("DeleteObservation", "Context is null");
                            }
                            if (observationModel == null) {
                                Log.e("DeleteObservation", "ObservationModel is null");
                            }
                        }

                    }
                });

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return observations.size();
    }

    class ObservationVH extends RecyclerView.ViewHolder {
        TextView observationName, observationComment;
        ImageView observationImage;

        CardView observationCardView;
        public ObservationVH(@NonNull View itemView) {
            super(itemView);
            observationName = itemView.findViewById(R.id.observationName);
            observationComment = itemView.findViewById(R.id.observationComment);
            observationImage = itemView.findViewById(R.id.observationImage);
            observationCardView = itemView.findViewById(R.id.observationCardView);
        }
    }
}
