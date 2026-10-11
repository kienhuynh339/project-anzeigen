import { useState } from "react";
import Header from "./Header";
import SearchBar from "./SearchBar";
import ItemList from "./ItemList";

// Item merken in der Itemansicht

function App() {
  return (
    <div>
      <Header />
      <SearchBar />
      <main className="container-fluid py-3">
        <ItemList />
      </main>
    </div>
  );
}

export default App;
