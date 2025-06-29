import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import { useTranslation } from "react-i18next";
import { toast } from "sonner";
import {
    useGetFinalTestByDifficultyQuery,
    useValidateSqlMutation,
    useValidateFinalTestMutation
} from "../../features/task/taskApi";

// Импортируй хук для получения и обновления профиля!
import { useGetMeQuery } from "@/features/auth/authApi";

export default function FinalTest() {
    const { difficulty } = useParams();
    const { t } = useTranslation();
    const navigate = useNavigate();
    const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);

    const {
        data: tasks = [],
        isLoading,
        isError,
    } = useGetFinalTestByDifficultyQuery(difficulty?.toUpperCase());

    // --- Вот здесь!
    // Инициализируем хук для рефетча профиля пользователя
    const { refetch: refetchProfile } = useGetMeQuery(undefined, { skip: !isAuthenticated });

    const [current, setCurrent] = useState(0);
    const [userAnswers, setUserAnswers] = useState([]);
    const [solved, setSolved] = useState([]);
    const [checking, setChecking] = useState(false);
    const [showHint, setShowHint] = useState(false);
    const [timer, setTimer] = useState(0);
    const [testFinished, setTestFinished] = useState(false);

    const [validateSql] = useValidateSqlMutation();
    const [validateFinalTest] = useValidateFinalTestMutation();

    useEffect(() => {
        setUserAnswers(tasks.map(() => ""));
        setSolved(tasks.map(() => false));
    }, [tasks]);

    useEffect(() => {
        if (testFinished) return;
        const id = setInterval(() => setTimer(t => t + 1), 1000);
        return () => clearInterval(id);
    }, [testFinished]);

    if (!isAuthenticated) {
        navigate("/login");
        return null;
    }

    if (isLoading) {
        return (
            <div className="min-h-screen flex flex-col items-center justify-center font-mono">
                <h1 className="text-3xl text-gray-600">{t("loading")}</h1>
            </div>
        );
    }
    if (isError || !tasks.length) {
        return (
            <div className="min-h-screen flex flex-col items-center justify-center font-mono">
                <h1 className="text-3xl text-red-400">{t("testNotFound")}</h1>
            </div>
        );
    }

    const currentTask = tasks[current];
    const allCorrect = solved.every(Boolean);

    // Проверить ответ
    const handleSubmit = async () => {
        if (!currentTask) return;
        setChecking(true);
        const answer = userAnswers[current].trim();
        try {
            const result = await validateSql({
                userSql: answer,
                taskCode: currentTask.taskCode,
            }).unwrap();
            if (result.correct) {
                toast.success(t("correctAnswer"));
                setSolved((old) => {
                    const arr = [...old];
                    arr[current] = true;
                    return arr;
                });
            } else {
                toast.error(t("incorrectAnswer"));
            }
        } catch (e) {
            toast.error(t("submissionError"));
        }
        setChecking(false);
    };

    // Завершить тест (добавлен рефетч профиля!)
    const handleFinish = async () => {
        setTestFinished(true);
        try {
            await validateFinalTest({
                schwierigkeit: difficulty?.toUpperCase(),
                spentTimeInSeconds: timer,
            }).unwrap();

            // ---- ВАЖНО! ----
            // Ждем обновления профиля (баллы, прогресс и т.д.)
            await refetchProfile();

            toast.success(t("testCompleted"));
            setTimeout(() => {
                // Переход на страницу тем (с поздравлением)
                navigate(`/level/${difficulty}`, {
                    state: { showCongrats: true }
                });
            }, 1800);
        } catch {
            toast.error(t("submissionError"));
        }
    };

    // Helper render table
    function renderTaskTable(task) {
        if (!task.tableName || !Array.isArray(task.tableData) || !task.tableData.length) return null;
        const columns = Object.keys(task.tableData[0]);
        return (
            <div className="my-2">
                <h3 className="text-base font-semibold mb-1">{task.tableName}</h3>
                <div className="overflow-x-auto rounded-lg shadow">
                    <table className="border-collapse border border-gray-400 min-w-[220px] text-sm">
                        <thead>
                        <tr>
                            {columns.map((col, i) => (
                                <th key={i} className="border px-2 py-1 bg-gray-100 font-semibold">{col}</th>
                            ))}
                        </tr>
                        </thead>
                        <tbody>
                        {task.tableData.map((row, i) => (
                            <tr key={i}>
                                {columns.map((col, j) => (
                                    <td key={j} className="border px-2 py-1">{row[col] != null ? row[col] : ""}</td>
                                ))}
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }

    const formatTime = (seconds) => {
        const mins = Math.floor(seconds / 60);
        const secs = seconds % 60;
        return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
    };

    return (
        <div className="min-h-screen bg-custom-background flex flex-col items-center font-mono">
            {showHint && (
                <div
                    className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
                    onClick={() => setShowHint(false)}
                >
                    <div
                        className="bg-white p-6 rounded-lg shadow-lg w-full max-w-md relative"
                        onClick={e => e.stopPropagation()}
                    >
                        <button
                            onClick={() => setShowHint(false)}
                            className="absolute top-2 right-2 text-gray-500 hover:text-gray-700"
                        >
                            ✕
                        </button>
                        <h3 className="text-xl font-bold mb-4">{t('hint')}</h3>
                        <p className="text-gray-700">{currentTask.hint}</p>
                    </div>
                </div>
            )}

            <div className="w-full max-w-4xl mt-8 mb-3 flex flex-col items-center">
                <h1 className="text-3xl font-bold mb-2">{t('finalTest')}</h1>
                <div className="flex gap-6 items-center text-lg font-semibold mb-2">
                    <span>
                        {t('task')} {current + 1}/{tasks.length}
                    </span>
                    <span>
                        ⏰ {t('timer')}: <span className="font-mono">{formatTime(timer)}</span>
                    </span>
                </div>
            </div>

            <div className="flex-grow flex flex-col items-center w-full">
                <div className="w-full max-w-3xl bg-white rounded-xl shadow-lg p-6 mb-6 flex flex-col gap-3">
                    <div className="text-lg font-bold mb-2">{t('taskDescription')}</div>
                    <div className="mb-2 whitespace-pre-line">{currentTask.aufgabe}</div>
                    {renderTaskTable(currentTask)}
                </div>
                <div className="w-full max-w-3xl bg-white rounded-xl shadow-lg p-6 mb-3 flex flex-col gap-3">
                    <div className="flex gap-2 mb-2">
                        <button
                            onClick={() => setShowHint(true)}
                            className="bg-yellow-200 hover:bg-yellow-300 text-gray-900 px-3 py-1 rounded font-bold"
                        >
                            💡 {t('hint')}
                        </button>
                        <button
                            onClick={() => setUserAnswers(a => {
                                const arr = [...a];
                                arr[current] = '';
                                return arr;
                            })}
                            className="bg-gray-200 hover:bg-gray-300 text-gray-700 px-3 py-1 rounded font-bold"
                            disabled={solved[current]}
                        >
                            {t('reset')}
                        </button>
                    </div>
                    <textarea
                        value={userAnswers[current]}
                        disabled={solved[current]}
                        onChange={e => setUserAnswers(a => {
                            const arr = [...a];
                            arr[current] = e.target.value;
                            return arr;
                        })}
                        className="w-full h-28 p-2 border rounded font-mono"
                        placeholder={t('typeYourQueryHere')}
                    />
                    <div className="flex gap-2">
                        <button
                            onClick={handleSubmit}
                            disabled={checking || solved[current]}
                            className={`bg-green-500 text-white px-6 py-2 rounded font-bold hover:bg-green-600 transition ${solved[current] ? "opacity-60" : ""}`}
                        >
                            {solved[current] ? t("solved") : checking ? t("checking") : t("check")}
                        </button>
                        <button
                            onClick={() => setCurrent(c => c - 1)}
                            disabled={current === 0}
                            className="bg-gray-200 hover:bg-gray-300 px-3 py-1 rounded font-bold"
                        >
                            {t('previous')}
                        </button>
                        <button
                            onClick={() => setCurrent(c => c + 1)}
                            disabled={current === tasks.length - 1}
                            className="bg-gray-200 hover:bg-gray-300 px-3 py-1 rounded font-bold"
                        >
                            {t('next')}
                        </button>
                    </div>
                    <div className="mt-2 text-green-700 font-bold text-lg">
                        {solved[current] && t('correctAnswer')}
                    </div>
                </div>
                <div className="w-full max-w-3xl flex flex-col items-end mb-8">
                    <button
                        onClick={handleFinish}
                        className={`bg-gradient-to-r from-green-500 to-blue-500 text-white px-8 py-3 rounded-xl font-bold text-xl shadow-xl mt-3 transition ${
                            !allCorrect || testFinished ? "opacity-50 cursor-not-allowed" : ""
                        }`}
                        disabled={!allCorrect || testFinished}
                    >
                        🏁 {t('finishTest')}
                    </button>
                </div>
            </div>
        </div>
    );
}
