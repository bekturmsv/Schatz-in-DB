import axios from "axios";
import { store } from "../store/store.js";
import { logoutUser } from "../features/auth/authSlice";

const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
    headers: {
        "Content-Type": "application/json",
    },
});

api.interceptors.request.use(
    (config) => {
        if (config.url.includes("/api/auth/login") || config.url.includes("/api/auth/register")) {
            return config;
        }

        const token = store.getState().auth.token;
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            store.dispatch(logoutUser());
            console.error("Unauthorized, redirecting to login...");
            window.location.href = "/login";
        }
        return Promise.reject(error.response?.data?.message || "An error occurred");
    }
);

export default api;