import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { useTranslation } from "react-i18next";
import { logout } from "@/features/auth/authSlice";

export default function Navbar() {
  const { t } = useTranslation();
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);
  const [isOpen, setIsOpen] = useState(false);

  const handleLogout = () => {
    dispatch(logout());
    navigate("/login");
  };

  const navLinks = [
    { to: "/", label: t("home") },
    { to: "/play", label: t("play") },
    { to: "/training", label: t("training") },
    { to: "/leaderboard", label: t("leaderboard") },
  ];

  if (isAuthenticated) {
    navLinks.push({ to: "/profile", label: t("profile") });
  }

  return (
    <nav className="bg-gray-300 border-b-2 border-green-400 font-mono text-green-400">
      <div className="container mx-auto px-4 flex items-center justify-between h-16">
        <Link to="/" className="text-2xl font-bold">
          LOGO
        </Link>

        <button
          className="md:hidden focus:outline-none"
          onClick={() => setIsOpen(!isOpen)}
        >
          <svg
            className="w-6 h-6"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2"
              d={isOpen ? "M6 18L18 6M6 6l12 12" : "M4 6h16M4 12h16M4 18h16"}
            />
          </svg>
        </button>

        <div
          className={`${
            isOpen ? "flex" : "hidden"
          } md:flex flex-col md:flex-row md:space-x-6 absolute md:static top-16 left-0 w-full md:w-auto bg-gray-300 md:bg-transparent p-4 md:p-0 space-y-4 md:space-y-0 z-10`}
        >
          {navLinks.map((link) => (
            <Link
              key={link.to}
              to={link.to}
              className="hover:underline text-lg uppercase"
              onClick={() => setIsOpen(false)}
            >
              {link.label}
            </Link>
          ))}
          {isAuthenticated ? (
            <button
              onClick={handleLogout}
              className="text-lg uppercase hover:underline md:ml-4"
            >
              {t("logout")}
            </button>
          ) : (
            <Link
              to="/login"
              className="text-lg uppercase hover:underline md:ml-4"
              onClick={() => setIsOpen(false)}
            >
              {t("signIn")}
            </Link>
          )}
        </div>
      </div>
    </nav>
  );
}
