import { Outlet } from "react-router-dom";
import { useSelector } from "react-redux";
import Navbar from "../components/custom/Navbar.jsx";
import Footer from "../components/custom/Footer.jsx";
import "../styles/theme.css";

export default function MainLayout() {


    return (
        <div className="flex flex-col min-h-screen" >
            <Navbar />
            <main className="flex-grow bg-transparent pt-16">
                <Outlet />
            </main>
            <Footer />
        </div>
    );
}