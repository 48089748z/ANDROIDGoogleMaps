package com.example.com.androidmappednotes;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import static android.content.res.Resources.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {

    private MapView map;
    private MyLocationNewOverlay myLocation;
    private ScaleBarOverlay scaleBarOverlay;
    private CompassOverlay compassOverlay;
    private IMapController mapController;
    private RadiusMarkerClusterer noteMarker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        map = (MapView) view.findViewById(R.id.Mmap);
        configurateMap();
        setMyLocationMarker();
        setNotesMarkers();
        map.invalidate();
        return view;
    }

    public void setMyLocationMarker()
    {
        noteMarker = new RadiusMarkerClusterer(getContext());
        map.getOverlays().add(noteMarker);
        Bitmap clusterIcon = ((BitmapDrawable)getResources().getDrawable(R.drawable.marker_cluster)).getBitmap();
        noteMarker.setIcon(clusterIcon);
        noteMarker.setRadius(100);

    }
    private void setNotesMarkers()
    {
        FirebaseConfig config = (FirebaseConfig) getActivity().getApplication();
        Firebase mainReference = config.getMainReference();
        final Firebase notes = mainReference.child("notes");
        notes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Note note = postSnapshot.getValue(Note.class);
                    Marker marker = new Marker(map);
                    GeoPoint point = new GeoPoint(Double.parseDouble(note.getLatitude()), Double.parseDouble(note.getLongitude()));
                    marker.setPosition(point);
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    marker.setIcon(getResources().getDrawable(R.drawable.marker_note));
                    marker.setTitle(note.getTitle());
                    marker.setAlpha(0.6f);
                    noteMarker.add(marker);
                }
                noteMarker.invalidate();
                map.invalidate();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }
    private void configurateMap()
    {
        map.setTileSource(TileSourceFactory.MAPQUESTOSM);
        map.setTilesScaledToDpi(true);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController = map.getController();
        mapController.setZoom(15);

        final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        myLocation = new MyLocationNewOverlay(getContext(), new GpsMyLocationProvider(getContext()), map);
        myLocation.enableMyLocation();

        myLocation.runOnFirstFix(new Runnable() {
            public void run() {
                mapController.animateTo(myLocation
                        .getMyLocation());
            }
        });

        scaleBarOverlay = new ScaleBarOverlay(map);
        scaleBarOverlay.setCentred(true);
        scaleBarOverlay.setScaleBarOffset(displayMetrics.widthPixels / 2, 10);
        compassOverlay = new CompassOverlay(getContext(), new InternalCompassOrientationProvider(getContext()), map);
        compassOverlay.enableCompass();
        map.getOverlays().add(myLocation);
        map.getOverlays().add(this.scaleBarOverlay);
        map.getOverlays().add(this.compassOverlay);
    }
    public MapFragment() {
        // Required empty public constructor
    }
}
