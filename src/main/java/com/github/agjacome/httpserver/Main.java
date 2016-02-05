package com.github.agjacome.httpserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;

import com.github.agjacome.httpserver.model.User;
import com.github.agjacome.httpserver.model.repository.SessionRepository;
import com.github.agjacome.httpserver.model.repository.UserRepository;
import com.github.agjacome.httpserver.model.repository.inmemory.InMemorySessionRepository;
import com.github.agjacome.httpserver.model.repository.inmemory.InMemoryUserRepository;
import com.github.agjacome.httpserver.server.ReactiveHttpServer;
import com.github.agjacome.httpserver.server.ReactiveHttpServerConfiguration;
import com.github.agjacome.httpserver.server.Server;
import com.github.agjacome.httpserver.server.ServerCancelable;
import com.github.agjacome.httpserver.server.ServerConfiguration;

import static java.lang.Runtime.getRuntime;
import static java.util.concurrent.Executors.newFixedThreadPool;

import static com.github.agjacome.httpserver.Route.route;
import static com.github.agjacome.httpserver.controller.IndexController.indexController;
import static com.github.agjacome.httpserver.controller.LoginGetController.loginGetController;
import static com.github.agjacome.httpserver.controller.LoginPostController.loginPostController;
import static com.github.agjacome.httpserver.controller.LogoutController.logoutController;
import static com.github.agjacome.httpserver.model.User.Role.ADMIN;
import static com.github.agjacome.httpserver.model.User.Role.PAGE_1;
import static com.github.agjacome.httpserver.model.User.Role.PAGE_2;
import static com.github.agjacome.httpserver.model.User.Role.PAGE_3;
import static com.github.agjacome.httpserver.server.filter.MethodFilter.method;
import static com.github.agjacome.httpserver.server.filter.PathFilter.path;
import static com.github.agjacome.httpserver.server.http.HttpMethod.StandardHttpMethod.GET;
import static com.github.agjacome.httpserver.server.http.HttpMethod.StandardHttpMethod.POST;
import static com.github.agjacome.httpserver.util.Arrays.asSet;
import static com.github.agjacome.httpserver.util.BCryptPassword.encrypt;
import static com.github.agjacome.httpserver.util.CaseInsensitiveString.uncased;
import static com.github.agjacome.httpserver.view.IndexView.indexView;
import static com.github.agjacome.httpserver.view.LoginView.loginView;

public final class Main {

    private static final ServerConfiguration config = new ReactiveHttpServerConfiguration(
        new InetSocketAddress(9000), 0, newFixedThreadPool(50), "/httpserver"
    );

    private static final UserRepository users = InMemoryUserRepository.of(
        new User(uncased("admin" ), encrypt("adminpass" ), asSet(ADMIN)),
        new User(uncased("page1" ), encrypt("page1pass" ), asSet(PAGE_1)),
        new User(uncased("page2" ), encrypt("page2pass" ), asSet(PAGE_2)),
        new User(uncased("page3" ), encrypt("page3pass" ), asSet(PAGE_3)),
        new User(uncased("page12"), encrypt("page12pass"), asSet(PAGE_1, PAGE_2)),
        new User(uncased("page13"), encrypt("page13pass"), asSet(PAGE_1, PAGE_3)),
        new User(uncased("page23"), encrypt("page23pass"), asSet(PAGE_2, PAGE_3))
    );

    private static final SessionRepository sessions = InMemorySessionRepository.empty();
    private static final Duration sessionDuration = Duration.ofMinutes(5);

    private static final Router router = Router.of(
        route(method(GET ).and(path("/?"     )), indexController(indexView)),
        route(method(GET ).and(path("/login" )), loginGetController(loginView, sessions)),
        route(method(POST).and(path("/login" )), loginPostController(users, sessions, sessionDuration)),
        route(method(GET ).and(path("/logout")), logoutController(sessions))
    );

    public static void main(final String[ ] args) {
        try {

          final Server server = new ReactiveHttpServer(config);
          router.enroute(server);
          runServer(server);

        } catch (final IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }
    }

    private static void runServer(final Server server) {
        final ServerCancelable running = server.run();
        getRuntime().addShutdownHook(new Thread(() -> running.shutdownNow()));

        try {
            running.awaitShutdown();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
            System.exit(1);
        }
    }

}
