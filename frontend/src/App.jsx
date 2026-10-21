import Navbar from "./features/navigation/Navbar";
import SearchBar from "./features/search/SearchBar";

function App() {
  return (
    <>
      <header>
        <Navbar></Navbar>
        <SearchBar></SearchBar>
      </header>
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
    </>
  );
}
/*
      <main className="d-flex flex-column" style={{ minHeight: "90vh" }}>
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<ItemList />} />
            <Route path="/anzeigen/:itemId" element={<ItemView />} />
          </Routes>
        </BrowserRouter>

      </main>
      <Footer />
*/
export default App;
