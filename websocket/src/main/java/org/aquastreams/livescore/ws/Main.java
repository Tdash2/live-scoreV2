package org.aquastreams.livescore.ws;

import lombok.extern.slf4j.Slf4j;
import org.aquastreams.livescore.ws.websocket.WebsocketEndpoint;
import org.glassfish.tyrus.server.Server;

import javax.websocket.DeploymentException;
import java.util.Scanner;

@Slf4j(topic = "BugWS Init")
public class Main {
    private static Server server;

    public static void main(String[] args) {
        log.info("Starting AquaStreams Bug WebSocket...");

        server = new Server("localhost", 8080, "/", WebsocketEndpoint.class);

        try {
            server.start();
            log.info("Server started. Press `t` to terminate.");

            // handle input
            Scanner scanner = new Scanner(System.in);
            String inp = scanner.nextLine();
            scanner.close();

            if (inp.equalsIgnoreCase("t")) {
                shutdown();
                log.info("Terminated.");
            }
        } catch (DeploymentException e) {
            e.printStackTrace(); // TODO: Proper logging.
        }
    }

    public static void shutdown() {
        log.info("Terminating server..");
        server.stop();
    }
}