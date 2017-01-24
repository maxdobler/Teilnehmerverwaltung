package de.maxdobler.teilnehmerverwaltung.events;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.maxdobler.teilnehmerverwaltung.R;

public class EventsFragment extends Fragment {
    private OnEventFragmentListener mListener;

    public EventsFragment() {
    }

    public static EventsFragment newInstance() {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.events_fragment, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEventFragmentListener) {
            mListener = (OnEventFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnEventFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnEventFragmentListener {
        void onEventSelected(String eventKey);
    }
}
