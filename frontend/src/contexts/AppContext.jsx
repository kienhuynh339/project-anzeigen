/**
 * @author Trung Kien Huynh
 */
import {
  createContext,
  useContext,
  useState,
  useCallback,
  useEffect,
} from "react";
import { useStomp, EVENT_ACTION, EVENT_ENTITY } from "./StompContext";
import { hasUnreadMessage } from "../apis/chatApi";
import { useAuthContext } from "./AuthContext";
const AppContext = createContext(null);

export function AppContextProvider({ children }) {
  const { isAuthenticated } = useAuthContext();

  const [dialog, setDialog] = useState(null);
  const [redPointActive, setRedPointActive] = useState(false);

  useEffect(() => {
    const hasUnread = async () => {
      try {
        const result = await hasUnreadMessage();
        setRedPointActive(result);
      } catch (e) {
        console.error(e);
        setRedPointActive(false);
      }
    };
    hasUnread();
  }, [isAuthenticated]);

  const activateRedPoint = () => {
    setRedPointActive(true);
  };

  const deactivateRedPoint = () => {
    setRedPointActive(false);
  };

  const openDialog = (DialogComponent, props = {}) => {
    setDialog({ DialogComponent, props });
  };
  const closeDialog = () => {
    setDialog(null);
  };

  const handleChatOrMessageEvent = useCallback(async (event) => {
    try {
      if (
        (event.entity === EVENT_ENTITY.CHAT &&
          event.action === EVENT_ACTION.CREATE) ||
        (event.entity === EVENT_ENTITY.MESSAGE &&
          event.action === EVENT_ACTION.CREATE)
      ) {
        activateRedPoint();
      }
    } catch (e) {
      console.error(e);
      return;
    }
  }, []);

  const { connected } = useStomp("/user/new-message", handleChatOrMessageEvent);

  return (
    <AppContext.Provider
      value={{
        openDialog,
        closeDialog,
        redPointActive,
        activateRedPoint,
        deactivateRedPoint,
      }}
    >
      {children}
      {/* Dialogfenster */}
      {dialog && (
        <dialog.DialogComponent
          {...dialog.props}
          closeDialog={closeDialog}
        ></dialog.DialogComponent>
      )}
    </AppContext.Provider>
  );
}

export function useAppContext() {
  const context = useContext(AppContext);
  if (!context) {
    throw new Error(
      "useAppContext muss innerhalb von AppContextProvider verwendet werden",
    );
  }
  return context;
}
