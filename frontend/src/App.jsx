import { Route, BrowserRouter, Routes } from "react-router";
import Navbar from "./features/header/Navbar";
import SearchBar from "./features/header/SearchBar";
import ItemView from "./features/item/ItemView";
import AllItems from "./features/item/AllItems";
import Chat from "./features/chat/Chat";
import { useState } from "react";
import UserItems from "./features/item/UserItems";
import CreateItem from "./features/item/CreateItem";
import MyFavoriteItems from "./features/item/MyFavoriteItems";

function App() {
  const [searchFilters, setSearchFilters] = useState({
    searchTerm: "",
    categoryId: 1,
    postcode: "",
    locationId: "",
    radiusKm: "",
    priceMin: "",
    priceMax: "",
  });
  const [submitState, setSubmitState] = useState(false);
  return (
    <BrowserRouter>
      <header>
        <Navbar
          setSearchFilters={setSearchFilters}
          submitState={submitState}
          setSubmitState={setSubmitState}
        />
        <SearchBar
          searchFilters={searchFilters}
          setSearchFilters={setSearchFilters}
          submitState={submitState}
          setSubmitState={setSubmitState}
        />
      </header>
      <main className="min-vh-100">
        <Routes>
          <Route
            path="/"
            element={
              <AllItems
                searchFilters={searchFilters}
                submitState={submitState}
              />
            }
          />
          <Route path="/anzeigen/:itemId" element={<ItemView />} />
          <Route path="/meine-anzeigen" element={<UserItems />} />
          <Route path="/nachrichten" element={<Chat />} />
          <Route path="/anzeige-erstellen" element={<CreateItem />} />
          <Route path="/meine-merkliste" element={<MyFavoriteItems />} />
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
