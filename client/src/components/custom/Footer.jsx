import { useTranslation } from "react-i18next";

export default function Footer() {
  const { t } = useTranslation();

  return (
    <footer className="bg-gray-300 py-4 text-black custom-font">
      <div className="container mx-auto px-4 flex justify-between items-center">
        <p>{t("copyright")}</p>
        <p>{t("socialMediaLinks")}</p>
      </div>
    </footer>
  );
}
