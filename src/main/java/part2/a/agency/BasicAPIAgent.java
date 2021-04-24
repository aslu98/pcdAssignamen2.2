package part2.a.agency;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public abstract class BasicAPIAgent<T>  extends AbstractVerticle {
    private final String name;
    private final String host;
    private final int port;
    private final Promise<T> promise;

    protected BasicAPIAgent(final Promise<T> promise, final String name, final String host, final int port){
        this.name = name;
        this.host = host;
        this.port = port;
        this.promise = promise;
    }

    protected void log(final String msg) {
        System.out.println("[" + name + "] " + msg);
    }

    protected Promise getPromise() {
        return this.promise;
    }

    protected String getHost() {
        return host;
    }

    protected int getPort() {
        return port;
    }
}
