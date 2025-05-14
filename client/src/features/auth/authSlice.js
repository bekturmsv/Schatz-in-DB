import { createSlice } from '@reduxjs/toolkit';
import {setTheme} from "@/features/theme/themeSlice.js";

const initialState = {
  user: null,
  token: null,
  isAuthenticated: false,
  role: "admin" // default role
};

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    setUser: (state, action) => {
      state.user = action.payload;
      state.isAuthenticated = true;
      state.role = action.payload.role || "admin";
    },
    setToken: (state, action) => {
      state.token = action.payload;
    },
    logout: (state) => {
      state.user = null;
      state.token = null;
      state.isAuthenticated = false;
    },
  },
});

export const { setUser, setToken, logout } = authSlice.actions;

console.log(initialState)
export const logoutUser = () => async (dispatch) => {
  dispatch(logout());
  dispatch(setTheme("default"));
}

export default authSlice.reducer;