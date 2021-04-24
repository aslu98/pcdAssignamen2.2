package part2.a.agency;
import io.vertx.core.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientSession;
import io.vertx.ext.web.codec.BodyCodec;
import part2.a.model.Solution;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;

public class TrainSolutionsAPIAgent extends BasicAPIAgent {

    private final int NUM_SOLUTIONS = 5;
    private final String from;
    private final String to;
    private final String date;
    private final String time;
    private WebClientSession session;
    private final List<Solution> solutions;

    public TrainSolutionsAPIAgent(Promise<List<Solution>> solutionsPromise, final String from, final String to, final String date, final String time) {
        super(solutionsPromise, "TrainSolutionAgent", "www.lefrecce.it", 443);
        this.solutions = new LinkedList<>();
        this.from = from;
        this.to = to;
        this.date = date;
        this.time = time;
    }

    public void start() {
        log("Service initializing...");
        this.session = WebClientSession.create(WebClient.create(vertx));

        session.get(super.getPort(), super.getHost(), this.getSolutionsURI())
                .ssl(true)
                .as(BodyCodec.jsonArray())
                .send()
                .onSuccess(response -> {
                    JsonArray body = response.body();
                    for (int i = 0; i < NUM_SOLUTIONS; i++){
                        JsonObject obj = body.getJsonObject(i);
                        solutions.add(this.createSolution(obj));
                        requestDetails(obj.getString("idsolution"), i);
                    }
                })
                .onFailure(err ->
                        log("Something went wrong " + err.getMessage()));

    }

    private void requestDetails(final String id, final int pos){
        session.get(super.getPort(), super.getHost(), this.getDetailsURI(id))
                .ssl(true)
                .as(BodyCodec.jsonArray())
                .send()
                .onSuccess(response -> {
                    JsonArray body = response.body();
                    addSolutionDetails(body.getJsonObject(0), pos);
                    if (pos + 1 == NUM_SOLUTIONS){
                        super.getPromise().complete(solutions);
                    }
                })
                .onFailure(err ->
                    log("requestDetails Something went wrong " + err.getMessage()));
    }

    private Solution createSolution(final JsonObject jsonSolution){
        String id = jsonSolution.getString("idsolution");
        String origin = jsonSolution.getString("origin");
        String destination = jsonSolution.getString("destination");
        String direction = jsonSolution.getString("direction");
        LocalDateTime departureTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(jsonSolution.getLong("departuretime")), ZoneId.systemDefault());
        LocalDateTime arrivalTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(jsonSolution.getLong("departuretime")), ZoneId.systemDefault());
        Double minprice = (double)((int)(jsonSolution.getFloat("minprice")*100))/100;
        String duration = jsonSolution.getString("duration");
        Integer changesno = jsonSolution.getInteger("changesno");
        Boolean saleable = jsonSolution.getBoolean("saleable");

        return new Solution(id, origin, destination, direction, departureTime, arrivalTime, minprice, duration, changesno, saleable);
    }

    private void addSolutionDetails(final JsonObject jsonDetails, final int pos){
        String trainid = jsonDetails.getString("trainidentifier");
        String trainacronym = jsonDetails.getString("trainacronym");
        JsonArray stoplist = jsonDetails.getJsonArray("stoplist");
        List<String> stopStations = new LinkedList<>();
        for (Object stop: stoplist){
            stopStations.add(((JsonObject) stop).getString("stationname"));
        }
        solutions.get(pos).addDetails(trainid,trainacronym, stopStations);
    }

    private String getSolutionsURI(){
        return "/msite/api/solutions?origin=" + from.replace(" ", "%20").toUpperCase() +
                "&destination=" + to.replace(" ", "%20").toUpperCase() +
                "&arflag=A&adate=" + date +
                "&atime=" + time +
                "&adultno=1&childno=0&direction=A&frecce=false&onlyRegional=false";
    }

    private String getDetailsURI(final String id){
        return "/msite/api/solutions/" + id + "/details";
    }
}
