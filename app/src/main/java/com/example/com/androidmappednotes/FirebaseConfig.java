package com.example.com.androidmappednotes;
import android.app.Application;
import com.firebase.client.Firebase;
/**
 * Created by 48089748z on 10/02/16.
 */
public class FirebaseConfig extends Application
{
    private Firebase mainReference;
    @Override
    public void onCreate()
    {
        super.onCreate();
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
        mainReference = new Firebase("https://uridatabase.firebaseio.com/");
    }
    public Firebase getMainReference() {
        return mainReference;
    }
}



