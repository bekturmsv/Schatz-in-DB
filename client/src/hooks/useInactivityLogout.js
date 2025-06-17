import { useSelector, useDispatch } from "react-redux";
import { useEffect } from "react";
import { logout } from "../features/auth/authSlice.js";

export function useInactivityLogout() {
    const token = useSelector((state) => state.auth.token);
    const dispatch = useDispatch();

    useEffect(() => {
        if (!token) return;

        let timeout = setTimeout(() => {
            dispatch(logout());
            localStorage.removeItem("authToken");
        }, 900000); // 15 мин

        const reset = () => {
            clearTimeout(timeout);
            timeout = setTimeout(() => {
                dispatch(logout());
                localStorage.removeItem("authToken");
            }, 900000);
        };

        window.addEventListener("click", reset);
        window.addEventListener("keydown", reset);

        return () => {
            clearTimeout(timeout);
            window.removeEventListener("click", reset);
            window.removeEventListener("keydown", reset);
        };
    }, [token, dispatch]);
}
