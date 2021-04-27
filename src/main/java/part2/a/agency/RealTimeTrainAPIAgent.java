package part2.a.agency;

import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientSession;
import io.vertx.ext.web.codec.BodyCodec;
import part2.a.model.train.Stop;
import part2.a.model.train.TrainState;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class RealTimeTrainAPIAgent extends RealTimeAPIAgent {

    private WebClientSession session;
    private String departureStationCode;

    public RealTimeTrainAPIAgent(Promise<TrainState> promise, final String code){
        super(promise, code);
    }

    public void start() {
        this.session = WebClientSession.create(WebClient.create(vertx));

        if (!super.getCode().matches("[0-9]+")){
            super.getPromise().fail("The code is not in the right format");
        } else {
            session.get(super.getPort(), super.getHost(), this.departureStationURI())
                    .send()
                    .onSuccess(response -> {
                        if (!checkNull(response.body())) {
                            this.setStationCode(response.bodyAsString());
                            this.getRealTimeData();
                        }
                    })
                    .onFailure(err -> super.getPromise().fail("Something went wrong " + err.getMessage()
                            + "\n URI was " + this.departureStationURI()));
        }
    }

    private void getRealTimeData(){
        session.get(super.getPort(), super.getHost(), this.realTimeURI())
                .as(BodyCodec.jsonArray())
                .send()
                .onSuccess(response -> {
                    if (!checkNull(response.body())){
                        List<Stop> stops = new LinkedList<>();
                        TrainState train = new TrainState(super.getCode());
                        for(Object obj : response.body()){
                            JsonObject jsonStop = (JsonObject) obj;
                            String stationCode = jsonStop.getString("id");
                            String station = jsonStop.getString("stazione");
                            JsonObject jsonFermata = jsonStop.getJsonObject("fermata");
                            Long arrivalScheduled = jsonFermata.getLong("arrivo_teorico");
                            Long departureScheduled = jsonFermata.getLong("partenza_teorica");
                            Stop stop = new Stop(station, stationCode, arrivalScheduled == null ? 0 : arrivalScheduled, departureScheduled == null ? 0 : departureScheduled);
                            stops.add(stop);
                            if (jsonStop.getBoolean("stazioneCorrente")){
                                int actualDelay = jsonFermata.getInteger("ritardo");
                                train.addState(actualDelay, stop);
                            }
                        }
                        train.addStops(stops);
                        super.getPromise().complete(train);
                    }
                })
                .onFailure(err -> super.getPromise().fail("Something went wrong " + err.getMessage()
                        + "\n URI was " + this.realTimeURI()));
    }

    private String departureStationURI(){
        return "/viaggiatrenonew/resteasy/viaggiatreno/cercaNumeroTrenoTrenoAutocomplete/" + super.getCode();
    }

    private String realTimeURI(){
        return "/viaggiatrenonew/resteasy/viaggiatreno/tratteCanvas/" + this.departureStationCode + "/" + super.getCode()
                + "/" + LocalDate.now().atStartOfDay().toEpochSecond(OffsetDateTime.now().getOffset())*1000;
    }

    private void setStationCode(final String response){
        int start = response.indexOf("|") + super.getCode().length() + 2;
        this.departureStationCode = response.substring(start, start + 6).toUpperCase(Locale.ROOT);
    }
}
