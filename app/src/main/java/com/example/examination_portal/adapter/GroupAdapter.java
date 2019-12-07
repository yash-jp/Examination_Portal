package com.example.examination_portal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examination_portal.R;
import com.example.examination_portal.model.Group;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupHolder>{

    private List<Group> groupList;

    public GroupAdapter(List<Group> groupList) {
        this.groupList = groupList;
    }

    @NonNull
    @Override
    public GroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_row, parent, false);

        return new GroupHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupHolder holder, int position) {
        Group group = groupList.get(position);
        holder.gretName.setText(group.getGroupName());
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public class GroupHolder extends RecyclerView.ViewHolder{

        public EditText gretName;

        public GroupHolder(@NonNull View itemView) {
            super(itemView);
            gretName = itemView.findViewById(R.id.gretName);
        }
    }

}
