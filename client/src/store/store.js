import { configureStore } from "@reduxjs/toolkit";
import languageReducer from "../features/language/languageSlice";
import themeReducer from "../features/theme/themeSlice";
import authReducer from "../features/auth/authSlice";
import { authApi } from "../features/auth/authApi";
import {taskApi} from "@/features/task/taskApi.js";
import {themeApi} from "@/features/theme/themeApi.js";

export const store = configureStore({
  reducer: {
    language: languageReducer,
    theme: themeReducer,
    auth: authReducer,
    [authApi.reducerPath]: authApi.reducer,
    [taskApi.reducerPath]: taskApi.reducer,
    [themeApi.reducerPath]: themeApi.reducer,
  },
  middleware: (getDefaultMiddleware) =>
      getDefaultMiddleware().concat(authApi.middleware, taskApi.middleware, themeApi.middleware),
});

window.store = store;

export default store;
