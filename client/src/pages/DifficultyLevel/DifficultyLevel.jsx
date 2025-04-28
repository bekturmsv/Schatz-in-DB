import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";

export default function DifficultyLevel() {
  const { t } = useTranslation();
  const navigate = useNavigate();

  const handleDifficultySelect = (difficulty) => {
    navigate(`/level/${difficulty.toLowerCase()}`);
  };

  const difficulties = [
    { name: "Easy", label: t("easy") },
    { name: "Medium", label: t("medium") },
    { name: "Hard", label: t("hard") },
  ];

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-green-200 font-mono">
      <h1 className="text-4xl font-bold mb-8 text-black uppercase">
        {t("difficultyLevel")}
      </h1>
      <div className="flex flex-col space-y-4">
        {difficulties.map((diff) => (
          <button
            key={diff.name}
            onClick={() => handleDifficultySelect(diff.name)}
            className="bg-gray-300 text-black py-4 px-8 rounded-lg text-xl uppercase hover:bg-gray-400 transition"
          >
            {diff.label}
          </button>
        ))}
      </div>
    </div>
  );
}
