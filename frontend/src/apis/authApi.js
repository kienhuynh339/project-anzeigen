/**
 * @author Trung Kien Huynh
 */

import { request, getCsrfToken } from "./request";

// Anmelden
export async function login(accountName, password) {
  await getCsrfToken();

  const body = new URLSearchParams();

  body.append("accountName", accountName);
  body.append("password", password);

  const response = await request("/auth/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
    },
    body: body.toString(),
  });

  await getCsrfToken();

  return response;
}

// Abmelden
export async function logout() {
  const response = await request(`/auth/logout`, {
    method: "POST",
  });

  await getCsrfToken();

  return response;
}
