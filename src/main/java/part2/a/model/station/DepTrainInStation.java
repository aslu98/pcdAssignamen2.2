package part2.a.model.station;

public class DepTrainInStation extends TrainInStation{

    private final String departureTime;
    private final String destination;

    public DepTrainInStation(final int delay, final String trainCode, final String trainCategory, final String departureTime, final String destination){
        super(delay, trainCode, trainCategory);
        this.departureTime = departureTime;
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "DEP train " + trainCode + " (" + trainCategory + ") to " + destination
                + "\n\t\tdeparture: " + departureTime + "\tactual delay: " + delay + " min.";
    }
}
