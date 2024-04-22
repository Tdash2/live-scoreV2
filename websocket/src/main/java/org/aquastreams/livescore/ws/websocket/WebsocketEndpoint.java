package org.aquastreams.livescore.ws.websocket;

import lombok.Getter;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/")
public class WebsocketEndpoint {
    @Getter
    private final Map<Integer, List<Session>> sessions = new ConcurrentHashMap<>();
    // haven't told us what bug id they want. lazy fucks.
    private final List<Session> waitingSessions = new ArrayList<>();

    @OnOpen
    public void onOpen(Session session) {
        waitingSessions.add(session);

        session.getAsyncRemote().sendText("Welcome. Please use `SETBUGID <id>` to start receiving team updates.");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // hack: please don't write production software like this. use an actual handler. don't yanderedev it.
        if (message.startsWith("SETBUGID")) {
            if(waitingSessions.contains(session))
                waitingSessions.remove(session);

            // BUG: we need to like, actually remove from the bug id if the client requests to switch. lol
            sessions.get(Integer.parseInt(message.replace("SETBUGID ", ""))).add(session);
        }
    }


}
