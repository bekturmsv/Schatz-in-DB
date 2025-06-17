import { useState } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { useGetRatingsQuery } from "../../features/rating/ratingApi";
import { motion, AnimatePresence } from "framer-motion";

const LEVELS = [
    { key: "ratingForEasy", label: "Easy", color: "from-green-300 to-green-500" },
    { key: "ratingForMedium", label: "Medium", color: "from-yellow-300 to-yellow-500" },
    { key: "ratingForHard", label: "Hard", color: "from-red-300 to-red-500" },
];

function formatTime(sec) {
    if (!sec && sec !== 0) return "-";
    const min = Math.floor(sec / 60);
    const s = sec % 60;
    return `${min}:${s.toString().padStart(2, "0")}`;
}

export default function RatingPage() {
    const { t } = useTranslation();
    const isAuthenticated = useSelector((s) => s.auth.isAuthenticated);
    const navigate = useNavigate();
    const [selectedTab, setSelectedTab] = useState(LEVELS[0].key);

    const {
        data: ratings = {},
        isLoading,
        isError,
    } = useGetRatingsQuery(undefined, { skip: !isAuthenticated });

    if (!isAuthenticated) {
        navigate("/login");
        return null;
    }

    return (
        <div className="min-h-screen flex flex-col items-center bg-custom-background custom-font pt-24 px-3">
            <motion.h1
                initial={{ opacity: 0, y: -20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.7 }}
                className="text-4xl md:text-5xl font-extrabold mb-10 custom-font uppercase tracking-wide drop-shadow z-10 text-center"
                style={{
                    color: "var(--color-primary)",
                }}
            >
                User Leaderboard
            </motion.h1>

            {/* Tabs */}
            <div className="mb-10 flex flex-row gap-2 md:gap-6 bg-white/80 dark:bg-gray-900/60 rounded-2xl px-3 py-2 shadow-lg border border-primary z-20">
                {LEVELS.map((level) => (
                    <button
                        key={level.key}
                        onClick={() => setSelectedTab(level.key)}
                        className={`
              px-5 py-2 rounded-xl font-bold uppercase tracking-wide text-base
              transition-all
              ${selectedTab === level.key
                            ? "bg-gradient-to-br from-green-400 via-yellow-400 to-red-400 text-white shadow-md scale-105"
                            : "bg-transparent text-gray-700 dark:text-gray-100 opacity-80 hover:opacity-100"}
            `}
                        style={{
                            background:
                                selectedTab === level.key
                                    ? "linear-gradient(90deg, var(--color-primary), var(--color-secondary))"
                                    : undefined,
                            color: selectedTab === level.key ? "white" : undefined,
                            border: selectedTab === level.key ? "2px solid var(--color-primary)" : "2px solid transparent",
                        }}
                    >
                        {level.label}
                    </button>
                ))}
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
                        Loading rating...
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
                        Error loading rating.
                    </motion.p>
                )}
            </AnimatePresence>

            {!isLoading && !isError && (
                <div className="w-full max-w-4xl z-10">
                    <motion.div
                        key={selectedTab}
                        initial={{ opacity: 0, y: 32, scale: 0.96 }}
                        animate={{ opacity: 1, y: 0, scale: 1 }}
                        transition={{ duration: 0.5 }}
                        className={`
        rounded-2xl shadow-lg bg-gradient-to-br 
        ${
                            LEVELS.find((l) => l.key === selectedTab)?.color || "from-slate-100 to-slate-300"
                        } p-7 flex flex-col
      `}
                        style={{
                            minHeight: "410px",
                            background: "var(--color-card-bg, #f8fafc)",
                            border: "2px solid var(--color-primary)",
                        }}
                    >
                        <h2
                            className="text-2xl font-bold mb-4 custom-font text-center uppercase tracking-wider"
                            style={{
                                background: "linear-gradient(90deg, var(--color-primary), var(--color-secondary))",
                                backgroundClip: "text",
                                color: "transparent",
                                WebkitBackgroundClip: "text",
                                WebkitTextFillColor: "transparent",
                            }}
                        >
                            {LEVELS.find((l) => l.key === selectedTab)?.label || "Level"}
                        </h2>
                        <div className="flex-1">
                            {(ratings[selectedTab] && ratings[selectedTab].length > 0) ? (
                                <ol className="space-y-2">
                                    {ratings[selectedTab]
                                        .slice()
                                        .sort((a, b) => a.spentTimeInSeconds - b.spentTimeInSeconds)
                                        .map((user, idx) => (
                                            <li
                                                key={user.username}
                                                className="flex justify-between items-center bg-white/90 dark:bg-gray-800/60 rounded-xl px-6 py-3 shadow text-lg"
                                            >
                                                <span className="font-mono font-semibold w-12 text-right">{idx + 1}.</span>
                                                <span className="ml-3 flex-1 font-medium custom-font truncate">{user.username}</span>
                                                <span className="ml-6 text-base text-gray-700 dark:text-gray-200 font-mono px-4 py-1 rounded-md bg-gray-100/90 dark:bg-gray-900/20">
                    {formatTime(user.spentTimeInSeconds)}
                  </span>
                                            </li>
                                        ))}
                                </ol>
                            ) : (
                                <div className="text-center opacity-70 pt-8 font-mono text-base">
                                    No users yet.
                                </div>
                            )}
                        </div>
                    </motion.div>
                </div>
            )}
        </div>
    );
}
