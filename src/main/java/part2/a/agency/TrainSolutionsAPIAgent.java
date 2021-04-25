package part2.a.agency;
import io.vertx.core.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientSession;
import io.vertx.ext.web.codec.BodyCodec;
import part2.a.model.solution.Solution;
import part2.a.model.solution.SolutionsWrapper;

import java.util.LinkedList;
import java.util.List;

public class TrainSolutionsAPIAgent extends BasicAPIAgent {

    private final int NUM_SOLUTIONS = 5;
    private final String from;
    private final String to;
    private final String date;
    private final String time;
    private WebClientSession session;
    private SolutionsWrapper solutionsWrap;

    public TrainSolutionsAPIAgent(Promise<SolutionsWrapper> solutionsPromise, final String from, final String to, final String date, final String time) {
        super(solutionsPromise, "TrainSolutionAgent", "www.lefrecce.it", 443);
        this.from = from;
        this.to = to;
        this.date = date;
        this.time = time;
        List<Solution> s = new LinkedList<>();
        this.solutionsWrap = new SolutionsWrapper(s, NUM_SOLUTIONS, this);
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
                        solutionsWrap.getSolutions().add(this.createSolution(obj));
                        requestDetails(obj.getString("idsolution"), i);
                    }
                })
                .onFailure(err -> super.getPromise().fail("Something went wrong " + err.getMessage()));

    }

    private void requestDetails(final String id, final int pos){
        session.get(super.getPort(), super.getHost(), this.getDetailsURI(id))
                .ssl(true)
                .as(BodyCodec.jsonArray())
                .send()
                .onSuccess(response -> {
                    JsonArray body = response.body();
                    addSolutionDetails(body.getJsonObject(0), pos);
                })
                .onFailure(err -> super.getPromise().fail("Something went wrong " + err.getMessage()));
    }

    public void SolutionsReady(){
        super.getPromise().complete(solutionsWrap);
    }

    private Solution createSolution(final JsonObject jsonSolution){
        String id = jsonSolution.getString("idsolution");
        String origin = jsonSolution.getString("origin");
        String destination = jsonSolution.getString("destination");
        String direction = jsonSolution.getString("direction");
        Long departureTime = jsonSolution.getLong("departuretime");
        Long arrivalTime = jsonSolution.getLong("arrivaltime");
        Double minprice = (double)((int)(jsonSolution.getFloat("minprice")*100))/100;
        String duration = jsonSolution.getString("duration");
        Integer changesno = jsonSolution.getInteger("changesno");
        Boolean saleable = jsonSolution.getBoolean("saleable");
        return new Solution(id, origin, destination, direction, departureTime, arrivalTime, minprice, duration, changesno, saleable, solutionsWrap);
    }

    private void addSolutionDetails(final JsonObject jsonDetails, final int pos){
        String trainid = jsonDetails.getString("trainidentifier");
        String trainacronym = jsonDetails.getString("trainacronym");
        JsonArray stoplist = jsonDetails.getJsonArray("stoplist");
        List<String> stopStations = new LinkedList<>();
        for (Object stop: stoplist){
            stopStations.add(((JsonObject) stop).getString("stationname"));
        }
        solutionsWrap.getSolutions().get(pos).addDetails(trainid,trainacronym, stopStations);
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
