package vertx.echo;

import io.vertx.core.Vertx;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class EchoVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(new EchoVerticle());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    router.route().handler(ctx -> {
      StringBuilder response = new StringBuilder();
      response.append(":method: ");
      response.append(ctx.request().method().toString());
      response.append("\r\n");

      response.append(":url: ");
      response.append(ctx.request().uri());
      response.append("\r\n");

      ctx.request().headers().forEach((key, values) -> response.append(key + ": " + values.toString() + "\r\n"));
      response.append("\r\n");

      response.append(ctx.body().asString());
      ctx.response()
          .putHeader("content-type", "text/plain")
          .end(response.toString());
    });

    vertx.createHttpServer().requestHandler(router).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }
}
