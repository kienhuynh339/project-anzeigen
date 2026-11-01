import React, { createContext, useContext, useState } from "react";
const AppContext = createContext(null);

export function AppContextProvider({ children }) {
  const [dialog, setDialog] = useState(null);
  const openDialog = (DialogComponent, props = {}) => {
    setDialog({ DialogComponent, props });
  };
  const closeDialog = () => {
    setDialog(null);
  };

  return (
    <AppContext.Provider value={{ openDialog, closeDialog }}>
      {children}
      {/* Dialogfenster */}
      {dialog && (
        <dialog.DialogComponent
          {...dialog.props}
          closeDialog={closeDialog}
        ></dialog.DialogComponent>
      )}
    </AppContext.Provider>
  );
}

export function useAppContext() {
  const context = useContext(AppContext);
  if (!context) {
    throw new Error(
      "useAppContext muss innerhalb von AppContextProvider verwendet werden",
    );
  }
  return context;
}
