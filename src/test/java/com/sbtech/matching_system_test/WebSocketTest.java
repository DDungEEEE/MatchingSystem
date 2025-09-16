package com.sbtech.matching_system_test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebSocketTest {

    @LocalServerPort
    private int port;

    private WebSocketStompClient stompClient;

    @BeforeEach
    void setup() {
        stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @Test
    void 매장_요청_알림_테스트() throws Exception {
        String url = "ws://localhost:" + port + "/ws";

        StompSession session = stompClient
                .connect(url, new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);

        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);

        session.subscribe("/topic/stores/1/requests", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((String) payload);
            }
        });

        // 실제 요청 생성 API 호출 → 메시지가 큐에 들어올 것
        // ...

        String message = blockingQueue.poll(3, TimeUnit.SECONDS);
        assertThat(message).contains("노트북 배터리 교체");
    }
}
