/**
 * @author Trung Kien Huynh
 */
import { Route, BrowserRouter, Routes } from "react-router";
import Navbar from "./features/header/Navbar";
import SearchBar from "./features/header/SearchBar";
import ItemView from "./features/item/ItemView";
import ChatView from "./features/chat/ChatView";
import UserItems from "./features/item/UserItems";
import CreateItem from "./features/item/CreateItem";
import MyFavoriteItems from "./features/item/MyFavoriteItems";
import AllItems from "./features/item/AllItems";
import MyItems from "./features/item/MyItems";
import EditItem from "./features/item/EditItem";
import Settings from "./features/profile/Settings";
import ProtectedRoute from "./components/ProtectedRoute";
import Login from "./pages/Login";
import Register from "./pages/Register";
import { ChatContextProvider } from "./contexts/ChatContext";
import NotFound from "./components/NotFound";
import { Navigate } from "react-router";
import { ItemListContextProvider } from "./contexts/ItemListContext";

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
          <Route
            path="/"
            element={
              <ItemListContextProvider>
                <AllItems />
              </ItemListContextProvider>
            }
          />
          <Route path="/anzeigen/:itemId" element={<ItemView />} />
          <Route
            path="/meine-anzeigen"
            element={
              <ProtectedRoute>
                <ItemListContextProvider>
                  <MyItems />
                </ItemListContextProvider>
              </ProtectedRoute>
            }
          />
          <Route
            path="/user/:otherUserId"
            element={
              <ItemListContextProvider>
                <UserItems />
              </ItemListContextProvider>
            }
          />
          <Route
            path="/nachrichten"
            element={
              <ProtectedRoute>
                <ChatContextProvider>
                  <ChatView />
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
            path="/meine-merkliste"
            element={
              <ProtectedRoute>
                <ItemListContextProvider>
                  <MyFavoriteItems />
                </ItemListContextProvider>
              </ProtectedRoute>
            }
          ></Route>
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
