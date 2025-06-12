import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  currentTheme: "default",
};

const themeSlice = createSlice({
  name: "theme",
  initialState,
  reducers: {
    setTheme(state, action) {
      state.currentTheme = action.payload;
      document.documentElement.setAttribute("data-theme", action.payload);
    },
  },
});

export const { setTheme } = themeSlice.actions;
export default themeSlice.reducer;
