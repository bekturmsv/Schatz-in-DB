import { useSelector, useDispatch } from "react-redux";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { availableThemes } from "../../data/mockUser";
import { setTheme } from "../../features/theme/themeSlice.js";
import { Button } from "@/components/ui/button";
import { toast } from "sonner";
import {useState, useRef, useEffect} from "react";
import { setUser } from "@/features/auth/authSlice.js";
import {useGetThemesQuery, usePurchaseThemeMutation} from "@/features/theme/themeApi.js";

export default function Profile() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const user = useSelector((state) => state.auth.user);
  const currentTheme = useSelector((state) => state.theme.currentTheme);
  const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);
  const {data: allThemes = [], isLoading: isThemesLoading, isError:isThemesError} = useGetThemesQuery()
  const [purchaseTheme, { isLoading: isPurchasing }] = usePurchaseThemeMutation();

  const [carouselPosition, setCarouselPosition] = useState(0);
  const carouselRef = useRef(null);

  console.log("User from Redux:", user);

  useEffect(() => {
    if (!isAuthenticated || !user) {
      navigate("/login");
    }
  }, [isAuthenticated, user, navigate])

  if (!isAuthenticated || !user) {
    return null; // можно <Loader />
  }

  const purchasedThemeNames = Array.isArray(user.purchasedThemes)
      ? user.purchasedThemes.map(t => t.name)
      : [];
  const purchasedThemeOptions = allThemes.filter(t => purchasedThemeNames.includes(t.name));



  const progressPercentage =
      (user.progress.tasksSolved / user.progress.totalTasks) * 100;

  const handlePurchaseTheme = (theme) => {
    if (user.points < theme.cost) {
      toast.error(t("notEnoughPoints"));
      return;
    }
    if (user.purchasedThemes.some(t => t.name === theme.name)) {
      toast.info(t("themeAlreadyPurchased"));
      return;
    }
    const updatedPoints = user.points - theme.cost;
    const updatedPurchasedThemes = [...user.purchasedThemes, { name: theme.name, cost: theme.cost }];
    dispatch(setUser({ ...user, points: updatedPoints, purchasedThemes: updatedPurchasedThemes }));
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
    carousel.scrollLeft -= 300;
    setCarouselPosition(Math.max(carouselPosition - 1, 0));
  };

  const scrollRight = (e) => {
    e.preventDefault();
    const carousel = carouselRef.current;
    carousel.scrollLeft += 300;
    setCarouselPosition(Math.min(carouselPosition + 1, user.tasks.length - 3));
  };

  const completedTasks = user.tasks.filter((task) => {
    console.log("Task:", task);
    return task.completed > 0;
  });
  console.log("Completed Tasks:", completedTasks);
  const latestCompletedTasks = completedTasks.length > 0
      ? completedTasks.sort((a, b) => (b.lastCompleted || 0) - (a.lastCompleted || 0)).slice(0, 3)
      : [];





  return (
      <div className="min-h-screen font-mono">
        <section className="container mx-auto py-12 px-4">
          <h1 className="text-3xl md:text-4xl font-bold mb-8">{t("myAccount")}</h1>
          <div className="flex flex-col md:flex-row gap-8">
            {/* Левая колонка */}
            <div className="md:w-1/3 flex flex-col items-center">
              <div className="w-24 h-24 bg-gray-200 rounded-full flex items-center justify-center mb-4">
                <svg
                    className="w-12 h-12 text-gray-500"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                    xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth="2"
                      d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"
                  />
                </svg>
              </div>
              <h2 className="text-xl font-bold">
                {user.firstname} {user.lastname}
              </h2>

              <p className="text-gray-600 mb-4">{user.nickname}</p>
              <p className="text-gray-600 mb-2">Group: {user.specialistGroup || "Not specified"}</p>
              <p className="text-gray-600 mb-4">Matrikelnummer: {user.matriculationNumber || "Not specified"}</p>
              <Button className="bg-orange-500 text-white px-6 py-2 rounded-lg hover:bg-orange-600 transition">
                {t("editProfile")}
              </Button>
            </div>

            {/* Правая колонка */}
            <div className="md:w-2/3">
              {/* Очки */}
              <div className="flex items-center mb-6">
                <svg
                    className="w-8 h-8 text-orange-500 mr-2"
                    fill="currentColor"
                    viewBox="0 0 20 20"
                    xmlns="http://www.w3.org/2000/svg"
                >
                  <path d="M10 2a8 8 0 100 16 8 8 0 000-16zm0 14a6 6 0 110-12 6 6 0 010 12z" />
                </svg>
                <span className="text-2xl font-bold text-orange-500">
                {user.points} {t("points")}
              </span>
              </div>

              {/* Прогресс */}
              <div className="bg-gray-300 p-6 rounded-lg mb-6">
                <p className="mb-2">
                  {t("difficulty")}: {user.progress.difficulty}
                </p>
                <p className="mb-2">
                  {t("currentTopic")}: {user.progress.topic}
                </p>
                <p className="mb-2">
                  {progressPercentage.toFixed(0)}% {t("outOf")}{" "}
                  {user.progress.totalTasks} {t("tasksSolved")}
                </p>
                <div className="w-full bg-gray-200 rounded-full h-4">
                  <div
                      className="bg-green-400 h-4 rounded-full"
                      style={{ width: `${progressPercentage}%` }}
                  />
                </div>
              </div>

              {/* Карусель задач */}
              <div className="relative mb-6">
                <h3 className="text-xl font-bold mb-4">{t("tasks")}</h3>
                <button
                    onClick={scrollLeft}
                    disabled={carouselPosition === 0}
                    className={`absolute left-0 top-1/2 transform -translate-y-1/2 bg-gray-300 p-2 rounded-full z-10 hover:bg-gray-400 disabled:bg-gray-200 disabled:cursor-not-allowed`}
                >
                  ←
                </button>
                <button
                    onClick={scrollRight}
                    disabled={carouselPosition >= user.tasks.length - 3}
                    className={`absolute right-0 top-1/2 transform -translate-y-1/2 bg-gray-300 p-2 rounded-full z-10 hover:bg-gray-400 disabled:bg-gray-200 disabled:cursor-not-allowed`}
                >
                  →
                </button>
                <div
                    id="taskCarousel"
                    ref={carouselRef}
                    className="flex overflow-x-auto space-x-4 snap-x snap-mandatory touch-pan-x"
                    style={{
                      scrollBehavior: "smooth",
                      scrollbarWidth: "none", /* Firefox */
                      msOverflowStyle: "none", /* IE and Edge */
                    }}
                >
                  {/* Скрываем скроллбар для Webkit (Chrome, Safari) */}
                  <style>
                    {`
                    #taskCarousel::-webkit-scrollbar {
                      display: none;
                    }
                  `}
                  </style>
                  {user.tasks.map((task, index) => (
                      <div
                          key={index}
                          className="min-w-[300px] h-40 rounded-lg bg-gray-200 flex-shrink-0 snap-start p-4"
                      >
                        <div className="text-green-400">
                          <p className="truncate">
                            {t("type")}: {task.type}
                          </p>
                          <p className="truncate">
                            {t("theme")}: {task.theme}
                          </p>
                          <p className="truncate">
                            {task.completed || 0}/{task.total || 0}
                          </p>
                        </div>
                      </div>
                  ))}
                </div>
              </div>

              {/* Последние решенные задачи */}
              <div className="bg-gray-300 p-6 rounded-lg">
                <h3 className="text-xl font-bold mb-4">{t("latestCompletedTasks")}</h3>
                {latestCompletedTasks.length > 0 ? (
                    <div className="flex flex-col space-y-4">
                      {latestCompletedTasks.map((task, index) => (
                          <div key={index} className="p-4 bg-white rounded-lg shadow">
                            <p className="truncate">
                              {t("type")}: {task.type}
                            </p>
                            <p className="truncate">
                              {t("theme")}: {task.theme}
                            </p>
                            <p className="truncate">
                              {t("completed")}: {task.completed}/{task.total}
                            </p>
                            <p className="truncate">
                              {t("lastCompleted")}:{" "}
                              {task.lastCompleted
                                  ? new Date(task.lastCompleted).toLocaleDateString("ru-RU", {
                                    day: "2-digit",
                                    month: "2-digit",
                                    year: "numeric",
                                  }) +
                                  " " +
                                  new Date(task.lastCompleted).toLocaleTimeString("ru-RU", {
                                    hour: "2-digit",
                                    minute: "2-digit",
                                  })
                                  : "N/A"}
                            </p>
                          </div>
                      ))}
                    </div>
                ) : (
                    <div className="text-gray-600">
                      <p>{t("noCompletedTasks")}</p>
                      <button
                          onClick={() => navigate("/play")}
                          className="mt-2 text-green-500 hover:underline"
                      >
                        {t("solveFirstTask")}
                      </button>
                    </div>
                )}
              </div>

              {/* Темы */}
              <h3 className="text-xl font-bold mb-4 mt-6">{t("styles")}</h3>
              <div className="mb-6">
                <label htmlFor="theme-select" className="block mb-2">
                  {t("select")} {t("theme")}:
                </label>
                <select
                    id="taskCarousel"
                    value={currentTheme}
                    onChange={(e) => handleSelectTheme(e.target.value)}
                    className="w-full p-2 border rounded"
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
                    <div
                        key={theme.name}
                        className="relative h-24 rounded-lg bg-gray-200 flex items-center justify-center"
                    >
                      <div className="text-center">
                        <p className="font-bold">{theme.name}</p>
                        <p>{theme.cost} {t("points")}</p>
                        {purchasedThemeNames.includes(theme.name) ? (
                            <span className="text-green-500">{t("purchased")}</span>
                        ) : (
                            <button
                                onClick={() => handlePurchaseTheme(theme)}
                                className="mt-2 bg-orange-500 text-white px-4 py-1 rounded-lg hover:bg-orange-600 transition"
                            >
                              {t("buy")}
                            </button>
                        )}
                      </div>
                    </div>
                ))}
              </div>
            </div>
          </div>
        </section>
      </div>
  );
}