import { useParams, useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { useTranslation } from 'react-i18next';
import { useGetTasksByTopicQuery } from '../../features/task/taskApi.js';

export default function TasksList() {
    const { difficulty, topicName } = useParams();
    const level = difficulty?.toUpperCase();
    const { t } = useTranslation();
    const navigate = useNavigate();
    const isAuthenticated = useSelector((s) => s.auth.isAuthenticated);

    // Fetch tasks for the specific difficulty and topic
    const {
        data: response = {},
        isLoading,
        isError,
    } = useGetTasksByTopicQuery(
        { difficulty: level, topicName: decodeURIComponent(topicName) },
        { skip: !difficulty || !topicName || !isAuthenticated }
    );

    if (!isAuthenticated) {
        navigate('/login');
        return null;
    }



    const tasks = response.tasksNotCompleted ?? [];

    console.log('TasksList.jsx — tasks response:', response);

    if (isLoading) {
        return <p className="p-4">{t('loadingTasks')}</p>;
    }
    if (isError) {
        return <p className="p-4 text-red-600">{t('errorLoadingTasks')}</p>;
    }

    return (
        <div className="min-h-screen font-mono bg-green-100 flex flex-col items-center justify-center">
            <h1 className="text-4xl font-bold text-black uppercase mb-6">
                {t('tasksForTopic', { topic: decodeURIComponent(topicName) })} ({t(`${difficulty.toLowerCase()}Level`)})
            </h1>
            <ul className="w-full max-w-md space-y-2">
                {tasks.length > 0 ? (
                    tasks.map((task) => (
                        <li
                            key={task.id}
                            className="bg-white p-4 rounded-lg shadow cursor-pointer hover:bg-gray-50"
                            onClick={() =>
                                navigate(
                                    `/level/${level}/topic/${encodeURIComponent(
                                        topicName
                                    )}/task/${task.id}`
                                )
                            }
                        >
                            <h2 className="text-xl font-medium">{task.title}</h2>
                            <p className="text-gray-600">{task.description}</p>
                        </li>
                    ))
                ) : (
                    <p className="text-gray-600">{t('noTasksAvailable')}</p>
                )}
            </ul>
        </div>
    );
}