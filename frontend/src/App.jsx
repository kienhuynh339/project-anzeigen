import { useState } from "react";
import Header from "./Header";
import SearchBar from "./SearchBar";

function App() {
  return (
    <div>
      <Header />
      <SearchBar />
      <div
        className="container-fluid bg-warning-subtle"
        style={{ height: "300px" }}
      >
        Produkte....
      </div>
    </div>
  );
}

export default App;
