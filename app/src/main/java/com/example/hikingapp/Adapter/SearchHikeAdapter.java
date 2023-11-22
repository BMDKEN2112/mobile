package com.example.hikingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hikingapp.HikeDetailActivity;
import com.example.hikingapp.Model.HikeModel;
import com.example.hikingapp.SearchHikeActivity;
import com.example.hikingapp.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SearchHikeAdapter extends RecyclerView.Adapter<SearchHikeAdapter.SearchHikeVH> implements Filterable {
    ArrayList<HikeModel> hike;
    Context context;
    private ArrayList<HikeModel> originalHike;

    public SearchHikeAdapter(ArrayList<HikeModel> hike, Context context){
        this.hike = hike;
        this.context = context;
        this.originalHike = new ArrayList<>(hike);
    }

    @NonNull
    @Override
    public SearchHikeAdapter.SearchHikeVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.hikedata, parent, false);
        return new SearchHikeAdapter.SearchHikeVH(view);
    }
    @Override
    public void onBindViewHolder(@NonNull SearchHikeAdapter.SearchHikeVH holder, int position) {
        HikeModel h = hike.get(position);
        holder.hikeName.setText(String.valueOf(h.getName()));
        holder.hikeLength.setText(String.valueOf(h.getLength()));
        holder.hikeLocation.setText(String.valueOf(h.getLocation()));
        holder.hikeDate.setText(String.valueOf(h.getDate()));
        holder.hikeDifficulty.setText(String.valueOf(h.getDifficulty()));
        holder.hikeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    HikeModel selectedHike = hike.get(position);
                    // Convert the HikeModel to a JSON string using Gson
                    Gson gson = new Gson();
                    String hikeJson = gson.toJson(selectedHike);
                    // Start the HikeDetailActivity and pass the JSON string
                    Intent intent = new Intent(holder.itemView.getContext(), HikeDetailActivity.class);
                    intent.putExtra("Hike_ID", h.getId());
                    intent.putExtra("Hike_JSON", hikeJson);
                    holder.itemView.getContext().startActivity(intent);
                    Toast.makeText(holder.itemView.getContext(), selectedHike.getName() + " is selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return hike.size();
    }

    class SearchHikeVH extends RecyclerView.ViewHolder {
        TextView hikeName, hikeLength, hikeLocation, hikeDifficulty, hikeDate;
        CardView hikeCardView;

        public SearchHikeVH(@NonNull View itemView) {
            super(itemView);
            hikeName = itemView.findViewById(R.id.hikeName);
            hikeLength = itemView.findViewById(R.id.hikeLength);
            hikeLocation = itemView.findViewById(R.id.hikeLocation);
            hikeDifficulty = itemView.findViewById(R.id.hikeDifficulty);
            hikeDate = itemView.findViewById(R.id.hikeDate);
            hikeCardView = itemView.findViewById(R.id.hikeCardView);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search = constraint.toString().toLowerCase().trim();
                ArrayList<HikeModel> filteredList = new ArrayList<>();
                for (HikeModel hikeModel : originalHike) {
                    if (hikeModel.getName().toLowerCase().contains(search)) {
                        filteredList.add(hikeModel);
                    }
                    else if (hikeModel.getLocation().toLowerCase().contains(search)) {
                        filteredList.add(hikeModel);
                    }
                    else if (hikeModel.getDifficulty().toLowerCase().contains(search)) {
                        filteredList.add(hikeModel);
                    }
                    else if (hikeModel.getDescription().toLowerCase().contains(search)) {
                        filteredList.add(hikeModel);
                    }
                    else if (hikeModel.getDate().contains(search)) {
                        filteredList.add(hikeModel);
                    }
                    else if (hikeModel.getLength().contains(search)) {
                        filteredList.add(hikeModel);
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                hike.clear();
                hike.addAll((ArrayList<HikeModel>) results.values);
                notifyDataSetChanged();
            }
        };
    }

}
