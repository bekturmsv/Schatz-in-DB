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

  return (
      <nav className="fixed top-0 left-0 w-full bg-white/90 backdrop-blur-lg shadow-lg font-mono z-50 transition-all duration-300">
        <div className="container mx-auto flex justify-between items-center py-2 px-2 md:px-0">
          <div
              className="flex items-center text-black text-2xl font-extrabold uppercase space-x-2 select-none"
          >
            {/* SVG Logo */}
            <motion.span
                className="w-9 h-9 inline-block"
                initial={{ rotate: -12, scale: 0.8, opacity: 0 }}
                animate={{ rotate: 0, scale: 1, opacity: 1 }}
                transition={{ type: "spring", stiffness: 260, damping: 18, duration: 0.8 }}
            >
              <svg viewBox="0 0 40 40" fill="none" className="w-full h-full" xmlns="http://www.w3.org/2000/svg">
                <circle cx="18" cy="18" r="10" stroke="#1f2937" strokeWidth="2"/>
                <ellipse cx="18" cy="14" rx="4" ry="2" fill="#e5e7eb" stroke="#1f2937" strokeWidth="1"/>
                <path d="M13 25c1.5-3 8.5-3 11 0" stroke="#1f2937" strokeWidth="1.5" strokeLinecap="round"/>
                <path d="M28 28l6 6" stroke="#1f2937" strokeWidth="2" strokeLinecap="round"/>
                <circle cx="28" cy="28" r="4" fill="#fbbf24" stroke="#1f2937" strokeWidth="2"/>
                <ellipse cx="16.5" cy="16.5" rx="1.2" ry="1.3" fill="#1f2937"/>
                <ellipse cx="21.5" cy="16.5" rx="1.2" ry="1.3" fill="#1f2937"/>
                <path d="M17 10c.2-2.5 5.8-2.5 6 0" stroke="#1f2937" strokeWidth="1"/>
                <rect x="14" y="7" width="8" height="3" rx="1.5" fill="#fbbf24" stroke="#1f2937" strokeWidth="1"/>
              </svg>
            </motion.span>
            <span className="tracking-wider bg-gradient-to-r from-green-400 to-blue-500 bg-clip-text text-transparent">
            Query Crime
          </span>
          </div>
          {/* Burger Menu */}
          <button
              className="md:hidden text-black focus:outline-none transition-transform"
              onClick={toggleMenu}
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
                absolute md:static top-16 left-0 w-full md:w-auto bg-white/90 md:bg-transparent
                rounded-xl md:rounded-none p-4 md:p-0 shadow-lg md:shadow-none z-40
                transition-all duration-300`}
                >
                  <button
                      onClick={() => { navigate('/'); setIsOpen(false); }}
                      className="cursor-pointer text-black hover:text-green-500 font-semibold transition py-2 md:py-0 text-lg"
                  >
                    {t('home')}
                  </button>
                  {isLogged ? (
                      <>
                        <button
                            onClick={() => { navigate('/play'); setIsOpen(false); }}
                            className="cursor-pointer text-green-600 hover:text-blue-500 font-bold transition py-2 md:py-0 text-lg"
                        >
                          {t('myProgress')}
                        </button>
                        <button
                            onClick={() => { navigate('/training'); setIsOpen(false); }}
                            className="cursor-pointer text-black hover:text-green-500 font-semibold transition py-2 md:py-0 text-lg"
                        >
                          {t('training')}
                        </button>
                        <button
                            onClick={() => { navigate('/leaderboard'); setIsOpen(false); }}
                            className="cursor-pointer text-black hover:text-green-500 font-semibold transition py-2 md:py-0 text-lg"
                        >
                          {t('leaderboard')}
                        </button>
                        {user ? (
                            <span className="flex items-center gap-2 text-black py-2 md:py-0">
                      <Link
                          to={"/profile"}
                          className="cursor-pointer font-bold hover:text-green-500 transition"
                      >
                        {user.nickname}
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
                            className="cursor-pointer text-black hover:text-green-500 font-semibold transition py-2 md:py-0 text-lg"
                        >
                          {t('logout')}
                        </button>
                      </>
                  ) : (
                      <>
                        <button
                            onClick={() => { navigate('/login'); setIsOpen(false); }}
                            className="cursor-pointer text-black hover:text-green-500 font-semibold transition py-2 md:py-0 text-lg"
                        >
                          {t('signIn')}
                        </button>
                        <button
                            onClick={() => { navigate('/register'); setIsOpen(false); }}
                            className="cursor-pointer text-black hover:text-green-500 font-semibold transition py-2 md:py-0 text-lg"
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
