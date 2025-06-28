// src/pages/Profile/Profile.jsx
import { useSelector, useDispatch } from "react-redux";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { setTheme } from "../../features/theme/themeSlice.js";
import { Button } from "@/components/ui/button";
import { toast } from "sonner";
import { useEffect, useMemo, useState } from "react";
import { setUser, initializeAuth } from "@/features/auth/authSlice.js";
import {
  useGetThemesQuery,
  usePurchaseThemeMutation,
  useSetThemeMutation,
} from "@/features/theme/themeApi.js";
import { motion } from "framer-motion";
import { useGetMeQuery } from "@/features/auth/authApi";
import ProfileEditModal from "@/components/custom/ProfileEditModal.jsx";

export default function Profile() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const dispatch = useDispatch();

  // Redux
  const user = useSelector((state) => state.auth.user);
  const reduxTheme = useSelector((state) => state.theme.currentTheme);
  const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);

  const currentTheme = reduxTheme || "default";

  // API
  const { data: allThemes = [], refetch: refetchThemes } = useGetThemesQuery();
  const [purchaseTheme, { isLoading: isPurchasing }] = usePurchaseThemeMutation();
  const [setThemeApi, { isLoading: isSettingTheme }] = useSetThemeMutation();
  const [editOpen, setEditOpen] = useState(false);

  const { refetch: refetchMe } = useGetMeQuery(undefined, { skip: !isAuthenticated });

  useEffect(() => {
    if (!isAuthenticated || !user) {
      navigate("/login");
    }
  }, [isAuthenticated, user, navigate]);

  // Меняй тему сразу
  useEffect(() => {
    if (typeof window !== "undefined") {
      document.documentElement.setAttribute("data-theme", currentTheme);
      localStorage.setItem("theme", currentTheme);
    }
  }, [currentTheme]);

  if (!isAuthenticated || !user) return null;

  const purchasedThemeNames = useMemo(
      () =>
          Array.isArray(user.purchasedThemes)
              ? user.purchasedThemes.map((t) => t.name)
              : [],
      [user.purchasedThemes]
  );

  const purchasedThemeOptions = useMemo(
      () => allThemes.filter((t) => purchasedThemeNames.includes(t.name)),
      [allThemes, purchasedThemeNames]
  );

  const progressPercentage =
      user.progress && user.progress.totalTasks
          ? (user.progress.tasksSolved / user.progress.totalTasks) * 100
          : 0;

  const handlePurchaseTheme = async (theme) => {
    if (user.points < theme.cost) {
      toast.error(t("notEnoughPoints"));
      return;
    }
    if (user.purchasedThemes.some((t) => t.name === theme.name)) {
      toast.info(t("themeAlreadyPurchased"));
      return;
    }
    try {
      await purchaseTheme({ name: theme.name }).unwrap();
      await refetchMe();
      toast.success(t("themePurchased", { theme: theme.name }));
    } catch {
      toast.error(t("purchaseFailed"));
    }
  };

  const handleSelectTheme = async (themeName) => {
    if (!purchasedThemeNames.includes(themeName)) {
      toast.error(t("themeNotPurchased"));
      return;
    }
    try {
      dispatch(setTheme(themeName));
      if (typeof window !== "undefined") {
        document.documentElement.setAttribute("data-theme", themeName);
        localStorage.setItem("theme", themeName);
      }
      await setThemeApi({ name: themeName }).unwrap();
      toast.success(t("themeSelected", { theme: themeName }));
    } catch {
      toast.error(t("themeSelectError"));
    }
  };

  const lastTasks = Array.isArray(user.lastTasks) ? user.lastTasks : [];

  const fadeUp = {
    hidden: { opacity: 0, y: 40 },
    visible: (i = 1) => ({
      opacity: 1,
      y: 0,
      transition: { delay: i * 0.15, duration: 0.7, ease: "easeOut" },
    }),
  };

  return (
      <div
          className="min-h-screen font-mono pt-4"
          style={{
            background: "var(--background-gradient, var(--color-background))",
            backgroundColor: "var(--color-background)",
            fontFamily: "var(--font-ui)",
          }}
      >
        <motion.section
            initial="hidden"
            animate="visible"
            variants={fadeUp}
            className="container mx-auto py-12 px-4"
        >
          <motion.h1
              initial={{ opacity: 0, y: -24 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.12, duration: 0.8 }}
              className="custom-title mb-8"
          >
            {t("myAccount")}
          </motion.h1>
          <div className="flex flex-col md:flex-row gap-8">
            {/* Левая колонка */}
            <motion.div
                variants={fadeUp}
                custom={1}
                className="md:w-1/3 flex flex-col items-center"
            >
              <motion.div
                  initial={{ scale: 0.85, opacity: 0 }}
                  animate={{ scale: 1, opacity: 1 }}
                  transition={{ duration: 0.6, type: "spring" }}
                  className="w-28 h-28 flex items-center justify-center mb-4 custom-card"
              >
                <svg
                    className="w-14 h-14"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                    style={{ color: "var(--color-secondary, #b8b8b8)" }}
                >
                  <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth="2"
                      d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"
                  />
                </svg>
              </motion.div>
              <h2
                  className="text-xl font-bold custom-font"
                  style={{ color: "var(--color-primary)" }}
              >
                {(user.firstName || "")} {(user.lastName || "")}
              </h2>
              <p
                  className="mb-4 custom-font"
                  style={{ color: "var(--color-secondary)" }}
              >
                @{user.nickname || user.username}
              </p>
              <p
                  className="mb-2 custom-body"
                  style={{ color: "var(--color-primary)" }}
              >
                {t("group")}: {user.specialistGroup || t("notSpecified")}
              </p>
              <p
                  className="mb-4 custom-body"
                  style={{ color: "var(--color-primary)" }}
              >
                {t("matriculationNumber")}: {user.matriculationNumber || t("notSpecified")}
              </p>
              <Button className="custom-btn px-7 py-2 text-base"
                      onClick={() => setEditOpen(true)}
              >
                {t("editProfile")}
              </Button>
            </motion.div>

            {/* Правая колонка */}
            <motion.div
                variants={fadeUp}
                custom={2}
                className="md:w-2/3 flex flex-col gap-8"
            >
              {/* Очки */}
              <motion.div
                  initial={{ opacity: 0, scale: 0.9 }}
                  animate={{ opacity: 1, scale: 1 }}
                  transition={{ delay: 0.18, duration: 0.7 }}
                  className="flex items-center mb-2"
              >
                <svg
                    className="w-9 h-9 mr-3 drop-shadow"
                    fill="currentColor"
                    viewBox="0 0 20 20"
                    style={{ color: "var(--color-secondary)" }}
                >
                  <circle cx="10" cy="10" r="8" />
                  <circle cx="10" cy="10" r="5" fill="#fff" />
                </svg>
                <span
                    className="text-3xl font-extrabold custom-font"
                    style={{ color: "var(--color-secondary)" }}
                >
                {user.points} {t("points")}
              </span>
              </motion.div>

              {/* Прогресс */}
              <motion.div
                  variants={fadeUp}
                  custom={3}
                  className="custom-card p-7 mb-1"
              >
                <div className="flex items-center justify-between mb-3 custom-font">
                <span>
                  {t("yourProgress")}:{" "}
                  <span style={{ color: "var(--color-secondary)" }}>
                    {user.progress?.difficulty}
                  </span>
                </span>
                </div>
                <div className="mb-2 flex items-center gap-2 custom-body">
                <span className="font-semibold">
                  {progressPercentage.toFixed(0)}%
                </span>
                  <span
                      style={{ color: "var(--color-secondary)", opacity: 0.75 }}
                  >
                  {t("outOf")}
                </span>
                  <span className="font-bold">{user.progress?.totalTasks}</span>
                  <span
                      style={{ color: "var(--color-secondary)", opacity: 0.75 }}
                  >
                  {t("tasksSolved")}
                </span>
                </div>
                <motion.div
                    initial={{ width: 0 }}
                    animate={{ width: `${progressPercentage}%` }}
                    transition={{ duration: 0.8, ease: "easeInOut" }}
                    className="h-4 rounded-full"
                    style={{
                      background:
                          "var(--progress-gradient, linear-gradient(90deg, var(--color-secondary), var(--color-primary)))",
                      maxWidth: "100%",
                    }}
                />
                <div className="w-full bg-[var(--color-card-bg, #ececec)] rounded-full h-4 mt-[-16px] z-0"></div>
              </motion.div>

              {/* Блок последних решённых задач */}
              <motion.div variants={fadeUp} custom={5} className="mb-4">
                <h3
                    className="text-xl font-bold custom-font mb-4"
                    style={{ color: "var(--color-primary)" }}
                >
                  {t("lastCompletedTasks")}
                </h3>
                {lastTasks.length > 0 ? (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                      {lastTasks.map((task, idx) => (
                          <div
                              key={idx}
                              className="custom-card p-4 flex flex-col gap-1 shadow rounded-2xl"
                          >
                            <div
                                className="font-semibold text-base custom-font"
                                style={{ color: "var(--color-secondary)" }}
                            >
                              {t("theme")}:{" "}
                              <span className="font-bold">{task.theme}</span>
                            </div>
                            <div className="custom-body">
                              {t("type")}: <span>{task.type}</span>
                            </div>
                            <div className="custom-body">
                              {t("taskCode")}: <span>{task.taskCode}</span>
                            </div>
                          </div>
                      ))}
                    </div>
                ) : (
                    <div className="p-5 bg-[var(--color-card-bg,#ececec)] rounded-xl text-[var(--color-secondary)] text-center shadow">
                      {t("noCompletedTasks") || "Нет последних решённых задач"}
                    </div>
                )}
              </motion.div>

              {/* Темы/стили */}
              <h3
                  className="text-xl font-bold mb-2 mt-3 custom-font"
                  style={{ color: "var(--color-primary)" }}
              >
                {t("styles")}
              </h3>
              <div className="mb-6">
                <label
                    htmlFor="theme-select"
                    className="block mb-2 font-semibold custom-font"
                    style={{ color: "var(--color-primary)" }}
                >
                  {t("select")} {t("theme")}:
                </label>
                <select
                    id="theme-select"
                    value={currentTheme}
                    onChange={(e) => handleSelectTheme(e.target.value)}
                    className="w-full p-2 custom-input"
                    disabled={isSettingTheme}
                >
                  {purchasedThemeOptions.map((theme) => (
                      <option key={theme.name} value={theme.name}>
                        {theme.name}
                        {currentTheme === theme.name ? ` (${t("selected")})` : ""}
                      </option>
                  ))}
                </select>
              </div>
              <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                {allThemes.map((theme) => (
                    <motion.div
                        key={theme.name}
                        whileHover={{
                          scale: 1.07,
                          boxShadow: "0 8px 24px 0 rgba(34,197,94,0.12)",
                        }}
                        className={`relative h-24 custom-card flex items-center justify-center shadow-lg transition-all`}
                        style={{
                          background: purchasedThemeNames.includes(theme.name)
                              ? "var(--color-card-bg)"
                              : "var(--color-card-bg-alt, #ececec)",
                        }}
                    >
                      <div className="text-center w-full custom-card p-2">
                        <p
                            className="custom-font text-lg"
                            style={{ color: "var(--color-primary)" }}
                        >
                          {theme.name}
                        </p>
                        <p
                            className="mb-1 custom-body"
                            style={{ color: "var(--color-secondary)" }}
                        >
                          {theme.cost} {t("points")}
                        </p>
                        {purchasedThemeNames.includes(theme.name) ? (
                            <span
                                className="custom-font"
                                style={{ color: "var(--color-secondary)" }}
                            >
                        {t("purchased")}
                      </span>
                        ) : (
                            <Button
                                onClick={() => handlePurchaseTheme(theme)}
                                disabled={isPurchasing}
                                className="mt-1 custom-btn px-3 py-1"
                            >
                              {t("buy")}
                            </Button>
                        )}
                      </div>
                    </motion.div>
                ))}
              </div>
            </motion.div>
          </div>
        </motion.section>
        <ProfileEditModal
            user={user}
            open={editOpen}
            onClose={() => setEditOpen(false)}
        />
      </div>
  );
}
