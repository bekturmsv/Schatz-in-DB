import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { getUser } from "../../data/mockUser";
import { motion } from "framer-motion";

export default function DifficultyLevel() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const user = getUser();

    // Динамический массив уровней из user.completedLevels
    const difficulties = Object.keys(user.completedLevels).map((key) => ({
        key,
        label: t(key),
        name: key.charAt(0).toUpperCase() + key.slice(1),
        isCompleted: user.completedLevels[key],
    }));

    const handleDifficultySelect = (difficulty, isLocked) => {
        if (!isLocked) {
            navigate(`/level/${difficulty.toLowerCase()}`);
        }
    };

    // Прогресс для прогресс-бара
    const progress = user.progress.tasksSolved;
    const total = user.progress.totalTasks;
    const percent = Math.round((progress / total) * 100);

    // Framer motion для stagger анимации
    const container = {
        hidden: { opacity: 0 },
        visible: {
            opacity: 1,
            transition: { staggerChildren: 0.12, delayChildren: 0.2 },
        },
    };
    const item = {
        hidden: { opacity: 0, y: 32, scale: 0.97 },
        visible: { opacity: 1, y: 0, scale: 1, transition: { duration: 0.6, ease: "easeOut" } },
    };

    return (
        <div className="min-h-screen flex flex-col items-center justify-center font-mono relative bg-gradient-to-br from-green-50 via-blue-100 to-cyan-100 overflow-x-hidden">
            {/* Блюр декоративный овал */}
            <div className="absolute top-0 left-1/2 -translate-x-1/2 w-[55vw] h-[30vh] bg-gradient-to-tr from-green-200 via-blue-200 to-green-100 opacity-40 blur-2xl rounded-full pointer-events-none z-0"></div>
            <motion.div
                initial={{ opacity: 0, y: -20, scale: 0.98 }}
                animate={{ opacity: 1, y: 0, scale: 1 }}
                transition={{ duration: 0.8 }}
                className="relative z-10 flex flex-col items-center w-full"
            >
                <h1 className="text-4xl md:text-5xl font-extrabold mb-10 text-transparent bg-gradient-to-r from-green-500 via-blue-600 to-cyan-400 bg-clip-text uppercase tracking-wide drop-shadow">
                    {t("difficultyLevel")}
                </h1>
                <div className="w-full max-w-md mb-10">
                    <h2 className="text-2xl font-bold mb-3">{t("progress")}</h2>
                    <div className="mb-2 flex items-center justify-between text-lg text-gray-700">
            <span>
              {t("totalTasksSolved")}: <span className="font-bold">{progress}</span>/{total}
            </span>
                        <span className="font-mono font-bold text-green-600">{percent}%</span>
                    </div>
                    {/* Прогресс-бар с анимацией */}
                    <div className="w-full h-4 bg-gray-200 rounded-full overflow-hidden shadow-inner">
                        <motion.div
                            initial={{ width: 0 }}
                            animate={{ width: `${percent}%` }}
                            transition={{ duration: 1.2, ease: "easeInOut" }}
                            className="h-4 bg-gradient-to-r from-green-400 via-blue-400 to-cyan-400 rounded-full"
                        />
                    </div>
                </div>
                {/* Список уровней */}
                <motion.div
                    variants={container}
                    initial="hidden"
                    animate="visible"
                    className="flex flex-col space-y-6 w-full max-w-lg"
                >
                    {difficulties.map((diff, idx) => {
                        let isLocked = false;
                        if (idx > 0) {
                            const prevLevel = difficulties[idx - 1];
                            isLocked = !prevLevel.isCompleted;
                        }
                        return (
                            <motion.button
                                key={diff.key}
                                variants={item}
                                onClick={() => handleDifficultySelect(diff.name, isLocked)}
                                disabled={isLocked}
                                whileHover={!isLocked ? { scale: 1.04, boxShadow: "0 6px 32px 0 rgba(34,197,94,0.10)" } : {}}
                                whileTap={!isLocked ? { scale: 0.97 } : {}}
                                className={`
                  relative py-5 px-8 rounded-xl text-2xl md:text-2xl uppercase font-bold transition
                  flex items-center justify-center border-2 shadow-lg
                  ${diff.isCompleted
                                    ? "bg-gradient-to-r from-green-400 to-cyan-400 border-green-500 text-white"
                                    : ""}
                  ${!diff.isCompleted && !isLocked
                                    ? "bg-white/90 border-blue-200 text-black hover:bg-green-50"
                                    : ""}
                  ${isLocked
                                    ? "bg-gray-100 border-gray-300 text-gray-400 cursor-not-allowed"
                                    : ""}
                `}
                                style={{ minHeight: 72 }}
                            >
                                <span>{diff.label}</span>
                                <span className="ml-3 flex items-center">
                  {/* Анимация для замка и галочки */}
                                    {isLocked && (
                                        <motion.span
                                            initial={{ scale: 0, opacity: 0 }}
                                            animate={{ scale: 1, opacity: 1 }}
                                            transition={{ delay: 0.2 }}
                                            className="text-2xl"
                                            title={t("completePreviousLevelFirst")}
                                        >
                                            <svg className="inline w-7 h-7 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                <path strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" d="M16 10V8a4 4 0 10-8 0v2M5 10h14a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2z" />
                                            </svg>
                                        </motion.span>
                                    )}
                                    {diff.isCompleted && (
                                        <motion.span
                                            initial={{ scale: 0, opacity: 0 }}
                                            animate={{ scale: 1, opacity: 1 }}
                                            transition={{ delay: 0.18 }}
                                            className="text-2xl"
                                            title={t("completed")}
                                        >
                                            <svg className="inline w-7 h-7 text-green-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                <path strokeWidth="3" strokeLinecap="round" strokeLinejoin="round" d="M5 13l4 4L19 7" />
                                            </svg>
                                        </motion.span>
                                    )}
                </span>
                            </motion.button>
                        );
                    })}
                </motion.div>
            </motion.div>
        </div>
    );
}
