import { useState } from "react";
import Header from "./Header";
import ItemList from "./ItemList";
import Footer from "./Footer";
import Message from "./Message";
import Settings from "./Settings";
import ItemView from "./ItemView";
import CreateItem from "./CreateItem";

function App() {
  return (
    <div>
      <Header />
      <main className="d-flex flex-column" style={{ minHeight: "90vh" }}>
        {/* <ItemList /> */}
        {/* <Message /> */}
        {/* <Settings /> */}
        {/* <ItemView /> */}
        <CreateItem />
      </main>
      <Footer />
    </div>
  );
}

export default App;
