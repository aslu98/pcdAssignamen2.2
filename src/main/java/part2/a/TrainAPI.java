package part2.a;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import part2.a.agency.RealTimeStationAPIAgent;
import part2.a.agency.RealTimeTrainAPIAgent;
import part2.a.agency.TrainSolutionsAPIAgent;
import part2.a.model.solution.SolutionsWrapper;
import part2.a.model.station.StationState;
import part2.a.model.train.TrainState;

public class TrainAPI {

    Vertx vertx = Vertx.vertx();

    public Future<SolutionsWrapper> getTrainSolutions(final String from, final String to, final String date, final String time){
        Promise<SolutionsWrapper> promise = Promise.promise();
        vertx.deployVerticle(new TrainSolutionsAPIAgent(promise, from, to, date, time));
        return promise.future();
    }

    public Future<TrainState> getRealTimeTrainInfo(final String trainCode){
        Promise<TrainState> promise = Promise.promise();
        vertx.deployVerticle(new RealTimeTrainAPIAgent(promise, trainCode));
        return promise.future();
    }

    public Future<StationState> getRealTimeStationInfo(final String stationCode){
        Promise<StationState> promise = Promise.promise();
        vertx.deployVerticle(new RealTimeStationAPIAgent(promise, stationCode));
        return promise.future();
    }
}
