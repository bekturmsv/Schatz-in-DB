import Navbar from "@/components/custom/Navbar";
import React from "react";
import { Outlet } from "react-router-dom";

const MainLayout = () => {
  return (
    <div className="min-h-screen flex flex-col">
      <header>{/* ваш хэдер */}</header>
      <main className="flex‑1">
        <Navbar />
        <Outlet />{" "}
      </main>
      <footer></footer>
    </div>
  );
};

export default MainLayout;
