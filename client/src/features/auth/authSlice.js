import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { authApi } from "./authApi";
import { taskApi } from "@/features/task/taskApi.js";
import { materialApi } from "@/features/material/materialApi.js";
import { themeApi } from "@/features/theme/themeApi.js";
import { ratingApi } from "@/features/rating/ratingApi.js";

// Глубокий merge (поверхностно рекурсивный для простых случаев)
function deepMerge(oldObj, newObj) {
  if (!oldObj) return newObj;
  if (typeof oldObj !== "object" || typeof newObj !== "object") return newObj;

  const merged = { ...oldObj };
  for (const key in newObj) {
    if (
        Object.prototype.hasOwnProperty.call(newObj, key)
        && newObj[key] !== undefined
    ) {
      if (
          typeof newObj[key] === "object"
          && newObj[key] !== null
          && !Array.isArray(newObj[key])
          && typeof oldObj[key] === "object"
          && oldObj[key] !== null
          && !Array.isArray(oldObj[key])
      ) {
        merged[key] = deepMerge(oldObj[key], newObj[key]);
      } else {
        merged[key] = newObj[key];
      }
    }
  }
  return merged;
}

const initialState = {
  user: null,
  token: localStorage.getItem("authToken") || null,
  isAuthenticated: !!localStorage.getItem("authToken"),
  role: "player",
  status: "idle",
  isAuthLoading: true,
};

export const initializeAuth = createAsyncThunk(
    "auth/initialize",
    async (_, { dispatch, getState }) => {
      const token = getState().auth.token;
      if (!token) return null;
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
      // Глубокий merge старого user и новых данных
      if (action.payload === null) {
        state.user = null;
        state.isAuthenticated = false;
        state.role = "player";
      } else if (state.user) {
        state.user = deepMerge(state.user, action.payload);
        state.isAuthenticated = true;
        state.role = state.user?.role || "player";
      } else {
        state.user = action.payload;
        state.isAuthenticated = !!action.payload;
        state.role = action.payload?.role || "player";
      }
    },
    setToken: (state, action) => {
      state.token = action.payload;
      if (action.payload) {
        localStorage.setItem("authToken", action.payload);
      }
    },
    logout: (state) => {
      state.user = null;
      state.token = null;
      state.isAuthenticated = false;
      state.role = "player";
      localStorage.removeItem("authToken");
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
          state.isAuthLoading = false;
        });
  },
});

export const { setUser, setToken, logout } = authSlice.actions;

export const logoutUser = () => async (dispatch) => {
  dispatch(logout());
  dispatch({ type: "theme/setTheme", payload: "default" });

  // СБРАСЫВАЕМ ВСЕ КЭШИ RTK QUERY
  dispatch(authApi.util.resetApiState());
  dispatch(taskApi.util.resetApiState());
  dispatch(themeApi.util.resetApiState());
  dispatch(ratingApi.util.resetApiState());
  dispatch(materialApi.util.resetApiState());
};

export default authSlice.reducer;
