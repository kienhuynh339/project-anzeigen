/**
 * @author Trung Kien Huynh
 */
import { createContext, useContext, useEffect, useState } from "react";

import * as authApi from "../apis/authApi";
import * as userApi from "../apis/userApi";

const AuthContext = createContext(null);

// Für Anmelden, Abmelden, Registrieren und den aktuellen User
export function AuthContextProvider({ children }) {
  // aktueller User
  const [user, setUser] = useState(null);
  const [isLoadingUser, setIsLoadingUser] = useState(true);

  useEffect(() => {
    async function loadCurrentUser() {
      setIsLoadingUser(true);
      try {
        const currentUser = await userApi.getCurrentUser();
        setUser(currentUser);
      } catch (e) {
        setUser(null);
      } finally {
        setIsLoadingUser(false);
      }
    }

    loadCurrentUser();
  }, []);

  async function login(accountName, password) {
    await authApi.login(accountName, password);
    const loggedInUser = await userApi.getCurrentUser();
    setUser(loggedInUser);
  }

  function register(profileName, accountName, password) {
    return userApi.register(profileName, accountName, password);
  }

  async function logout() {
    await authApi.logout();
    setUser(null);
  }

  return (
    <AuthContext.Provider
      value={{
        user,
        isAuthenticated: !!user,
        isLoadingUser,
        login,
        register,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuthContext() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error(
      "useAuthContext darf nur innerhalb von AuthContextProvider verwendet werden",
    );
  }
  return context;
}
