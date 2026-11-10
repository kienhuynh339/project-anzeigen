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
      <header>
        <Navbar></Navbar>
        <SearchBar></SearchBar>
      </header>
      <main className="min-vh-100 d-flex flex-column">
        <Routes>
          <Route path="/404" element={<NotFound />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/" element={<AllItems />} />
          <Route path="/anzeigen/:itemId" element={<ItemDetails />} />
          <Route
            path="/meine-anzeigen"
            element={
              <ProtectedRoute>
                <MyItems />
              </ProtectedRoute>
            }
          />
          <Route path="/user/:otherUserId" element={<UserItems />} />
          <Route
            path="/meine-merkliste"
            element={
              <ProtectedRoute>
                <MyFavoriteItems />
              </ProtectedRoute>
            }
          ></Route>
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
