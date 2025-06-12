import { useSelector, useDispatch } from "react-redux";
// import { setTheme } from "../features/theme/themeSlice";
import { useTranslation } from "react-i18next";
import { setTheme } from "@/features/theme/themeSlice";

export default function ThemeSelector() {
  const dispatch = useDispatch();
  //   const purchasedThemes = useSelector(
  //     (state) => state.auth.user.purchasedThemes
  //   );
  const { t } = useTranslation();

  const themes = [
    { id: "default", name: "Default" },
    { id: "dark", name: "Dark" },
    { id: "retro", name: "Retro" },
  ];

  return (
    <div className="flex gap-2">
      {themes.map((theme) => (
        <button
          key={theme.id}
          onClick={() => dispatch(setTheme(theme.id))}
          //   disabled={!purchasedThemes.includes(theme.id)}
          className="px-4 py-2 bg-primary text-white rounded disabled:opacity-50"
        >
          {theme.name}
        </button>
      ))}
    </div>
  );
}
