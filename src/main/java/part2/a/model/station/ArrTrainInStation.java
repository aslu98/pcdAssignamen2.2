package part2.a.model.station;

public class ArrTrainInStation extends TrainInStation{
    private final String origin;
    private final String originCode;
    private final String arriveTime;

    public ArrTrainInStation(final int delay, final String trainCode, final String trainCategory, final String origin, final String originCode, final String arriveTime){
        super(delay, trainCode, trainCategory);
        this.origin = origin;
        this.originCode = originCode;
        this.arriveTime = arriveTime;
    }

    @Override
    public String toString() {
        return "ARR train " + trainCode + " (" + trainCategory + ") " + " from " + origin + " (" + originCode + ")"
                + "\n\t\tarrive: " + arriveTime + "\tactual delay: " + delay + " min.";
    }
}
