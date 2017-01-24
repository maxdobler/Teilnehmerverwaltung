package de.maxdobler.teilnehmerverwaltung;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AttendeeViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.name)
    TextView nameTextView;

    public AttendeeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Attendee attendee) {
        nameTextView.setText(attendee.getName());
    }
}
