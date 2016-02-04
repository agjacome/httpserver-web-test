package com.github.agjacome.httpserver;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.github.agjacome.httpserver.server.ReactiveHttpServer;
import com.github.agjacome.httpserver.server.ReactiveHttpServerConfiguration;
import com.github.agjacome.httpserver.server.Server;
import com.github.agjacome.httpserver.server.ServerCancelable;
import com.github.agjacome.httpserver.server.ServerConfiguration;

import static java.lang.Runtime.getRuntime;
import static java.util.concurrent.Executors.newFixedThreadPool;

import static com.github.agjacome.httpserver.Route.route;
import static com.github.agjacome.httpserver.controller.LoginController.loginControler;
import static com.github.agjacome.httpserver.server.filter.MethodFilter.method;
import static com.github.agjacome.httpserver.server.filter.PathFilter.path;
import static com.github.agjacome.httpserver.server.http.HttpMethod.StandardHttpMethod.GET;
import static com.github.agjacome.httpserver.view.LoginView.loginView;

public final class Main {

    private static final ServerConfiguration config = new ReactiveHttpServerConfiguration(
        new InetSocketAddress(9000), 0, newFixedThreadPool(50), "/httpserver"
    );

    private static final Router router = Router.of(
        route(method(GET).and(path("/login")), loginControler(loginView))
    );

    public static void main(final String[ ] args) {
        try {

          final Server server = new ReactiveHttpServer(config);
          router.route(server);
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
