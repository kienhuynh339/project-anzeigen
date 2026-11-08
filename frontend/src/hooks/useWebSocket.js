import { useEffect } from "react";
import { Client } from "@stomp/stompjs";

const WS_URL = "ws://localhost:8080/ws";

export function useWebSocket(onDataChanged) {
  useEffect(() => {
    const client = new Client({
      brokerURL: WS_URL,

      reconnectDelay: 5000,

      onConnect: () => {
        console.log("WebSocket connected");

        client.subscribe("/topic/items", (message) => {
          onDataChanged(JSON.parse(message.body));
        });

        client.subscribe("/topic/chats", (message) => {
          onDataChanged(JSON.parse(message.body));
        });
      },

      onStompError: (frame) => {
        console.error("STOMP error:", frame);
      },
    });

    client.activate();

    return () => {
      client.deactivate();
    };
  }, [onDataChanged]);
}
