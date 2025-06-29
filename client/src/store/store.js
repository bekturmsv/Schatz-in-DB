import { configureStore } from "@reduxjs/toolkit";
import languageReducer from "../features/language/languageSlice";
import themeReducer from "../features/theme/themeSlice";
import authReducer from "../features/auth/authSlice";
import { authApi } from "../features/auth/authApi";
import {taskApi} from "@/features/task/taskApi.js";
import {themeApi} from "@/features/theme/themeApi.js";
import {ratingApi} from "@/features/rating/ratingApi.js";
import {materialApi} from "@/features/material/materialApi.js";
import {profileApi} from "@/features/profile/profileApi.js";
import {adminApi} from "@/features/admin/adminApi.js";
import {teacherApi} from "@/features/teacher/teacherApi.js";

export const store = configureStore({
  reducer: {
    language: languageReducer,
    theme: themeReducer,
    auth: authReducer,
    [authApi.reducerPath]: authApi.reducer,
    [taskApi.reducerPath]: taskApi.reducer,
    [themeApi.reducerPath]: themeApi.reducer,
    [ratingApi.reducerPath]: ratingApi.reducer,
    [materialApi.reducerPath]: materialApi.reducer,
    [profileApi.reducerPath]: profileApi.reducer,
    [adminApi.reducerPath]: adminApi.reducer,
    [teacherApi.reducerPath]: teacherApi.reducer,
  },
  middleware: (getDefaultMiddleware) =>
      getDefaultMiddleware().concat(authApi.middleware, taskApi.middleware, themeApi.middleware, ratingApi.middleware, materialApi.middleware, profileApi.middleware, adminApi.middleware, teacherApi.middleware),
});

window.store = store;

export default store;
