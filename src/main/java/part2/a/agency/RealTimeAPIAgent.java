package part2.a.agency;

import io.vertx.core.Promise;

public abstract class RealTimeAPIAgent<T> extends BasicAPIAgent {

    private final String code;

    public RealTimeAPIAgent(Promise<T> promise, final String code){
        super(promise, "RealTimeTrainAgent", "www.viaggiatreno.it", 80);
        this.code = code;
    }

    protected String getCode() {
        return code;
    }
}
