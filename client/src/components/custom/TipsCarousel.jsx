import { useTranslation } from "react-i18next";
import { useState } from "react";

export default function TipsCarousel() {
    const { t } = useTranslation();
    const tips = t("sqlTips", { returnObjects: true }) || [];
    const [tipIndex, setTipIndex] = useState(0);

    const handleNextTip = () => setTipIndex((tipIndex + 1) % tips.length);

    return (
        <div className="flex flex-col items-center w-full">
            <p className="mb-6 md:text-lg font-semibold custom-font text-[var(--color-secondary)] min-h-[54px] flex items-center justify-center">
                {tips[tipIndex]}
            </p>
            <button
                onClick={handleNextTip}
                className="px-5 py-2 bg-gradient-to-r from-green-400 to-cyan-400 text-white font-bold rounded-xl shadow hover:from-green-500 hover:to-green-500 transition custom-font text-base"
                style={{ minWidth: 120 }}
            >
                {t("nextTip")}
            </button>
        </div>
    );
}
