import { useState } from "react";
import UsersPage from "./UsersPage";
import CreateTeacherPage from "./CreateTeacherPage";
import EditTeacherPage from "./EditTeacherPage";
import EditPlayerPage from "./EditPlayerPage";
import TeachersPage from "@/pages/Admin/TeachersPage.jsx";
import { useTranslation } from "react-i18next";

const TABS = [
    { key: "users", labelKey: "players" },
    { key: "teachers", labelKey: "teachers" },
    { key: "create-teacher", labelKey: "createTeacher" }
];

export default function AdminDashboard() {
    const { t } = useTranslation();
    const [activeTab, setActiveTab] = useState("users");
    const [editTeacherId, setEditTeacherId] = useState(null);
    const [editPlayerId, setEditPlayerId] = useState(null);

    let content;
    if (activeTab === "users") {
        content = (
            <UsersPage
                setActiveTab={setActiveTab}
                setEditTeacherId={setEditTeacherId}
                setEditPlayerId={setEditPlayerId}
            />
        );
    } else if (activeTab === "teachers") {
        content = <TeachersPage  setActiveTab={setActiveTab}
                                 setEditTeacherId={setEditTeacherId} />;
    } else if (activeTab === "create-teacher") {
        content = <CreateTeacherPage />;
    } else if (activeTab === "edit-teacher") {
        content = (
            <EditTeacherPage
                id={editTeacherId}
                onBack={() => setActiveTab("users")}
            />
        );
    } else if (activeTab === "edit-player") {
        content = (
            <EditPlayerPage
                id={editPlayerId}
                onBack={() => setActiveTab("users")}
            />
        );
    }

    return (
        <div className="admin-dashboard custom-card flex min-h-[calc(100vh-64px)] mt-16">
            <div className="w-64 min-w-56 border-r border-[var(--color-secondary)] py-8 px-4 bg-[var(--card-bg)]">
                <div className="font-bold text-xl mb-8 text-[var(--color-primary)]">
                    {t("adminPanel")}
                </div>
                <div className="flex flex-col gap-3">
                    {TABS.map(tab => (
                        <button
                            key={tab.key}
                            className={`py-2 px-4 text-left rounded-2xl font-semibold transition-all
                                ${activeTab === tab.key
                                ? "bg-[var(--color-primary)] text-white shadow-md"
                                : "hover:bg-[var(--color-secondary-light)] text-[var(--color-primary)]"
                            }
                            `}
                            onClick={() => {
                                setActiveTab(tab.key);
                                setEditTeacherId(null);
                                setEditPlayerId(null);
                            }}
                        >
                            {t(tab.labelKey)}
                        </button>
                    ))}
                </div>
            </div>
            <div className="flex-1 p-8">{content}</div>
        </div>
    );
}
