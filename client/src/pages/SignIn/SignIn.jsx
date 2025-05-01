import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { useTranslation } from 'react-i18next';
import { toast } from 'sonner';
import { setUser, setToken } from '../../features/auth/authSlice';
import { getUser } from '../../data/mockUser';

export default function SignIn() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = () => {
    const user = getUser();
    if (user.email === email && user.password === password) {
      dispatch(setUser(user));
      dispatch(setToken('mock-token'));
      toast.success(t('loginSuccess'));
      navigate('/');
    } else {
      toast.error(t('loginFailed'));
    }
  };

  return (
      <div className="min-h-screen flex flex-col items-center justify-center bg-green-200 font-mono">
        <h1 className="text-4xl font-bold mb-8 text-black uppercase">{t('signIn')}</h1>
        <div className="bg-gray-300 p-8 rounded-lg shadow-md w-full max-w-md">
          <div className="mb-4">
            <label className="block text-black text-lg mb-2" htmlFor="email">
              {t('email')}
            </label>
            <input
                id="email"
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="w-full p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400"
                placeholder={t('enterEmail')}
            />
          </div>
          <div className="mb-6">
            <label className="block text-black text-lg mb-2" htmlFor="password">
              {t('password')}
            </label>
            <input
                id="password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400"
                placeholder={t('enterPassword')}
            />
          </div>
          <button
              onClick={handleLogin}
              className="w-full bg-green-500 text-white py-2 rounded-lg hover:bg-green-600 transition"
          >
            {t('signIn')}
          </button>
          <p className="mt-4 text-center text-black">
            {t('dontHaveAccount')}{' '}
            <span
                className="text-green-500 hover:underline cursor-pointer"
                onClick={() => navigate('/register')}
            >
            {t('signUp')}
          </span>
          </p>
        </div>
      </div>
  );
}