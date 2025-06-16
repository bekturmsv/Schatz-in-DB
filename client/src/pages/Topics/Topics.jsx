import { useParams, useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import { useTranslation } from "react-i18next";
import { useGetTopicsQuery } from "../../features/task/taskApi.js";
import { motion, AnimatePresence } from "framer-motion";
import DetectiveStory from "@/components/custom/DetectiveStory.jsx";
import { useEffect, useState } from "react";

export default function Topic() {
    const { difficulty } = useParams();
    const level = difficulty?.toUpperCase();
    const { t } = useTranslation();
    const navigate = useNavigate();
    const isAuthenticated = useSelector((s) => s.auth.isAuthenticated);

    const {
        data: response = {},
        isLoading,
        isError,
    } = useGetTopicsQuery(level, { skip: !difficulty });

    // Ключи для localStorage
    const detectiveGreetingKey = `detective_shown_${difficulty}`;
    const detectiveCongratsKey = `detective_congrats_shown_${difficulty}`;

    // --- Флаги показа детектива
    const [showDetectiveGreeting, setShowDetectiveGreeting] = useState(() => {
        return !localStorage.getItem(detectiveGreetingKey);
    });
    const [showDetectiveCongrats, setShowDetectiveCongrats] = useState(() => {
        return !localStorage.getItem(detectiveCongratsKey);
    });

    // Для финального примера (ты можешь вставить свою логику)
    // Например, когда все темы решены:
    const topics = response.tasks ?? [];
    // ЭТО ПРИМЕР: считаем, что все темы решены если их 0 (или можешь сделать свою логику)
    const allTopicsCompleted = topics.length === 0; // <-- подправь под себя

    // После закрытия приветствия ставим флаг
    useEffect(() => {
        if (!showDetectiveGreeting) {
            localStorage.setItem(detectiveGreetingKey, "true");
        }
    }, [showDetectiveGreeting, detectiveGreetingKey]);

    // После закрытия поздравления ставим флаг
    useEffect(() => {
        if (!showDetectiveCongrats) {
            localStorage.setItem(detectiveCongratsKey, "true");
        }
    }, [showDetectiveCongrats, detectiveCongratsKey]);

    // Не даём войти неавторизованным
    if (!isAuthenticated) {
        navigate("/login");
        return null;
    }

    return (
        <div className="min-h-screen font-mono flex flex-col items-center justify-center bg-custom-background custom-font relative">

            {/* Детектив в начале (только 1 раз) */}
            <DetectiveStory
                isVisible={showDetectiveGreeting}
                onClose={() => setShowDetectiveGreeting(false)}
                difficulty={difficulty}
                isEnd={false}
            />

            {/* Детектив в конце (только 1 раз) */}
            <DetectiveStory
                isVisible={showDetectiveCongrats && allTopicsCompleted}
                onClose={() => setShowDetectiveCongrats(false)}
                difficulty={difficulty}
                isEnd={true}
            />

            <motion.h1
                initial={{ opacity: 0, y: -24 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.7 }}
                className="text-4xl md:text-5xl font-extrabold mb-10 mt-4 custom-font uppercase tracking-wide drop-shadow z-10"
                style={{
                    color: "var(--color-primary)",
                    textShadow: "0 2px 16px rgba(0,0,0,0.07)",
                }}
            >
                {t("selectTopic")}{" "}
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

            {!isLoading && !isError && !allTopicsCompleted && (
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
        </div>
    );
}
