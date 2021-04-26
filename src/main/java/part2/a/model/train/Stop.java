package part2.a.model.train;

public class Stop {
    private final String station;
    private final String stationCode;
    private final TimeInfo timeInfo;

    public Stop(String station, String stationCode, long arriveScheduled, long departureScheduled) {
        this.station = station;
        this.stationCode = stationCode;
        this.timeInfo = new TimeInfo(arriveScheduled, departureScheduled);
    }

    public String getStation() {
        return station;
    }

    public String getStationCode() {
        return stationCode;
    }


    public TimeInfo getTimeInfo() {
        return timeInfo;
    }

    public void stopReached(final long arriveReal, final long departureReal, final  int arrivalDelay , final int departureDelay){
        this.timeInfo.stopReached(arriveReal, departureReal, arrivalDelay, departureDelay);
    }

    @Override
    public String toString() {
        return "\n\t" + station + " (" + stationCode +") " + timeInfo.toString();
    }
}
