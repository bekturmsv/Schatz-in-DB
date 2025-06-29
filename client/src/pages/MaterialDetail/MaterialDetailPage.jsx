import { useParams, useNavigate } from "react-router-dom";
import { useGetMaterialByIdQuery } from "../../features/material/materialApi";
import { useTranslation } from "react-i18next";
import { motion, AnimatePresence } from "framer-motion";
import { renderMaterial } from "@/lib/renderMaterial.jsx";

export default function MaterialDetailPage() {
    const { t } = useTranslation();
    const { id } = useParams();
    const navigate = useNavigate();

    const { data, isLoading, isError } = useGetMaterialByIdQuery(id);

    return (
        <div className="min-h-screen flex flex-col items-center bg-custom-background custom-font pt-24 px-3">
            <motion.button
                onClick={() => navigate(-1)}
                className="self-start mb-6 text-base text-blue-600 dark:text-blue-400 underline px-2"
                whileHover={{ x: -5, scale: 1.04 }}
            >
                ← {t("backToMaterials") || "Back to materials"}
            </motion.button>
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
                        {t("loadingMaterial") || "Loading..."}
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
                        {t("errorLoadingMaterial") || "Error loading material."}
                    </motion.p>
                )}
            </AnimatePresence>
            {!isLoading && !isError && data && (
                <motion.div
                    initial={{ opacity: 0, y: 24, scale: 0.97 }}
                    animate={{ opacity: 1, y: 0, scale: 1 }}
                    transition={{ duration: 0.45 }}
                    className="w-full max-w-3xl rounded-2xl border shadow-lg p-7 bg-[var(--color-card-bg)]"
                    style={{
                        borderColor: "var(--color-primary)",
                        color: "var(--color-primary)",
                    }}
                >
                    <h1 className="text-3xl md:text-4xl font-extrabold mb-5 custom-font uppercase tracking-wide"
                        style={{
                            background: "linear-gradient(90deg, var(--color-primary), var(--color-secondary))",
                            backgroundClip: "text",
                            color: "transparent",
                            WebkitBackgroundClip: "text",
                            WebkitTextFillColor: "transparent",
                        }}
                    >
                        {data.title}
                    </h1>
                    <div className="mb-6">
                        <span className="inline-block text-sm rounded px-3 py-1 font-mono bg-gray-100 dark:bg-gray-800/60 text-gray-700 dark:text-gray-200">
                            SQL Category: {data.sqlKategorie}
                        </span>
                        <span className="ml-4 inline-block text-sm rounded px-3 py-1 font-mono bg-gray-100 dark:bg-gray-800/60 text-gray-700 dark:text-gray-200">
                            Teacher ID: {data.teacher}
                        </span>
                    </div>
                    {data.description
                        ? renderMaterial(data.description, t)
                        : <div>{t("noDescription") || "No description."}</div>
                    }
                </motion.div>
            )}
        </div>
    );
}
