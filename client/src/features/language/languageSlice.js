import { createSlice } from "@reduxjs/toolkit";
import i18n from "../../utils/i18n";

const initialState = {
  currentLanguage: "en",
};

const languageSlice = createSlice({
  name: "language",
  initialState,
  reducers: {
    setLanguage(state, action) {
      state.currentLanguage = action.payload;
      i18n.changeLanguage(action.payload);
    },
  },
});

export const { setLanguage } = languageSlice.actions;
export default languageSlice.reducer;
