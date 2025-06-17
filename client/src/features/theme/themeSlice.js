import { createSlice } from "@reduxjs/toolkit";

const getInitialTheme = () => {
  if (typeof window !== "undefined") {
    return localStorage.getItem("theme") || "default";
  }
  return "default";
};

const initialState = {
  currentTheme: getInitialTheme(),
};

const themeSlice = createSlice({
  name: "theme",
  initialState,
  reducers: {
    setTheme(state, action) {
      state.currentTheme = action.payload;
      // Важно: меняем data-theme и сохраняем в localStorage!
      if (typeof window !== "undefined") {
        document.documentElement.setAttribute("data-theme", action.payload);
        localStorage.setItem("theme", action.payload);
      }
    },
    initTheme(state) {
      // При инициализации (например, в MainLayout)
      const saved = getInitialTheme();
      state.currentTheme = saved;
      if (typeof window !== "undefined") {
        document.documentElement.setAttribute("data-theme", saved);
      }
    },
  },
});

export const { setTheme, initTheme } = themeSlice.actions;
export default themeSlice.reducer;
