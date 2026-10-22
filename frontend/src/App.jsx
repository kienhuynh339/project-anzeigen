import { Route, BrowserRouter, Routes } from "react-router";
import Navbar from "./features/navigation/Navbar";
import SearchBar from "./features/search/SearchBar";
import ItemView from "./features/item/ItemView";
import ItemList from "./features/item/ItemList";
import { useState } from "react";

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
        <Navbar />
        <SearchBar
          searchFilters={searchFilters}
          setSearchFilters={setSearchFilters}
          submitState={submitState}
          setSubmitState={setSubmitState}
        />
      </header>
      <main>
        <Routes>
          <Route
            path="/"
            element={
              <ItemList
                searchFilters={searchFilters}
                submitState={submitState}
              />
            }
          />
          <Route path="/anzeigen/:itemId" element={<ItemView />} />
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
