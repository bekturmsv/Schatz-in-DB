import { Outlet } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import Navbar from "../components/custom/Navbar.jsx";
import Footer from "../components/custom/Footer.jsx";
import "../styles/theme.css";
import { useEffect } from "react";
import { initializeAuth } from "@/features/auth/authSlice.js";
import Loading from "@/components/custom/Loading.jsx";

export default function MainLayout() {
    const dispatch = useDispatch();
    const isAuthLoading = useSelector((state) => state.auth.isAuthLoading);
    const token = useSelector((state) => state.auth.token);

    useEffect(() => {
        dispatch(initializeAuth());
    }, [token,dispatch]);

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