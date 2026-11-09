/**
 * @author Trung Kien Huynh
 */
import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import App from "./App.jsx";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap-icons/font/bootstrap-icons.css";
import { AppContextProvider } from "./contexts/AppContext.jsx";
import { AuthContextProvider } from "./contexts/AuthContext.jsx";
import { SearchContextProvider } from "./contexts/SearchContext.jsx";
import { StompContextProvider } from "./contexts/StompContext.jsx";

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <AuthContextProvider>
      <StompContextProvider>
        <AppContextProvider>
          <SearchContextProvider>
            <App />
          </SearchContextProvider>
        </AppContextProvider>
      </StompContextProvider>
    </AuthContextProvider>
  </StrictMode>,
);
