package net.albertogarrido.stepcounter.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

import net.albertogarrido.stepcounter.WalkActivity;
import net.albertogarrido.stepcounter.database.DatabaseManager;
import net.albertogarrido.stepcounter.database.IDatabaseManager;
import net.albertogarrido.stepcounter.panoramio.model.Panoramio;
import net.albertogarrido.stepcounter.panoramio.rest.RestClient;


public class LocationPicturesService extends Service implements LocationListener,
                                                        GoogleApiClient.ConnectionCallbacks,
                                                        GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location location;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();

        return  START_NOT_STICKY;

    }

    public void onDestroy() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        googleApiClient.disconnect();
        super.onDestroy();
    }

    @Override
    public void onConnected(Bundle bundle) {

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setNumUpdates(1);
        locationRequest.setInterval(10000);

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        getLocationPicture(location);
    }

    private void getLocationPicture(final Location location) {
        Thread panoramioDownloader = new Thread(){
            @Override
            public void run() {
                RestClient rc = new RestClient();
                Panoramio panorama = rc.getSuggestionAPI().getPanoramas(location.getLongitude(), location.getLatitude());
                if(!("").equals(panorama.getPhotoFileUrl())){
                    IDatabaseManager dbManager = new DatabaseManager();
                    dbManager.savePanorama(panorama);
                    sendBroadcastNewPanorama();
                }
                stopSelf();
            }
        };
        panoramioDownloader.start();
    }

    private void sendBroadcastNewPanorama() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(WalkActivity.NEW_PANORAMA_DOWNLOADED);
        sendBroadcast(broadcastIntent);
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}
}