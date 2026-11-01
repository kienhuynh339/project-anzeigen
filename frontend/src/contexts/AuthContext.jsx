import { createContext, useContext, useEffect, useState } from "react";

import * as authService from "../services/authService";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  // Prüfen, ob bereits eine Session existiert
  useEffect(() => {
    async function checkAuthentication() {
      try {
        const currentUser = await authService.getCurrentUser();

        setUser(currentUser);
      } catch (error) {
        setUser(null);
      } finally {
        setLoading(false);
      }
    }

    checkAuthentication();
  }, []);

  // ANMELDEN
  async function login(accountName, password) {
    const loggedInUser = await authService.login(accountName, password);

    setUser(loggedInUser);

    return loggedInUser;
  }

  // REGISTRIEREN
  async function register(profileName, accountName, password) {
    return authService.register(profileName, accountName, password);
  }

  // ABMELDEN
  async function logout() {
    await authService.logout();

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

export function useAuth() {
  return useContext(AuthContext);
}
