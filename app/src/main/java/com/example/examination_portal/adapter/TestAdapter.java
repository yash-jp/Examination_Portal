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

import com.example.examination_portal.Examee.EQuestionActivity;
import com.example.examination_portal.organizer.QuestionActivity;
import com.example.examination_portal.R;
import com.example.examination_portal.model.Test;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestHolder>{

    private List<Test> testList;
    private Context context;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public TestAdapter(List<Test> groupList,Context context) {
        this.testList = groupList;
        this.context = context;

        sharedPreferences = context.getApplicationContext().getSharedPreferences("UserPref",0);
//        editor = sharedPreferences.edit();
    }

    @NonNull
    @Override
    public TestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_row, parent, false);

        return new TestHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TestHolder holder, int position) {
        Test test = testList.get(position);
        holder.grtvName.setText(test.getTestName());
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    public class TestHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView grtvName;

        public TestHolder(@NonNull View itemView) {
            super(itemView);
            grtvName = itemView.findViewById(R.id.gretName);

//            setting onclick listener on recyclerview textview
            grtvName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            Toast.makeText(context,groupList.get(getAdapterPosition()).getGroupName(),Toast.LENGTH_LONG).show();
            if(sharedPreferences.getString("user_type",null).equals("stu")){
                Intent intent = new Intent(context, EQuestionActivity.class);
                    intent.putExtra("test_id",testList.get(getAdapterPosition()).getTestID());
                context.startActivity(intent);
            }else{
                Intent intent = new Intent(context, QuestionActivity.class);
                intent.putExtra("test_id",testList.get(getAdapterPosition()).getTestID());
                context.startActivity(intent);
            }
        }
    }
}
