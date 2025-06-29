// TeacherMaterialDetailSidePanel.jsx

import { useGetMaterialByIdQuery } from "@/features/teacher/teacherApi";
import { useTranslation } from "react-i18next";
import { motion, AnimatePresence } from "framer-motion";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";
import { Prism as SyntaxHighlighter } from "react-syntax-highlighter";
import { vscDarkPlus } from "react-syntax-highlighter/dist/esm/styles/prism";

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
                                <div className="prose prose-base max-w-none text-[var(--color-secondary)] opacity-95 dark:prose-invert custom-body"
                                     style={{ wordBreak: "break-word" }}
                                >
                                    {data.description ? (
                                        <ReactMarkdown
                                            children={data.description}
                                            remarkPlugins={[remarkGfm]}
                                            components={{
                                                code({node, inline, className, children, ...props}) {
                                                    const match = /language-(\w+)/.exec(className || "");
                                                    return !inline && match ? (
                                                        <SyntaxHighlighter
                                                            style={vscDarkPlus}
                                                            language={match[1]}
                                                            PreTag="div"
                                                            customStyle={{
                                                                borderRadius: "0.7em",
                                                                fontSize: "1em",
                                                                margin: "1.2em 0",
                                                            }}
                                                            {...props}
                                                        >
                                                            {String(children).replace(/\n$/, "")}
                                                        </SyntaxHighlighter>
                                                    ) : (
                                                        <code
                                                            className="bg-gray-200 dark:bg-gray-800 rounded px-1 py-0.5 font-mono text-[0.97em]"
                                                            {...props}
                                                        >
                                                            {children}
                                                        </code>
                                                    );
                                                }
                                            }}
                                        />
                                    ) : (
                                        t("noDescription")
                                    )}
                                </div>
                            </>
                        )}
                    </div>
                </motion.div>
            </motion.div>
        </AnimatePresence>
    );
}
