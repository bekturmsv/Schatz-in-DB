import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { topics } from "../../data/mockTopics";

export default function Topics() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const isAuthenticated = true; // Замените на useSelector для реальной проверки

    if (!isAuthenticated) {
        navigate("/login");
        return null;
    }

    return (
        <div className="min-h-screen font-mono">
            <section className="container mx-auto py-12 px-4">
                <h1 className="text-4xl font-bold text-blue-600 mb-8">TOPICS</h1>
                <div className="flex flex-col space-y-4">
                    {topics.map((topic) => (
                        <div
                            key={topic.id}
                            className="cursor-pointer hover:bg-gray-200 p-4 rounded"
                            onClick={() => navigate(`/training/${topic.id}`)}
                        >
                            <h2 className="text-xl font-semibold">{topic.title}</h2>
                        </div>
                    ))}
                </div>
            </section>
        </div>
    );
}