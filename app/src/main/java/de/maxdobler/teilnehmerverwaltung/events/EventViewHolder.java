package de.maxdobler.teilnehmerverwaltung.events;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.maxdobler.teilnehmerverwaltung.R;

public class EventViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.name)
    TextView nameTextView;

    @BindView(R.id.attendeesCount)
    TextView attendeesCountTextView;

    public EventViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Event event) {
        nameTextView.setText(event.getName());
        attendeesCountTextView.setText(itemView.getContext().getString(R.string.event_attendees_count, event.getAttendeesCount()));
    }

    public void setSelected(boolean isSelected) {
        if (isSelected) {
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.colorPrimaryLight));
        } else {
            TypedValue colorValue = new TypedValue();
            itemView.getContext().getTheme().resolveAttribute(android.R.attr.colorBackground, colorValue, true);
            itemView.setBackgroundColor(colorValue.data);
        }
    }

    public void bind(String eventName) {
        this.nameTextView.setText(eventName);
    }
}
