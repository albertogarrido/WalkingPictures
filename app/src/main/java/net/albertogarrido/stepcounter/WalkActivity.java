package net.albertogarrido.stepcounter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import net.albertogarrido.stepcounter.panoramio.model.Panoramio;
import net.albertogarrido.stepcounter.presenters.WalkActivityPresenter;
import net.albertogarrido.stepcounter.presenters.WalkActivityPresenterImpl;
import net.albertogarrido.stepcounter.views.WalkActivityView;
import net.albertogarrido.stepcounter.views.adapters.PanoramasAdapter;

import java.util.List;


public class WalkActivity extends ActionBarActivity implements WalkActivityView {

    public static final String SENSOR_NOT_FOUND = "sensor_not_found";
    public static final String NEW_PANORAMA_DOWNLOADED = "new_panorama_downloaded";

    private WalkActivityPresenter walkActivityPresenter;

    protected TextView tvInitialMessage;
    protected Menu menu;
    private RecyclerView recyclerViewPanoramas;

    private boolean firstUse = true;

    private SensorReceiver sensorReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk);

        tvInitialMessage = (TextView) findViewById(R.id.tvInitialMessage);
        recyclerViewPanoramas = (RecyclerView) findViewById(R.id.panoramasList);
        recyclerViewPanoramas.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerViewPanoramas.setLayoutManager(mLayoutManager);

        walkActivityPresenter = new WalkActivityPresenterImpl(this);
        registerBroadcast();
    }

    @Override
    protected void onResume() {
        walkActivityPresenter.loadPanoramas();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(sensorReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_walk, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_start) {
            firstUse = false;
            // delete all old pictures from previous tracks
            walkActivityPresenter.deleteOldWalkPanoramas();
            walkActivityPresenter.loadPanoramas();
            walkActivityPresenter.startTrackerService();
            item.setVisible(false);
            menu.findItem(R.id.action_stop).setVisible(true);
        } else if(id == R.id.action_stop) {
            walkActivityPresenter.stopTrackerService();
            item.setVisible(false);
            menu.findItem(R.id.action_start).setVisible(true);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(SENSOR_NOT_FOUND);
        filter.addAction(NEW_PANORAMA_DOWNLOADED);
        sensorReceiver = new SensorReceiver();
        registerReceiver(sensorReceiver, filter);
    }

    @Override
    public void setPanoramasAdapter(List<Panoramio> panoramas) {
        if(panoramas.size() > 0){
            tvInitialMessage.setVisibility(View.GONE);
            recyclerViewPanoramas.setVisibility(View.VISIBLE);
            RecyclerView.Adapter adapter = new PanoramasAdapter(panoramas);
            recyclerViewPanoramas.setAdapter(adapter);
        } else {
            tvInitialMessage.setVisibility(View.VISIBLE);
            recyclerViewPanoramas.setVisibility(View.GONE);
            if(!firstUse) tvInitialMessage.setText(getResources().getString(R.string.start_walking_message));
        }
    }

    /**
     * Receives the broadcast sent by the TrackerService
     */
    public class SensorReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("SENSOR RECEIVER", "new broadcast received: " + intent.getAction());
            if(intent.getAction().equals(SENSOR_NOT_FOUND)) {
                tvInitialMessage.setText(getResources().getString(R.string.sensor_error));
            } else if(intent.getAction().equals(NEW_PANORAMA_DOWNLOADED)) {
                walkActivityPresenter.loadPanoramas();
            }
        }
    }
}
