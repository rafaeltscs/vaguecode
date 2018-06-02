package repository;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import models.User;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * A repository that executes database operations in a different
 * execution context.
 */
public class UserRepository {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public UserRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    public CompletionStage<List<User>> list(String role) {
        return supplyAsync(() ->
                ebeanServer.find(User.class).where()
                        .eq("role", role)
                        .findList(), executionContext);
    }

    public CompletionStage<Long> insert(User user) {
        return supplyAsync(() -> {
            ebeanServer.insert(user);
            return user.id;
        }, executionContext);
    }
}
