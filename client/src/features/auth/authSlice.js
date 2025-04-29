import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  user: {
    nickname: "Guest",
    points: 0,
    purchasedThemes: ["default"],
    email: "",
    firstname: "",
    lastname: "",
  },
  token: null,
  isAuthenticated: false,
  completedLevels: {
    easy: false,
    medium: false,
    hard: false,
  },

};

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    setUser(state, action) {
      state.user = { ...state.user, ...action.payload };
      state.isAuthenticated = true;
    },
    setToken(state, action) {
      state.token = action.payload;
    },
    addPoints(state, action) {
      state.user.points += action.payload;
    },
    purchaseTheme(state, action) {
      state.user.purchasedThemes.push(action.payload);
    },
    logout(state) {
      state.user = initialState.user;
      state.token = null;
      state.isAuthenticated = false;
    },
    completeLevel: (state, action) => {
      const {difficulty} = action.payload;
      state.completedLevels[difficulty] = true;
    }
  },
});

export const { setUser, setToken, addPoints, purchaseTheme, logout, completeLevel } =
  authSlice.actions;
export default authSlice.reducer;
