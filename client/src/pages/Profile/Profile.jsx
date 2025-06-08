import { useSelector, useDispatch } from "react-redux";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { setTheme } from "../../features/theme/themeSlice.js";
import { Button } from "@/components/ui/button";
import { toast } from "sonner";
import { useState, useRef, useEffect } from "react";
import { setUser } from "@/features/auth/authSlice.js";
import { useGetThemesQuery, usePurchaseThemeMutation } from "@/features/theme/themeApi.js";
import { motion, AnimatePresence } from "framer-motion";

export default function Profile() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const user = useSelector((state) => state.auth.user);
  const currentTheme = useSelector((state) => state.theme.currentTheme);
  const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);
  const { data: allThemes = [], isLoading: isThemesLoading } = useGetThemesQuery();
  const [purchaseTheme, { isLoading: isPurchasing }] = usePurchaseThemeMutation();

  const [carouselPosition, setCarouselPosition] = useState(0);
  const carouselRef = useRef(null);

  useEffect(() => {
    if (!isAuthenticated || !user) {
      navigate("/login");
    }
  }, [isAuthenticated, user, navigate]);

  if (!isAuthenticated || !user) return null; // можно <Loader />

  const purchasedThemeNames = Array.isArray(user.purchasedThemes)
      ? user.purchasedThemes.map((t) => t.name)
      : [];
  const purchasedThemeOptions = allThemes.filter((t) =>
      purchasedThemeNames.includes(t.name)
  );

  const progressPercentage =
      (user.progress.tasksSolved / user.progress.totalTasks) * 100;

  const handlePurchaseTheme = (theme) => {
    if (user.points < theme.cost) {
      toast.error(t("notEnoughPoints"));
      return;
    }
    if (user.purchasedThemes.some((t) => t.name === theme.name)) {
      toast.info(t("themeAlreadyPurchased"));
      return;
    }
    const updatedPoints = user.points - theme.cost;
    const updatedPurchasedThemes = [
      ...user.purchasedThemes,
      { name: theme.name, cost: theme.cost },
    ];
    dispatch(
        setUser({
          ...user,
          points: updatedPoints,
          purchasedThemes: updatedPurchasedThemes,
        })
    );
    toast.success(t("themePurchased", { theme: theme.name }));
  };

  const handleSelectTheme = (themeName) => {
    if (!purchasedThemeNames.includes(themeName)) {
      toast.error(t("themeNotPurchased"));
      return;
    }
    dispatch(setTheme(themeName));
    toast.success(t("themeSelected", { theme: themeName }));
  };

  const scrollLeft = (e) => {
    e.preventDefault();
    const carousel = carouselRef.current;
    carousel.scrollLeft -= 320;
    setCarouselPosition(Math.max(carouselPosition - 1, 0));
  };

  const scrollRight = (e) => {
    e.preventDefault();
    const carousel = carouselRef.current;
    carousel.scrollLeft += 320;
    setCarouselPosition(Math.min(carouselPosition + 1, user.tasks.length - 3));
  };

  const completedTasks = user.tasks.filter((task) => task.completed > 0);
  const latestCompletedTasks =
      completedTasks.length > 0
          ? completedTasks
              .sort(
                  (a, b) => (b.lastCompleted || 0) - (a.lastCompleted || 0)
              )
              .slice(0, 3)
          : [];

  // Card and section fade variants
  const fadeUp = {
    hidden: { opacity: 0, y: 40 },
    visible: (i = 1) => ({
      opacity: 1,
      y: 0,
      transition: { delay: i * 0.15, duration: 0.7, ease: "easeOut" },
    }),
  };

  return (
      <div className="min-h-screen font-mono bg-gradient-to-br from-[#f8fafc] to-[#cbe7fa] pt-4">
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
              className="text-3xl md:text-4xl font-extrabold mb-8 bg-gradient-to-r from-green-500 via-blue-400 to-cyan-400 bg-clip-text text-transparent"
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
                  className="w-28 h-28 bg-gradient-to-tr from-gray-200 via-gray-100 to-blue-100 rounded-full flex items-center justify-center mb-4 shadow-lg"
              >
                <svg
                    className="w-14 h-14 text-gray-500"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                >
                  <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth="2"
                      d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"
                  />
                </svg>
              </motion.div>
              <h2 className="text-xl font-bold">{user.firstname} {user.lastname}</h2>
              <p className="text-gray-600 mb-4">@{user.nickname}</p>
              <p className="text-gray-600 mb-2">
                {t("group")}: {user.specialistGroup || t("notSpecified")}
              </p>
              <p className="text-gray-600 mb-4">
                {t("matriculationNumber")}: {user.matriculationNumber || t("notSpecified")}
              </p>
              <Button className="bg-gradient-to-r from-orange-500 to-yellow-400 text-white px-7 py-2 text-base rounded-xl shadow-md hover:scale-105 transition">
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
                    className="w-9 h-9 text-orange-400 mr-3 drop-shadow"
                    fill="currentColor"
                    viewBox="0 0 20 20"
                >
                  <circle cx="10" cy="10" r="8" />
                  <circle cx="10" cy="10" r="5" fill="#fff" />
                </svg>
                <span className="text-3xl font-extrabold text-orange-500">
                {user.points} {t("points")}
              </span>
              </motion.div>

              {/* Прогресс */}
              <motion.div
                  variants={fadeUp}
                  custom={3}
                  className="bg-white/80 backdrop-blur-lg p-7 rounded-xl mb-1 shadow-xl"
              >
                <div className="flex items-center justify-between mb-3">
                  <span className="font-semibold">{t("difficulty")}: <span className="font-bold">{user.progress.difficulty}</span></span>
                  <span className="font-semibold">{t("currentTopic")}: <span className="font-bold">{user.progress.topic}</span></span>
                </div>
                <div className="mb-2 flex items-center gap-2">
                  <span className="font-semibold">{progressPercentage.toFixed(0)}%</span>
                  <span className="text-gray-500">{t("outOf")}</span>
                  <span className="font-bold">{user.progress.totalTasks}</span>
                  <span className="text-gray-500">{t("tasksSolved")}</span>
                </div>
                {/* Анимированная прогресс-бар */}
                <motion.div
                    initial={{ width: 0 }}
                    animate={{ width: `${progressPercentage}%` }}
                    transition={{ duration: 0.8, ease: "easeInOut" }}
                    className="bg-gradient-to-r from-green-400 to-blue-400 h-4 rounded-full"
                    style={{ maxWidth: "100%" }}
                />
                <div className="w-full bg-gray-200 rounded-full h-4 mt-[-16px] z-0"></div>
              </motion.div>

              {/* Карусель задач */}
              <motion.div
                  variants={fadeUp}
                  custom={4}
                  className="relative mb-2"
              >
                <div className="flex items-center mb-3">
                  <h3 className="text-xl font-bold flex-1">{t("tasks")}</h3>
                  <Button
                      onClick={scrollLeft}
                      disabled={carouselPosition === 0}
                      className="mr-2 rounded-full bg-gray-200 hover:bg-green-200 text-xl px-2 py-1 transition disabled:opacity-60"
                  >
                    &#8592;
                  </Button>
                  <Button
                      onClick={scrollRight}
                      disabled={carouselPosition >= user.tasks.length - 3}
                      className="rounded-full bg-gray-200 hover:bg-green-200 text-xl px-2 py-1 transition disabled:opacity-60"
                  >
                    &#8594;
                  </Button>
                </div>
                <div
                    id="taskCarousel"
                    ref={carouselRef}
                    className="flex overflow-x-auto space-x-4 snap-x snap-mandatory touch-pan-x scrollbar-hide"
                    style={{
                      scrollBehavior: "smooth",
                      scrollbarWidth: "none",
                      msOverflowStyle: "none",
                    }}
                >
                  {/* Hide scrollbar for Webkit (Chrome, Safari) */}
                  <style>
                    {`
                    #taskCarousel::-webkit-scrollbar {
                      display: none;
                    }
                  `}
                  </style>
                  {user.tasks.map((task, index) => (
                      <motion.div
                          key={index}
                          initial={{ opacity: 0, scale: 0.95 }}
                          animate={{ opacity: 1, scale: 1 }}
                          transition={{ delay: index * 0.06 }}
                          className="min-w-[300px] h-40 rounded-xl bg-gradient-to-tr from-gray-100 to-blue-100 shadow flex-shrink-0 snap-start p-4 flex flex-col justify-between"
                      >
                        <div className="text-green-400 font-bold text-lg truncate">
                          {t("type")}: {task.type}
                        </div>
                        <div className="truncate">
                          {t("theme")}: <span className="font-semibold">{task.theme}</span>
                        </div>
                        <div className="truncate text-gray-700">
                          {task.completed || 0}/{task.total || 0}
                        </div>
                      </motion.div>
                  ))}
                </div>
              </motion.div>



              {/* Темы/стили */}
              <h3 className="text-xl font-bold mb-2 mt-3">{t("styles")}</h3>
              <div className="mb-6">
                <label htmlFor="theme-select" className="block mb-2 font-semibold">
                  {t("select")} {t("theme")}:
                </label>
                <select
                    id="theme-select"
                    value={currentTheme}
                    onChange={(e) => handleSelectTheme(e.target.value)}
                    className="w-full p-2 border-2 border-blue-200 rounded-xl bg-white/80 shadow focus:outline-green-300"
                >
                  {purchasedThemeOptions.map((theme) => (
                      <option key={theme.name} value={theme.name}>
                        {theme.name} ({theme.cost} {t("points")}) {currentTheme === theme.name ? `(${t("selected")})` : ""}
                      </option>
                  ))}
                </select>
              </div>
              <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                {allThemes.map((theme) => (
                    <motion.div
                        key={theme.name}
                        whileHover={{ scale: 1.07, boxShadow: "0 8px 24px 0 rgba(34,197,94,0.12)" }}
                        className={`relative h-24 rounded-xl bg-gradient-to-br ${
                            purchasedThemeNames.includes(theme.name)
                                ? "from-green-100 to-blue-100"
                                : "from-gray-100 to-gray-200"
                        } flex items-center justify-center shadow-lg transition-all`}
                    >
                      <div className="text-center w-full">
                        <p className="font-bold text-lg">{theme.name}</p>
                        <p className="mb-1">{theme.cost} {t("points")}</p>
                        {purchasedThemeNames.includes(theme.name) ? (
                            <span className="text-green-600 font-semibold">{t("purchased")}</span>
                        ) : (
                            <Button
                                onClick={() => handlePurchaseTheme(theme)}
                                disabled={isPurchasing}
                                className="mt-1 bg-gradient-to-r from-orange-400 to-orange-600 text-white px-3 py-1 rounded-lg hover:scale-105 transition"
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
      </div>
  );
}
