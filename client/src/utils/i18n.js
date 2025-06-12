import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import LanguageDetector from "i18next-browser-languagedetector";
import resourcesToBackend from "i18next-resources-to-backend";

i18n
  .use(LanguageDetector)
  .use(initReactI18next)
  .use(
    resourcesToBackend((language, namespace) =>
      import(`../assets/locales/${language}/${namespace}.json`)
    )
  )
  .init({
    fallbackLng: "en",
    ns: ["translation"],
    defaultNS: "translation",
    interpolation: {
      escapeValue: false,
    },
  });

export default i18n;
