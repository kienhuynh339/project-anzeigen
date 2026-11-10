import { createContext, useContext, useEffect, useRef, useState } from "react";
import { Client } from "@stomp/stompjs";
import { useAuthContext } from "./AuthContext";

const WS_URL = "ws://localhost:8080/ws"; // "http://localhost:8080/ws"
const StompContext = createContext(null);

export const EVENT_ENTITY = Object.freeze({
  ITEM: "item",
  CHAT: "chat",
  MESSAGE: "message",
});

export const EVENT_ACTION = Object.freeze({
  CREATE: "create",
  UPDATE: "update",
  DELETE: "delete",
  SOFT_DELETE: "softDelete",
});

export function StompContextProvider({ children }) {
  const { isAuthenticated } = useAuthContext();

  const clientRef = useRef(null);

  const [connected, setConnected] = useState(false);

  useEffect(() => {
    if (!isAuthenticated) {
      return;
    }

    // STOMP-Client (Simple Text Oriented Messaging Protocol)
    const client = new Client({
      brokerURL: WS_URL, // Websocket-Endpunkt (Backend)
      // wie lange wartet der STOMP-Client nach einem Verbindungsabbruch, bevor er einen erneuten Verbindungsversuch startet. (milsec)
      reconnectDelay: 5000,
      // wie häufig erwartet Client eingehende Heartbeat-Nachrichten vom Server.
      // Mit Heartbeat können beide Seiten erkennen, ob die Verbindung noch funktioniert.
      heartbeatIncoming: 10000,
      // wie häufig sendet Client Heartbeat-Nachrichten an den Server.
      heartbeatOutgoing: 9000,

      // wird von stomp aufgerufen, wenn Debug-Nachrichten ausgegeben werden.
      debug: (message) => {
        console.log("[STOMP]", message);
      },

      // wird von stomp aufgerufen, wenn die STOMP-Verbindung erfolgreich hergestellt wurde.
      onConnect: () => {
        console.log("STOMP connected");
        setConnected(true);
      },

      // wenn die STOMP-Verbindung ordnungsgemäß beendet wird.
      onDisconnect: () => {
        console.log("STOMP disconnected");
        setConnected(false);
      },

      // wenn die WebSocket-Verbindung geschlossen wird.
      onWebSocketClose: () => {
        console.log("WebSocket closed");
        setConnected(false);
      },

      // wenn ein Fehler auf der WebSocket-Ebene auftritt.
      onWebSocketError: (error) => {
        console.error("WebSocket error:", error);
      },

      // wenn Client einen STOMP-ERROR-Frame bekommt.
      onStompError: (frame) => {
        console.error("STOMP error:", frame.headers["message"], frame.body);
      },
    });

    clientRef.current = client;

    // Startet den STOMP-Client:
    // Der STOMP-Client startet den Verbindungsaufbau.
    // Er öffnet eine WebSocket-Verbindung.
    // Er sendet eine STOMP-CONNECT-Nachricht.
    // Der Spring-Boot-Server verarbeitet die Verbindung.
    // Der Server antwortet mit STOMP-CONNECTED.
    // onConnect()-Callback wird ausgeführt.
    client.activate();

    // STOMP-Verbindung schließen
    return () => {
      setConnected(false);
      client.deactivate();
      clientRef.current = null;
    };
  }, [isAuthenticated]);

  // einen bestimmten Nachrichtenkanal (destination) beim Backend abonnieren
  // callback erhält ein Event-Objekt (DataChangedEvent vom Backend) als Parameter.
  const subscribe = (destination, callback) => {
    const client = clientRef.current;

    if (!client || !client.connected) {
      return null;
    }

    const subscription = client.subscribe(destination, (message) => {
      try {
        const event = JSON.parse(message.body);
        callback(event);
      } catch (error) {
        console.error("STOMP message parsing error:", error);
      }
    });

    return subscription;
  };

  return (
    <StompContext.Provider
      value={{
        client: clientRef,
        connected,
        subscribe,
      }}
    >
      {children}
    </StompContext.Provider>
  );
}

export function useStomp(destination, callback) {
  const context = useContext(StompContext);

  const callbackRef = useRef(callback);

  useEffect(() => {
    callbackRef.current = callback;
  }, [callback]);

  useEffect(() => {
    if (!context || !context.connected || !destination) {
      return;
    }

    const subscription = context.subscribe(destination, (event) => {
      callbackRef.current?.(event);
    });

    return () => {
      subscription?.unsubscribe();
    };
  }, [context?.connected, destination]);

  return {
    connected: context?.connected ?? false,
  };
}
