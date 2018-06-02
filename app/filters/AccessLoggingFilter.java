package filters;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import javax.inject.Inject;
import akka.stream.Materializer;
import play.Logger;
import play.mvc.*;

public class AccessLoggingFilter extends Filter {

    private Logger.ALogger accessLogger = Logger.of("access");

    @Inject
    public AccessLoggingFilter(Materializer mat) {
        super(mat);
    }

    @Override
    public CompletionStage<Result> apply(
            Function<Http.RequestHeader, CompletionStage<Result>> nextFilter,
            Http.RequestHeader requestHeader) {
        long startTime = System.currentTimeMillis();
        return nextFilter.apply(requestHeader).thenApply(result -> {
            long endTime = System.currentTimeMillis();
            long requestTime = endTime - startTime;

            accessLogger.info("{} {} took {}ms and returned {}. remote-address={}",
                    requestHeader.method(), requestHeader.uri(), requestTime, result.status(), requestHeader.remoteAddress());

            return result.withHeader("Request-Time", "" + requestTime);
        });
    }
}