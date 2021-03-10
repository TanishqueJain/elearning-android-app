package material.study.elearning.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import material.study.elearning.Interfae.ItemClickListner;
import material.study.elearning.R;

public class ClassListViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView classname;
    public ItemClickListner itemClickListner;

    public ClassListViewHolder(@NonNull View itemView) {
        super(itemView);
        classname = (TextView) itemView.findViewById(R.id.view_holder_class_name);

    }

    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view, getAdapterPosition(),false);
    }
    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
