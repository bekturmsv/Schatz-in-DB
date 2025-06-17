import { Link, useNavigate } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import { useTranslation } from 'react-i18next';
import { logoutUser } from '../../features/auth/authSlice';
import LanguageSelector from './LanguageSelector';
import { useState } from 'react';
import { motion, AnimatePresence } from "framer-motion";

export default function Navbar() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const user = useSelector((state) => state.auth.user);
  const token = useSelector((state) => state.auth.token);
  const [isOpen, setIsOpen] = useState(false);

  const handleLogout = () => {
    dispatch(logoutUser());
    navigate('/login');
    setIsOpen(false);
  };

  const toggleMenu = () => setIsOpen(!isOpen);

  const isLogged = Boolean(token);

  console.log(user)

  return (
      <nav
          className="fixed top-0 left-0 w-full z-50 custom-card font-mono transition-all duration-300"
          style={{
            background: "var(--card-bg)",
            boxShadow: "var(--card-shadow)",
            borderRadius: "0 0 var(--card-radius) var(--card-radius)",
            backdropFilter: "blur(16px)",
          }}
      >
        <div className="container mx-auto flex justify-between items-center py-2 px-2 md:px-0">
          {/* Logo */}
          <div
              className="flex items-baseline text-2xl font-extrabold  space-x-2 select-none custom-title"
              style={{ fontFamily: "var(--font-ui)" }}
          >
            <motion.span
                className="w-9 h-9 "
                initial={{ rotate: -12, scale: 0.8, opacity: 0 }}
                animate={{ rotate: 0, scale: 1, opacity: 1 }}
                transition={{ type: "spring", stiffness: 260, damping: 18, duration: 0.8 }}
            >
              <svg viewBox="0 0 40 40" fill="none" className="w-full h-full" xmlns="http://www.w3.org/2000/svg">
                {/* База данных */}
                <ellipse cx="18" cy="16" rx="7" ry="4.5" fill="var(--color-card-bg)" stroke="var(--color-primary)" strokeWidth="2"/>
                <rect x="11" y="16" width="14" height="11" rx="7" fill="var(--color-card-bg)" stroke="var(--color-primary)" strokeWidth="2"/>
                <ellipse cx="18" cy="27" rx="7" ry="2.7" fill="var(--color-card-bg)" stroke="var(--color-primary)" strokeWidth="1.3"/>

                {/* Лупа */}
                <circle cx="28" cy="28" r="6" stroke="var(--color-secondary)" strokeWidth="2.1" fill="none"/>
                <rect x="32.3" y="32.2" width="6.2" height="2.1" rx="1" transform="rotate(45 32.3 32.2)" fill="var(--color-secondary)" />

                {/* Молния */}
                <polyline points="18,18 21,22 17,22 20,26" stroke="var(--color-secondary)" strokeWidth="2" fill="none"/>

                {/* Небольшая тень */}
                <ellipse cx="18" cy="30.6" rx="7" ry="1" fill="#000" opacity="0.08"/>
              </svg>
            </motion.span>
            <span className="logo-title uppercase">Query Crime</span>
          </div>
          {/* Burger */}
          <button
              className="md:hidden text-black focus:outline-none transition-transform"
              onClick={toggleMenu}
              style={{
                color: "var(--color-primary)",
              }}
          >
            <svg className="w-8 h-8" fill="none" stroke="currentColor"
                 viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <motion.path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth="2"
                  d={isOpen ? 'M6 18L18 6M6 6l12 12' : 'M4 6h16M4 12h16M4 18h16'}
                  initial={false}
                  animate={{ pathLength: isOpen ? 1 : 1 }}
                  transition={{ duration: 0.3 }}
              />
            </svg>
          </button>
          {/* Desktop + Mobile Menu */}
          <AnimatePresence>
            {(isOpen || window.innerWidth >= 768) && (
                <motion.div
                    key="menu"
                    initial={{ opacity: 0, y: -20 }}
                    animate={{ opacity: 1, y: 0 }}
                    exit={{ opacity: 0, y: -40 }}
                    transition={{ duration: 0.35 }}
                    className={`
                ${isOpen ? 'flex' : 'hidden'}
                md:flex custom-font flex-col md:flex-row md:space-x-4 items-center
                absolute md:static top-16 left-0 w-full md:w-auto
                bg-[var(--card-bg)] md:bg-transparent
                rounded-xl md:rounded-none p-4 md:p-0
                shadow-lg md:shadow-none z-40
                transition-all duration-300
              `}
                    style={{
                      boxShadow: isOpen ? "var(--card-shadow)" : "none",
                      borderRadius: isOpen ? "var(--card-radius)" : 0,
                      color: "var(--color-primary)",
                      background: isOpen ? "var(--card-bg)" : "none",
                      fontFamily: "var(--font-ui)",
                    }}
                >
                  <button
                      onClick={() => { navigate('/'); setIsOpen(false); }}
                      className="cursor-pointer hover:text-green-500 font-semibold transition py-2 md:py-0 text-lg"
                      style={{ color: "var(--color-primary)" }}
                  >
                    {t('home')}
                  </button>
                  {isLogged ? (
                      <>
                        <button
                            onClick={() => { navigate('/play'); setIsOpen(false); }}
                            className="cursor-pointer font-bold transition py-2 md:py-0 text-lg"
                            style={{ color: "var(--color-secondary)" }}
                        >
                          {t('Progress')}
                        </button>
                        <button
                            onClick={() => { navigate('/materials'); setIsOpen(false); }}
                            className="cursor-pointer hover:text-green-500 font-semibold transition py-2 md:py-0 text-lg"
                            style={{ color: "var(--color-primary)" }}
                        >
                          {t('training')}
                        </button>
                        <button
                            onClick={() => { navigate('/leaderboard'); setIsOpen(false); }}
                            className="cursor-pointer hover:text-green-500 font-semibold transition py-2 md:py-0 text-lg"
                            style={{ color: "var(--color-primary)" }}
                        >
                          {t('leaderboard')}
                        </button>
                        {user ? (
                            <span className="flex items-center gap-2 text-black py-2 md:py-0">
                      <Link
                          to={"/profile"}
                          className="cursor-pointer font-bold hover:text-green-500 transition"
                          style={{ color: "var(--color-primary)" }}
                      >
                        {user.username}
                      </Link>
                      <b className="text-orange-500">
                        {user.points} {t("points")}
                      </b>
                    </span>
                        ) : (
                            <span className="text-black py-2 md:py-0">{t('loadingProfile') || "Loading..."}</span>
                        )}
                        <button
                            onClick={handleLogout}
                            className="cursor-pointer hover:text-green-500 font-semibold transition py-2 md:py-0 text-lg"
                            style={{ color: "var(--color-primary)" }}
                        >
                          {t('logout')}
                        </button>
                      </>
                  ) : (
                      <>
                        <button
                            onClick={() => { navigate('/login'); setIsOpen(false); }}
                            className="cursor-pointer hover:text-green-500 font-semibold transition py-2 md:py-0 text-lg"
                            style={{ color: "var(--color-primary)" }}
                        >
                          {t('signIn')}
                        </button>
                        <button
                            onClick={() => { navigate('/register'); setIsOpen(false); }}
                            className="cursor-pointer hover:text-green-500 font-semibold transition py-2 md:py-0 text-lg"
                            style={{ color: "var(--color-primary)" }}
                        >
                          {t('signUp')}
                        </button>
                      </>
                  )}
                  <div className="ml-0 md:ml-4">
                    <LanguageSelector />
                  </div>
                </motion.div>
            )}
          </AnimatePresence>
        </div>
      </nav>
  );
}
