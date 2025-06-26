import { useState, useEffect, useRef } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { useTranslation } from "react-i18next";
import { toast } from "sonner";
import {
    useGetTaskByIdQuery,
    useGetTasksByTopicQuery,
    useValidateSqlMutation
} from "../../features/task/taskApi";
import { motion, AnimatePresence } from "framer-motion";
import { initializeAuth } from "@/features/auth/authSlice.js";

export default function Task() {
    const { taskId } = useParams();
    const { t } = useTranslation();
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);

    // Для refetch списка задач — нам нужны параметры топика и сложности
    const pathParts = window.location.pathname.split('/');
    const difficulty = pathParts[2];
    const topicName = decodeURIComponent(pathParts[4] || '');

    // Log URL parameters for debugging
    console.log("URL Parts:", { difficulty, topicName });

    // Получаем refetch для списка задач
    const { refetch: refetchTasksList } = useGetTasksByTopicQuery(
        { difficulty, topicName },
        { skip: !difficulty || !topicName || !isAuthenticated }
    );

    // Состояния
    const [availableBlocks, setAvailableBlocks] = useState([]);
    const [userBlocks, setUserBlocks] = useState([]);
    const [draggedBlock, setDraggedBlock] = useState(null);
    const dropInputRef = useRef();

    const [answer, setAnswer] = useState("");
    const [showHint, setShowHint] = useState(false);
    const [isCompleted, setIsCompleted] = useState(false);
    const [submissionStatus, setSubmissionStatus] = useState(null);

    // Состояние для модального окна результата SQL
    const [resultModal, setResultModal] = useState({
        open: false,
        correct: false,
        userResult: null,
        userSql: "",
    });

    const [validateSql, { isLoading: isValidating }] = useValidateSqlMutation();

    const {
        data: currentTask,
        isLoading,
        isError,
        refetch
    } = useGetTaskByIdQuery(
        { taskId: parseInt(taskId) },
        { skip: !taskId }
    );

    if (!isAuthenticated) {
        navigate("/login");
        return null;
    }

    // DRAG & DROP setup
    useEffect(() => {
        if (
            currentTask &&
            currentTask.taskInteractionType === "DRAG_AND_DROP" &&
            Array.isArray(currentTask.dragAndDropQuery)
        ) {
            setAvailableBlocks(currentTask.dragAndDropQuery);
            setUserBlocks([]);
        }
    }, [currentTask]);

    useEffect(() => {
        if (
            currentTask?.taskInteractionType === "INCORRECT_SQL" &&
            currentTask?.wrongQuery &&
            !answer
        ) {
            setAnswer(currentTask.wrongQuery);
        }
    }, [currentTask, answer]);

    const solved = currentTask?.solved || isCompleted;

    // DRAG & DROP handlers
    const onDragStart = (block, origin, idx) => setDraggedBlock({ block, origin, idx });
    const onDropToInput = () => {
        if (!draggedBlock || solved) return;
        if (draggedBlock.origin === "available") {
            setUserBlocks([...userBlocks, draggedBlock.block]);
            setAvailableBlocks(availableBlocks.filter((b, i) => i !== draggedBlock.idx));
        }
        setDraggedBlock(null);
    };
    const onDropToAvailable = () => {
        if (!draggedBlock || solved) return;
        if (draggedBlock.origin === "input") {
            setAvailableBlocks([...availableBlocks, draggedBlock.block]);
            setUserBlocks(userBlocks.filter((b, i) => i !== draggedBlock.idx));
        }
        setDraggedBlock(null);
    };
    const moveBlockInUserBlocks = (fromIdx, toIdx) => {
        if (fromIdx === toIdx) return;
        const updated = [...userBlocks];
        const [removed] = updated.splice(fromIdx, 1);
        updated.splice(toIdx, 0, removed);
        setUserBlocks(updated);
    };

    // ===== handleSubmit: Показывает модалку с результатом SQL после submit, не обновляет страницу =====
    const handleSubmit = async (e) => {
        e?.preventDefault(); // Prevent form submission from refreshing the page
        let userSql = "";
        if (currentTask.taskInteractionType === "DRAG_AND_DROP") {
            userSql = userBlocks.join(" ").replace(/\s+/g, " ").trim();
        } else if (currentTask.taskInteractionType === "INCORRECT_SQL") {
            userSql = answer.trim();
        } else {
            userSql = answer.trim();
        }
        userSql = userSql.replace(/;+$/g, "").trim();

        try {
            const response = await validateSql({
                userSql,
                taskCode: currentTask.taskCode
            }).unwrap();

            setSubmissionStatus(response.correct ? "correct" : "incorrect");

            // Показываем модалку с результатом, включая userSql
            setResultModal({
                open: true,
                correct: response.correct,
                userResult: response.userResult || null,
                userSql: userSql,
            });

            console.log("handleSubmit response:", response);
        } catch (error) {
            console.error("handleSubmit error:", error);
            setSubmissionStatus("error");
            setResultModal({
                open: true,
                correct: false,
                userResult: null,
                userSql: userSql,
            });
        }
    };

    // ===== Закрыть модалку, обработать переход и завершение =====
    const handleResultOk = async () => {
        console.log("handleResultOk called", { correct: resultModal.correct, difficulty, topicName });

        // Close the modal first
        setResultModal((prev) => ({ ...prev, open: false }));

        if (resultModal.correct) {
            if (!difficulty || !topicName) {
                console.error("Invalid difficulty or topicName", { difficulty, topicName });
                toast.error(t("invalidUrl") || "Invalid URL parameters");
                return;
            }
            try {
                setIsCompleted(true);
                await Promise.all([
                    refetch(),
                    dispatch(initializeAuth()),
                    refetchTasksList()
                ]);
                const targetUrl = `/level/${difficulty}/topic/${encodeURIComponent(topicName)}`;
                console.log("Navigating to:", targetUrl);
                navigate(targetUrl);
            } catch (error) {
                console.error("Error in handleResultOk:", error);
                toast.error(t("navigationError") || "Failed to navigate to task list");
            }
        } else {
            console.log("Not navigating: result is not correct");
        }
    };

    const handleReset = () => {
        setAnswer(currentTask.taskInteractionType === "INCORRECT_SQL" ? (currentTask.wrongQuery || "") : "");
        if (currentTask.taskInteractionType === "DRAG_AND_DROP" && Array.isArray(currentTask.dragAndDropQuery)) {
            setAvailableBlocks(currentTask.dragAndDropQuery);
            setUserBlocks([]);
        }
        setSubmissionStatus(null);
    };
    const handleShowHint = () => setShowHint(true);
    const handleCloseHint = () => setShowHint(false);

    // Модалка с результатом SQL
    function renderResultModal() {
        if (!resultModal.open) return null;

        return (
            <motion.div
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                exit={{ opacity: 0 }}
                className="fixed inset-0 z-50 flex items-center justify-center bg-black/50"
                onClick={handleResultOk}
            >
                <motion.div
                    initial={{ scale: 0.96, y: 14, opacity: 0 }}
                    animate={{ scale: 1, y: 0, opacity: 1 }}
                    exit={{ scale: 0.97, y: 12, opacity: 0 }}
                    transition={{ type: "spring", stiffness: 210, damping: 22 }}
                    className="bg-[var(--color-card-bg)] p-8 rounded-2xl shadow-2xl w-full max-w-xl relative"
                    onClick={e => e.stopPropagation()}
                >
                    <h2 className={`text-2xl font-bold mb-5 custom-font ${resultModal.correct ? "text-green-600" : "text-red-600"}`}>
                        {resultModal.correct ? t("correctAnswer") : t("incorrectAnswer")}
                    </h2>
                    {resultModal.correct && resultModal.userResult && Array.isArray(resultModal.userResult) && (
                        <div className="overflow-x-auto max-h-64">
                            <ResultTable data={resultModal.userResult} />
                        </div>
                    )}
                    {!resultModal.correct && resultModal.userSql === "" && (
                        <div className="text-gray-400">{t("sqlExecutionError") || "SQL konnte nicht gestartet werden oder es ist ein Fehler aufgetreten"}</div>
                    )}
                    {!resultModal.correct && resultModal.userSql !== "" && !resultModal.userResult && (
                        <div className="text-gray-400">{t("noSqlResult") || "Kein Ergebnis."}</div>
                    )}
                    {!resultModal.correct && resultModal.userResult && Array.isArray(resultModal.userResult) && (
                        <div className="overflow-x-auto max-h-64">
                            <ResultTable data={resultModal.userResult} />
                        </div>
                    )}
                    <button
                        className={`mt-6 w-full py-3 rounded-xl font-bold text-lg shadow ${
                            resultModal.correct
                                ? "bg-gradient-to-r from-green-400 to-cyan-400 text-white"
                                : "bg-gray-300 text-gray-700"
                        }`}
                        onClick={handleResultOk}
                    >
                        {resultModal.correct ? t("completeTask") : t("returnToTask")}
                    </button>
                </motion.div>
            </motion.div>
        );
    }

    // Таблица результата SQL
    function ResultTable({ data }) {
        if (!data.length) return null;
        const columns = Object.keys(data[0]);
        return (
            <table className="min-w-[240px] border border-gray-500 rounded-xl mb-2 text-base">
                <thead>
                <tr>
                    {columns.map((col, i) => (
                        <th key={i} className="border px-3 py-2 font-bold bg-gray-50 dark:bg-gray-800 text-left">
                            {col}
                        </th>
                    ))}
                </tr>
                </thead>
                <tbody>
                {data.map((row, i) => (
                    <tr key={i}>
                        {columns.map((col, j) => (
                            <td key={j} className="border px-3 py-2">
                                {row[col]}
                            </td>
                        ))}
                    </tr>
                ))}
                </tbody>
            </table>
        );
    }

    // Таблица задачи
    function renderTaskTable(task) {
        if (!task.tableName || !Array.isArray(task.tableData) || !task.tableData.length) {
            return <p>{t("noTablesAvailable")}</p>;
        }
        const columns = Object.keys(task.tableData[0]);
        return (
            <div>
                <h3 className="text-base font-semibold mb-1 custom-font text-[var(--color-primary)]">{task.tableName}</h3>
                <div className="overflow-auto rounded-lg shadow">
                    <table className="border-collapse border border-gray-600 dark:border-gray-700 min-w-[220px] text-sm">
                        <thead>
                        <tr>
                            {columns.map((col, i) => (
                                <th key={i} className="border px-3 py-2 bg-[var(--color-background)] font-semibold custom-font text-[var(--color-primary)]">{col}</th>
                            ))}
                        </tr>
                        </thead>
                        <tbody>
                        {task.tableData.map((row, i) => (
                            <tr key={i}>
                                {columns.map((col, j) => (
                                    <td key={j} className="border px-3 py-2 custom-font text-[var(--color-secondary)]">
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
    }

    const fadeUp = {
        hidden: { opacity: 0, y: 32 },
        visible: (i = 1) => ({
            opacity: 1,
            y: 0,
            transition: { delay: i * 0.12, duration: 0.7, ease: "easeOut" },
        }),
    };

    // Drag and drop UI
    const renderDragAndDrop = () => (
        <div className="flex flex-col gap-4 mb-3">
            <div
                className="min-h-[62px] bg-blue-50 dark:bg-blue-950 rounded-xl p-3 flex flex-wrap gap-4 items-center border-2 border-dashed border-blue-200 dark:border-blue-800"
                onDragOver={e => { e.preventDefault(); }}
                onDrop={onDropToAvailable}
            >
                {availableBlocks.length === 0 && (
                    <span className="text-blue-300 italic">{t("dragDropBlockPool")}</span>
                )}
                {availableBlocks.map((block, idx) => (
                    <motion.div
                        key={block + idx}
                        className="cursor-grab py-2 px-5 bg-blue-200 dark:bg-blue-800/90 text-blue-950 dark:text-blue-50 rounded-xl shadow font-mono text-xl font-bold transition-all min-w-[60px] min-h-[48px] flex items-center justify-center"
                        draggable={!solved}
                        onDragStart={() => onDragStart(block, "available", idx)}
                        whileTap={{ scale: 0.98 }}
                    >
                        {block}
                    </motion.div>
                ))}
            </div>
            <div
                className="min-h-[72px] bg-green-50 dark:bg-green-950 border-2 border-dashed border-green-300 dark:border-green-800 rounded-xl p-3 flex flex-wrap gap-4 items-center"
                ref={dropInputRef}
                onDragOver={e => { e.preventDefault(); }}
                onDrop={onDropToInput}
            >
                {userBlocks.length === 0 && (
                    <span className="text-green-300 italic">{t("dragDropAnswerZone")}</span>
                )}
                {userBlocks.map((block, idx) => (
                    <motion.div
                        key={block + idx}
                        className="cursor-grab py-2 px-5 bg-green-200 dark:bg-green-800/90 text-green-950 dark:text-green-50 rounded-xl shadow font-mono text-xl font-bold transition-all min-w-[60px] min-h-[48px] flex items-center justify-center"
                        draggable={!solved}
                        onDragStart={() => onDragStart(block, "input", idx)}
                        onDragOver={e => {
                            e.preventDefault();
                        }}
                        onDrop={e => {
                            if (!draggedBlock || draggedBlock.origin !== "input") return;
                            moveBlockInUserBlocks(draggedBlock.idx, idx);
                            setDraggedBlock(null);
                        }}
                        whileTap={{ scale: 0.98 }}
                    >
                        {block}
                    </motion.div>
                ))}
            </div>
            <input
                className="w-full p-4 border rounded-lg font-mono mt-2 text-lg bg-[var(--color-background)] text-[var(--color-primary)]"
                value={userBlocks.join(" ")}
                readOnly
            />
        </div>
    );

    // UI variants based on task type
    let answerBox;
    if (currentTask?.taskInteractionType === "DRAG_AND_DROP") {
        answerBox = (
            <>
                <label className="font-semibold mb-2 block text-lg">{t("dragBlocksArrange")}</label>
                {renderDragAndDrop()}
                <div className="text-base text-[var(--color-secondary)] mb-2">
                    {t("dragDropInstruction")}
                </div>
            </>
        );
    } else if (currentTask?.taskInteractionType === "INCORRECT_SQL") {
        answerBox = (
            <>
                <label className="font-semibold mb-2 block">{t("editSqlWithError")}</label>
                <textarea
                    value={answer}
                    onChange={e => setAnswer(e.target.value)}
                    className="w-full h-36 p-4 border-2 border-blue-100 dark:border-gray-700 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400 font-mono text-base transition bg-[var(--color-background)] text-[var(--color-primary)]"
                    disabled={solved}
                />
            </>
        );
    } else {
        answerBox = (
            <>
                <label className="font-semibold mb-2 block">{t("sqlInputLabel")}</label>
                <textarea
                    value={answer}
                    onChange={e => setAnswer(e.target.value)}
                    className="w-full h-36 p-4 border-2 border-blue-100 dark:border-gray-700 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400 font-mono text-base transition bg-[var(--color-background)] text-[var(--color-primary)]"
                    disabled={solved}
                />
            </>
        );
    }

    // Loader/error UI
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

    return (
        <div className="min-h-screen bg-custom-background custom-font flex flex-col relative">
            {/* Модалка результата SQL */}
            {renderResultModal()}

            {/* Hint modal */}
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
                            onClick={e => e.stopPropagation()}
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
            {/* Main content */}
            <div className="flex-grow flex items-center justify-center p-4 md:p-8 relative z-10">
                <motion.div
                    initial="hidden"
                    animate="visible"
                    variants={{
                        hidden: { opacity: 0, y: 32 },
                        visible: { opacity: 1, y: 0, transition: { staggerChildren: 0.11, delayChildren: 0.13 } }
                    }}
                    custom={1}
                    className="w-full max-w-6xl flex flex-col md:flex-row gap-8 md:gap-12 justify-center items-stretch"
                >
                    {/* Left column */}
                    <motion.div
                        variants={fadeUp}
                        custom={2}
                        className="flex-1 md:max-w-[720px] bg-[var(--color-card-bg)] rounded-2xl shadow-xl p-8 mb-8 md:mb-0"
                        style={{
                            minWidth: 0,
                            color: "var(--color-primary)"
                        }}
                    >
                        <h2 className="text-2xl font-extrabold mb-4 flex items-center custom-font" style={{ color: "var(--color-primary)" }}>
                            <span className="mr-2">📝</span> {t("taskDescription")}
                            {solved && (
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

                    {/* Right column */}
                    <div className="flex flex-col gap-6 md:w-[380px] min-w-[320px] flex-shrink-0">
                        {/* Task info + buttons */}
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
                                {currentTask.points && (
                                    <span>
                                        <b>{t("points")}:</b> {currentTask.points}
                                    </span>
                                )}
                            </div>
                            <div className="flex gap-3 mt-3">
                                <button
                                    onClick={handleReset}
                                    className="bg-gray-300 dark:bg-gray-700 text-black dark:text-gray-100 py-2 px-5 rounded-xl hover:bg-gray-400 dark:hover:bg-gray-600 transition font-semibold custom-font"
                                    disabled={solved}
                                >
                                    {t("reset")}
                                </button>
                                <button
                                    onClick={handleShowHint}
                                    className="bg-gradient-to-r from-green-400 to-cyan-400 text-white py-2 px-5 rounded-xl hover:from-green-500 hover:to-green-600 font-semibold shadow transition flex items-center gap-2 custom-font"
                                    disabled={solved}
                                >
                                    <span>💡</span> {t("hint")}
                                </button>
                            </div>
                        </motion.div>
                        {/* Answer box */}
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
                            {answerBox}
                            <button
                                type="button" // Explicitly set to prevent form submission
                                onClick={handleSubmit}
                                className={`mt-4 w-full bg-gradient-to-r from-green-500 to-cyan-400 text-white py-3 rounded-xl font-bold text-lg shadow-lg hover:from-green-400 hover:to-green-600 transition custom-font ${
                                    solved || isValidating ? "opacity-60 cursor-not-allowed" : ""
                                }`}
                                disabled={solved || isValidating}
                            >
                                {isValidating ? t("submitting") : t("submit")}
                            </button>
                            {submissionStatus && (
                                <div
                                    className={`mt-4 text-center text-lg font-semibold custom-font ${
                                        submissionStatus === "correct"
                                            ? "text-green-600"
                                            : submissionStatus === "incorrect"
                                                ? "text-red-600"
                                                : "text-red-600"
                                    }`}
                                >
                                    {submissionStatus === "correct"
                                        ? t("correctAnswer")
                                        : submissionStatus === "incorrect"
                                            ? t("incorrectAnswer")
                                            : t("submissionError")}
                                </div>
                            )}
                        </motion.div>
                    </div>
                </motion.div>
            </div>
        </div>
    );
}