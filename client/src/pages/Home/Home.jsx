import { useSelector } from "react-redux";
import { useTranslation } from "react-i18next";
import LanguageSelector from "@/components/custom/LanguageSelector";
import ThemeSelector from "@/components/custom/ThemeSelector";
// import ThemeSelector from "../components/ThemeSelector";

const Home = () => {
  //   const user = useSelector((state) => state.auth.user);
  const { t } = useTranslation();

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-4">{t("welcome")}</h1>
      {/* <p>{t("points", { count: user.points })}</p> */}
      HEllo HOme
      <div className="mt-4">{/* <ThemeSelector /> */}</div>
      <div className="mt-4">
        <LanguageSelector />

        <ThemeSelector />
      </div>
    </div>
  );
};

export default Home;
