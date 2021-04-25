package part2.a.model.station;

public abstract class TrainInStation {

    protected final String trainCode;
    protected final String trainCategory;
    protected int delay;

    public TrainInStation(final int delay, final String trainCode, final String trainCategory) {
        this.delay = delay;
        this.trainCode = trainCode;
        this.trainCategory = trainCategory;
    }

    public int getDelay(){
        return this.delay;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public String getTrainCategory() {
        return trainCategory;
    }
}
