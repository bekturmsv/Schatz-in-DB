import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { toast } from "sonner";
import { useLoginMutation } from "../../features/auth/authApi.js";
import { motion } from "framer-motion";

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

  // Анимация появления полей
  const fieldVariants = {
    hidden: { opacity: 0, y: 40 },
    visible: (i = 1) => ({
      opacity: 1,
      y: 0,
      transition: { delay: i * 0.15, duration: 0.7, ease: "easeOut" },
    }),
  };

  return (
      <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-green-100 via-cyan-100 to-blue-200 font-mono">
        {/* Блюр декоративный круг/волна */}
        <div className="absolute top-0 left-1/2 -translate-x-1/2 w-[60vw] h-[40vh] bg-gradient-to-tr from-green-200 via-green-300 to-blue-300 opacity-50 blur-2xl rounded-full pointer-events-none z-0"></div>

        <motion.div
            initial={{ opacity: 0, scale: 0.97, y: 32 }}
            animate={{ opacity: 1, scale: 1, y: 0 }}
            transition={{ duration: 0.7, ease: "easeOut" }}
            className="relative z-10 bg-white/90 shadow-2xl rounded-2xl px-8 py-12 w-full max-w-md flex flex-col items-center"
        >
          <motion.h1
              initial={{ opacity: 0, y: -24 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.1, duration: 0.7 }}
              className="text-4xl font-extrabold mb-8 bg-gradient-to-r from-green-500 via-blue-500 to-cyan-400 bg-clip-text text-transparent uppercase tracking-wider"
          >
            {t("signIn")}
          </motion.h1>
          {error && (
              <motion.p
                  initial={{ opacity: 0 }}
                  animate={{ opacity: 1 }}
                  className="text-red-500 text-center mb-4"
              >
                {t("loginFailed")}: {error?.data?.message || error?.message}
              </motion.p>
          )}
          <motion.div
              variants={fieldVariants}
              initial="hidden"
              animate="visible"
              custom={1}
              className="mb-5 w-full"
          >
            <label className="block text-black text-lg mb-2" htmlFor="username">
              {t("username")}
            </label>
            <input
                id="username"
                type="text"
                value={username}
                autoFocus
                onChange={(e) => setUsername(e.target.value)}
                className="w-full p-2 border-2 border-blue-100 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400 transition"
                placeholder={t("enterUsername")}
                disabled={isLoading}
            />
          </motion.div>
          <motion.div
              variants={fieldVariants}
              initial="hidden"
              animate="visible"
              custom={2}
              className="mb-7 w-full"
          >
            <label className="block text-black text-lg mb-2" htmlFor="password">
              {t("password")}
            </label>
            <input
                id="password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full p-2 border-2 border-blue-100 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400 transition"
                placeholder={t("enterPassword")}
                disabled={isLoading}
                onKeyDown={e => e.key === "Enter" && handleLogin()}
            />
          </motion.div>
          <motion.button
              whileHover={{ scale: 1.04, backgroundColor: "#22c55e", color: "#fff" }}
              whileTap={{ scale: 0.97 }}
              onClick={handleLogin}
              className="w-full bg-gradient-to-r from-green-500 to-cyan-400 text-white py-2 rounded-xl font-bold text-lg shadow-lg hover:from-green-400 hover:to-green-600 transition disabled:bg-gray-400"
              disabled={isLoading}
          >
            {isLoading ? t("loading") : t("signIn")}
          </motion.button>
          <motion.p
              initial={{ opacity: 0, y: 18 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.2, duration: 0.7 }}
              className="mt-7 text-center text-black"
          >
            {t("dontHaveAccount")}{" "}
            <span
                className="text-green-500 hover:underline cursor-pointer font-bold"
                onClick={() => navigate("/register")}
            >
            {t("signUp")}
          </span>
          </motion.p>
        </motion.div>
      </div>
  );
}
