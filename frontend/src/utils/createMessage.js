/**
 * @author Trung Kien Huynh
 */
import { createRequest } from "./createRequest";

export function createMessage(itemId, interestedUserId, senderId, content) {
  const newMessage = {
    itemId: itemId,
    interestedUserId: interestedUserId,
    senderId: senderId,
    content: content,
  };
  return createRequest("http://localhost:8080/api/messages", newMessage);
}
