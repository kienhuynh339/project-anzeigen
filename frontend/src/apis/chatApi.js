/**
 * @author Trung Kien Huynh
 */
import { request } from "./request";
export function deleteChat(chatId) {
  return request(`/chats/${chatId}`, {
    method: "DELETE",
  });
}

export function getChat(chatId) {
  return request(`/chats/${chatId}`, {
    method: "GET",
  });
}

export function hasUnreadMessage() {
  return request(`/chats/has-unread-message`, {
    method: "GET",
  });
}
