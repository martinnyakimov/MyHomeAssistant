package myhomeassistant.server;

import myhomeassistant.server.service.ActionToClassMapper;
import myhomeassistant.server.util.Constants;
import myhomeassistant.server.util.MainUtil;
import myhomeassistant.server.util.UserInputObject;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

@WebSocket
public class WebSocketHandler {

    @OnWebSocketMessage
    public void handleMessage(Session session, String command) {
        System.out.println("Received: " + command);

        try {
            ActionToClassMapper.detectActionAndRedirectToClass(new UserInputObject().convertJsonToObject(command));
        } catch (Exception e) {
            MainUtil.textToSpeech(Constants.ERROR);
        }
        //session.getRemote().sendString(message);
    }
}
