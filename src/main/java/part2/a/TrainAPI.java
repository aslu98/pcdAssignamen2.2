package part2.a;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import part2.a.agency.RealTimeStationAPIAgent;
import part2.a.agency.RealTimeTrainAPIAgent;
import part2.a.agency.TrainSolutionsAPIAgent;
import part2.a.model.solution.Solution;
import part2.a.model.solution.SolutionsWrapper;
import part2.a.model.station.StationState;
import part2.a.model.train.TrainState;

import java.util.List;

public class TrainAPI {

    public static Future<SolutionsWrapper> getTrainSolutions(final String from, final String to, final String date, final String time){
        Promise<SolutionsWrapper> promise = Promise.promise();
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new TrainSolutionsAPIAgent(promise, from, to, date, time));
        return promise.future();
    }

    public static Future<TrainState> getRealTimeTrainInfo(final String trainCode){
        Promise<TrainState> promise = Promise.promise();
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new RealTimeTrainAPIAgent(promise, trainCode));
        return promise.future();
    }

    public static Future<StationState> getRealTimeStationInfo(final String stationCode){
        Promise<StationState> promise = Promise.promise();
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new RealTimeStationAPIAgent(promise, stationCode));
        return promise.future();
    }
}
