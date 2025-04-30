import { useNavigate } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import { useTranslation } from 'react-i18next';
import { logout } from '../../features/auth/authSlice';
import { resetUser } from '../../data/mockUser';
import LanguageSelector from './LanguageSelector';

export default function Navbar() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);
  const user = useSelector((state) => state.auth.user); // Используем user из Redux

  const handleLogout = () => {
    dispatch(logout());
    resetUser();
    navigate('/login');
  };

  return (
      <nav className="bg-gray-300 p-4 shadow-md font-mono">
        <div className="container mx-auto flex justify-between items-center">
          <div className="text-black text-2xl font-bold uppercase">
            {t('appName')}
          </div>
          <div className="flex space-x-4 items-center">
            <button
                onClick={() => navigate('/')}
                className="text-black hover:text-green-500 transition"
            >
              {t('home')}
            </button>
            {isAuthenticated && user && (
                <>
                  <button
                      onClick={() => navigate('/play')}
                      className="text-black hover:text-green-500 transition"
                  >
                    {t('myProgress')}
                  </button>
                  <button
                      onClick={() => navigate('/profile')}
                      className="text-black hover:text-green-500 transition"
                  >
                    {t('profile')}
                  </button>
                  <button
                      onClick={() => navigate('/training')}
                      className="text-black hover:text-green-500 transition"
                  >
                    {t('training')}
                  </button>
                  <button
                      onClick={() => navigate('/leaderboard')}
                      className="text-black hover:text-green-500 transition"
                  >
                    {t('leaderboard')}
                  </button>
                  <span className="text-black">
                {user.nickname} ({t('points')}: {user.points})
              </span>
                  <button
                      onClick={handleLogout}
                      className="text-black hover:text-green-500 transition"
                  >
                    {t('logout')}
                  </button>
                </>
            )}
            {!isAuthenticated && (
                <>
                  <button
                      onClick={() => navigate('/login')}
                      className="text-black hover:text-green-500 transition"
                  >
                    {t('signIn')}
                  </button>
                  <button
                      onClick={() => navigate('/register')}
                      className="text-black hover:text-green-500 transition"
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