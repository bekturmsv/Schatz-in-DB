import { useParams, useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { useTranslation } from 'react-i18next';
import mockTasks from '../../data/mockTasks.js';

export default function Level() {
  const { difficulty } = useParams();
  const { t } = useTranslation();
  const navigate = useNavigate();
  const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);
  const completedLevels = useSelector((state) => state.auth.completedLevels);

  // Проверяем авторизацию
  if (!isAuthenticated) {
    navigate('/login');
    return null;
  }

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

  const isLevelCompleted = completedLevels[difficulty.toLowerCase()];
  const allRegularTasksCompleted = level.regularTasks.every((task) =>
      level.regularTasks.some((t) => t.id === task.id)
  );

  const handleTaskClick = (taskId) => {
    navigate(`/level/${difficulty}/task/${taskId}`);
  };

  const handleFinalTestClick = () => {
    navigate(`/level/${difficulty}/final-test`);
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
                  <span>{isLevelCompleted ? '✅' : '⬜'}</span>
                </li>
            ))}
          </ul>
          {allRegularTasksCompleted && !isLevelCompleted && (
              <button
                  onClick={handleFinalTestClick}
                  className="mt-4 bg-yellow-500 text-black py-2 px-4 rounded-lg hover:bg-yellow-600 transition"
              >
                {t('finalTest')}
              </button>
          )}
        </div>
      </div>
  );
}