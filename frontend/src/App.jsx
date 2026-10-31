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
import ContinueDraft from "./features/item/ContinueDraft";

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
          <Route path="/meine-anzeigen" element={<MyItems />} />
          <Route path="/user/:otherUserId" element={<UserItems />} />
          <Route path="/nachrichten" element={<ChatView />} />
          {/* Anzeige erstellen */}
          <Route path="/anzeige-erstellen" element={<CreateItem />} />
          {/* Entwurf fortsetzen */}
          <Route
            path="/anzeige-erstellen/:itemId"
            element={<ContinueDraft />}
          />
          {/* Anzeige bearbeiten */}
          <Route path="/anzeige-bearbeiten/:itemId" element={<EditItem />} />
          <Route
            path="/meine-merkliste"
            element={<MyFavoriteItems setSearchFilters={setSearchFilters} />}
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
