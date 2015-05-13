package net.albertogarrido.stepcounter.presenters;

import android.content.Intent;

import net.albertogarrido.stepcounter.WalkActivity;
import net.albertogarrido.stepcounter.database.DatabaseManager;
import net.albertogarrido.stepcounter.database.IDatabaseManager;
import net.albertogarrido.stepcounter.panoramio.model.Panoramio;
import net.albertogarrido.stepcounter.services.TrackerService;
import net.albertogarrido.stepcounter.views.WalkActivityView;

import java.util.List;

public class WalkActivityPresenterImpl implements WalkActivityPresenter {

    private WalkActivityView walkActivity;
    private Intent trackerServiceIntent;
    private IDatabaseManager dbManager;

    public WalkActivityPresenterImpl(WalkActivity walkActivity) {
        this.walkActivity = walkActivity;
        trackerServiceIntent = new Intent(walkActivity.getContext(), TrackerService.class);
        dbManager = new DatabaseManager();
    }

    @Override
    public void startTrackerService() {
        walkActivity.getContext().startService(trackerServiceIntent);
    }

    @Override
    public void stopTrackerService() {
        walkActivity.getContext().stopService(trackerServiceIntent);
    }

    @Override
    public void loadPanoramas() {
        List<Panoramio> panoramas = dbManager.getPanoramas();
        walkActivity.setPanoramasAdapter(panoramas);
    }

    @Override
    public void deleteOldWalkPanoramas() {
        dbManager.deleteAllPanoramas();
    }
}
