import { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { useTranslation } from "react-i18next";
import { toast } from "sonner";
import { useGetTaskByIdQuery, useSubmitTaskAnswerMutation } from "../../features/task/taskApi";
import { setUser } from "../../features/auth/authSlice";
import { executeSqlQuery } from "../../utils/SqlExecutor";
import { motion, AnimatePresence } from "framer-motion";

export default function Task() {
    const { taskId } = useParams();
    const { t } = useTranslation();
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);
    const user = useSelector((state) => state.auth.user);

    const [answer, setAnswer] = useState("");
    const [showHint, setShowHint] = useState(false);
    const [isCompleted, setIsCompleted] = useState(false);
    const [submitTaskAnswer] = useSubmitTaskAnswerMutation();

    if (!isAuthenticated) {
        navigate("/login");
        return null;
    }

    const { data: currentTask, isLoading, isError } = useGetTaskByIdQuery(
        { taskId: parseInt(taskId) },
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
            <div className="min-h-screen flex flex-col items-center justify-center bg-custom-background custom-font">
                <h1 className="text-4xl font-bold text-[var(--color-primary)] uppercase">{t("loading")}</h1>
            </div>
        );
    }

    if (isError) {
        return (
            <div className="min-h-screen flex flex-col items-center justify-center bg-custom-background custom-font">
                <h1 className="text-4xl font-bold text-[var(--color-primary)] uppercase">{t("error")}</h1>
                <p className="text-lg text-gray-400 mt-2">{t("failedToLoadTask")}</p>
            </div>
        );
    }

    if (!currentTask) {
        return (
            <div className="min-h-screen flex flex-col items-center justify-center bg-custom-background custom-font">
                <h1 className="text-4xl font-bold text-[var(--color-primary)] uppercase">{t("taskNotFound")}</h1>
            </div>
        );
    }

    // Отрисовка таблицы по новым полям
    const renderTaskTable = (task) => {
        if (!task.tableName || !Array.isArray(task.tableData) || !task.tableData.length) {
            return <p>{t("noTablesAvailable")}</p>;
        }
        const columns = Object.keys(task.tableData[0]);
        return (
            <div>
                <h3 className="text-base font-semibold mb-1 custom-font text-[var(--color-primary)]">{task.tableName}</h3>
                <div className="overflow-auto rounded-lg shadow">
                    <table className="border-collapse border border-gray-600 dark:border-gray-700 min-w-[180px] text-xs">
                        <thead>
                        <tr>
                            {columns.map((col, i) => (
                                <th key={i} className="border px-2 py-1 bg-[var(--color-background)] font-medium custom-font text-[var(--color-primary)]">{col}</th>
                            ))}
                        </tr>
                        </thead>
                        <tbody>
                        {task.tableData.map((row, i) => (
                            <tr key={i}>
                                {columns.map((col, j) => (
                                    <td key={j} className="border px-2 py-1 custom-font text-[var(--color-secondary)]">
                                        {row[col] != null ? row[col] : ""}
                                    </td>
                                ))}
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>
        );
    };

    // handleSubmit может остаться прежним, если не поменялся формат ответа от submitTaskAnswer
    const handleSubmit = async () => {
        try {
            const result = await executeSqlQuery(answer, { [currentTask.tableName]: currentTask.tableData });

            const response = await submitTaskAnswer({
                taskId: parseInt(taskId),
                answer: result,
            }).unwrap();

            if (response.isCorrect) {
                toast.success(t("correctAnswer"));
                setIsCompleted(true);

                // Здесь можно обновлять пользователя и прогресс, если нужно
                // ...
                setTimeout(() => navigate(-1), 1000);
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
        <div className="min-h-screen bg-custom-background custom-font flex flex-col relative">
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
                            className="bg-[var(--color-card-bg)] p-8 rounded-2xl shadow-2xl w-full max-w-md relative"
                            onClick={(e) => e.stopPropagation()}
                        >
                            <button
                                onClick={handleCloseHint}
                                className="absolute top-3 right-4 text-2xl text-gray-400 hover:text-gray-700"
                                title={t("close")}
                            >
                                ✕
                            </button>
                            <h3 className="text-2xl font-bold mb-4 text-[var(--color-primary)]">{t("hint")}</h3>
                            <p className="text-[var(--color-secondary)]">{currentTask.hint}</p>
                        </motion.div>
                    </motion.div>
                )}
            </AnimatePresence>
            {/* Основной контент */}
            <div className="flex-grow flex items-center justify-center p-4 md:p-8 relative z-10">
                <motion.div
                    initial="hidden"
                    animate="visible"
                    variants={fadeUp}
                    custom={1}
                    className="w-full max-w-6xl flex flex-col md:flex-row gap-8 md:gap-12 justify-center items-stretch"
                >
                    {/* Левая колонка */}
                    <motion.div
                        variants={fadeUp}
                        custom={2}
                        className="flex-1 md:max-w-[700px] bg-[var(--color-card-bg)] rounded-2xl shadow-xl p-7 mb-8 md:mb-0"
                        style={{
                            minWidth: 0,
                            color: "var(--color-primary)"
                        }}
                    >
                        <h2 className="text-2xl font-extrabold mb-4 flex items-center custom-font" style={{ color: "var(--color-primary)" }}>
                            <span className="mr-2">📝</span> {t("taskDescription")}
                            {currentTask.solved && (
                                <span className="ml-3 text-green-500" title={t("taskCompleted")}>
                                    <svg className="inline w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <circle cx="12" cy="12" r="10" strokeWidth="2" />
                                        <path strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round" d="M8 12.5l2.5 2L16 9" />
                                    </svg>
                                </span>
                            )}
                        </h2>
                        <div className="mb-3 text-base md:text-lg custom-font text-[var(--color-secondary)] whitespace-pre-line">
                            {currentTask.aufgabe}
                        </div>
                        <div className="overflow-x-auto">{renderTaskTable(currentTask)}</div>
                    </motion.div>

                    {/* Правая колонка */}
                    <div className="flex flex-col gap-6 md:w-[350px] min-w-[320px] flex-shrink-0">
                        {/* Информация о задаче + кнопки */}
                        <motion.div
                            variants={fadeUp}
                            custom={3}
                            className="rounded-2xl shadow-lg custom-card p-6"
                            style={{
                                background: "var(--color-card-bg-alt, #e7f3ee)",
                                color: "var(--color-secondary)"
                            }}
                        >
                            <div className="flex flex-wrap gap-x-6 gap-y-2 text-base md:text-lg custom-font mb-3">
                                <span>
                                    <b>{t("taskCode")}:</b> {currentTask.taskCode}
                                </span>
                                <span>
                                    <b>{t("category")}:</b> {currentTask.kategorie}
                                </span>
                                <span>
                                    <b>{t("difficulty")}:</b> {currentTask.schwierigkeitsgrad}
                                </span>
                            </div>
                            <div className="flex gap-3 mt-3">
                                <button
                                    onClick={handleReset}
                                    className="bg-gray-300 dark:bg-gray-700 text-black dark:text-gray-100 py-2 px-5 rounded-xl hover:bg-gray-400 dark:hover:bg-gray-600 transition font-semibold custom-font"
                                    disabled={isCompleted}
                                >
                                    {t("reset")}
                                </button>
                                <button
                                    onClick={handleShowHint}
                                    className="bg-gradient-to-r from-green-400 to-cyan-400 text-white py-2 px-5 rounded-xl hover:from-green-500 hover:to-green-600 font-semibold shadow transition flex items-center gap-2 custom-font"
                                    disabled={isCompleted}
                                >
                                    <span>💡</span> {t("hint")}
                                </button>
                            </div>
                        </motion.div>
                        {/* Ответ */}
                        <motion.div
                            variants={fadeUp}
                            custom={4}
                            className="rounded-2xl shadow-lg flex-1 flex flex-col custom-card p-6"
                            style={{
                                background: "var(--color-card-bg)",
                                color: "var(--color-primary)"
                            }}
                        >
                            <h2 className="text-xl font-bold mb-3 custom-font" style={{ color: "var(--color-primary)" }}>{t("answerBox")}</h2>
                            <textarea
                                value={answer}
                                onChange={(e) => setAnswer(e.target.value)}
                                className="w-full h-36 p-3 border-2 border-blue-100 dark:border-gray-700 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400 font-mono text-base transition bg-[var(--color-background)] text-[var(--color-primary)]"
                                placeholder={t("typeYourSqlQueryHere")}
                                disabled={isCompleted}
                            />
                            <button
                                onClick={handleSubmit}
                                className={`mt-4 w-full bg-gradient-to-r from-green-500 to-cyan-400 text-white py-3 rounded-xl font-bold text-lg shadow-lg hover:from-green-400 hover:to-green-600 transition custom-font ${
                                    isCompleted ? "opacity-60 cursor-not-allowed" : ""
                                }`}
                                disabled={isCompleted}
                            >
                                {t("submit")}
                            </button>
                            {isCompleted && (
                                <div className="mt-4 text-center text-green-600 text-lg font-semibold custom-font">
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
