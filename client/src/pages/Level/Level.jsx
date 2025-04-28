import { useParams } from "react-router-dom";
import { useTranslation } from "react-i18next";

// Моковые данные для уровней сложности
const levelData = {
  easy: {
    title: "easyLevel",
    description: "This is the easy level with basic SQL tasks.",
    tasks: [
      { id: 1, name: "SELECT Basics", completed: false },
      { id: 2, name: "Simple JOIN", completed: false },
    ],
  },
  medium: {
    title: "mediumLevel",
    description: "This is the medium level with intermediate SQL tasks.",
    tasks: [
      { id: 1, name: "Complex JOINs", completed: false },
      { id: 2, name: "WHERE Clauses", completed: false },
    ],
  },
  hard: {
    title: "hardLevel",
    description: "This is the hard level with advanced SQL tasks.",
    tasks: [
      { id: 1, name: "GROUP BY Challenges", completed: false },
      { id: 2, name: "Subqueries", completed: false },
    ],
  },
};

export default function Level() {
  const { difficulty } = useParams(); // Извлекаем уровень сложности из URL
  const { t } = useTranslation();

  // Проверяем, существует ли уровень
  const level = levelData[difficulty.toLowerCase()];
  if (!level) {
    return (
      <div className="min-h-screen flex flex-col items-center justify-center bg-green-200 font-mono">
        <h1 className="text-4xl font-bold text-black uppercase">
          {t("levelNotFound")}
        </h1>
      </div>
    );
  }

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-green-200 font-mono">
      <h1 className="text-4xl font-bold text-black uppercase mb-4">
        {t(level.title)}
      </h1>
      <p className="text-lg text-gray-700 mb-6">{level.description}</p>
      <div className="w-full max-w-md">
        <h2 className="text-2xl font-bold mb-4">{t("tasks")}</h2>
        <ul className="space-y-2">
          {level.tasks.map((task) => (
            <li
              key={task.id}
              className="bg-gray-300 p-4 rounded-lg text-black flex justify-between items-center"
            >
              <span>{task.name}</span>
              <span>{task.completed ? "✅" : "⬜"}</span>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}
