import { useParams, useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { useTranslation } from 'react-i18next';
import mockTasks from '../../data/mockTasks';
import { getUser } from '../../data/mockUser.js';

export default function Level() {
    const { difficulty } = useParams();
    const { t } = useTranslation();
    const navigate = useNavigate();
    const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);

    // Проверяем авторизацию
    if (!isAuthenticated) {
        navigate('/login');
        return null;
    }

    // Получаем данные пользователя
    const user = getUser();
    const completedTasks = user.completedTasks[difficulty.toLowerCase()] || [];
    const isLevelCompleted = user.completedLevels[difficulty.toLowerCase()];
    const userTasks = user.tasks.filter((task) => task.type.toLowerCase() === difficulty.toLowerCase());

    // Получаем данные уровня
    const level = mockTasks[difficulty.toLowerCase()];
    if (!level) {
        return (
            <div className="min-h-screen flex flex-col items-center justify-center bg-green-200 font-mono">
                <h1 className="text-4xl font-bold text-black uppercase">
                    {t('levelNotFound')}
                </h1>
            </div>
        );
    }

    // Проверяем, все ли обычные задачи завершены
    const allRegularTasksCompleted = level.regularTasks.every((task) =>
        completedTasks.includes(task.id)
    );

    const handleTaskClick = (taskId) => {
        navigate(`/level/${difficulty}/task/${taskId}`);
    };

    const handleFinalTestClick = () => {
        if (allRegularTasksCompleted) {
            navigate(`/level/${difficulty}/final-test`);
        }
    };

    return (
        <div className="min-h-screen flex flex-col items-center justify-center bg-green-200 font-mono">
            <h1 className="text-4xl font-bold text-black uppercase mb-4">
                {t(`${difficulty.toLowerCase()}Level`)}
            </h1>
            <p className="text-lg text-gray-700 mb-6">
                {t(`${difficulty.toLowerCase()}Description`)}
            </p>
            <div className="w-full max-w-md">
                <h2 className="text-2xl font-bold mb-4">{t('tasks')}</h2>
                <ul className="space-y-2">
                    {level.regularTasks.map((task) => (
                        <li
                            key={task.id}
                            className="bg-gray-300 p-4 rounded-lg text-black flex justify-between items-center cursor-pointer hover:bg-gray-400 transition"
                            onClick={() => handleTaskClick(task.id)}
                        >
                            <span>{task.name}</span>
                            <span>{completedTasks.includes(task.id) || isLevelCompleted ? '✅' : '⬜'}</span>
                        </li>
                    ))}
                </ul>
                <div className="mt-4">
                    <h3 className="text-xl font-bold">{t('progress')}</h3>
                    {userTasks.map((task, index) => (
                        <p key={index} className="text-gray-700">
                            {task.theme}: {task.completed}/{task.total} {t('tasksCompleted')}
                        </p>
                    ))}
                </div>
                <button
                    onClick={handleFinalTestClick}
                    className={`mt-4 flex items-center justify-center space-x-2 py-2 px-4 rounded-lg transition ${
                        allRegularTasksCompleted && !isLevelCompleted
                            ? 'bg-yellow-500 text-black hover:bg-yellow-600'
                            : 'bg-gray-400 text-gray-600 cursor-not-allowed'
                    }`}
                    disabled={!allRegularTasksCompleted || isLevelCompleted}
                >
                    <span>{t('finalTest')}</span>
                    {!allRegularTasksCompleted && <span>🔒</span>}
                </button>
            </div>
        </div>
    );
}