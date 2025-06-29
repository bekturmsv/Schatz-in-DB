import { useParams, useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import { useTranslation } from "react-i18next";
import { useGetTasksByTopicQuery } from "../../features/task/taskApi.js";
import { motion, AnimatePresence } from "framer-motion";
import { useState } from "react";

const PAGE_SIZE = 6;

export default function TasksList() {
    const { difficulty, topicName } = useParams();
    const level = difficulty?.toUpperCase();
    const { t } = useTranslation();
    const navigate = useNavigate();
    const isAuthenticated = useSelector((s) => s.auth.isAuthenticated);

    const {
        data: response = [],
        isLoading,
        isError,
    } = useGetTasksByTopicQuery(
        { difficulty: level, topicName: decodeURIComponent(topicName) },
        { skip: !difficulty || !topicName || !isAuthenticated }
    );

    const [page, setPage] = useState(1);

    if (!isAuthenticated) {
        navigate("/login");
        return null;
    }

    const tasks = Array.isArray(response) ? response : [];
    const totalPages = Math.ceil(tasks.length / PAGE_SIZE);
    const pagedTasks = tasks.slice((page - 1) * PAGE_SIZE, page * PAGE_SIZE);

    // Кнопка назад ведёт к списку тем по уровню (level)
    const handleBack = () => {
        navigate(`/level/${level}`);
    };

    // Pagination render
    const renderPagination = () => (
        totalPages > 1 && (
            <div className="flex gap-2 justify-center items-center mt-8 mb-4">
                <button
                    className="px-3 py-1 rounded bg-gray-200 text-gray-700 font-semibold hover:bg-gray-300 transition"
                    onClick={() => setPage((p) => Math.max(p - 1, 1))}
                    disabled={page === 1}
                >
                    {t("previous")}
                </button>
                {Array.from({ length: totalPages }, (_, i) => (
                    <button
                        key={i}
                        className={`px-3 py-1 rounded font-semibold transition
                            ${page === i + 1
                            ? "bg-blue-500 text-white shadow"
                            : "bg-gray-100 text-gray-800 hover:bg-gray-200"
                        }`}
                        onClick={() => setPage(i + 1)}
                    >
                        {i + 1}
                    </button>
                ))}
                <button
                    className="px-3 py-1 rounded bg-gray-200 text-gray-700 font-semibold hover:bg-gray-300 transition"
                    onClick={() => setPage((p) => Math.min(p + 1, totalPages))}
                    disabled={page === totalPages}
                >
                    {t("next")}
                </button>
            </div>
        )
    );

    const cardVariants = {
        hidden: { opacity: 0, y: 24, scale: 0.97 },
        visible: (i = 1) => ({
            opacity: 1,
            y: 0,
            scale: 1,
            transition: { delay: i * 0.09, duration: 0.45, ease: "easeOut" },
        }),
    };

    return (
        <div className="min-h-screen font-mono flex flex-col items-center justify-center bg-custom-background custom-font relative">

            {/* Красивая кнопка назад и заголовок рядом */}
            <div className="w-full flex flex-col items-center md:items-start max-w-5xl px-3 md:px-8">
                <div className="flex items-center gap-3 mt-10 mb-2">
                    <button
                        onClick={handleBack}
                        className={`
                            flex items-center gap-2 px-5 py-2 rounded-full
                            bg-[var(--color-card-bg)] shadow-lg border border-[var(--color-secondary)]
                            text-[var(--color-primary)] font-bold text-lg custom-font
                            transition hover:bg-[var(--color-card-bg-alt)] hover:shadow-xl focus:outline-none
                            active:scale-95
                        `}
                        style={{
                            minWidth: 110,
                            boxShadow: "0 2px 12px 0 rgba(34,197,94,0.08)"
                        }}
                    >
                        <svg className="w-5 h-5" fill="none" stroke="currentColor" strokeWidth={2.3} viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" d="M15 19l-7-7 7-7" />
                        </svg>
                        <span className="tracking-tight">{t("back")}</span>
                    </button>
                    <motion.h1
                        initial={{ opacity: 0, y: -18 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.7 }}
                        className="text-3xl md:text-5xl font-extrabold custom-font uppercase tracking-wide drop-shadow z-10 text-center"
                        style={{
                            color: "var(--color-primary)",
                            marginBottom: 0
                        }}
                    >
                        {t("tasksForTopic", { topic: decodeURIComponent(topicName) })}{" "}
                        <span className="text-xl md:text-2xl font-normal ml-2" style={{ color: "var(--color-secondary)" }}>
                            ({t(`${difficulty.toLowerCase()}Level`)})
                        </span>
                    </motion.h1>
                </div>
            </div>

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
                        {t("loadingTasks")}
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
                        {t("errorLoadingTasks")}
                    </motion.p>
                )}
            </AnimatePresence>

            {renderPagination()}

            {!isLoading && !isError && (
                <motion.ul
                    initial="hidden"
                    animate="visible"
                    variants={{
                        visible: {
                            transition: { staggerChildren: 0.11, delayChildren: 0.14 },
                        },
                    }}
                    className="w-full max-w-2xl grid grid-cols-1 md:grid-cols-2 gap-7 z-10"
                >
                    {pagedTasks.length > 0 ? (
                        pagedTasks.map((task, idx) => (
                            <motion.li
                                key={task.id}
                                variants={cardVariants}
                                custom={idx + 1}
                                whileHover={
                                    !task.solved
                                        ? {
                                            scale: 1.045,
                                            boxShadow: "0 6px 24px 0 rgba(34,197,94,0.13)",
                                            background: "var(--color-card-hover, #eef3fa)",
                                        }
                                        : {}
                                }
                                whileTap={!task.solved ? { scale: 0.97 } : {}}
                                onClick={
                                    task.solved
                                        ? undefined
                                        : () =>
                                            navigate(
                                                `/level/${level}/topic/${encodeURIComponent(topicName)}/task/${task.id}`
                                            )
                                }
                                className={`
                                    cursor-pointer rounded-2xl border transition-all flex flex-col min-h-[110px] shadow-lg
                                    ${task.solved ? "bg-green-50 dark:bg-green-900/20 pointer-events-none opacity-60" : ""}
                                `}
                                style={{
                                    background: "var(--color-card-bg, #f8fafc)",
                                    borderColor: "var(--color-primary)",
                                    color: "var(--color-primary)",
                                    padding: "2rem 1.6rem",
                                    fontFamily: "var(--font-family)",
                                    boxShadow: "0 3px 18px 0 rgba(30,36,43,0.03)",
                                    pointerEvents: task.solved ? "none" : "auto",
                                    opacity: task.solved ? 0.6 : 1,
                                }}
                            >
                                <h2
                                    className="text-xl font-bold mb-1 custom-font flex items-center"
                                    style={{
                                        background: "linear-gradient(90deg, var(--color-primary), var(--color-secondary))",
                                        backgroundClip: "text",
                                        color: "transparent",
                                        WebkitBackgroundClip: "text",
                                        WebkitTextFillColor: "transparent",
                                    }}
                                >
                                    {task.aufgabe?.split('\n')[0] || t("task")}
                                    <span className="ml-2 flex items-center">
                                        {task.solved && (
                                            <motion.span
                                                initial={{ scale: 0, opacity: 0 }}
                                                animate={{ scale: 1, opacity: 1 }}
                                                transition={{ delay: 0.08 }}
                                                className="text-2xl"
                                                title={t("taskCompleted")}
                                            >
                                                <svg className="inline w-7 h-7 text-green-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <circle cx="12" cy="12" r="10" strokeWidth="2" />
                                                    <path strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round" d="M8 12.5l2.5 2L16 9" />
                                                </svg>
                                            </motion.span>
                                        )}
                                    </span>
                                </h2>
                                <div
                                    className="text-base custom-body line-clamp-3"
                                    style={{
                                        color: "var(--color-secondary)",
                                        opacity: 0.88,
                                    }}
                                >
                                    {task.aufgabe?.split('\n').slice(1).join('\n')}
                                </div>
                            </motion.li>
                        ))
                    ) : (
                        <motion.div
                            initial={{ opacity: 0, y: 14 }}
                            animate={{ opacity: 1, y: 0 }}
                            className="w-full col-span-2 text-center py-8 custom-font"
                            style={{
                                color: "var(--color-secondary)",
                                opacity: 0.8,
                                background: "var(--color-card-bg, #f8fafc)",
                                borderRadius: "1rem",
                            }}
                        >
                            {t("noTasksAvailable")}
                        </motion.div>
                    )}
                </motion.ul>
            )}

            {renderPagination()}
        </div>
    );
}
