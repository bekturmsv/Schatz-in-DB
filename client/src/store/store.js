import { configureStore } from "@reduxjs/toolkit";
import languageReducer from "../features/language/languageSlice";
import themeReducer from "../features/theme/themeSlice";

export const store = configureStore({
  reducer: {
    language: languageReducer,
    theme: themeReducer,
  },
});

export default store;
