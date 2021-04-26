package part2.a.agency;

import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientSession;
import io.vertx.ext.web.codec.BodyCodec;
import part2.a.model.station.ArrTrainInStation;
import part2.a.model.station.DepTrainInStation;
import part2.a.model.station.StationState;
import part2.a.model.station.TrainInStation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Optional;

public class RealTimeStationAPIAgent extends RealTimeAPIAgent {

    private WebClientSession session;
    private final LinkedList<TrainInStation> trains;

    public RealTimeStationAPIAgent(Promise<StationState> promise, final String code){
        super(promise, code);
        trains = new LinkedList<>();
    }

    public void start() {
        this.session = WebClientSession.create(WebClient.create(vertx));

        String uriArrivi = this.getURI("arrivi");
        session.get(super.getPort(), super.getHost(), uriArrivi)
                .as(BodyCodec.jsonArray())
                .send()
                .onSuccess(response -> {
                    for (Object obj: response.body()){
                        JsonObject jsonTrain = (JsonObject) obj;
                        String trainCode = jsonTrain.getString("numeroTreno");
                        String trainCategory = jsonTrain.getString("categoria");
                        String origin = jsonTrain.getString("origine");
                        String originCode = jsonTrain.getString("codOrigine");
                        String arriveTime = jsonTrain.getString("compOrarioArrivo");
                        int delay = jsonTrain.getInteger("ritardo");
                        TrainInStation train = new ArrTrainInStation(delay, trainCode, trainCategory, origin, originCode, arriveTime);
                        trains.add(train);
                    }
                    this.addDepartureInfo();
                })
                .onFailure(err -> log("Something went wrong " + err.getMessage()
                        + "\n URI was " + uriArrivi));

    }

    private void addDepartureInfo(){
        String uriPartenze = this.getURI("partenze");
        session.get(super.getPort(), super.getHost(), uriPartenze)
                .as(BodyCodec.jsonArray())
                .send()
                .onSuccess(response -> {
                    for (Object obj: response.body()){
                        JsonObject jsonTrain = (JsonObject) obj;
                        String trainCode = jsonTrain.getString("numeroTreno");
                        String trainCategory = jsonTrain.getString("categoria");
                        String departureTime = jsonTrain.getString("compOrarioPartenza");
                        String destination = jsonTrain.getString("destinazione");
                        int delay = jsonTrain.getInteger("ritardo");
                        TrainInStation train = new DepTrainInStation(delay, trainCode, trainCategory, departureTime, destination);
                        trains.add(train);
                    }
                    StationState station = new StationState(trains, super.getCode());
                    super.getPromise().complete(station);
                })
                .onFailure(err -> super.getPromise().fail("Something went wrong " + err.getMessage()
                                    + "\n URI was " + uriPartenze));
    }

    private String getURI(String direction){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E%20MMM%20dd%20yyyy%20HH:mm:ss", Locale.ENGLISH);
        return "/viaggiatrenonew/resteasy/viaggiatreno/" + direction + "/" + super.getCode().toUpperCase()
                + "/" + LocalDateTime.now().minusMinutes(10).format(formatter).replace(":", "%3A")+ "%20GMT%2B0100";
    }
}
