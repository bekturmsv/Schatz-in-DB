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
    <div className="min-h-screen   font-mono">
      {/* Герой-секция */}
      <section className="bg-custom-background py-32 md:py-64 text-center text-black">
        <h1 className="text-4xl md:text-4xl font-bold mb-4 custom-font">
          {t("heroTitle")}
        </h1>
        <p className="text-lg md:text-xl mb-8 custom-font">{t("heroSubtitle")}</p>
        <Button
          onClick={() => navigate("/play")}
          className=" custom-font bg-gray-400 text-white px-12 py-6 cursor-pointer rounded-lg hover:bg-green-400 hover:text-black transition"
        >
          {t("startJourney")}
        </Button>
      </section>

      {/* Контент-секция */}
      <section className="custom-font container mx-auto py-12 px-4">
        {/* Game Progress */}
        <div className="bg-custom-background rounded-lg text-center py-24 px-12 mb-8">
          <h2 className="text-2xl md:text-3xl font-bold mb-2">{t("gameProgress")}</h2>
          <p className="mb-2  md:text-xl">{t("progressDescription")}</p>
          <p className=" md:text-xl">{t("progressSummary")}</p>
        </div>

        {/* Leaderboard */}
        <div className="bg-custom-background rounded-lg text-center py-24 px-12 mb-8">
          <h2 className="text-2xl md:text-3xl font-bold">{t("leaderboard")}</h2>
        </div>

        {/* News and Updates */}
        <div className="bg-custom-background rounded-lg text-center py-24 px-12">
          <h2 className="text-2xl md:text-3xl font-bold">{t("newsAndUpdates")}</h2>
        </div>
      </section>
    </div>
  );
};

export default Home;
