import { useSelector } from "react-redux";
import { useTranslation } from "react-i18next";
import LanguageSelector from "@/components/custom/LanguageSelector";
import ThemeSelector from "@/components/custom/ThemeSelector";
// import ThemeSelector from "../components/ThemeSelector";
import { useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button";

const Home = () => {
  //   const user = useSelector((state) => state.auth.user);
  const { t } = useTranslation();
  const navigate = useNavigate();

  return (
    <div className="min-h-screen  font-mono">
      {/* Герой-секция */}
      <section className="bg-green-400 py-16 text-center text-black">
        <h1 className="text-4xl md:text-5xl font-bold mb-4">
          {t("heroTitle")}
        </h1>
        <p className="text-lg md:text-xl mb-8">{t("heroSubtitle")}</p>
        <Button
          onClick={() => navigate("/play")}
          className="bg-gray-300 text-white px-6 py-3 rounded-lg hover:bg-gray-400 transition"
        >
          {t("startJourney")}
        </Button>
      </section>

      {/* Контент-секция */}
      <section className="container mx-auto py-12 px-4">
        {/* Game Progress */}
        <div className="bg-green-400 rounded-lg p-6 mb-8">
          <h2 className="text-2xl font-bold mb-2">{t("gameProgress")}</h2>
          <p className="mb-2">{t("progressDescription")}</p>
          <p className="text-sm">{t("progressSummary")}</p>
        </div>

        {/* Leaderboard */}
        <div className="bg-green-400 rounded-lg p-6 mb-8">
          <h2 className="text-2xl font-bold">{t("leaderboard")}</h2>
        </div>

        {/* News and Updates */}
        <div className="bg-green-400 rounded-lg p-6">
          <h2 className="text-2xl font-bold">{t("newsAndUpdates")}</h2>
        </div>
      </section>
    </div>
  );
};

export default Home;
