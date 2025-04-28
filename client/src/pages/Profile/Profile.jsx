import { useSelector } from "react-redux";
import { useTranslation } from "react-i18next";
import { mockUser } from "../../data/mockUser";
import { Button } from "@/components/ui/button";

export default function Profile() {
  const { t } = useTranslation();
  const user = useSelector((state) => state.auth.user);

  //
  const progressPercentage =
    (mockUser.progress.tasksSolved / mockUser.progress.totalTasks) * 100;

  return (
    <div className="min-h-screen bg-white font-mono">
      <section className="container mx-auto py-12 px-4">
        <h1 className="text-3xl md:text-4xl font-bold mb-8">
          {t("myAccount")}
        </h1>
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
                {t("difficulty")}: {mockUser.progress.difficulty}
              </p>
              <p className="mb-2">
                {t("currentTopic")}: {mockUser.progress.topic}
              </p>
              <p className="mb-2">
                {progressPercentage.toFixed(0)}% {t("outOf")}{" "}
                {mockUser.progress.totalTasks} {t("tasksSolved")}
              </p>
              <div className="w-full bg-gray-200 rounded-full h-4">
                <div
                  className="bg-green-400 h-4 rounded-full"
                  style={{ width: `${progressPercentage}%` }}
                />
              </div>
            </div>

            {/* Задачи */}
            <div className="flex gap-4 mb-6">
              {mockUser.tasks.map((task, index) => (
                <div
                  key={index}
                  className="relative w-1/2 h-40 rounded-lg bg-cover bg-center"
                  style={{
                    backgroundImage: `url('/assets/images/theme1/task-bg.jpg')`,
                  }}
                >
                  <div className="absolute top-2 left-2 text-green-400">
                    <p>
                      {t("type")}: {task.type}
                    </p>
                    <p>
                      {t("theme")}: {task.theme}
                    </p>
                    <p>
                      {task.completed}/{task.total}
                    </p>
                  </div>
                </div>
              ))}
            </div>

            {/* Стили */}
            <h3 className="text-xl font-bold mb-4">{t("styles")}</h3>
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
              {mockUser.themes.map((theme, index) => (
                <div
                  key={index}
                  className="relative h-24 rounded-lg bg-cover bg-center"
                  style={{
                    backgroundImage: `url('/assets/images/themes/${theme.image}')`,
                  }}
                >
                  <button className="absolute bottom-2 left-2 bg-gray-300 text-white px-4 py-1 rounded-lg">
                    {theme.price}
                  </button>
                </div>
              ))}
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
