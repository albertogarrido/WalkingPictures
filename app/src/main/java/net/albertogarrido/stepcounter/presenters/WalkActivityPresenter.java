package net.albertogarrido.stepcounter.presenters;

public interface WalkActivityPresenter {

    void startTrackerService();
    void stopTrackerService();

    void loadPanoramas();

    void deleteOldWalkPanoramas();
}
