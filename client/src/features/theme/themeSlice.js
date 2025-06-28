// src/features/theme/themeSlice.js
import { createSlice } from "@reduxjs/toolkit";

// Универсальный deepMerge (поверхностно рекурсивный для простых случаев)
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

const getInitialTheme = () => {
  if (typeof window !== "undefined") {
    try {
      const item = localStorage.getItem("theme");
      // поддержка и строки, и объекта в будущем
      return item && item.startsWith("{") ? JSON.parse(item) : item || "default";
    } catch {
      return "default";
    }
  }
  return "default";
};

const initialState = {
  currentTheme: getInitialTheme(),
};

const themeSlice = createSlice({
  name: "theme",
  initialState,
  reducers: {
    setTheme(state, action) {
      // deepMerge для совместимости, если payload — объект
      if (typeof action.payload === "object" && action.payload !== null) {
        state.currentTheme = deepMerge(state.currentTheme, action.payload);
      } else {
        state.currentTheme = action.payload;
      }
      if (typeof window !== "undefined") {
        document.documentElement.setAttribute(
            "data-theme",
            typeof action.payload === "string" ? action.payload : "default"
        );
        localStorage.setItem(
            "theme",
            typeof action.payload === "string"
                ? action.payload
                : JSON.stringify(action.payload)
        );
      }
    },
    initTheme(state) {
      const saved = getInitialTheme();
      if (typeof saved === "object" && saved !== null) {
        state.currentTheme = deepMerge(state.currentTheme, saved);
      } else {
        state.currentTheme = saved;
      }
      if (typeof window !== "undefined") {
        document.documentElement.setAttribute(
            "data-theme",
            typeof saved === "string" ? saved : "default"
        );
      }
    },
  },
});

export const { setTheme, initTheme } = themeSlice.actions;
export default themeSlice.reducer;
