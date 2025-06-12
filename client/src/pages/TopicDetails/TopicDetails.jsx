import {useNavigate, useParams} from "react-router-dom";
import { useTranslation } from "react-i18next";
import { useSelector } from "react-redux";
import { useEffect } from "react";
import { topics } from "../../data/mockTopics";

export default function TopicDetail() {
    const { topicId } = useParams();
    const { t } = useTranslation();
    const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);
    const navigate = useNavigate();

    useEffect(() => {
        if (!isAuthenticated) {
            navigate("/login");
        }
    }, [isAuthenticated, navigate]);

    const topic = topics.find((t) => t.id === topicId);
    if (!topic) {
        return <div>{t("topicNotFound")}</div>;
    }

    return (
        <div className="min-h-screen bg-green-200 font-mono">
            <section className="container mx-auto py-12 px-4">
                <div className="bg-white p-6 rounded-lg shadow-md">
                    <h1 className="text-3xl font-bold mb-6">{topic.title}</h1>
                    <div dangerouslySetInnerHTML={{ __html: topic.content }} />
                    {topic.details?.length > 0 && (
                        <div className="mt-8">
                            <h2 className="text-2xl font-bold mb-4">Details</h2>
                            {topic.details.map((detail, index) => (
                                <div key={detail.id} className="mb-6">
                                    <h3 className="text-xl font-semibold mb-2">{detail.title}</h3>
                                    <div dangerouslySetInnerHTML={{ __html: detail.content }} />
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </section>
        </div>
    );
}