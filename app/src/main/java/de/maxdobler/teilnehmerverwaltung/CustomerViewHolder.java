package de.maxdobler.teilnehmerverwaltung;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.name)
    TextView nameTextView;

    @BindView(R.id.userCard)
    CardView userCard;

    public CustomerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Customer customer, boolean isAttendee) {
        nameTextView.setText(customer.getName());
        int color = ContextCompat.getColor(itemView.getContext(), android.R.color.background_light);
        if (isAttendee) {
            color = ContextCompat.getColor(itemView.getContext(), R.color.colorPrimaryLight);
        }
        userCard.setCardBackgroundColor(color);
    }
}
