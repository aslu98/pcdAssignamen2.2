package part2.a.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class Solution {

    private final String  id;
    private final String origin;
    private final String destination;
    private final String direction;
    private final LocalDateTime departureTime;
    private final LocalDateTime arrivalTime;
    private final Double minprice;
    private final String duration;
    private final Integer changesno;
    private final Boolean saleable;

    private String trainid;
    private String trainacronym;
    private List<String> stopStations;

    public Solution(final String id, final String origin, final String destination, final String direction, final LocalDateTime departureTime, final LocalDateTime arrivalTime,
                    final Double minprice, final String duration, final Integer changesno, final Boolean saleable){
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.direction = direction;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.minprice = minprice;
        this.duration = duration;
        this.changesno = changesno;
        this.saleable = saleable;
    }

    public void addDetails(final String trainid, final String trainacronym, final List<String> stopStations){
        this.trainid = trainid;
        this.trainacronym = trainacronym;
        this.stopStations = stopStations;
    }

    public String getId() {
        return id;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getTrainid() { return trainid; }

    public String getTrainacronym() {
        return trainacronym;
    }

    public List<String> getStopStations() {
        return stopStations;
    }

    public String getDirection() {
        return direction;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public Double getMinprice() {
        return minprice;
    }

    public String getDuration() {
        return duration;
    }

    public Integer getChangesno() {
        return changesno;
    }

    public Boolean getSaleable() {
        return saleable;
    }

    public String getNumTreno(){
        return this.trainid.substring(this.trainid.lastIndexOf(" ") + 1);
    }

}
