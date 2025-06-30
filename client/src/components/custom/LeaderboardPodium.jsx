// src/components/LeaderboardPodium.jsx
import { useGetRatingsQuery } from "@/features/rating/ratingApi";
import { useTranslation } from "react-i18next";
import { useEffect, useState } from "react";
import { motion, AnimatePresence } from "framer-motion";

const LEVELS = [
    { key: "ratingForEasy", label: "Easy", color: "from-green-300 to-green-500" },
    { key: "ratingForMedium", label: "Medium", color: "from-yellow-300 to-yellow-500" },
    { key: "ratingForHard", label: "Hard", color: "from-red-300 to-red-500" },
];

function formatTime(sec) {
    if (sec === undefined || sec === null) return "-";
    const min = Math.floor(sec / 60);
    const s = sec % 60;
    return `${min}:${s.toString().padStart(2, "0")}`;
}

export default function LeaderboardPodium({ autoRotate = true, interval = 4000 }) {
    const { t } = useTranslation();
    const { data: ratings = {}, isLoading, isError } = useGetRatingsQuery();
    const [currentLevel, setCurrentLevel] = useState(0);

    // Автосвайп/карусель между уровнями
    useEffect(() => {
        if (!autoRotate) return;
        const timer = setInterval(() => {
            setCurrentLevel((prev) => (prev + 1) % LEVELS.length);
        }, interval);
        return () => clearInterval(timer);
    }, [autoRotate, interval]);

    const level = LEVELS[currentLevel];
    const topUsers = (ratings[level.key] || []).slice(0, 3)
        .sort((a, b) => a.spentTimeInSeconds - b.spentTimeInSeconds);

    return (
        <motion.div
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
            className="w-full h-full flex flex-col items-center justify-center"
        >
            <div className="flex justify-center gap-2 mb-4">
                {LEVELS.map((l, idx) => (
                    <button
                        key={l.key}
                        onClick={() => setCurrentLevel(idx)}
                        className={`w-3 h-3 rounded-full mx-1 transition-all
                            ${currentLevel === idx ? "bg-[var(--color-primary)] scale-110" : "bg-gray-300 dark:bg-gray-700"}
                        `}
                        aria-label={l.label}
                    />
                ))}
            </div>
            <h3 className="text-xl font-bold mb-5 custom-font uppercase tracking-wide text-center"
                style={{
                    background: `linear-gradient(90deg, var(--color-primary), var(--color-secondary))`,
                    backgroundClip: "text",
                    color: "transparent",
                    WebkitBackgroundClip: "text",
                    WebkitTextFillColor: "transparent",
                }}>
                {t(level.label)}
            </h3>
            <div className="w-full flex justify-center items-end gap-4 relative min-h-[160px] py-2">
                <AnimatePresence mode="wait">
                    {isLoading && (
                        <motion.div key="loading" initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }}
                                    className="absolute left-0 right-0 text-center w-full top-10 text-lg">
                            {t("loadingRating") || "Loading..."}
                        </motion.div>
                    )}
                    {isError && (
                        <motion.div key="error" initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }}
                                    className="absolute left-0 right-0 text-center w-full top-10 text-lg text-red-400">
                            {t("errorLoadingRating") || "Error loading rating."}
                        </motion.div>
                    )}
                    {!isLoading && !isError && (
                        <>
                            {topUsers.length === 0 ? (
                                <motion.div key="empty" initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }}
                                            className="w-full text-center text-gray-400 mt-12 font-mono">
                                    {t("beFirstOnLeaderboard") || "Be the first on the leaderboard!"}
                                </motion.div>
                            ) : (
                                <>
                                    {/* 2nd place */}
                                    <motion.div
                                        key={topUsers[1]?.username || "no-2"}
                                        initial={{ opacity: 0, y: 30 }}
                                        animate={{ opacity: 1, y: 0 }}
                                        exit={{ opacity: 0, y: 30 }}
                                        className="flex flex-col items-center w-1/4"
                                    >
                                        <div className="w-12 h-12 bg-blue-100 dark:bg-blue-900 rounded-full flex items-center justify-center text-2xl mb-1 border-2 border-blue-300 dark:border-blue-700 font-bold">
                                            🥈
                                        </div>
                                        <div className="text-sm font-semibold truncate">{topUsers[1]?.username || "-"}</div>
                                        <div className="text-xs text-gray-500">{topUsers[1] && formatTime(topUsers[1].spentTimeInSeconds)}</div>
                                    </motion.div>
                                    {/* 1st place */}
                                    <motion.div
                                        key={topUsers[0]?.username || "no-1"}
                                        initial={{ opacity: 0, y: 30, scale: 0.9 }}
                                        animate={{ opacity: 1, y: 0, scale: 1.12 }}
                                        exit={{ opacity: 0, y: 30, scale: 0.85 }}
                                        className="flex flex-col items-center w-1/4 z-10"
                                    >
                                        <div className="w-16 h-16 bg-yellow-100 dark:bg-yellow-900 rounded-full flex items-center justify-center text-3xl mb-1 border-2 border-yellow-400 dark:border-yellow-700 font-bold shadow-lg">
                                            🥇
                                        </div>
                                        <div className="font-bold text-base truncate">{topUsers[0]?.username || "-"}</div>
                                        <div className="text-xs text-gray-700">{topUsers[0] && formatTime(topUsers[0].spentTimeInSeconds)}</div>
                                    </motion.div>
                                    {/* 3rd place */}
                                    <motion.div
                                        key={topUsers[2]?.username || "no-3"}
                                        initial={{ opacity: 0, y: 30 }}
                                        animate={{ opacity: 1, y: 0 }}
                                        exit={{ opacity: 0, y: 30 }}
                                        className="flex flex-col items-center w-1/4"
                                    >
                                        <div className="w-10 h-10 bg-amber-100 dark:bg-amber-900 rounded-full flex items-center justify-center text-xl mb-1 border-2 border-amber-300 dark:border-amber-700 font-bold">
                                            🥉
                                        </div>
                                        <div className="text-xs font-semibold truncate">{topUsers[2]?.username || "-"}</div>
                                        <div className="text-xs text-gray-500">{topUsers[2] && formatTime(topUsers[2].spentTimeInSeconds)}</div>
                                    </motion.div>
                                </>
                            )}
                        </>
                    )}
                </AnimatePresence>
            </div>
        </motion.div>
    );
}
