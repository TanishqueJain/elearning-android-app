package material.study.elearning.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import material.study.elearning.Interfae.ItemClickListner;
import material.study.elearning.R;

public class SubjectListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        {
public TextView subjectname;
public ItemClickListner itemClickListner;

public SubjectListViewHolder(@NonNull View itemView) {
        super(itemView);
        subjectname = (TextView) itemView.findViewById(R.id.view_holder_subject_name);

        }

@Override
public void onClick(View view) {
        itemClickListner.onClick(view, getAdapterPosition(),false);
        }
public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
        }
        }
