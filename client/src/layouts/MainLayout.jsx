import Footer from "@/components/custom/Footer";
import Navbar from "@/components/custom/Navbar";
import React from "react";
import { Outlet } from "react-router-dom";

const MainLayout = () => {
  return (
    <div className="min-h-screen flex flex-col">
      <Navbar />

      <main className="flex‑1">
        <Outlet />
      </main>
      <Footer />
    </div>
  );
};

export default MainLayout;
