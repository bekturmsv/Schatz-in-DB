import { useParams, useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import { useTranslation } from "react-i18next";
import { useGetTasksByTopicQuery } from "../../features/task/taskApi.js";
import { motion, AnimatePresence } from "framer-motion";

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

    if (!isAuthenticated) {
        navigate("/login");
        return null;
    }

    // Теперь response - это сразу массив заданий
    const tasks = Array.isArray(response) ? response : [];

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
            <motion.h1
                initial={{ opacity: 0, y: -22 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.7 }}
                className="text-4xl md:text-5xl font-extrabold mb-10 mt-4 custom-font uppercase tracking-wide drop-shadow z-10 text-center"
                style={{
                    color: "var(--color-primary)",
                }}
            >
                {t("tasksForTopic", { topic: decodeURIComponent(topicName) })}{" "}
                <span className="text-2xl font-normal ml-2" style={{ color: "var(--color-secondary)" }}>
                    ({t(`${difficulty.toLowerCase()}Level`)})
                </span>
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
                    {tasks.length > 0 ? (
                        tasks.map((task, idx) => (
                            <motion.li
                                key={task.id}
                                variants={cardVariants}
                                custom={idx + 1}
                                whileHover={{
                                    scale: 1.045,
                                    boxShadow: "0 6px 24px 0 rgba(34,197,94,0.13)",
                                    background: "var(--color-card-hover, #eef3fa)",
                                }}
                                whileTap={{ scale: 0.97 }}
                                onClick={() =>
                                    navigate(
                                        `/level/${level}/topic/${encodeURIComponent(topicName)}/task/${task.id}`
                                    )
                                }
                                className={`
                                    cursor-pointer rounded-2xl border transition-all flex flex-col min-h-[110px] shadow-lg
                                    ${task.solved ? "bg-green-50 dark:bg-green-900/20" : ""}
                                    `}
                                style={{
                                    background: "var(--color-card-bg, #f8fafc)",
                                    borderColor: "var(--color-primary)",
                                    color: "var(--color-primary)",
                                    padding: "2rem 1.6rem",
                                    fontFamily: "var(--font-family)",
                                    boxShadow: "0 3px 18px 0 rgba(30,36,43,0.03)",
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
        </div>
    );
}
