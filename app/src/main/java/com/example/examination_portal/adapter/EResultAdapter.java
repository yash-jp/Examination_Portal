package com.example.examination_portal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examination_portal.R;
import com.example.examination_portal.model.Result;


import org.w3c.dom.Text;

import java.util.List;

public class EResultAdapter extends RecyclerView.Adapter<EResultAdapter.ResultHolder> {
    private List<Result> resultList;
    private Context context;

    public EResultAdapter(List<Result> resultList, Context context) {
        this.resultList = resultList;
        this.context = context;
    }

    @NonNull
    @Override
    public ResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_row_result, parent, false);

        return new EResultAdapter.ResultHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultHolder holder, int position) {
        Result result = resultList.get(position);
        holder.trtvTestName.setText(result.getTestName());
        holder.trtvTestScore.setText(String.valueOf(result.getTestScore()));
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class ResultHolder extends RecyclerView.ViewHolder{
        TextView trtvTestName,trtvTestScore;
        public ResultHolder(@NonNull View itemView) {
            super(itemView);
            trtvTestName = itemView.findViewById(R.id.trtvTestName);
            trtvTestScore = itemView.findViewById(R.id.trtvTestScore);
        }
    }
}
