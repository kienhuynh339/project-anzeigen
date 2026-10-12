import { useState } from "react";
import Header from "./Header";
import SearchBar from "./SearchBar";
import ItemList from "./ItemList";
import Footer from "./Footer";
import Message from "./Message";
import Settings from "./Settings";

// Item merken in der Itemansicht

function App() {
  return (
    <div>
      <Header />
      <SearchBar />
      <main className="d-flex flex-column" style={{ minHeight: "90vh" }}>
        {/* <ItemList /> */}
        {/* <Message /> */}
        <Settings />
      </main>
      <Footer />
    </div>
  );
}

export default App;
