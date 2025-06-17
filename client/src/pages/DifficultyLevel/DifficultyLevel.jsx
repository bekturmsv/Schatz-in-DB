import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { motion } from "framer-motion";
import { useGetLevelsQuery } from "../../features/task/taskApi.js";

export default function DifficultyLevel() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const { data, isLoading, isError } = useGetLevelsQuery();

    if (isLoading) return <div className="text-center mt-24">{t("loading")}</div>;
    if (isError || !data) return <div className="text-center mt-24 text-red-400">{t("errorLoadingLevels")}</div>;

    const progress = data.totalCompletedTasks;
    const total = data.totalTasks;
    const percent = Math.round((progress / total) * 100);

    const difficulties = data.levels.map((lvl, idx) => ({
        key: lvl.title,
        label: t(lvl.title),
        name: lvl.title.charAt(0).toUpperCase() + lvl.title.slice(1).toLowerCase(),
        isCompleted: lvl.completed,
        idx,
    }));

    const container = {
        hidden: { opacity: 0 },
        visible: {
            opacity: 1,
            transition: { staggerChildren: 0.11, delayChildren: 0.13 },
        },
    };
    const item = {
        hidden: { opacity: 0, y: 32, scale: 0.98 },
        visible: { opacity: 1, y: 0, scale: 1, transition: { duration: 0.55, ease: "easeOut" } },
    };

    const handleDifficultySelect = (difficulty, isLocked) => {
        if (!isLocked) {
            navigate(`/level/${difficulty.toLowerCase()}`);
        }
    };

    return (
        <div className="min-h-screen flex flex-col items-center justify-center bg-custom-background custom-font relative overflow-x-hidden">
            <motion.div
                initial={{ opacity: 0, y: -20, scale: 0.98 }}
                animate={{ opacity: 1, y: 0, scale: 1 }}
                transition={{ duration: 0.8 }}
                className="relative z-10 flex flex-col items-center w-full"
            >
                <h1
                    className="text-4xl md:text-5xl font-extrabold mb-10 uppercase tracking-wide drop-shadow custom-font"
                    style={{
                        color: "var(--color-primary)",
                    }}
                >
                    {t("difficultyLevel")}
                </h1>
                <div className="w-full max-w-md mb-10">
                    <h2
                        className="text-2xl font-semibold mb-3 custom-font"
                        style={{ color: "var(--color-primary)" }}
                    >
                        {t("progress")}
                    </h2>
                    <div className="mb-2 flex items-center justify-between text-lg">
                        <span className="custom-body" style={{ color: "var(--color-secondary)" }}>
                            {t("totalTasksSolved")}: <span className="font-bold">{progress}</span>/{total}
                        </span>
                        <span className="font-mono font-semibold" style={{ color: "var(--color-secondary)" }}>
                            {percent}%
                        </span>
                    </div>
                    <div className="w-full h-4 bg-gray-200 dark:bg-[#23272d] rounded-full overflow-hidden shadow-inner">
                        <motion.div
                            initial={{ width: 0 }}
                            animate={{ width: `${percent}%` }}
                            transition={{ duration: 1.1, ease: "easeInOut" }}
                            className="h-4 rounded-full"
                            style={{
                                background:
                                    "linear-gradient(90deg, var(--color-secondary) 10%, var(--color-primary) 95%)",
                                minWidth: 8,
                            }}
                        />
                    </div>
                </div>
                <motion.div
                    variants={container}
                    initial="hidden"
                    animate="visible"
                    className="flex flex-col space-y-6 w-full max-w-lg"
                >
                    {difficulties.map((diff, idx) => {
                        // Lock level if previous is not completed (except first)
                        // + теперь заблокирован если уже completed!
                        let isLocked = diff.isCompleted || (idx > 0 ? !difficulties[idx - 1].isCompleted : false);
                        return (
                            <motion.button
                                key={diff.key}
                                variants={item}
                                onClick={() => handleDifficultySelect(diff.name, isLocked)}
                                disabled={isLocked}
                                whileHover={!isLocked ? { scale: 1.025 } : {}}
                                whileTap={!isLocked ? { scale: 0.98 } : {}}
                                className={
                                    `relative py-4 px-8 rounded-xl text-2xl md:text-2xl uppercase font-semibold transition
                                    flex items-center justify-center border-2 shadow-md custom-font
                                    focus:outline-none
                                    ${
                                        diff.isCompleted
                                            ? "bg-gray-200 dark:bg-[#18181b] border-gray-300 text-gray-400 cursor-not-allowed"
                                            : !isLocked
                                                ? "bg-[var(--color-card-bg,rgba(30,36,43,0.87))] dark:bg-[var(--color-card-bg,#23272d)] border-[var(--color-primary)] text-[var(--color-primary)] hover:bg-[var(--color-card-hover,#323843)] dark:hover:bg-[#262b32]"
                                                : "bg-gray-200 dark:bg-[#18181b] border-gray-300 text-gray-400 cursor-not-allowed"
                                    }`
                                }
                                style={{
                                    minHeight: 64,
                                    letterSpacing: "0.045em",
                                }}
                            >
                                <span>{diff.label}</span>
                                <span className="ml-3 flex items-center">
                                    {isLocked && (
                                        <motion.span
                                            initial={{ scale: 0, opacity: 0 }}
                                            animate={{ scale: 1, opacity: 1 }}
                                            transition={{ delay: 0.15 }}
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
                                            transition={{ delay: 0.13 }}
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
