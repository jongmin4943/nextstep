package http;

import java.util.HashMap;
import java.util.Map;

public class Sessions {
    private static Map<String, Session> sessions = new HashMap<>();

    public static Session getSession(String id) {
        Session session = sessions.get(id);

        if(session == null) {
            session = new Session(id);
            sessions.put(id,session);
            return session;
        }
        return session;
    }

    static void remove(String id) {
        sessions.remove(id);
    }
}
