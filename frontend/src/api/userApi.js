/**
 * @author Trung Kien Huynh
 */
import { request } from "./request";

export function updateProfileName(profileName) {
  const body = { profileName: profileName };
  return request(`/users/profile-name`, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  });
}

export function updateAccountName(accountName) {
  const body = { accountName: accountName };
  return request(`/users/account-name`, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  });
}

export function updatePassword(password) {
  const body = { password: password };
  return request(`/users/password`, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  });
}
