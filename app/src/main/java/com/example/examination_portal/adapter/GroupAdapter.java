package com.example.examination_portal.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examination_portal.R;
import com.example.examination_portal.Examee.TestActivity;
import com.example.examination_portal.model.Group;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupHolder>{

    private List<Group> groupList;
    private Context context;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public GroupAdapter(List<Group> groupList,Context context) {
        this.groupList = groupList;
        this.context = context;

        sharedPreferences = context.getApplicationContext().getSharedPreferences("UserPref",0);
//        editor = sharedPreferences.edit();
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
        holder.grtvName.setText(group.getGroupName());
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public class GroupHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView grtvName;

        public GroupHolder(@NonNull View itemView) {
            super(itemView);
            grtvName = itemView.findViewById(R.id.gretName);

//            setting onclick listener on recyclerview textview
            grtvName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(sharedPreferences.getString("user_type",null).equals("stu")){
                Intent intent = new Intent(context, TestActivity.class);
                intent.putExtra("group_id",groupList.get(getAdapterPosition()).getGroupID());
                context.startActivity(intent);
            }else{
                Intent intent = new Intent(context, TestActivity.class);
                intent.putExtra("group_id",groupList.get(getAdapterPosition()).getGroupID());
                context.startActivity(intent);
            }


        }
    }
}
