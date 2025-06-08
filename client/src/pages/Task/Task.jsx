import { useState } from "react";
import { useParams, useNavigate, useLocation } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { useTranslation } from "react-i18next";
import { toast } from "sonner";
import { useGetTaskByIdQuery, useSubmitTaskAnswerMutation } from "../../features/task/taskApi";
import { setUser } from "../../features/auth/authSlice";
import { executeSqlQuery } from "../../utils/SqlExecutor";
import { motion, AnimatePresence } from "framer-motion";

export default function Task() {
    const { difficulty, topicName, taskId } = useParams();
    const { t } = useTranslation();
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);
    const user = useSelector((state) => state.auth.user);
    const { state } = useLocation();
    const fallbackTopicName = state?.topicName || "WHERE";

    const [answer, setAnswer] = useState("");
    const [showHint, setShowHint] = useState(false);
    const [isCompleted, setIsCompleted] = useState(false);
    const [submitTaskAnswer] = useSubmitTaskAnswerMutation();

    if (!isAuthenticated) {
        navigate("/login");
        return null;
    }

    const { data: currentTask, isLoading, isError } = useGetTaskByIdQuery(
        { difficulty: difficulty.toUpperCase(), topicName: topicName || fallbackTopicName, taskId: parseInt(taskId) },
        { skip: !taskId }
    );

    // Анимация для секций
    const fadeUp = {
        hidden: { opacity: 0, y: 32 },
        visible: (i = 1) => ({
            opacity: 1,
            y: 0,
            transition: { delay: i * 0.12, duration: 0.7, ease: "easeOut" },
        }),
    };

    if (isLoading) {
        return (
            <div className="min-h-screen flex flex-col items-center justify-center bg-gradient-to-br from-green-50 via-blue-100 to-cyan-100 font-mono">
                <h1 className="text-4xl font-bold text-black uppercase">{t("loading")}</h1>
            </div>
        );
    }

    if (isError) {
        return (
            <div className="min-h-screen flex flex-col items-center justify-center bg-gradient-to-br from-green-50 via-blue-100 to-cyan-100 font-mono">
                <h1 className="text-4xl font-bold text-black uppercase">{t("error")}</h1>
                <p className="text-lg text-gray-700 mt-2">{t("failedToLoadTask")}</p>
            </div>
        );
    }

    if (!currentTask) {
        return (
            <div className="min-h-screen flex flex-col items-center justify-center bg-gradient-to-br from-green-50 via-blue-100 to-cyan-100 font-mono">
                <h1 className="text-4xl font-bold text-black uppercase">{t("taskNotFound")}</h1>
            </div>
        );
    }

    const jsonToTables = (json) => {
        if (!json || !json.sampleData) return <p>{t("noTablesAvailable")}</p>;
        try {
            const data = json.sampleData;
            if (!data || typeof data !== "object" || Object.keys(data).length === 0) {
                return <p>{t("noTablesAvailable")}</p>;
            }
            return (
                <div>
                    {Object.entries(data).map(([tableName, table], index) => {
                        if (!table || !Array.isArray(table)) {
                            return <p key={index}>{t("invalidTableFormat", { tableName })}</p>;
                        }
                        const columns = table.length > 0 ? Object.keys(table[0]) : [];
                        const rows = table.map((item) => columns.map((col) => item[col] || ""));
                        return (
                            <div key={index} className="mb-6">
                                <h3 className="text-lg font-bold mb-2">{tableName}</h3>
                                <div className="overflow-auto rounded-lg shadow">
                                    <table className="border-collapse border border-gray-300 min-w-[280px]">
                                        <thead>
                                        <tr>
                                            {columns.map((col, colIndex) => (
                                                <th key={colIndex} className="border p-2 bg-blue-100 text-sm font-semibold">
                                                    {col}
                                                </th>
                                            ))}
                                        </tr>
                                        </thead>
                                        <tbody>
                                        {rows.map((row, rowIndex) => (
                                            <tr key={rowIndex}>
                                                {row.map((cell, cellIndex) => (
                                                    <td key={cellIndex} className="border p-2 text-sm">
                                                        {cell !== undefined ? cell : ""}
                                                    </td>
                                                ))}
                                            </tr>
                                        ))}
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        );
                    })}
                </div>
            );
        } catch (error) {
            return <p>{t("errorLoadingTables")}</p>;
        }
    };

    const handleSubmit = async () => {
        try {
            const result = await executeSqlQuery(answer, currentTask.sampleData);

            const response = await submitTaskAnswer({
                taskId: parseInt(taskId),
                answer: result,
            }).unwrap();

            if (response.isCorrect) {
                toast.success(t("correctAnswer"));
                setIsCompleted(true);

                const updatedCompletedTasks = {
                    ...user.completedTasks,
                    [difficulty.toUpperCase()]: [
                        ...(user.completedTasks[difficulty.toUpperCase()] || []),
                        parseInt(taskId),
                    ],
                };

                const updatedTasks = user.tasks.map((userTask) => {
                    if (
                        userTask.type.toUpperCase() === difficulty.toUpperCase() &&
                        userTask.theme === currentTask.topicName
                    ) {
                        return {
                            ...userTask,
                            completed: userTask.completed + 1,
                        };
                    }
                    return userTask;
                });

                const updatedProgress = {
                    ...user.progress,
                    tasksSolved: user.progress.tasksSolved + 1,
                };
                const updatedPoints = user.points + currentTask.points;

                const updatedUser = {
                    completedTasks: updatedCompletedTasks,
                    tasks: updatedTasks,
                    progress: updatedProgress,
                    points: updatedPoints,
                };

                dispatch(setUser({ ...user, ...updatedUser }));
                setTimeout(() => navigate(`/level/${difficulty.toUpperCase()}`), 1000);
            } else {
                toast.error(t("incorrectAnswer"));
            }
        } catch (error) {
            if (error.message === "Invalid SQL syntax") {
                toast.error(t("invalidSqlSyntax"));
            } else if (error.message === "SQL query or sample data is missing") {
                toast.error(t("missingQueryOrData"));
            } else {
                toast.error(t("submissionError"));
            }
        }
    };

    const handleReset = () => setAnswer("");
    const handleShowHint = () => setShowHint(true);
    const handleCloseHint = () => setShowHint(false);

    return (
        <div className="min-h-screen bg-gradient-to-br from-green-50 via-blue-100 to-cyan-100 font-mono flex flex-col relative">
            {/* Декоративный блюр */}
            <div className="absolute top-0 left-1/2 -translate-x-1/2 w-[56vw] h-[30vh] bg-gradient-to-tr from-green-200 via-blue-200 to-green-100 opacity-30 blur-2xl rounded-full pointer-events-none z-0"></div>

            {/* Модалка подсказки */}
            <AnimatePresence>
                {showHint && (
                    <motion.div
                        initial={{ opacity: 0 }}
                        animate={{ opacity: 1 }}
                        exit={{ opacity: 0 }}
                        className="fixed inset-0 bg-black/50 flex items-center justify-center z-50"
                        onClick={handleCloseHint}
                    >
                        <motion.div
                            initial={{ scale: 0.94, y: 24, opacity: 0 }}
                            animate={{ scale: 1, y: 0, opacity: 1 }}
                            exit={{ scale: 0.95, y: 16, opacity: 0 }}
                            transition={{ type: "spring", stiffness: 220, damping: 18 }}
                            className="bg-white/90 p-8 rounded-2xl shadow-2xl w-full max-w-md relative"
                            onClick={(e) => e.stopPropagation()}
                        >
                            <button
                                onClick={handleCloseHint}
                                className="absolute top-3 right-4 text-2xl text-gray-500 hover:text-gray-700"
                                title={t("close")}
                            >
                                ✕
                            </button>
                            <h3 className="text-2xl font-bold mb-4 text-green-700">{t("hint")}</h3>
                            <p className="text-gray-700">{currentTask.description}</p>
                        </motion.div>
                    </motion.div>
                )}
            </AnimatePresence>

            {/* Основной контент */}
            <div className="flex-grow flex flex-col items-center justify-center p-4 relative z-10">
                <motion.div
                    initial="hidden"
                    animate="visible"
                    variants={fadeUp}
                    custom={1}
                    className="flex flex-col md:flex-row w-full max-w-5xl gap-8"
                >
                    {/* Описание задачи и таблицы */}
                    <motion.div
                        variants={fadeUp}
                        custom={2}
                        className="flex-1 bg-white/90 p-8 rounded-2xl shadow-xl mb-8 md:mb-0"
                    >
                        <h2 className="text-2xl font-bold mb-4 text-green-800 flex items-center">
                            <span className="mr-2">📝</span> {t("taskDescription")}
                        </h2>
                        <p className="mb-3 text-lg text-gray-800">{currentTask.description}</p>
                        <div className="overflow-x-auto">{jsonToTables(currentTask)}</div>
                    </motion.div>

                    {/* Правая колонка: инфо и ввод */}
                    <div className="flex-1 flex flex-col gap-6">
                        <motion.div
                            variants={fadeUp}
                            custom={3}
                            className="bg-white/90 p-6 rounded-2xl shadow-lg"
                        >
                            <div className="flex flex-wrap gap-x-6 gap-y-2 text-lg text-gray-700 mb-3">
                <span>
                  <b>{t("name")}:</b> {currentTask.title}
                </span>
                                <span>
                  <b>{t("point")}:</b> {currentTask.points}
                </span>
                                <span>
                  <b>{t("topic")}:</b> {currentTask.topicName}
                </span>
                            </div>
                            <div className="flex gap-3 mt-3">
                                <button
                                    onClick={handleReset}
                                    className="bg-gray-200 text-black py-2 px-5 rounded-xl hover:bg-gray-300 transition font-semibold"
                                    disabled={isCompleted}
                                >
                                    {t("reset")}
                                </button>
                                <button
                                    onClick={handleShowHint}
                                    className="bg-gradient-to-r from-green-400 to-cyan-400 text-white py-2 px-5 rounded-xl hover:from-green-500 hover:to-green-600 font-semibold shadow transition flex items-center gap-2"
                                    disabled={isCompleted}
                                >
                                    <span>💡</span> {t("hint")}
                                </button>
                            </div>
                        </motion.div>

                        <motion.div
                            variants={fadeUp}
                            custom={4}
                            className="bg-white/90 p-6 rounded-2xl shadow-lg flex-1 flex flex-col"
                        >
                            <h2 className="text-xl font-bold mb-3 text-green-700">{t("answerBox")}</h2>
                            <textarea
                                value={answer}
                                onChange={(e) => setAnswer(e.target.value)}
                                className="w-full h-36 p-3 border-2 border-blue-100 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400 font-mono text-base transition"
                                placeholder={t("typeYourSqlQueryHere")}
                                disabled={isCompleted}
                            />
                            <button
                                onClick={handleSubmit}
                                className={`mt-4 w-full bg-gradient-to-r from-green-500 to-cyan-400 text-white py-3 rounded-xl font-bold text-lg shadow-lg hover:from-green-400 hover:to-green-600 transition ${
                                    isCompleted ? "opacity-60 cursor-not-allowed" : ""
                                }`}
                                disabled={isCompleted}
                            >
                                {t("submit")}
                            </button>
                            {isCompleted && (
                                <div className="mt-4 text-center text-green-600 text-lg font-semibold">
                                    {t("correctAnswer")}
                                </div>
                            )}
                        </motion.div>
                    </div>
                </motion.div>
            </div>
        </div>
    );
}
