/**
 * @author Trung Kien Huynh
 */
import { createContext, useContext, useEffect, useRef, useState } from "react";
import { Client } from "@stomp/stompjs";
import { useAuthContext } from "./AuthContext";

const WS_URL = "ws://localhost:8080/ws";
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

// Für STOMP-WebSocket-Verbindung
export function StompContextProvider({ children }) {
  const { isAuthenticated } = useAuthContext();

  // STOMP-Client
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
        // console.log("[STOMP]", message);
      },

      // wird von stomp aufgerufen, wenn die STOMP-Verbindung erfolgreich hergestellt wurde.
      onConnect: () => {
        console.log("STOMP-Verbindung hergestellt");
        setConnected(true);
      },

      // wenn die STOMP-Verbindung ordnungsgemäß beendet wird.
      onDisconnect: () => {
        console.log("STOMP-Verbindung getrennt");
        setConnected(false);
      },

      // wenn die WebSocket-Verbindung geschlossen wird.
      onWebSocketClose: () => {
        console.log("WebSocket-Verbindung geschlossen");
        setConnected(false);
      },

      // wenn ein Fehler auf der WebSocket-Ebene auftritt.
      onWebSocketError: (error) => {
        console.error("WebSocket-Fehler:", error);
      },

      // wenn Client einen STOMP-ERROR-Frame bekommt.
      onStompError: (frame) => {
        console.error("STOMP-Fehler:", frame.headers["message"], frame.body);
      },
    });

    clientRef.current = client;

    // Startet den STOMP-Client:
    // Der STOMP-Client startet den Verbindungsaufbau.
    // Er öffnet eine WebSocket-Verbindung.
    // Er sendet eine STOMP-CONNECT-Nachricht.
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
  // callback erhält ein Event-Objekt (sp.backend.dto.Event vom Backend) als Parameter.
  const subscribe = (destination, callback) => {
    const client = clientRef.current;

    if (!client || !client.connected) {
      return null;
    }

    const subscription = client.subscribe(destination, (message) => {
      try {
        // message.body enthält das Event-Objekt
        const event = JSON.parse(message.body);
        callback(event);
      } catch (error) {
        console.error("Fehler beim Verarbeiten der STOMP-Nachricht:", error);
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

  if (!context) {
    throw new Error(
      "useStomp darf nur innerhalb von StompContextProvider verwendet werden",
    );
  }

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
