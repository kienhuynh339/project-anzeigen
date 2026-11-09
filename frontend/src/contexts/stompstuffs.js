import { useCallback, useEffect, useState } from "react";

import { getMessagesByChatId, createMessage } from "../api/messageApi";

import { useStomp } from "../websocket/useStomp";

export function useMessages(chatId) {
  const [messages, setMessages] = useState([]);

  const [loading, setLoading] = useState(false);

  const [error, setError] = useState(null);

  const loadMessages = useCallback(() => {
    if (!chatId) return Promise.resolve();

    setLoading(true);

    return getMessagesByChatId(chatId, 20)
      .then((response) => {
        setMessages(response.content);

        setError(null);
      })

      .catch((error) => {
        console.error(error);

        setError(error.message);
      })

      .finally(() => {
        setLoading(false);
      });
  }, [chatId]);

  // Initiales Laden und Laden bei Seitenwechsel
  useEffect(() => {
    loadMessages();
  }, [loadMessages]);

  // callback für stomp-Event
  const handleMessageEvent = useCallback(
    (event) => {
      if (event.entity === "MESSAGE" && event.parentId === Number(chatId)) {
        console.log("Neue Änderung im Chat:", event);
        // aktualisiere die States hier:
        loadMessages(); // enthält setMessages(response.content);
      }
    },
    [chatId, loadMessages],
  );
  // Abonieren
  const { connected } = useStomp(`/topic/chats/${chatId}`, handleMessageEvent);

  return {
    messages,
    loading,
    error,
    connected,
    reload: loadMessages,
  };
}

import { useCallback, useEffect, useState } from "react";

import { getChatsByUserId } from "../api/chatApi";

import { useStomp } from "../websocket/useStomp";
/*
Dein Backend sendet bei Chat-Änderungen an: "/topic/chats"
Damit wird die Chat-Liste aktualisiert, wenn dein Backend ein entsprechendes CHAT-Event verschickt.
*/
export function useChats(userId) {
  const [chats, setChats] = useState([]);

  const [loading, setLoading] = useState(false);

  const loadChats = useCallback(() => {
    if (!userId) return Promise.resolve();

    setLoading(true);

    return getChatsByUserId(userId)
      .then((response) => {
        setChats(response.content);
      })

      .catch((error) => {
        console.error(error);
      })

      .finally(() => {
        setLoading(false);
      });
  }, [userId]);

  useEffect(() => {
    loadChats();
  }, [loadChats]);

  const handleChatEvent = useCallback(
    (event) => {
      if (event.entity === "CHAT") {
        loadChats();
      }
    },
    [loadChats],
  );

  const { connected } = useStomp("/topic/chats", handleChatEvent);

  return {
    chats,
    loading,
    connected,
    reload: loadChats,
  };
}

/*
Dein Backend verwendet für Items:
destination = "/topic/items";
*/
import { useCallback, useEffect, useState } from "react";

import { getItems } from "../api/itemApi";

import { useStomp } from "../websocket/useStomp";

export function useItems(searchParams = {}) {
  const [items, setItems] = useState([]);

  const [loading, setLoading] = useState(false);

  const loadItems = useCallback(() => {
    setLoading(true);

    return getItems(searchParams)
      .then((response) => {
        setItems(response.content);
      })

      .catch((error) => {
        console.error(error);
      })

      .finally(() => {
        setLoading(false);
      });
  }, [searchParams]);

  useEffect(() => {
    loadItems();
  }, [loadItems]);

  const handleItemEvent = useCallback(
    (event) => {
      if (event.entity === "ITEM") {
        loadItems();
      }
    },
    [loadItems],
  );

  const { connected } = useStomp("/topic/items", handleItemEvent);

  return {
    items,
    loading,
    connected,
    reload: loadItems,
  };
}
