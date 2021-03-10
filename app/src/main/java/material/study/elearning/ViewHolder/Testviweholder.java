package material.study.elearning.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import material.study.elearning.Interfae.ItemClickListner;
import material.study.elearning.R;

public class Testviweholder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView question,answer;
    public Button submitAnswer, submitTest;
    public ItemClickListner itemClickListner;

    public Testviweholder(@NonNull View itemView) {
        super(itemView);
        question = (TextView) itemView.findViewById(R.id.view_holder_question);
        answer = (TextView) itemView.findViewById(R.id.view_holder_answer);
        submitAnswer = (Button) itemView.findViewById(R.id.view_holder_answer_submit);

    }

    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view, getAdapterPosition(),false);
    }
    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
