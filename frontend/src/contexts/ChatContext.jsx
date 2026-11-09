/**
 * @author Trung Kien Huynh
 */
import { createContext, useContext, useState, useEffect } from "react";
import { markMessageAsRead } from "../apis/messageApi";
import { getPath, useFetch } from "../hooks/useFetch";
import { useAuthContext } from "./AuthContext";

const ChatContext = createContext(null);
const initialChat = {
  id: "",
  itemId: "",
  itemTitle: "",
  itemStatus: "",
  poster: {
    createdAt: "",
    id: "",
    profileName: "",
  },
  interestedUser: {
    createdAt: "",
    id: "",
    profileName: "",
  },
  lastMessage: {
    content: "",
    createdAt: "",
    id: "",
    read: "",
    senderId: "",
  },
  fistDeletedByUserId: "",
};

export function ChatContextProvider({ children }) {
  const { user } = useAuthContext();

  const [currentChat, setCurrentChat] = useState(initialChat);
  const [isChatDeleteActive, setIsChatDeleteActive] = useState(false);
  const [deleteList, setDeleteList] = useState([]); // Ids von zu löschenden Chats
  const [chatUpdateTrigger, setChatUpdateTrigger] = useState(false);

  // Chats abrufen
  const { data: chatPage } = useFetch(getPath.chats(), [chatUpdateTrigger]);

  const handleChatEvent = useCallback((event) => {
    if (event.entity) {
    }
  });

  /*
  client.subscribe("/user/queue/chats", (event) => {
    const data = JSON.parse(event.body);

    console.log("Chat-Veränderung:", data);
});
       String entity,
        String action,
        Long entityId,
        Long parentId
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
  */

  const chats = chatPage?.content ?? [];
  const chatListLength = chats.length;

  // Aktualisiere currentChat
  useEffect(() => {
    if (!currentChat.id) return;

    const updatedChat = chats.find((chat) => chat.id === currentChat.id);

    if (updatedChat) {
      setCurrentChat(updatedChat);
    }
  }, [chats]);

  const otherUser =
    user.id != currentChat.poster.id
      ? currentChat.poster
      : currentChat.interestedUser;

  const resetCurrentChat = () => {
    setCurrentChat(initialChat);
  };

  const selectChatToDelete = (chat) => {
    setDeleteList((prev) => {
      return prev.includes(chat.id)
        ? prev.filter((id) => id !== chat.id)
        : [...prev, chat.id];
    });
  };

  const selectChat = async (chat) => {
    if (!chat.lastMessage.read && chat.lastMessage.senderId !== user?.id) {
      // Nachricht(en) als gelesen markieren

      try {
        await markMessageAsRead(chat.id);
        chat.lastMessage.read = true;
      } catch (e) {
        console.error(e);
      }
    }
    setCurrentChat(chat);
  };

  const updateChat = () => {
    setChatUpdateTrigger((prev) => !prev);
  };

  return (
    <ChatContext.Provider
      value={{
        currentChat,
        setCurrentChat,
        resetCurrentChat,
        isChatDeleteActive,
        setIsChatDeleteActive,
        deleteList,
        setDeleteList,
        chats,
        chatListLength,
        otherUser,
        selectChatToDelete,
        selectChat,
        updateChat,
      }}
    >
      {children}
    </ChatContext.Provider>
  );
}

export function useChatContext() {
  const context = useContext(ChatContext);
  if (!context) {
    throw new Error(
      "useChatContext muss innerhalb von ChatContextProvider verwendet werden",
    );
  }
  return context;
}
