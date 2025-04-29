import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { useTranslation } from 'react-i18next';
import { toast } from 'sonner';
import mockTasks from '../../data/mockTasks.js';

export default function Task() {
    const { difficulty, taskId } = useParams();
    const { t } = useTranslation();
    const navigate = useNavigate();
    const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);

    // Проверяем авторизацию
    if (!isAuthenticated) {
        navigate('/login');
        return null;
    }

    // Получаем данные задачи
    const levelData = mockTasks[difficulty.toLowerCase()];
    if (!levelData) {
        return (
            <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100 font-mono">
                <h1 className="text-4xl font-bold text-black uppercase">
                    {t('levelNotFound')}
                </h1>
            </div>
        );
    }

    const task = levelData.regularTasks.find((t) => t.id === parseInt(taskId));
    if (!task) {
        return (
            <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100 font-mono">
                <h1 className="text-4xl font-bold text-black uppercase">
                    {t('taskNotFound')}
                </h1>
            </div>
        );
    }

    const [answer, setAnswer] = useState('');
    const [showHint, setShowHint] = useState(false);
    const [isCompleted, setIsCompleted] = useState(false);

    // Проверка ответа
    const handleSubmit = () => {
        if (answer.trim().toUpperCase() === task.correctAnswer.toUpperCase()) {
            toast.success(t('correctAnswer'));
            setIsCompleted(true);
            setTimeout(() => navigate(`/level/${difficulty}`), 1000);
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

    return (
        <div className="min-h-screen bg-gray-100 font-mono flex flex-col">
            {/* Модальное окно с подсказкой */}
            {showHint && (
                <div
                    className="fixed inset-0 bg-black bg-opacity-20 bg flex items-center justify-center z-50"
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
                        <p className="text-gray-700">{task.hint}</p>
                    </div>
                </div>
            )}

            <div className="flex-grow flex flex-col items-center justify-center p-4">
                <div className="flex w-full max-w-5xl space-x-4">
                    {/* Левая часть: Описание задачи */}
                    <div className="flex-1 bg-gray-200 p-6 rounded-lg shadow-md">
                        <h2 className="text-xl font-bold mb-4">{t('taskDescription')}</h2>
                        <p>{task.description}</p>
                    </div>

                    {/* Правая часть: Информация о задаче и поле для ответа */}
                    <div className="flex-1 flex flex-col space-y-4">
                        <div className="bg-gray-200 p-6 rounded-lg shadow-md">
                            <p className="text-lg">
                                <span className="font-bold">{t('name')}:</span> {task.name}
                            </p>
                            <p className="text-lg">
                                <span className="font-bold">{t('point')}:</span> {task.points}
                            </p>
                            <p className="text-lg">
                                <span className="font-bold">{t('topic')}:</span> {task.topic}
                            </p>
                            <div className="flex space-x-2 mt-4">
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
                                disabled={isCompleted}
                            />
                        </div>

                        {/* Кнопка отправки ответа */}
                        <button
                            onClick={handleSubmit}
                            className="bg-green-500 text-white py-2 px-4 rounded-lg hover:bg-green-600 transition mt-4"
                            disabled={isCompleted}
                        >
                            {t('submit')}
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}