package controllers;

import models.User;
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
            // Run companies db operation and then render the form
            return CompletableFuture.completedFuture(badRequest("Could not create user."));

//            return applyAsync(companies -> {
//                // This is the HTTP rendering thread context
//                return badRequest("Could not create user.");
//            }, httpExecutionContext.current());
        }

        User user = userForm.get();
        // Run insert db operation, then redirect
        return userRepository.insert(user).thenApplyAsync(data -> {
            return created("User created.");
        }, httpExecutionContext.current());
    }
}
