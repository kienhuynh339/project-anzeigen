/**
 * @author Trung Kien Huynh
 */
import { Route, BrowserRouter, Routes } from "react-router";
import Navbar from "./features/header/Navbar";
import SearchBar from "./features/header/SearchBar";
import ItemView from "./features/item/ItemView";
import ChatView from "./features/chat/ChatView";
import { useState } from "react";
import UserItems from "./features/item/UserItems";
import CreateItem from "./features/item/CreateItem";
import MyFavoriteItems from "./features/item/MyFavoriteItems";
import { initialSearchFilters } from "./utils/initialSearchFilters";
import AllItems from "./features/item/AllItems";
import MyItems from "./features/item/MyItems";
import EditItem from "./features/item/EditItem";
import Settings from "./features/profile/Settings";
import ProtectedRoute from "./components/ProtectedRoute";
import Login from "./pages/Login";
import Register from "./pages/Register";

function App() {
  const [searchFilters, setSearchFilters] = useState(initialSearchFilters);
  const [searchTrigger, setSearchTrigger] = useState(false);
  return (
    <BrowserRouter>
      <header>
        <Navbar
          setSearchFilters={setSearchFilters}
          searchTrigger={searchTrigger}
          setSearchTrigger={setSearchTrigger}
        />
        <SearchBar
          searchFilters={searchFilters}
          setSearchFilters={setSearchFilters}
          searchTrigger={searchTrigger}
          setSearchTrigger={setSearchTrigger}
        />
      </header>
      <main className="min-vh-100 d-flex flex-column">
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route
            path="/"
            element={
              <AllItems
                searchFilters={searchFilters}
                searchTrigger={searchTrigger}
              />
            }
          />
          <Route path="/anzeigen/:itemId" element={<ItemView />} />
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
            path="/nachrichten"
            element={
              <ProtectedRoute>
                <ChatView />
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
                <MyFavoriteItems setSearchFilters={setSearchFilters} />
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
