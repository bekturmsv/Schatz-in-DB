import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import { useTranslation } from 'react-i18next';
import { toast } from 'sonner';
import { completeLevel } from '../../features/auth/authSlice.js';
import mockTasks from '../../data/mockTasks.js';

export default function FinalTest() {
    const { difficulty } = useParams();
    const { t } = useTranslation();
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);

    // Проверяем авторизацию
    if (!isAuthenticated) {
        navigate('/login');
        return null;
    }

    // Получаем данные теста
    const levelData = mockTasks[difficulty.toLowerCase()];
    if (!levelData || !levelData.finalTest.tasks.length) {
        return (
            <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100 font-mono">
                <h1 className="text-4xl font-bold text-black uppercase">
                    {t('testNotFound')}
                </h1>
            </div>
        );
    }

    const tasks = levelData.finalTest.tasks;
    const [currentTaskIndex, setCurrentTaskIndex] = useState(0);
    const [completedTasks, setCompletedTasks] = useState([]);
    const [answer, setAnswer] = useState('');
    const [showHint, setShowHint] = useState(false);
    const [timer, setTimer] = useState(0);
    const [isTimerRunning, setIsTimerRunning] = useState(true);

    // Таймер
    useEffect(() => {
        let interval;
        if (isTimerRunning) {
            interval = setInterval(() => {
                setTimer((prev) => prev + 1);
            }, 1000);
        }
        return () => clearInterval(interval);
    }, [isTimerRunning]);

    // Форматирование времени
    const formatTime = (seconds) => {
        const mins = Math.floor(seconds / 60);
        const secs = seconds % 60;
        return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
    };

    const currentTask = tasks[currentTaskIndex];

    // Проверка ответа
    const handleSubmit = () => {
        if (answer.trim().toUpperCase() === currentTask.correctAnswer.toUpperCase()) {
            toast.success(t('correctAnswer'));
            setCompletedTasks([...completedTasks, currentTask.id]);

            // Если все задачи завершены
            if (completedTasks.length + 1 === tasks.length) {
                setIsTimerRunning(false);
                toast.success(t('levelCompleted', { time: formatTime(timer) }));
                dispatch(completeLevel({ difficulty: difficulty.toLowerCase() }));
                setTimeout(() => navigate('/play'), 2000);
            } else {
                setCurrentTaskIndex((prev) => (prev + 1) % tasks.length);
                setAnswer('');
            }
        } else {
            toast.error(t('incorrectAnswer'));
        }
    };

    // Сброс ответа
    const handleReset = () => {
        setAnswer('');
    };

    // Показать подсказку
    const handleShowHint = () => {
        setShowHint(true);
    };

    // Закрыть подсказку
    const handleCloseHint = () => {
        setShowHint(false);
    };

    // Переключение задач
    const handleNext = () => {
        setCurrentTaskIndex((prev) => (prev + 1) % tasks.length);
        setAnswer('');
    };

    const handlePrevious = () => {
        setCurrentTaskIndex((prev) => (prev - 1 + tasks.length) % tasks.length);
        setAnswer('');
    };

    return (
        <div className="min-h-screen bg-gray-100 font-mono flex flex-col">
            {/* Модальное окно с подсказкой */}
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
                        <h3 className="text-xl font-bold mb-4">{t('hint')}</h3>
                        <p className="text-gray-700">{currentTask.hint}</p>
                    </div>
                </div>
            )}

            <div className="flex-grow flex flex-col items-center justify-center p-4">
                <div className="flex w-full max-w-5xl space-x-4">
                    {/* Левая часть: Описание задачи */}
                    <div className="flex-1 bg-gray-200 p-6 rounded-lg shadow-md">
                        <h2 className="text-xl font-bold mb-4">{t('taskDescription')}</h2>
                        <p>{currentTask.description}</p>
                    </div>

                    {/* Правая часть: Информация о задаче и поле для ответа */}
                    <div className="flex-1 flex flex-col space-y-4">
                        <div className="bg-gray-200 p-6 rounded-lg shadow-md">
                            <h2 className="text-xl font-bold mb-2">{t('finalTest')}</h2>
                            <div className="flex justify-between items-center mb-4">
                                <p className="text-lg">
                                    {currentTaskIndex + 1} / {tasks.length}
                                </p>
                                <p className="text-lg">
                                    <span className="font-bold">{t('remainingTime')}:</span> {formatTime(timer)}
                                </p>
                            </div>
                            <div className="flex space-x-2 mb-4">
                                <button
                                    onClick={handleReset}
                                    className="bg-gray-300 text-black py-2 px-4 rounded-lg hover:bg-gray-400 transition"
                                >
                                    {t('reset')}
                                </button>
                                <button
                                    onClick={handleShowHint}
                                    className="bg-gray-300 text-black py-2 px-4 rounded-lg hover:bg-gray-400 transition flex items-center"
                                >
                                    <span className="mr-2">?</span> {t('answer')}
                                </button>
                            </div>
                        </div>

                        {/* Поле для ответа */}
                        <div className="bg-gray-200 p-6 rounded-lg shadow-md flex-1">
                            <h2 className="text-xl font-bold mb-4">{t('answerBox')}</h2>
                            <textarea
                                value={answer}
                                onChange={(e) => setAnswer(e.target.value)}
                                className="w-full h-32 p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400"
                                placeholder={t('typeYourQueryHere')}
                            />
                            <div className="flex justify-between mt-4">
                                <button
                                    onClick={handlePrevious}
                                    className="bg-gray-300 text-black py-2 px-4 rounded-lg hover:bg-gray-400 transition"
                                    disabled={currentTaskIndex === 0}
                                >
                                    {t('previous')}
                                </button>
                                <button
                                    onClick={handleNext}
                                    className="bg-gray-300 text-black py-2 px-4 rounded-lg hover:bg-gray-400 transition"
                                    disabled={currentTaskIndex === tasks.length - 1 && completedTasks.length !== tasks.length - 1}
                                >
                                    {t('next')}
                                </button>
                            </div>
                        </div>

                        {/* Кнопка отправки ответа */}
                        <button
                            onClick={handleSubmit}
                            className="bg-green-500 text-white py-2 px-4 rounded-lg hover:bg-green-600 transition mt-4"
                        >
                            {t('submit')}
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}