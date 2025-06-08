import { useParams, useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import { useTranslation } from "react-i18next";
import { useGetTopicsQuery } from "../../features/task/taskApi.js";
import { motion, AnimatePresence } from "framer-motion";

export default function Topic() {
    const { difficulty } = useParams();
    const level = difficulty?.toUpperCase();
    const { t } = useTranslation();
    const navigate = useNavigate();
    const isAuthenticated = useSelector((s) => s.auth.isAuthenticated);

    // Запрашиваем данные по level
    const {
        data: response = {},
        isLoading,
        isError,
    } = useGetTopicsQuery(level, { skip: !difficulty });

    // Если не авторизован — редирект на логин
    if (!isAuthenticated) {
        navigate("/login");
        return null;
    }

    // Выбираем список тем из поля `tasks`
    const topics = response.tasks ?? [];

    // Анимации
    const cardVariants = {
        hidden: { opacity: 0, y: 24, scale: 0.97 },
        visible: (i = 1) => ({
            opacity: 1,
            y: 0,
            scale: 1,
            transition: { delay: i * 0.08, duration: 0.5, ease: "easeOut" },
        }),
    };

    return (
        <div className="min-h-screen font-mono flex flex-col items-center justify-center bg-gradient-to-br from-green-50 via-blue-100 to-cyan-100 relative">
            {/* Декоративная блюр-волна */}
            <div className="absolute top-0 left-1/2 -translate-x-1/2 w-[50vw] h-[30vh] bg-gradient-to-tr from-green-200 via-blue-200 to-green-100 opacity-40 blur-2xl rounded-full pointer-events-none z-0"></div>

            <motion.h1
                initial={{ opacity: 0, y: -24 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.7 }}
                className="text-4xl md:text-5xl font-extrabold mb-10 mt-4 text-transparent bg-gradient-to-r from-green-500 via-blue-600 to-cyan-400 bg-clip-text uppercase tracking-wide drop-shadow z-10"
            >
                {t("selectTopic")}{" "}
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
                        className="text-lg text-gray-600 bg-white/70 py-4 px-8 rounded-xl shadow z-10"
                    >
                        {t("loadingTopics")}
                    </motion.p>
                )}
                {isError && (
                    <motion.p
                        initial={{ opacity: 0 }}
                        animate={{ opacity: 1 }}
                        className="text-lg text-red-600 bg-white/70 py-4 px-8 rounded-xl shadow z-10"
                    >
                        {t("errorLoadingTopics")}
                    </motion.p>
                )}
            </AnimatePresence>

            {/* Темы */}
            {!isLoading && !isError && (
                <motion.ul
                    initial="hidden"
                    animate="visible"
                    variants={{
                        visible: {
                            transition: { staggerChildren: 0.11, delayChildren: 0.15 },
                        },
                    }}
                    className="w-full max-w-2xl grid grid-cols-1 md:grid-cols-2 gap-7 z-10"
                >
                    {topics.map((topic, idx) => (
                        <motion.li
                            key={topic.name}
                            variants={cardVariants}
                            custom={idx + 1}
                            whileHover={{
                                scale: 1.045,
                                boxShadow: "0 6px 28px 0 rgba(34,197,94,0.09)",
                            }}
                            whileTap={{ scale: 0.97 }}
                            onClick={() =>
                                navigate(
                                    `/level/${level}/topic/${encodeURIComponent(topic.name)}`
                                )
                            }
                            className="cursor-pointer bg-white/80 p-7 rounded-2xl shadow-lg border border-blue-100 hover:bg-gradient-to-r hover:from-green-100 hover:to-cyan-100 transition-all flex flex-col items-start min-h-[110px]"
                        >
                            <h2 className="text-xl font-bold mb-1 bg-gradient-to-r from-green-500 to-blue-400 bg-clip-text text-transparent">
                                {topic.name}
                            </h2>
                            <div className="text-gray-500 text-base">
                                {topic.description || ""}
                            </div>
                        </motion.li>
                    ))}
                </motion.ul>
            )}
        </div>
    );
}
