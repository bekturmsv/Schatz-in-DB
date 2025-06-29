import { useGetMaterialByIdQuery } from "@/features/teacher/teacherApi";
import { useTranslation } from "react-i18next";
import { motion, AnimatePresence } from "framer-motion";
import { renderMaterial } from "@/lib/renderMaterial.jsx";

export default function TeacherMaterialDetailSidePanel({ id, onClose }) {
    const { t } = useTranslation();
    const { data, isLoading, isError } = useGetMaterialByIdQuery(id);

    return (
        <AnimatePresence>
            <motion.div
                key="sidepanel-bg"
                className="fixed inset-0 z-50 bg-black/30"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                exit={{ opacity: 0 }}
                onClick={onClose}
            >
                <motion.div
                    key="sidepanel"
                    className="absolute right-0 top-0 h-full w-full max-w-2xl bg-[var(--color-card-bg)] shadow-2xl border-l border-[var(--color-primary)] flex flex-col overflow-y-auto"
                    initial={{ x: "100%" }}
                    animate={{ x: 0 }}
                    exit={{ x: "100%" }}
                    transition={{ duration: 0.35 }}
                    onClick={e => e.stopPropagation()}
                >
                    <button
                        onClick={onClose}
                        className="absolute top-4 right-4 text-2xl font-bold text-[var(--color-primary)] hover:text-[var(--color-secondary)] transition"
                        aria-label="Close"
                    >×</button>
                    <div className="p-8 pt-12">
                        {isLoading && <div className="text-lg py-4">{t("loadingMaterial")}</div>}
                        {isError && <div className="text-lg py-4 text-red-600">{t("errorLoadingMaterial")}</div>}
                        {!isLoading && !isError && data && (
                            <>
                                <h1 className="text-2xl font-bold mb-3">{data.title}</h1>
                                <div className="mb-3">
                                    <span className="inline-block text-sm rounded px-3 py-1 font-mono bg-gray-100 dark:bg-gray-800/60 text-gray-700 dark:text-gray-200">
                                        {t("sqlCategory")}: {data.sqlKategorie}
                                    </span>
                                    <span className="ml-4 inline-block text-sm rounded px-3 py-1 font-mono bg-gray-100 dark:bg-gray-800/60 text-gray-700 dark:text-gray-200">
                                        {t("teacherId")}: {data.teacher}
                                    </span>
                                </div>
                                {data.description
                                    ? renderMaterial(data.description, t)
                                    : <div>{t("noDescription")}</div>
                                }
                            </>
                        )}
                    </div>
                </motion.div>
            </motion.div>
        </AnimatePresence>
    );
}
