// src/features/level/Level.jsx
import { useParams, useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { useTranslation } from 'react-i18next';
import { useGetTopicsQuery } from '../../features/task/taskApi.js';
import Loading from "@/components/custom/Loading.jsx";

export default function Level() {
    const { difficulty } = useParams();
    const { t } = useTranslation();
    const navigate = useNavigate();
    const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);

    const {
        data: topics = [],
        isLoading,
        isError,
    } = useGetTopicsQuery(difficulty, { skip: !difficulty });

    // Редирект на логин, если не в системе
    if (!isAuthenticated) {
        navigate('/login');
        return null;
    }

    // Запрашиваем темы по уровню


    // Логируем то, что пришло от API
    console.log('Level.jsx — topics response:', topics);

    if (isLoading) {
        return <Loading/>;
    }

    if (isError) {
        return (
            <p className="p-4 text-red-600">
                {t('errorLoadingTopics')}
            </p>
        );
    }

    return (
        <div className="min-h-screen font-mono bg-green-200 flex flex-col items-center justify-center">
            <h1 className="text-4xl font-bold text-black uppercase mb-6">
                {t(`${difficulty}Level`)}
            </h1>
            <div className="w-full max-w-md space-y-4">
                {topics.map((topic) => (
                    <div
                        key={topic.id}
                        className="cursor-pointer hover:bg-gray-300 bg-white p-4 rounded-lg shadow"
                        onClick={() => navigate(`/level/${difficulty}/topic/${topic.id}`)}
                    >
                        <h2 className="text-2xl font-semibold">{topic.title}</h2>
                        {topic.description && (
                            <p className="text-gray-700 mt-1">{topic.description}</p>
                        )}
                    </div>
                ))}
            </div>
        </div>
    );
}
