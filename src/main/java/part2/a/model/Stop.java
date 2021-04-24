package part2.a.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Stop {
    private final String station;
    private final String stationCode;
    private final long arriveScheduled;
    private final long departureScheduled;
    private long arriveReal;
    private long departureReal;
    private int arrivalDelay;
    private int departureDelay;

    public Stop(String station, String stationCode, long arriveScheduled, long departureScheduled) {
        this.station = station;
        this.stationCode = stationCode;
        this.arriveScheduled = arriveScheduled;
        this.departureScheduled = departureScheduled;
    }

    public String getStation() {
        return station;
    }

    public String getStationCode() {
        return stationCode;
    }

    public long getArriveScheduled() {
        return arriveScheduled;
    }

    public long getDepartureScheduled() {
        return departureScheduled;
    }

    public long getArriveReal() {
        return arriveReal;
    }

    public long getDepartureReal() {
        return departureReal;
    }

    public int getArrivalDelay() {
        return arrivalDelay;
    }

    public int getDepartureDelay() {
        return departureDelay;
    }

    public LocalDateTime toDateTime(final long unixTime){
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(unixTime), ZoneId.systemDefault());
    }

    public void stopReached(final long arriveReal, final long departureReal, final  int arrivalDelay , final int departureDelay){
        this.arrivalDelay = arrivalDelay;
        this.arriveReal = arriveReal;
        this.departureDelay = departureDelay;
        this.departureReal = departureReal;
    }

}
