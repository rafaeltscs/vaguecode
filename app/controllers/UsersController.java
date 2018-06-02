package controllers;

import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.*;
import repository.UserRepository;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class UsersController extends Controller {

    private final UserRepository userRepository;
    private final HttpExecutionContext httpExecutionContext;

    @Inject
    public UsersController(UserRepository userRepository, HttpExecutionContext httpExecutionContext) {
        this.httpExecutionContext = httpExecutionContext;
        this.userRepository = userRepository;
    }

    public CompletionStage<Result> index(Optional<String> role) {
        return userRepository.list(role).thenApplyAsync(list -> ok(Json.toJson(list)), httpExecutionContext.current());
    }

}
