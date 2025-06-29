import { useGetMaterialsQuery } from "../../features/material/materialApi";
import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { motion, AnimatePresence } from "framer-motion";
import { getPlainText } from "@/lib/stripText.js";

export default function MaterialsListPage() {
    const { t } = useTranslation("materials"); // Указываем namespace
    const navigate = useNavigate();

    const { data = [], isLoading, isError } = useGetMaterialsQuery();

    return (
        <div className="min-h-screen flex flex-col items-center bg-custom-background custom-font pt-24 px-3">
            <motion.h1
                initial={{ opacity: 0, y: -20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.7 }}
                className="text-4xl md:text-5xl font-extrabold mb-10 custom-font uppercase tracking-wide drop-shadow z-10 text-center"
                style={{
                    color: "var(--color-primary)",
                }}
            >
                {t("studyMaterials")}
            </motion.h1>
            <AnimatePresence>
                {isLoading && (
                    <motion.p
                        initial={{ opacity: 0 }}
                        animate={{ opacity: 1 }}
                        className="text-lg py-4 px-8 rounded-xl shadow z-10"
                        style={{
                            color: "var(--color-primary)",
                            background: "var(--color-card-bg)",
                            border: "1px solid var(--color-primary)",
                        }}
                    >
                        {t("loadingMaterials")}
                    </motion.p>
                )}
                {isError && (
                    <motion.p
                        initial={{ opacity: 0 }}
                        animate={{ opacity: 1 }}
                        className="text-lg py-4 px-8 rounded-xl shadow z-10"
                        style={{
                            color: "#f87171",
                            background: "var(--color-card-bg)",
                            border: "1px solid #f87171",
                        }}
                    >
                        {t("errorLoadingMaterials")}
                    </motion.p>
                )}
            </AnimatePresence>
            {!isLoading && !isError && (
                <div className="w-full max-w-4xl grid grid-cols-1 md:grid-cols-2 gap-7 z-10">
                    {Array.isArray(data) && data.length > 0 ? (
                        data.map((mat, i) => (
                            <motion.div
                                key={mat.id}
                                initial={{ opacity: 0, y: 20 }}
                                animate={{ opacity: 1, y: 0 }}
                                transition={{ delay: i * 0.06, duration: 0.35 }}
                                whileHover={{ scale: 1.03, boxShadow: "0 8px 32px 0 rgba(34,197,94,0.11)" }}
                                className="cursor-pointer rounded-2xl border shadow-lg transition-all p-6 flex flex-col bg-[var(--card-bg)]"
                                style={{
                                    borderColor: "var(--color-primary)",
                                    color: "var(--color-primary)",
                                    minHeight: "160px",
                                }}
                                onClick={() => navigate(`/materials/${mat.id}`)}
                            >
                                <h2 className="text-2xl font-bold mb-2 custom-font"
                                    style={{
                                        background: "linear-gradient(90deg, var(--color-primary), var(--color-secondary))",
                                        backgroundClip: "text",
                                        color: "transparent",
                                        WebkitBackgroundClip: "text",
                                        WebkitTextFillColor: "transparent",
                                    }}
                                >
                                    {mat.title}
                                </h2>
                                <div className="flex-1 text-base text-[var(--color-secondary)] opacity-90 mb-2">
                                    {getPlainText(mat.description).slice(0, 120) || t("noDescription")}
                                    {mat.description && getPlainText(mat.description).length > 120 && "..."}
                                </div>
                                <div className="mt-3">
                                    <span className="inline-block text-sm rounded px-3 py-1 font-mono bg-gray-100 dark:bg-gray-800/60 text-gray-700 dark:text-gray-200">
                                        {t("sqlCategory")}: {mat.sqlKategorie}
                                    </span>
                                </div>
                            </motion.div>
                        ))
                    ) : (
                        <div className="text-center opacity-70 py-12 font-mono text-base col-span-2">
                            {t("noMaterialsFound")}
                        </div>
                    )}
                </div>
            )}
        </div>
    );
}
