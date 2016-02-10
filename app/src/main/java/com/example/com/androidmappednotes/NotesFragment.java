package com.example.com.androidmappednotes;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment {
    private FirebaseListAdapter<Note> adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ListView lvParkings = (ListView) view.findViewById(R.id.LVnotes);

        FirebaseConfig app = (FirebaseConfig) getActivity().getApplication();
        Firebase ref = app.getMainReference();

        Firebase notes = ref.child("notes");

        adapter = new FirebaseListAdapter<Note>(getActivity(), Note.class, R.layout.row, notes) {
            @Override
            protected void populateView(View view, Note parking, int position) {

                TextView tvName = (TextView) view.findViewById(R.id.tvName);
                tvName.setText(parking.getName());
            }
        };

        lvParkings.setAdapter(adapter);
        return view;
    }
    public NotesFragment() {
        // Required empty public constructor
    }

}
