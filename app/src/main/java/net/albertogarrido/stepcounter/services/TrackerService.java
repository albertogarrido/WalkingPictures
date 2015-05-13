package net.albertogarrido.stepcounter.services;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

import net.albertogarrido.stepcounter.WalkActivity;

public class TrackerService extends Service {

    private final float STEP_IN_METERS = 0.762f;

    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;
    private Intent locationPictureServiceIntent;
    private float currentSteps;



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        currentSteps = 0.0f;

        sensorManager = (SensorManager) getApplicationContext().getSystemService(SENSOR_SERVICE);
        sensorEventListener = new TrackSensorListener();
        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (stepSensor != null) {
            unregisterSensorListener();
            registerSensorListener(stepSensor);

            locationPictureServiceIntent = new Intent(this, LocationPicturesService.class);
        } else {
            sendNoSensorErrorBroadcast();
        }

        return START_STICKY;
    }

    private void registerSensorListener(Sensor stepSensor){
        sensorManager.registerListener(sensorEventListener, stepSensor, SensorManager.SENSOR_DELAY_UI);
    }

    private void unregisterSensorListener(){
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    public void onDestroy() {
        unregisterSensorListener();
        stopService(locationPictureServiceIntent);
        super.onDestroy();
    }

    private void handleSensorInfo(float value) {

        currentSteps += value;
        float currentMeters = currentSteps * STEP_IN_METERS;
        if( Float.compare(currentMeters, 100f) > 0 ){
            if(locationPictureServiceIntent != null){
                startService(locationPictureServiceIntent);
            }
            currentSteps = 0.0f;

        }
    }

    private void sendNoSensorErrorBroadcast() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(WalkActivity.SENSOR_NOT_FOUND);
        sendBroadcast(broadcastIntent);
    }

    public class TrackSensorListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            handleSensorInfo(event.values[0]); // x axis values
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    }
}
