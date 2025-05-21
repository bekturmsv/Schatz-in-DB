import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { toast } from "sonner";
import { useLoginMutation } from "../../features/auth/authApi.js";

export default function Login() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const [login, { isLoading, error }] = useLoginMutation();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async () => {
    if (!username || !password) {
      toast.error(t("usernameAndPasswordRequired"));
      return;
    }

    try {
      const result = await login({ username, password }).unwrap();
      toast.success(t("loginSuccessful"));
      navigate("/");
    } catch (err) {
      const errorMessage = err?.data?.message || err?.message || "Unknown error";
      toast.error(t("loginFailed") + ": " + errorMessage);
      console.error("Login error details:", err);
    }
  };

  return (
      <div className="min-h-screen flex flex-col items-center justify-center bg-green-200 font-mono">
        <h1 className="text-4xl font-bold mb-8 text-black uppercase">{t("signIn")}</h1>
        <div className="bg-gray-300 p-8 rounded-lg shadow-md w-full max-w-md">
          {error && (
              <p className="text-red-500 text-center mb-4">
                {t("loginFailed")}: {error?.data?.message || error?.message}
              </p>
          )}
          <div className="mb-4">
            <label className="block text-black text-lg mb-2" htmlFor="username">
              {t("username")}
            </label>
            <input
                id="username"
                type="text"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                className="w-full p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400"
                placeholder={t("enterUsername")}
                disabled={isLoading}
            />
          </div>
          <div className="mb-6">
            <label className="block text-black text-lg mb-2" htmlFor="password">
              {t("password")}
            </label>
            <input
                id="password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400"
                placeholder={t("enterPassword")}
                disabled={isLoading}
            />
          </div>
          <button
              onClick={handleLogin}
              className="w-full bg-green-500 text-white py-2 rounded-lg hover:bg-green-600 transition disabled:bg-gray-400"
              disabled={isLoading}
          >
            {isLoading ? t("loading") : t("signIn")}
          </button>
          <p className="mt-4 text-center text-black">
            {t("dontHaveAccount")}{" "}
            <span
                className="text-green-500 hover:underline cursor-pointer"
                onClick={() => navigate("/register")}
            >
            {t("signUp")}
          </span>
          </p>
        </div>
      </div>
  );
}