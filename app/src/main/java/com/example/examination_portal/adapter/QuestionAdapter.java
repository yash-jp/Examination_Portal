package com.example.examination_portal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examination_portal.R;
import com.example.examination_portal.model.Group;
import com.example.examination_portal.model.Question;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionHolder> {
    private List<Question> questionList;
    private Context context;

    public QuestionAdapter(List<Question> questionList, Context context) {
        this.questionList = questionList;
        this.context = context;
    }

    @NonNull
    @Override
    public QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.question_row, parent, false);

        return new QuestionAdapter.QuestionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionHolder holder, int position) {
        Question question = questionList.get(position);

        holder.qretQuestionSentence.setText(question.getQuestionSentence());
        holder.qretOptionA.setText(question.getOptionA());
        holder.qretOptionB.setText(question.getOptionB());
        holder.qretOptionC.setText(question.getOptionC());
        holder.qretOptionD.setText(question.getOptionD());
        holder.qretAnswer.setText(question.getQuestionAnswer());
    }

    public int getItemCount() {
        return this.questionList.size();
    }

    public class QuestionHolder extends RecyclerView.ViewHolder{
        public EditText qretQuestionSentence,qretOptionA,qretOptionB,qretOptionC,qretOptionD,qretAnswer;

        public QuestionHolder(@NonNull View itemView) {
            super(itemView);
            qretQuestionSentence = itemView.findViewById(R.id.qretQuestionSentence);
            qretOptionA = itemView.findViewById(R.id.qretOptionA);
            qretOptionB = itemView.findViewById(R.id.qretOptionB);
            qretOptionC = itemView.findViewById(R.id.qretOptionC);
            qretOptionD = itemView.findViewById(R.id.qretOptionD);
            qretAnswer = itemView.findViewById(R.id.qretAnswer);
        }
    }
}
