import { Outlet } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import Navbar from "../components/custom/Navbar.jsx";
import Footer from "../components/custom/Footer.jsx";
import "../styles/theme.css";
import { useEffect, useRef } from "react";
import { initializeAuth } from "@/features/auth/authSlice.js";
import Loading from "@/components/custom/Loading.jsx";
import { useInactivityLogout } from "@/hooks/useInactivityLogout.js";
import { initTheme } from "@/features/theme/themeSlice.js";

export default function MainLayout() {
    const dispatch = useDispatch();
    const isAuthLoading = useSelector((state) => state.auth.isAuthLoading);
    const didInit = useRef(false);

    // Ставим тему из localStorage на <html> при старте приложения
    useEffect(() => {
        dispatch(initTheme());
    }, [dispatch]);

    useEffect(() => {
        if (!didInit.current) {
            dispatch(initializeAuth());
            didInit.current = true;
        }
    }, [dispatch]);

    useInactivityLogout();

    return (
        <div className="flex flex-col min-h-screen relative">
            <Navbar />
            <main className=" min-h-screen">
                <Outlet />
            </main>
            {isAuthLoading && <Loading />}
            <Footer />
        </div>
    );
}
