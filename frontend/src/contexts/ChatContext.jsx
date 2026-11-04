import { createContext, useContext, useState, useEffect } from "react";
import { markMessageAsRead } from "../api/messageApi";
import { getPath, useFetch } from "../hooks/useFetch";
import { useAuth } from "./AuthContext";

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
  const [currentChat, setCurrentChat] = useState(initialChat);
  const [isChatDeleteActive, setIsChatDeleteActive] = useState(false);
  const [deleteList, setDeleteList] = useState([]); // Ids von zu löschenden Chats
  const { user } = useAuth();

  const { data: chatPage } = useFetch(getPath.chats(), []);

  const chats = chatPage?.content ?? [];
  const chatListLength = chats.length;

  // // Aktualisiere currentChat
  // useEffect(() => {
  //   if (!currentChat.id) return;

  //   const updatedChat = chats.find((chat) => chat.id === currentChat.id);

  //   if (updatedChat) {
  //     setCurrentChat(updatedChat);
  //   }
  // }, [chats, currentChat.id]);

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
