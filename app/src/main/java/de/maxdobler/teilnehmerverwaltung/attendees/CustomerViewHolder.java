package de.maxdobler.teilnehmerverwaltung.attendees;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.maxdobler.teilnehmerverwaltung.R;

public class CustomerViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.name)
    TextView nameTextView;

    @BindView(R.id.quota)
    TextView quotaTextView;

    @BindView(R.id.userCard)
    CardView userCard;

    public CustomerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Customer customer, boolean isAttendee) {
        nameTextView.setText(customer.getName());
        String quotaText = itemView.getContext().getString(R.string.customer_quota_text, customer.getQuota());
        quotaTextView.setText(quotaText);

        setBackgroundColor(customer, isAttendee);
    }

    private void setBackgroundColor(Customer customer, boolean isAttendee) {
        Context context = itemView.getContext();
        if (customer.isDeactivted()) {
            if (isAttendee) {
                setBackground(ContextCompat.getColor(context, R.color.primaryDisabledBackground));
                return;
            }
            setBackground(ContextCompat.getColor(context, R.color.disabledBackground));
            return;
        }
        if (isAttendee) {
            setBackground(ContextCompat.getColor(context, R.color.colorPrimaryLight));
            return;
        }

        setBackground(ContextCompat.getColor(context, android.R.color.background_light));
    }

    private void setBackground(int color) {
        userCard.setCardBackgroundColor(color);
    }
}
