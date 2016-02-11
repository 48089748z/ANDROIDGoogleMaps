package com.example.com.androidmappednotes;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;
import com.squareup.picasso.Picasso;
/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        final ListView notesList = (ListView) view.findViewById(R.id.LVnotes);
        FirebaseConfig config  = (FirebaseConfig) getActivity().getApplication();
        Firebase ref = config.getMainReference();
        final Firebase notesReference = ref.child("notes");
        FirebaseListAdapter<Note> adapter = new FirebaseListAdapter<Note>(getActivity(), Note.class, R.layout.listview_layout, notesReference) {
            @Override
            protected void populateView(View view, Note note, int position) {
                TextView title = (TextView) view.findViewById(R.id.TVtitle);
                TextView description = (TextView) view.findViewById(R.id.TVdescription);
                TextView latlng = (TextView) view.findViewById(R.id.TVlatlng);
                ImageView image = (ImageView) view.findViewById(R.id.IVimage);
                title.setText(note.getTitle());
                description.setText(note.getDescription());
                latlng.setText("Latitude: " + note.getLatitude() + "\nLongitude: " + note.getLongitude());

                //If there is no image to be loaded, load this.
                Picasso.with(getContext()).load(R.drawable.noimage).fit().into(image);

                //Else load the image that was taken.
                //Picasso.with(getContext()).load(note.getImagePath()).fit().into(image);
            }
        };
        notesList.setAdapter(adapter);
        return view;
    }
    public NotesFragment() {
    }
}
