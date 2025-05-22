import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { getUser } from '../../data/mockUser';

export default function DifficultyLevel() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const user = getUser();

    // Динамический массив уровней из user.completedLevels
    const difficulties = Object.keys(user.completedLevels).map(key => ({
        key,
        label: t(key),
        name: key.charAt(0).toUpperCase() + key.slice(1),
        isCompleted: user.completedLevels[key],
    }));

    const handleDifficultySelect = (difficulty, isLocked) => {
        if (!isLocked) {
            navigate(`/level/${difficulty.toLowerCase()}`);
        }
    };

    return (
        <div className="min-h-screen flex flex-col items-center justify-center font-mono">
            <h1 className="text-4xl font-bold mb-8 text-black uppercase">
                {t('difficultyLevel')}
            </h1>
            <div className="w-full max-w-md mb-8">
                <h2 className="text-2xl font-bold mb-4">{t('progress')}</h2>
                <p className="text-lg text-gray-700">
                    {t('totalTasksSolved')}: {user.progress.tasksSolved}/{user.progress.totalTasks}
                </p>
            </div>
            <div className="flex flex-col space-y-4">
                {difficulties.map((diff, idx) => {
                    let isLocked = false;
                    if (idx > 0) {
                        // блокируем, если предыдущий уровень не завершён
                        const prevLevel = difficulties[idx - 1];
                        isLocked = !prevLevel.isCompleted;
                    }

                    return (
                        <button
                            key={diff.key}
                            onClick={() => handleDifficultySelect(diff.name, isLocked)}
                            disabled={isLocked}
                            className={`
                                relative py-4 px-8 rounded-lg text-xl uppercase font-bold transition 
                                flex items-center justify-center
                                border-2
                                ${diff.isCompleted ? 'bg-green-500 border-green-700 text-white' : ''}
                                ${!diff.isCompleted && !isLocked ? 'bg-gray-300 border-gray-400 text-black hover:bg-green-100' : ''}
                                ${isLocked ? 'bg-gray-200 border-gray-300 text-gray-400 cursor-not-allowed' : ''}
                            `}
                        >
                            {diff.label}
                            {isLocked && (
                                <span className="ml-2 text-2xl" title={t('completePreviousLevelFirst')}>🔒</span>
                            )}
                            {diff.isCompleted && (
                                <span className="ml-2 text-2xl" title={t('completed')}>✅</span>
                            )}
                        </button>
                    );
                })}
            </div>
        </div>
    );
}
