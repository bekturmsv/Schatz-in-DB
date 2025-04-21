import { useSelector, useDispatch } from "react-redux";
import { setLanguage } from "../../features/language/languageSlice";
import { useTranslation } from "react-i18next";

export default function LanguageSelector() {
  const dispatch = useDispatch();
  const currentLanguage = useSelector(
    (state) => state.language.currentLanguage
  );
  const { t } = useTranslation();

  const languages = [
    { code: "en", name: "English" },
    { code: "de", name: "Deutsch" },
  ];

  return (
    <select
      value={currentLanguage}
      onChange={(e) => dispatch(setLanguage(e.target.value))}
      className="px-4 py-2 bg-primary text-white rounded"
    >
      {languages.map((lang) => (
        <option key={lang.code} value={lang.code}>
          {lang.name}
        </option>
      ))}
    </select>
  );
}
