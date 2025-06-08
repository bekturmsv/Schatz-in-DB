// src/features/topic/Topic.jsx
import { useParams, useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { useTranslation } from 'react-i18next';
import { useGetTopicsQuery } from '../../features/task/taskApi.js';

export default function Topic() {
    const { difficulty } = useParams();
    const level = difficulty?.toUpperCase();
    const { t } = useTranslation();
    const navigate = useNavigate();
    const isAuthenticated = useSelector((s) => s.auth.isAuthenticated);

    // Запрашиваем данные по level
    const {
        data: response = {},
        isLoading,
        isError,
    } = useGetTopicsQuery(level, { skip: !difficulty });

    // Если не авторизован — редирект на логин
    if (!isAuthenticated) {
        navigate('/login');
        return null;
    }

    // Выбираем список тем из поля `tasks`
    const topics = response.tasks ?? [];


    if (isLoading) {
        return <p className="p-4">{t('loadingTopics')}</p>;
    }
    if (isError) {
        return <p className="p-4 text-red-600">{t('errorLoadingTopics')}</p>;
    }

    return (
        <div className="min-h-screen font-mono bg-green-100 flex flex-col items-center justify-center">
            <h1 className="text-4xl font-bold text-black uppercase mb-6">
                {t('selectTopic')} ({t(`${difficulty.toLowerCase()}Level`)})
            </h1>
            <ul className="w-full max-w-md space-y-2">
                {topics.map((topic) => (
                    <li
                        key={topic.name}
                        className="bg-white p-4 rounded-lg shadow cursor-pointer hover:bg-gray-50"
                        onClick={() =>
                            navigate(`/level/${level}/topic/${encodeURIComponent(topic.name)}`)
                        }
                    >
                        <h2 className="text-xl font-medium">{topic.name}</h2>
                    </li>
                ))}
            </ul>
        </div>
    );
}
