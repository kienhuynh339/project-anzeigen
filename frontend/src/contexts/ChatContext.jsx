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

  const [chats, setChats] = useState([]);
  const [currentChat, setCurrentChat] = useState(initialChat);
  const [isChatDeleteActive, setIsChatDeleteActive] = useState(false);
  const [deleteList, setDeleteList] = useState([]); // Ids von zu löschenden Chats
  const [chatUpdateTrigger, setChatUpdateTrigger] = useState(false);

  // Chats abrufen
  const { data: chatPage } = useFetch(getPath.chats(), [chatUpdateTrigger]);

  useEffect(() => {
    setChats(chatPage?.content ?? []);
  }, [chatPage]);

  const chatListLength = chats.length;

  const replaceChatInList = (newChat, newChatId) => {
    setChats((prev) =>
      prev.map((chat) => (chat.id === newChatId ? newChat : chat)),
    );
  };

  const handleChatOrMessageEvent = useCallback(async (event) => {
    try {
      const fetchedChat = await getChat(event.entityId);

      if (event.entity === EVENT_ENTITY.CHAT) {
        // CREATE -> neuen Chat hinzufügen
        if (event.action === EVENT_ACTION.CREATE) {
          setChats((prev) => [...prev, fetchedChat]);

          // DELETE -> betroffenen Chat aktualisieren
        } else if (event.action === EVENT_ACTION.DELETE) {
          replaceChatInList(fetchedChat, event.entityId);
        }
      } else if (event.entity === EVENT_ENTITY.MESSAGE) {
        // Neue Nachricht für einen bestehenden Chat -> den Chat aktualisieren
        if (event.action === EVENT_ACTION.CREATE) {
          replaceChatInList(fetchedChat, event.entityId);
        }
      }
    } catch (e) {
      console.error(e);
      return;
    }
  }, []);

  const { connected } = useStomp("/user/chats", handleChatOrMessageEvent);

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
