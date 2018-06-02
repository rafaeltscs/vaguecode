package controllers;

import exception.DuplicatedRecordException;
import models.User;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.*;
import repository.UserRepository;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class UsersController extends Controller {

    private static final Logger.ALogger logger = Logger.of(UsersController.class);

    private final UserRepository userRepository;
    private final HttpExecutionContext httpExecutionContext;
    private final FormFactory formFactory;

    @Inject
    public UsersController(UserRepository userRepository,
                           HttpExecutionContext httpExecutionContext,
                           FormFactory formFactory) {
        this.httpExecutionContext = httpExecutionContext;
        this.userRepository = userRepository;
        this.formFactory = formFactory;
    }

    public CompletionStage<Result> index(Optional<String> role) {
        return userRepository.list(role).thenApplyAsync(list -> ok(Json.toJson(list)), httpExecutionContext.current());
    }

    public CompletionStage<Result> create() {
        Form<User> userForm = formFactory.form(User.class).bindFromRequest();
        if (userForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest("Could not create user."));
        }

        User user = userForm.get();

        try {
            return userRepository.insert(user).thenApplyAsync(data ->  created("User created."), httpExecutionContext.current());
        } catch (DuplicatedRecordException e) {
            return CompletableFuture.completedFuture(badRequest(e.getMessage()));
        }
    }
}
