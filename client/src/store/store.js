import { configureStore } from "@reduxjs/toolkit";
import languageReducer from "../features/language/languageSlice";
import themeReducer from "../features/theme/themeSlice";
import authReducer from "../features/auth/authSlice";
import { authApi } from "../features/auth/authApi";
import {taskApi} from "@/features/task/taskApi.js";

export const store = configureStore({
  reducer: {
    language: languageReducer,
    theme: themeReducer,
    auth: authReducer,
    [authApi.reducerPath]: authApi.reducer,
    [taskApi.reducerPath]: taskApi.reducer,
  },
  middleware: (getDefaultMiddleware) =>
      getDefaultMiddleware().concat(authApi.middleware, taskApi.middleware),
});

window.store = store;

export default store;
