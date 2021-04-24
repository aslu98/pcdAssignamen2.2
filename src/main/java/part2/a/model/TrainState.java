package part2.a.model;

import java.util.LinkedList;
import java.util.List;

public class TrainState {

    private final String trainId;
    private List<Stop> stops;
    private Stop lastStop;
    private int actualDelay;

    public TrainState(final String trainId){
        this.trainId = trainId;
    }

    public void addState(final int actualDelay, final Stop lastStop) {
        this.actualDelay = actualDelay;
        this.lastStop = lastStop;
    }

    public void addStops(final List<Stop> stops){
        this.stops = stops;
    }

    public List<Stop> getStops(){
        return this.stops;
    }

    public String getTrainId() {
        return trainId;
    }

    public Stop getLastStop() {
        return lastStop;
    }

    public int getActualDelay() {
        return actualDelay;
    }

}
