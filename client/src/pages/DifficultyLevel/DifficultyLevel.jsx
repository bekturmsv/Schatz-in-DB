import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { getUser } from '../../data/mockUser';

export default function DifficultyLevel() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const user = getUser();

    const handleDifficultySelect = (difficulty) => {
        navigate(`/level/${difficulty.toLowerCase()}`);
    };

    const difficulties = [
        { name: 'Easy', label: t('easy'), key: 'easy' },
        { name: 'Medium', label: t('medium'), key: 'medium' },
        { name: 'Hard', label: t('hard'), key: 'hard' },
    ];

    return (
        <div className="min-h-screen flex flex-col items-center justify-center  font-mono">
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
                {difficulties.map((diff) => (
                    <button
                        key={diff.key}
                        onClick={() => handleDifficultySelect(diff.name)}
                        className={`relative bg-gray-300 text-black py-4 px-8 rounded-lg text-xl uppercase hover:bg-gray-400 transition ${
                            user.completedLevels[diff.key] ? 'bg-green-400 hover:bg-green-500' : ''
                        }`}
                    >
                        {diff.label}
                        {user.completedLevels[diff.key] && (
                            <span className="absolute right-2 top-1/2 transform -translate-y-1/2">
                ✅
              </span>
                        )}
                    </button>
                ))}
            </div>
        </div>
    );
}