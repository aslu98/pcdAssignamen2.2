package part2.a.model.train;

import part2.a.model.TimeUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TimeInfo {
    private final long arriveScheduled;
    private final long departureScheduled;
    private boolean stationReached;
    private long arriveReal;
    private long departureReal;
    private int arrivalDelay;
    private int departureDelay;

    public TimeInfo(long arriveScheduled, long departureScheduled){
        this.arriveScheduled = arriveScheduled;
        this.departureScheduled = departureScheduled;
    }

    public void stopReached(final long arriveReal, final long departureReal, final  int arrivalDelay , final int departureDelay){
        this.arrivalDelay = arrivalDelay;
        this.arriveReal = arriveReal;
        this.departureDelay = departureDelay;
        this.departureReal = departureReal;
        this.stationReached = true;
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

    @Override
    public String toString() {
        if (stationReached) {
            return (arriveScheduled == 0 ? "" : "   arrive: " + TimeUtils.getStringTime(arriveScheduled)
                        + " real arrive: " + TimeUtils.getStringTime(arriveReal)
                        + " arrive delay: " + arrivalDelay)
                    + (departureScheduled == 0 ? "" : "\n     departure: " + TimeUtils.getStringTime(departureScheduled)
                        + " real departure: " + TimeUtils.getStringTime(departureReal)
                        + " departure delay: " + arrivalDelay)
                    + "\n";
        } else {
            return (arriveScheduled == 0 ? "" : "  arrive: " + TimeUtils.getStringTime(arriveScheduled))
                    + (departureScheduled == 0 ? "" : "  departure: " + TimeUtils.getStringTime(departureScheduled));
        }
    }
}
