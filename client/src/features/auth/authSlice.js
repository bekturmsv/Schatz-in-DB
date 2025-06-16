import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { authApi } from "./authApi";
import { setTheme } from "../theme/themeSlice";

const initialState = {
  user: null,
  token: localStorage.getItem("authToken") || null,
  isAuthenticated: !!localStorage.getItem("authToken"),
  role: "player",
  status: "idle",
  isAuthLoading: false,
};

let inactivityTimeout;

const resetInactivityTimeout = (dispatch) => {
  if (inactivityTimeout) clearTimeout(inactivityTimeout);
  inactivityTimeout = setTimeout(() => {
    dispatch(logout());
    localStorage.removeItem("authToken");
  }, 900000);
};

export const initializeAuth = createAsyncThunk(
    "auth/initialize",
    async (_, { dispatch, getState }) => {
      const token = getState().auth.token;
      if (!token) {
        // Нет токена — не надо грузить, сразу сбрасываем loading
        return null;
      }
      try {
        const result = await dispatch(authApi.endpoints.getMe.initiate()).unwrap();
        if (result.token) dispatch(setToken(result.token));

        if (result.user) dispatch(setUser(result.user));
        return result.user;
      } catch (error) {
        if (error && error.status === 401) {
          dispatch(logout());
          localStorage.removeItem("authToken");
        }
        return null;
      }
    }
);

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    setUser: (state, action) => {
      state.user = action.payload || null;
      state.isAuthenticated = !!action.payload;
      state.role = action.payload?.role || "player";
    },
    setToken: (state, action) => {
      state.token = action.payload;
      if (action.payload) {
        localStorage.setItem("authToken", action.payload);
        resetInactivityTimeout();
      }
    },
    logout: (state) => {
      state.user = null;
      state.token = null;
      state.isAuthenticated = false;
      state.role = "player";
      localStorage.removeItem("authToken");
      if (inactivityTimeout) clearTimeout(inactivityTimeout);
    },
  },
  extraReducers: (builder) => {
    builder
        .addCase(initializeAuth.pending, (state) => {
          state.status = "loading";
          state.isAuthLoading = true;
        })
        .addCase(initializeAuth.fulfilled, (state, action) => {
          state.status = "succeeded";
          state.isAuthLoading = false;
          if (action.payload) {
            state.user = action.payload;
            state.isAuthenticated = true;
            state.role = action.payload.role || "player";
          } else {
            state.user = null;
            state.isAuthenticated = !!state.token;
          }
        })
        .addCase(initializeAuth.rejected, (state) => {
          state.status = "failed";
        });
  },
});

export const { setUser, setToken, logout } = authSlice.actions;

export const logoutUser = () => async (dispatch) => {
  dispatch(logout());
  dispatch(setTheme("default"));
};

export default authSlice.reducer;

// inactivity таймер
document.addEventListener("click", () => {
  const state = window.store.getState();
  if (state.auth.token) resetInactivityTimeout(window.store.dispatch);
});
document.addEventListener("keydown", () => {
  const state = window.store.getState();
  if (state.auth.token) resetInactivityTimeout(window.store.dispatch);
});
