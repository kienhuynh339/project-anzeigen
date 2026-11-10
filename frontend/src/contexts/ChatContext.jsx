/**
 * @author Trung Kien Huynh
 */
import {
  createContext,
  useContext,
  useState,
  useEffect,
  useCallback,
} from "react";
import { markMessageAsRead } from "../apis/messageApi";
import { getPath, useFetch } from "../hooks/useFetch";
import { useAuthContext } from "./AuthContext";
import { useStomp, EVENT_ACTION, EVENT_ENTITY } from "./StompContext";
import { getChat } from "../apis/chatApi";
import { useAppContext } from "./AppContext";

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
  const { activateRedPoint, deactivateRedPoint } = useAppContext();

  const [chats, setChats] = useState([]);
  const [currentChat, setCurrentChat] = useState(initialChat);
  const [isChatDeleteActive, setIsChatDeleteActive] = useState(false);
  const [deleteList, setDeleteList] = useState([]); // Ids von zu löschenden Chats

  // Chats abrufen
  const { data: chatPage } = useFetch(getPath.chats(), []);

  useEffect(() => {
    setChats(chatPage?.content ?? []);
  }, [chatPage]);

  const chatListLength = chats.length;

  const replaceChatInList = (newChat, newChatId) => {
    setChats((prev) =>
      prev.map((chat) => (chat.id === newChatId ? newChat : chat)),
    );
  };

  const removeChatFromList = (chatId) => {
    setChats((prev) => prev.filter((chat) => chat.id !== chatId));
  };

  const addChatToList = (chat) => {
    setChats((prev) => [...prev, chat]);
  };

  const handleChatOrMessageEvent = useCallback(async (event) => {
    try {
      if (event.entity === EVENT_ENTITY.CHAT) {
        if (event.action === EVENT_ACTION.CREATE) {
          // CREATE -> neuen Chat hinzufügen
          const fetchedChat = await getChat(event.entityId);
          addChatToList(fetchedChat);
        } else if (event.action === EVENT_ACTION.DELETE) {
          // DELETE -> betroffenen Chat entfernen
          removeChatFromList(event.entityId);
        } else if (event.action === EVENT_ACTION.SOFT_DELETE) {
          // SOFT_DELETE
          const fetchedChat = await getChat(event.entityId);

          if (user.id === fetchedChat.fistDeletedByUserId) {
            // Chat entfernen bei dem Löschenden
            removeChatFromList(event.entityId);
          } else {
            replaceChatInList(fetchedChat, event.entityId);
          }
        }
      } else if (event.entity === EVENT_ENTITY.MESSAGE) {
        const fetchedChat = await getChat(event.entityId);

        if (event.action === EVENT_ACTION.CREATE) {
          // Neue Nachricht für einen bestehenden Chat -> den Chat aktualisieren
          replaceChatInList(fetchedChat, event.entityId);
        }
      }
    } catch (e) {
      console.error(e);
      return;
    }
  }, []);

  const { connected } = useStomp("/user/chats", handleChatOrMessageEvent);

  useEffect(() => {
    // Aktualisiere currentChat
    if (currentChat.id) {
      const updatedChat = chats.find((chat) => chat.id === currentChat.id);

      if (updatedChat) {
        setCurrentChat(updatedChat);
      }
    }

    const hasUnreadMessages = chats.some(
      (chat) =>
        chat.lastMessage.read === false &&
        user.id !== chat.lastMessage.senderId,
    );
    // Roter Punkt für HamburgerMenu
    if (hasUnreadMessages) {
      activateRedPoint();
    } else {
      deactivateRedPoint();
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

  const updateChatStatus = (chatId, status) => {
    setChats((prev) =>
      prev.map((chat) =>
        chat.id === chatId
          ? {
              ...chat,
              lastMessage: {
                ...chat.lastMessage,
                read: status,
              },
            }
          : chat,
      ),
    );
  };

  const selectChat = async (chat) => {
    if (!chat.lastMessage.read && chat.lastMessage.senderId !== user?.id) {
      // Nachricht(en) als gelesen markieren
      try {
        await markMessageAsRead(chat.id);
        updateChatStatus(chat.id, true);
      } catch (e) {
        console.error(e);
      }
    }
    setCurrentChat(chat);
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
        replaceChatInList,
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
