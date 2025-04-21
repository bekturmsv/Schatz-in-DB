import React from "react";
import { Outlet } from "react-router-dom";

const MainLayout = () => {
  return (
    <div className="min-h-screen flex flex-col">
      <header>{/* ваш хэдер */}</header>
      <main className="flex‑1">
        <Outlet />{" "}
      </main>
      <footer></footer>
    </div>
  );
};

export default MainLayout;
