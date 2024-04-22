package org.aquastreams.livescore.ws.websocket;

import lombok.Getter;

import javax.websocket.OnClose;
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
    int clientCount = 0;

    @Getter
    private final Map<Integer, List<Session>> sessions = new ConcurrentHashMap<>();
    private final Map<Session, Integer> sessionToBugID = new ConcurrentHashMap<>();
    // haven't told us what bug id they want. lazy fucks.
    private final List<Session> waitingSessions = new ArrayList<>();

    @OnOpen
    public void onOpen(Session session) {
        waitingSessions.add(session);
        clientCount++;

        session.getAsyncRemote().sendText("Welcome. Please use `SETBUGID <id>` to start receiving team updates.");
    }

    @OnClose
    public void onClose(Session session) {
        waitingSessions.remove(session);

        if(sessionToBugID.containsKey(session)) {
            sessions.get(sessionToBugID.get(session)).remove(session);
            sessionToBugID.remove(session);
        }

        clientCount--;
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // hack: please don't write production software like this. use an actual handler. don't yanderedev it.
        // hack: it's fine here because we have literally one command to switch what team you're pulling,
        // hack: but generally, this is god awful to do. don't do this :3 - rip
        if (message.startsWith("SETBUGID")) {
            waitingSessions.remove(session);

            int bugID = Integer.parseInt(message.replace("SETBUGID ", ""));

            if(sessionToBugID.containsKey(session)) {
                sessions.get(sessionToBugID.get(session)).remove(session);
            }

            sessions.get(bugID).add(session);
            sessionToBugID.put(session, bugID);
        }
    }


}
