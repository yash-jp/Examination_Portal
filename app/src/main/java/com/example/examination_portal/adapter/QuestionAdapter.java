package com.example.examination_portal.adapter;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class QuestionAdapter extends RecyclerView.Adapter<> {

    public class QuestionHolder extends RecyclerView.ViewHolder{
        public EditText qretQuestionSentence,qrrgOptions;
        public RadioGroup qrrgOptions;

        public QuestionHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
