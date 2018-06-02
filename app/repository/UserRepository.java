package repository;

import exception.DuplicatedRecordException;
import io.ebean.Ebean;
import io.ebean.EbeanServer;
import models.User;
import play.db.ebean.EbeanConfig;
import play.db.ebean.EbeanDynamicEvolutions;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * A repository that executes database operations in a different
 * execution context.
 */
public class UserRepository {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;
    private final EbeanDynamicEvolutions ebeanDynamicEvolutions;

    @Inject
    public UserRepository(EbeanConfig ebeanConfig, EbeanDynamicEvolutions ebeanDynamicEvolutions, DatabaseExecutionContext executionContext) {
        this.ebeanDynamicEvolutions = ebeanDynamicEvolutions;
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    public CompletionStage<List<User>> list(Optional<String> roleOpt) {
        return roleOpt.map(s -> supplyAsync(() ->
                ebeanServer.find(User.class).where()
                        .eq("role", s)
                        .findList(), executionContext)).orElseGet(() -> supplyAsync(() ->
                ebeanServer.find(User.class).findList()));
    }

    public CompletionStage<Long> insert(User user) throws DuplicatedRecordException {
        if (ebeanServer.find(User.class).where().eq("name", user.name).findCount() > 0)
            throw new DuplicatedRecordException("User already exists.");

        return supplyAsync(() -> {
            ebeanServer.insert(user);
            return user.id;
        }, executionContext);
    }
}
