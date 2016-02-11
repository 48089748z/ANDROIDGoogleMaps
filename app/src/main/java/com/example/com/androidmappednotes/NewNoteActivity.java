package com.example.com.androidmappednotes;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.firebase.client.Firebase;


public class NewNoteActivity extends AppCompatActivity  implements LocationListener
{
    private Location loc;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        dialog = ProgressDialog.show(this, "  TURN ON YOUR GPS PLEASE","We are getting your location.\nThis may take a few seconds.");
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        final TextView title = (TextView) this.findViewById(R.id.ETtitle);
        final TextView description = (TextView) this.findViewById(R.id.ETdescription);
        FloatingActionButton add = (FloatingActionButton) this.findViewById(R.id.fab);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Note newNote = new Note();
                newNote.setTitle(title.getText().toString());
                newNote.setDescription(description.getText().toString());
                newNote.setLatitude(String.valueOf(loc.getLatitude()));
                newNote.setLongitude(String.valueOf(loc.getLongitude()));
                addNoteToFireBase(newNote);
            }
        });
    }
    public void addNoteToFireBase(Note note)
    {
        FirebaseConfig config  = (FirebaseConfig) this.getApplication();
        Firebase ref = config.getMainReference();
        Firebase notesReference = ref.child("notes");
        Firebase fnote = notesReference.push();
        fnote.setValue(note);
        this.finish();
    }

    @Override
    public void onLocationChanged(Location location)
    {
        if (location!=null) {
            loc = location;
            dialog.dismiss();
        }
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onProviderDisabled(String provider) {}
}
