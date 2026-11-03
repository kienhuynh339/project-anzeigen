/**
 * @author Trung Kien Huynh
 */
import { request } from "./request";
export function deleteChat(chatId) {
  return request(`/chats/${chatId}`, {
    method: "DELETE",
  });
}
