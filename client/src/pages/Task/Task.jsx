import { useState } from "react";
import { useParams, useNavigate, useLocation } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { useTranslation } from "react-i18next";
import { toast } from "sonner";
import { useGetTaskByIdQuery, useSubmitTaskAnswerMutation } from "../../features/task/taskApi";
import { setUser } from "../../features/auth/authSlice";
import { executeSqlQuery } from "../../utils/SqlExecutor";

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

    const { data: currentTask, isLoading, isError, error } = useGetTaskByIdQuery(
        { difficulty: difficulty.toUpperCase(), topicName: topicName || fallbackTopicName, taskId: parseInt(taskId) },
        {
            skip: !taskId,
        }
    );

    if (isLoading) {
        return (
            <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100 font-mono">
                <h1 className="text-4xl font-bold text-black uppercase">{t("loading")}</h1>
            </div>
        );
    }

    if (isError) {
        return (
            <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100 font-mono">
                <h1 className="text-4xl font-bold text-black uppercase">{t("error")}</h1>
                <p className="text-lg text-gray-700 mt-2">{t("failedToLoadTask")}</p>
            </div>
        );
    }

    if (!currentTask) {
        return (
            <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100 font-mono">
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
                            console.log(`Invalid table format for ${tableName}:`, table);
                            return <p key={index}>{t("invalidTableFormat", { tableName })}</p>;
                        }
                        const columns = table.length > 0 ? Object.keys(table[0]) : [];
                        const rows = table.map((item) => columns.map((col) => item[col] || ""));
                        return (
                            <div key={index} className="mb-6">
                                <h3 className="text-lg font-bold mb-2">{tableName}</h3>
                                <table className="border-collapse border border-gray-300">
                                    <thead>
                                    <tr>
                                        {columns.map((col, colIndex) => (
                                            <th key={colIndex} className="border p-2">
                                                {col}
                                            </th>
                                        ))}
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {rows.map((row, rowIndex) => (
                                        <tr key={rowIndex}>
                                            {row.map((cell, cellIndex) => (
                                                <td key={cellIndex} className="border p-2">
                                                    {cell !== undefined ? cell : ""}
                                                </td>
                                            ))}
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            </div>
                        );
                    })}
                </div>
            );
        } catch (error) {
            console.error("Error parsing sampleData JSON:", error);
            return <p>{t("errorLoadingTables")}</p>;
        }
    };

    const handleSubmit = async () => {
        toast.success("Gut")

        try {
            // Выполняем SQL-запрос на фронте
            const result = await executeSqlQuery(answer, currentTask.sampleData);
            console.log("SQL Query Result:", result);

            // Отправляем результат на бэкенд через RTK Query
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
            console.error("Error during submission:", error);
            if (error.message === 'Invalid SQL syntax') {
                toast.error(t("invalidSqlSyntax"));
            } else if (error.message === 'SQL query or sample data is missing') {
                toast.error(t("missingQueryOrData"));
            } else {
                toast.error(t("submissionError"));
            }
        }

    };

    const handleReset = () => {
        setAnswer("");
    };

    const handleShowHint = () => {
        setShowHint(true);
    };

    const handleCloseHint = () => {
        setShowHint(false);
    };

    return (
        <div className="min-h-screen bg-gray-100 font-mono flex flex-col">
            {showHint && (
                <div
                    className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
                    onClick={handleCloseHint}
                >
                    <div
                        className="bg-white p-6 rounded-lg shadow-lg w-full max-w-md relative"
                        onClick={(e) => e.stopPropagation()}
                    >
                        <button
                            onClick={handleCloseHint}
                            className="absolute top-2 right-2 text-gray-500 hover:text-gray-700"
                        >
                            ✕
                        </button>
                        <h3 className="text-xl font-bold mb-4">{t("hint")}</h3>
                        <p className="text-gray-700">{currentTask.description}</p>
                    </div>
                </div>
            )}

            <div className="flex-grow flex flex-col items-center justify-center p-4">
                <div className="flex w-full max-w-5xl space-x-4">
                    <div className="flex-1 bg-gray-200 p-6 rounded-lg shadow-md">
                        <h2 className="text-xl font-bold mb-4">{t("taskDescription")}</h2>
                        <p>{currentTask.description}</p>
                        {jsonToTables(currentTask)}
                    </div>

                    <div className="flex-1 flex flex-col space-y-4">
                        <div className="bg-gray-200 p-6 rounded-lg shadow-md">
                            <p className="text-lg">
                                <span className="font-bold">{t("name")}:</span> {currentTask.title}
                            </p>
                            <p className="text-lg">
                                <span className="font-bold">{t("point")}:</span> {currentTask.points}
                            </p>
                            <p className="text-lg">
                                <span className="font-bold">{t("topic")}:</span> {currentTask.topicName}
                            </p>
                            <div className="flex space-x-2 mt-4">
                                <button
                                    onClick={handleReset}
                                    className="bg-gray-300 text-black py-2 px-4 rounded-lg hover:bg-gray-400 transition"
                                >
                                    {t("reset")}
                                </button>
                                <button
                                    onClick={handleShowHint}
                                    className="bg-gray-300 text-black py-2 px-4 rounded-lg hover:bg-gray-400 transition flex items-center"
                                >
                                    <span className="mr-2">?</span> {t("answer")}
                                </button>
                            </div>
                        </div>

                        <div className="bg-gray-200 p-6 rounded-lg shadow-md flex-1">
                            <h2 className="text-xl font-bold mb-4">{t("answerBox")}</h2>
                            <textarea
                                value={answer}
                                onChange={(e) => setAnswer(e.target.value)}
                                className="w-full h-32 p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400"
                                placeholder={t("typeYourSqlQueryHere")}
                                disabled={isCompleted}
                            />
                        </div>

                        <button
                            onClick={handleSubmit}
                            className="bg-green-500 text-white py-2 px-4 rounded-lg hover:bg-green-600 transition mt-4"
                            disabled={isCompleted}
                        >
                            {t("submit")}
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}