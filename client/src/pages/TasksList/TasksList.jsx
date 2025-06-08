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

    // Fetch tasks for the specific difficulty and topic
    const {
        data: response = {},
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

    const tasks = response.tasksNotCompleted ?? [];

    // Анимация для карточек
    const cardVariants = {
        hidden: { opacity: 0, y: 24, scale: 0.97 },
        visible: (i = 1) => ({
            opacity: 1,
            y: 0,
            scale: 1,
            transition: { delay: i * 0.09, duration: 0.5, ease: "easeOut" },
        }),
    };

    return (
        <div className="min-h-screen font-mono flex flex-col items-center justify-center bg-gradient-to-br from-green-50 via-blue-100 to-cyan-100 relative">
            {/* Декоративная блюр-волна */}
            <div className="absolute top-0 left-1/2 -translate-x-1/2 w-[48vw] h-[26vh] bg-gradient-to-tr from-green-200 via-blue-200 to-green-100 opacity-40 blur-2xl rounded-full pointer-events-none z-0"></div>

            <motion.h1
                initial={{ opacity: 0, y: -22 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.7 }}
                className="text-4xl md:text-5xl font-extrabold mb-10 mt-4 text-transparent bg-gradient-to-r from-green-500 via-blue-600 to-cyan-400 bg-clip-text uppercase tracking-wide drop-shadow z-10 text-center"
            >
                {t("tasksForTopic", { topic: decodeURIComponent(topicName) })}{" "}
                <span className="text-2xl text-black/50 font-normal ml-2">
          ({t(`${difficulty.toLowerCase()}Level`)})
        </span>
            </motion.h1>

            {/* Лоадер / Ошибка */}
            <AnimatePresence>
                {isLoading && (
                    <motion.p
                        initial={{ opacity: 0 }}
                        animate={{ opacity: 1 }}
                        className="text-lg text-gray-600 bg-white/80 py-4 px-8 rounded-xl shadow z-10"
                    >
                        {t("loadingTasks")}
                    </motion.p>
                )}
                {isError && (
                    <motion.p
                        initial={{ opacity: 0 }}
                        animate={{ opacity: 1 }}
                        className="text-lg text-red-600 bg-white/80 py-4 px-8 rounded-xl shadow z-10"
                    >
                        {t("errorLoadingTasks")}
                    </motion.p>
                )}
            </AnimatePresence>

            {/* Список задач */}
            {!isLoading && !isError && (
                <motion.ul
                    initial="hidden"
                    animate="visible"
                    variants={{
                        visible: {
                            transition: { staggerChildren: 0.12, delayChildren: 0.13 },
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
                                    boxShadow: "0 6px 28px 0 rgba(34,197,94,0.10)",
                                }}
                                whileTap={{ scale: 0.97 }}
                                onClick={() =>
                                    navigate(
                                        `/level/${level}/topic/${encodeURIComponent(topicName)}/task/${task.id}`
                                    )
                                }
                                className="cursor-pointer bg-white/80 p-7 rounded-2xl shadow-lg border border-blue-100 hover:bg-gradient-to-r hover:from-green-100 hover:to-cyan-100 transition-all flex flex-col min-h-[110px]"
                            >
                                <h2 className="text-xl font-bold mb-1 bg-gradient-to-r from-green-500 to-blue-400 bg-clip-text text-transparent">
                                    {task.title}
                                </h2>
                                <div className="text-gray-500 text-base line-clamp-3">
                                    {task.description}
                                </div>
                            </motion.li>
                        ))
                    ) : (
                        <motion.div
                            initial={{ opacity: 0, y: 14 }}
                            animate={{ opacity: 1, y: 0 }}
                            className="text-gray-600 w-full col-span-2 text-center py-8"
                        >
                            {t("noTasksAvailable")}
                        </motion.div>
                    )}
                </motion.ul>
            )}
        </div>
    );
}
