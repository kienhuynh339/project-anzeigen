/**
 * @author Trung Kien Huynh
 */
import { Navigate } from "react-router";
import { useAuthContext } from "../contexts/AuthContext";

// Nicht angemeldete Users zu "/login" weiterleiten.
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
