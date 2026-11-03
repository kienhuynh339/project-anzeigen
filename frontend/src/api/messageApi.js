/**
 * @author Trung Kien Huynh
 */
import { request } from "./request";
export function sendMessage(itemId, interestedUserId, content) {
  const body = {
    itemId: itemId,
    interestedUserId: interestedUserId,
    content: content,
  };
  return request("/messages", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  });
}

export function markMessageAsRead(chatId) {
  return request(`/messages/chat/${chatId}`, {
    method: "PATCH",
  });
}
