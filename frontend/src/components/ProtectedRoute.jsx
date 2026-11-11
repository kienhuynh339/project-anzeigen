/**
 * @author Trung Kien Huynh
 */
import { Navigate } from "react-router";
import { useAuthContext } from "../contexts/AuthContext";

// Leitet nicht angemeldete Users zu "/login" weiter.
export default function ProtectedRoute({ children }) {
  const { isAuthenticated, isLoadingUser } = useAuthContext();

  if (isLoadingUser) {
    return <p>Lade...</p>;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return children;
}
