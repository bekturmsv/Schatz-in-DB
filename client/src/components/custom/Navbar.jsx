import {Link, useNavigate} from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import { useTranslation } from 'react-i18next';
import {logout, logoutUser} from '../../features/auth/authSlice';
import { resetUser } from '../../data/mockUser';
import LanguageSelector from './LanguageSelector';
import { useState } from 'react';

export default function Navbar() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);
  const user = useSelector((state) => state.auth.user);
  const [isOpen, setIsOpen] = useState(false); // Состояние для бургер-меню

  const handleLogout = () => {
    dispatch(logoutUser());
    resetUser();
    // navigate('/login');
    setIsOpen(false); // Закрываем меню при выходе
  };

  const toggleMenu = () => {
    setIsOpen(!isOpen);
  };

  return (
      <nav className=" fixed top-0 left-0 w-full bg-gray-300 p-4 shadow-md font-mono z-50">
        <div className="container mx-auto flex justify-between items-center">
          <div className="text-black text-2xl font-bold uppercase">
            {t('appName')}
          </div>

          {/* Кнопка бургер-меню (видна на мобильных) */}
          <button
              className="md:hidden text-black focus:outline-none"
              onClick={toggleMenu}
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
                  d={isOpen ? 'M6 18L18 6M6 6l12 12' : 'M4 6h16M4 12h16M4 18h16'}
              />
            </svg>
          </button>

          {/* Пункты меню */}
          <div
              className={`${
                  isOpen ? 'flex' : 'hidden'
              } md:flex custom-font  flex-col md:flex-row md:space-x-4 items-center absolute md:static top-16 left-0 w-full md:w-auto bg-gray-300 md:bg-transparent p-4 md:p-0 transition-all duration-300`}
          >
            <button
                onClick={() => {
                  navigate('/');
                  setIsOpen(false);
                }}
                className=" cursor-pointer text-black hover:text-green-500 transition py-2 md:py-0"
            >
              {t('home')}
            </button>
            {isAuthenticated && user && (
                <>
                  <button
                      onClick={() => {
                        navigate('/play');
                        setIsOpen(false);
                      }}
                      className="text-black text-green-500 hover:text-white transition py-2 md:py-0"
                  >
                    {t('myProgress')}
                  </button>
                  {/*<button*/}
                  {/*    onClick={() => {*/}
                  {/*      navigate('/profile');*/}
                  {/*      setIsOpen(false);*/}
                  {/*    }}*/}
                  {/*    className="cursor-pointer text-black hover:text-green-500 transition py-2 md:py-0"*/}
                  {/*>*/}
                  {/*  {t('profile')}*/}
                  {/*</button>*/}
                  <button
                      onClick={() => {
                        navigate('/training');
                        setIsOpen(false);
                      }}
                      className="cursor-pointer text-black hover:text-green-500 transition py-2 md:py-0"
                  >
                    {t('training')}
                  </button>
                  <button
                      onClick={() => {
                        navigate('/leaderboard');
                        setIsOpen(false);
                      }}
                      className="cursor-pointer text-black hover:text-green-500 transition py-2 md:py-0"
                  >
                    {t('leaderboard')}
                  </button>
                  <span className="text-black py-2 md:py-0">
                   <Link to={"/profile"} className="cursor-pointer hover:text-green-500"> {user.nickname} </Link>&nbsp;<b className="text-orange-500"> {user.points} {t('points')} </b>
              </span>
                  <button
                      onClick={handleLogout}
                      className="cursor-pointer text-black hover:text-green-500 transition py-2 md:py-0"
                  >
                    {t('logout')}
                  </button>
                </>
            )}
            {!isAuthenticated && (
                <>
                  <button
                      onClick={() => {
                        navigate('/login');
                        setIsOpen(false);
                      }}
                      className="cursor-pointer text-black hover:text-green-500 transition py-2 md:py-0"
                  >
                    {t('signIn')}
                  </button>
                  <button
                      onClick={() => {
                        navigate('/register');
                        setIsOpen(false);
                      }}
                      className="cursor-pointer text-black hover:text-green-500 transition py-2 md:py-0"
                  >
                    {t('signUp')}
                  </button>
                </>
            )}
            <LanguageSelector />
          </div>
        </div>
      </nav>
  );
}