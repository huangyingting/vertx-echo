package vertx.echo;

import io.vertx.core.Vertx;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class EchoVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(new EchoVerticle());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.createHttpServer().requestHandler(req -> {
      req.bodyHandler(buffer -> {
        StringBuilder sb = new StringBuilder();

        sb.append(":method: ")
            .append(req.method().toString())
            .append(System.getProperty("line.separator"));

        sb.append(":url: ")
            .append(req.uri().toString())
            .append(System.getProperty("line.separator"));

        req.headers().forEach((key, values) -> sb.append(key)
            .append(": ")
            .append(values.toString())
            .append(System.getProperty("line.separator")));

        sb.append(System.getProperty("line.separator"));
        sb.append(buffer.toString());
        req.response()
            .putHeader("content-type", "text/plain")
            .end(sb.toString());
      });
    }).listen(8080, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }
}
