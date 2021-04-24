package part2.a.agency;

import io.vertx.core.Promise;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientSession;

import java.util.Date;

public class RealTimeStationAPIAgent extends RealTimeAPIAgent {

    private WebClientSession session;

    public RealTimeStationAPIAgent(Promise<Integer> promise, final String code){
        super(promise, code);
    }

    public void start() {
        this.session = WebClientSession.create(WebClient.create(vertx));

        log(this.getURI());
        session.post(super.getPort(), super.getHost(), this.getURI())
                .addQueryParam("orario", new Date().toString())
                .send()
                .onSuccess(response -> {
                    log(response.body().toString());
                    super.getPromise().complete(1);
                })
                .onFailure(err ->
                        log("Something went wrong " + err.getMessage()));

    }

    private String getURI(){
        return "/viaggiatrenonew/resteasy/viaggiatreno/partenze/" + super.getCode()
                + "/Thu%20Apr%2022%202021%2011%3A36%3A18%20CEST%202021";
        //+ "/" + new Date();
    }
}
