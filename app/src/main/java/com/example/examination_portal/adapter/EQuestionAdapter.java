package com.example.examination_portal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examination_portal.R;
import com.example.examination_portal.model.Question;

import java.util.List;

public class EQuestionAdapter extends RecyclerView.Adapter<EQuestionAdapter.EQuestionHolder> {
    private List<Question> questionList;
    private Context context;

    public EQuestionAdapter(List<Question> questionList, Context context) {
        this.questionList = questionList;
        this.context = context;
    }

    @NonNull
    @Override
    public EQuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.equestion_row, parent, false);

        return new EQuestionAdapter.EQuestionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EQuestionHolder holder, int position) {
        Question question = questionList.get(position);

        holder.eqaQuestion.setText(question.getQuestionSentence());
        holder.eqaOptionA.setText(question.getOptionA());
        holder.eqaOptionB.setText(question.getOptionB());
        holder.eqaOptionC.setText(question.getOptionC());
        holder.eqaOptionD.setText(question.getOptionD());

        if(position == questionList.size()-1){
            holder.eqabtnSubmit.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class EQuestionHolder extends RecyclerView.ViewHolder{
        TextView eqaQuestion;
        RadioButton eqaOptionA,eqaOptionB,eqaOptionC,eqaOptionD;
        Button eqabtnSubmit;

        public EQuestionHolder(@NonNull View itemView) {
            super(itemView);
            eqaQuestion = itemView.findViewById(R.id.eqaQuestion);
            eqaOptionA = itemView.findViewById(R.id.eqaOptionA);
            eqaOptionB = itemView.findViewById(R.id.eqaOptionB);
            eqaOptionC = itemView.findViewById(R.id.eqaOptionC);
            eqaOptionD = itemView.findViewById(R.id.eqaOptionD);
            eqabtnSubmit = itemView.findViewById(R.id.eqabtnSubmit);
        }
    }
}
