/**
 * @author Trung Kien Huynh
 */
import { useChatContext } from "../../contexts/ChatContext";
import { Link } from "react-router";
import ItemDeleteButton from "../../features/item/buttons/ItemDeleteButton";
import ItemActivateButton from "../../features/item/buttons/ItemActivateButton";
import ItemReserveButton from "../../features/item/buttons/ItemReserveButton";
import ItemTitle from "../../features/item/ItemTitle";
import { useAuthContext } from "../../contexts/AuthContext";
import { useCallback, useEffect } from "react";
import { getChat } from "../../apis/chatApi";
import {
  useStomp,
  EVENT_ACTION,
  EVENT_ENTITY,
} from "../../contexts/StompContext";

export default function ChatHeader() {
  const { currentChat, replaceChatInList } = useChatContext();
  const { user } = useAuthContext();

  useEffect(() => {
    // aktualisiert Chat
    const refreshChat = async () => {
      try {
        const fetchedChat = await getChat(currentChat.id);
        replaceChatInList(fetchedChat, currentChat.id);
      } catch (e) {
        console.error(e);
      }
    };
    refreshChat();
  }, [currentChat.id]);

  // Aktualisiert Chat anhand von Änderungen an der Anzeige
  const handleItemEvent = useCallback(
    async (event) => {
      try {
        if (
          event.entity === EVENT_ENTITY.ITEM &&
          (event.action === EVENT_ACTION.DELETE ||
            event.action === EVENT_ACTION.UPDATE)
        ) {
          // aktualisiert Chat
          const fetchedChat = await getChat(currentChat.id);
          replaceChatInList(fetchedChat, currentChat.id);
        }
      } catch (e) {
        console.error(e);
        return;
      }
    },
    [currentChat],
  );
  const { connected } = useStomp(
    `/topic/items/${currentChat.itemId}`,
    handleItemEvent,
  );

  return (
    <>
      {/* Anzeigetitel */}
      <h5
        className="text-primary m-0 ps-3  d-flex  align-items-center"
        style={{ height: "2.95rem" }}
      >
        <Link
          className="text-decoration-none"
          to={`/anzeigen/${currentChat.itemId}`}
        >
          <ItemTitle
            itemStatus={currentChat.itemStatus}
            itemTitle={currentChat.itemTitle}
            textLimit={40}
          ></ItemTitle>
        </Link>
      </h5>
      {/* Anzeige löschen/reservieren/aktivieren */}
      {user.id === currentChat.poster.id &&
        currentChat.itemStatus !== "DELETED" && (
          <>
            <div className="border-top border-dark-subtle mx-2"></div>
            <div className="d-flex flex-wrap justify-content-between p-2">
              <ItemDeleteButton itemId={currentChat.itemId}>
                <span className="ps-1">Anzeige löschen</span>
              </ItemDeleteButton>
              {currentChat.itemStatus === "RESERVED" && (
                <ItemActivateButton
                  itemId={currentChat.itemId}
                ></ItemActivateButton>
              )}
              {currentChat.itemStatus === "ACTIVE" && (
                <ItemReserveButton
                  itemId={currentChat.itemId}
                ></ItemReserveButton>
              )}
            </div>
          </>
        )}
    </>
  );
}
