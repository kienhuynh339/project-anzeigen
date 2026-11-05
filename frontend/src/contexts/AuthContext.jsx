import { createContext, useContext, useEffect, useState } from "react";

import * as authApi from "../api/authApi";

const AuthContext = createContext(null);

export function AuthContextProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  // Prüfen, ob bereits eine Session existiert
  useEffect(() => {
    async function checkAuthentication() {
      try {
        const currentUser = await authApi.getCurrentUser();
        setUser(currentUser);
      } catch (error) {
        setUser(null);
      } finally {
        setLoading(false);
      }
    }

    checkAuthentication();
  }, []);

  // Anmelden
  async function login(accountName, password) {
    const loggedInUser = await authApi.login(accountName, password);
    setUser(loggedInUser);
  }

  // Registrieren
  function register(profileName, accountName, password) {
    return authApi.register(profileName, accountName, password);
  }

  // Abmelden
  async function logout() {
    await authApi.logout();
    setUser(null);
  }

  return (
    <AuthContext.Provider
      value={{
        user,
        loading,
        isAuthenticated: !!user,
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
  return useContext(AuthContext);
}
