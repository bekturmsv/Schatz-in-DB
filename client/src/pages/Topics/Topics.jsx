import { useParams, useNavigate, useLocation } from "react-router-dom";
import { useSelector } from "react-redux";
import { useTranslation } from "react-i18next";
import { useGetTopicsQuery, useGetFinalTestByDifficultyQuery } from "../../features/task/taskApi.js";
import { motion, AnimatePresence } from "framer-motion";
import DetectiveStory from "@/components/custom/DetectiveStory.jsx";
import { useEffect, useState } from "react";

export default function Topic() {
    const { difficulty } = useParams();
    const level = difficulty?.toUpperCase();
    const { t } = useTranslation();
    const navigate = useNavigate();
    const isAuthenticated = useSelector((s) => s.auth.isAuthenticated);
    const location = useLocation();

    const {
        data: response = {},
        isLoading,
        isError,
    } = useGetTopicsQuery(level, { skip: !difficulty });

    const { data: finalTestData = [], isLoading: isFinalTestLoading } = useGetFinalTestByDifficultyQuery(level, { skip: !level });

    const detectiveGreetingKey = `detective_shown_${difficulty}`;
    const detectiveCongratsKey = `detective_congrats_shown_${difficulty}`;

    const [showDetectiveGreeting, setShowDetectiveGreeting] = useState(() => {
        return !localStorage.getItem(detectiveGreetingKey) && !(location.state && location.state.showCongrats);
    });

    const [showDetectiveCongrats, setShowDetectiveCongrats] = useState(() => {
        return !!(location.state && location.state.showCongrats);
    });

    const topics = response.tasks ?? [];

    useEffect(() => {
        if (!showDetectiveGreeting) {
            localStorage.setItem(detectiveGreetingKey, "true");
        }
    }, [showDetectiveGreeting, detectiveGreetingKey]);

    useEffect(() => {
        if (location.state && location.state.showCongrats) {
            window.history.replaceState({}, document.title);
        }
    }, [location.state]);

    if (!isAuthenticated) {
        navigate("/login");
        return null;
    }

    const handleCongratsClose = () => {
        setShowDetectiveCongrats(false);
        localStorage.setItem(detectiveCongratsKey, "true");
        navigate("/play");
    };

    // Кнопка "назад" ведёт всегда к выбору уровня
    const handleBack = () => {
        navigate("/play");
    };

    return (
        <div className="min-h-screen font-mono flex flex-col items-center justify-center bg-custom-background custom-font relative">
            <DetectiveStory
                isVisible={showDetectiveGreeting}
                onClose={() => setShowDetectiveGreeting(false)}
                difficulty={difficulty}
                isEnd={false}
            />
            <DetectiveStory
                isVisible={showDetectiveCongrats}
                onClose={handleCongratsClose}
                difficulty={difficulty}
                isEnd={true}
            />

            {/* Кнопка назад и заголовок на одной линии */}
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
                        className="text-3xl md:text-5xl font-extrabold custom-font uppercase tracking-wide drop-shadow z-10"
                        style={{
                            color: "var(--color-primary)",
                            textShadow: "0 2px 16px rgba(0,0,0,0.07)",
                            marginBottom: 0
                        }}
                    >
                        {t("selectTopic")}{" "}
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
                        {t("loadingTopics")}
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
                        {t("errorLoadingTopics")}
                    </motion.p>
                )}
            </AnimatePresence>

            {!isLoading && !isError && topics.length > 0 && (
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
                            variants={{
                                hidden: { opacity: 0, y: 24, scale: 0.97 },
                                visible: {
                                    opacity: 1,
                                    y: 0,
                                    scale: 1,
                                    transition: { delay: (idx + 1) * 0.08, duration: 0.5, ease: "easeOut" },
                                },
                            }}
                            initial="hidden"
                            animate="visible"
                            whileHover={{
                                scale: 1.04,
                                boxShadow: "0 6px 24px 0 rgba(34,197,94,0.13)",
                            }}
                            whileTap={{ scale: 0.97 }}
                            onClick={() =>
                                navigate(
                                    `/level/${level}/topic/${encodeURIComponent(topic.name)}`
                                )
                            }
                            className="cursor-pointer rounded-2xl border transition-all flex flex-col items-start min-h-[110px] shadow-lg"
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
                                className="text-xl font-bold mb-1 custom-font"
                                style={{
                                    background: "linear-gradient(90deg, var(--color-primary), var(--color-secondary))",
                                    backgroundClip: "text",
                                    color: "transparent",
                                    WebkitBackgroundClip: "text",
                                    WebkitTextFillColor: "transparent",
                                }}
                            >
                                {topic.name}
                            </h2>
                            <div
                                className="text-base custom-body"
                                style={{
                                    color: "var(--color-secondary)",
                                    opacity: 0.88,
                                }}
                            >
                                {topic.description || ""}
                            </div>
                        </motion.li>
                    ))}
                </motion.ul>
            )}

            {!isLoading && !isError && Array.isArray(finalTestData) && finalTestData.length > 0 && (
                <div className="w-full flex flex-col items-center mt-8 z-10">
                    <button
                        onClick={() => navigate(`/level/${level}/final-test`)}
                        className="bg-gradient-to-r from-green-500 to-blue-500 hover:from-green-600 hover:to-blue-600 text-white font-bold py-5 px-12 rounded-2xl shadow-xl text-3xl tracking-wide uppercase custom-font transition"
                    >
                        🏁 {t("startFinalTest")}
                    </button>
                </div>
            )}
        </div>
    );
}
