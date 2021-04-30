package part2.a.model.station;

import java.util.List;

public class StationState {
    private final List<TrainInStation> trains;
    private final String stationCode;

    public StationState(final List<TrainInStation> trains, final String stationCode){
        this.stationCode = stationCode;
        this.trains = trains;
    }

    public List<TrainInStation> getTrains() {
        return trains;
    }

    public String getStationCode() {
        return stationCode;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("\nstation " + stationCode);
        for (TrainInStation t: trains) {
            str.append("\n   ").append(t.toString());
        }
        return str + "\n";
    }
}
