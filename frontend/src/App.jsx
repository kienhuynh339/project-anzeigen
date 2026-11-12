/**
 * @author Trung Kien Huynh
 */
import { Route, BrowserRouter, Routes } from "react-router";
import Navbar from "./components/Navbar";
import SearchBar from "./features/search/SearchBar";
import ItemDetails from "./pages/item/ItemDetails";
import Chat from "./pages/chat/Chat";
import UserItems from "./pages/item/UserItems";
import CreateItem from "./pages/item/CreateItem";
import MyFavoriteItems from "./pages/item/MyFavoriteItems";
import AllItems from "./pages/item/AllItems";
import MyItems from "./pages/item/MyItems";
import EditItem from "./pages/item/EditItem";
import Settings from "./pages/setting/Settings";
import ProtectedRoute from "./components/ProtectedRoute";
import Login from "./pages/authentication/Login";
import Register from "./pages/authentication/Register";
import { ChatContextProvider } from "./contexts/ChatContext";
import NotFound from "./pages/not-found/NotFound";
import { Navigate } from "react-router";

function App() {
  return (
    <BrowserRouter>
      {/* Kopfbereich  */}
      <header>
        <Navbar></Navbar>
        <SearchBar></SearchBar>
      </header>
      {/* Hauptbereich  */}
      <main className="min-vh-100 d-flex flex-column">
        <Routes>
          {/* 404 Not Found */}
          <Route path="/404" element={<NotFound />} />
          {/* Anmelden */}
          <Route path="/login" element={<Login />} />
          {/* Registrieren */}
          <Route path="/register" element={<Register />} />
          {/* Alle (gefilterten) Anzeigen */}
          <Route path="/" element={<AllItems />} />
          {/* Anzeigedetails */}
          <Route path="/anzeigen/:itemId" element={<ItemDetails />} />
          {/* Meine Anzeigen */}
          <Route
            path="/meine-anzeigen"
            element={
              <ProtectedRoute>
                <MyItems />
              </ProtectedRoute>
            }
          />
          {/* Anzeigen von User ... */}
          <Route path="/user/:otherUserId" element={<UserItems />} />
          {/* Meine Merkliste */}
          <Route
            path="/meine-merkliste"
            element={
              <ProtectedRoute>
                <MyFavoriteItems />
              </ProtectedRoute>
            }
          ></Route>
          {/* Nachrichten */}
          <Route
            path="/nachrichten"
            element={
              <ProtectedRoute>
                <ChatContextProvider>
                  <Chat />
                </ChatContextProvider>
              </ProtectedRoute>
            }
          />
          {/* Anzeige erstellen */}
          <Route
            path="/anzeige-erstellen"
            element={
              <ProtectedRoute>
                <CreateItem />
              </ProtectedRoute>
            }
          />
          {/* Entwurf fortsetzen / Anzeige bearbeiten */}
          <Route
            path="/bearbeiten/:itemId"
            element={
              <ProtectedRoute>
                <EditItem />
              </ProtectedRoute>
            }
          />
          {/* Einstellungen */}
          <Route
            path="/einstellungen"
            element={
              <ProtectedRoute>
                <Settings />
              </ProtectedRoute>
            }
          ></Route>
          {/* Alle unbekannten Pfade */}
          <Route path="*" element={<Navigate to="/404" replace />} />
        </Routes>
      </main>
      {/* Fußbereich  */}
      <footer className="bg-dark mt-auto py-3 flex-grow-1">
        <div className="container">
          <div className="text-center text-secondary">
            <small>
              &copy; {new Date().getFullYear()} anzeigen. Alle Rechte
              vorbehalten.
            </small>
          </div>
        </div>
      </footer>
    </BrowserRouter>
  );
}

export default App;
