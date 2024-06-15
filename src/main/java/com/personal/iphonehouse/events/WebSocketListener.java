package com.personal.iphonehouse.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketListener {
    public void handleWebsocketDisconnectEvent(SessionDisconnectEvent event) {
        // todo
        //
    }
}
