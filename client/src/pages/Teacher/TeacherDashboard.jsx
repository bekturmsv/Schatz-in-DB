import { useState } from "react";
import TeacherMaterialsPage from "./TeacherMaterialsPage";
import TeacherCreateMaterialPage from "./TeacherCreateMaterialPage";
import TeacherGroupsPage from "./TeacherGroupsPage";
import TeacherCreateGroupPage from "./TeacherCreateGroupPage";
import TeacherGroupDetailPage from "./TeacherGroupDetailPage";
import { useTranslation } from "react-i18next";
import TeacherMaterialDetailSidePanel from "@/pages/Teacher/TeacherMaterialDetailSidePanel.jsx";

const TABS = [
    { key: "materials", labelKey: "materials" },
    { key: "create-material", labelKey: "createMaterial" },
    { key: "groups", labelKey: "groups" },
    { key: "create-group", labelKey: "createGroup" },
];

export default function TeacherDashboard() {
    const { t } = useTranslation();
    const [activeTab, setActiveTab] = useState("materials");
    const [selectedGroupId, setSelectedGroupId] = useState(null);
    const [selectedMaterialId, setSelectedMaterialId] = useState(null);

    let content;
    if (activeTab === "materials") {
        content = (
            <TeacherMaterialsPage setSelectedMaterialId={setSelectedMaterialId} />
        );
    } else if (activeTab === "create-material") {
        content = <TeacherCreateMaterialPage />;
    } else if (activeTab === "groups") {
        content = (
            <TeacherGroupsPage
                setSelectedGroupId={setSelectedGroupId}
                setActiveTab={setActiveTab}
            />
        );
    } else if (activeTab === "create-group") {
        content = <TeacherCreateGroupPage />;
    } else if (activeTab === "group-detail") {
        content = (
            <TeacherGroupDetailPage
                groupId={selectedGroupId}
                onBack={() => setActiveTab("groups")}
            />
        );
    }

    return (
        <div className="teacher-dashboard custom-card flex min-h-[calc(100vh-64px)] mt-16">
            <div className="w-64 min-w-56 border-r border-[var(--color-secondary)] py-8 px-4 bg-[var(--card-bg)]">
                <div className="font-bold text-xl mb-8 text-[var(--color-primary)]">
                    {t("teacherPanel")}
                </div>
                <div className="flex flex-col gap-3">
                    {TABS.map(tab => (
                        <button
                            key={tab.key}
                            className={`py-2 px-4 text-left rounded-2xl font-semibold transition-all
                                ${activeTab === tab.key
                                ? "bg-[var(--color-primary)] text-white shadow-md"
                                : "hover:bg-[var(--color-secondary-light)] text-[var(--color-primary)]"
                            }`}
                            onClick={() => setActiveTab(tab.key)}
                        >
                            {t(tab.labelKey)}
                        </button>
                    ))}
                </div>
            </div>
            <div className="flex-1 p-8 relative">
                {content}
                {selectedMaterialId && (
                    <TeacherMaterialDetailSidePanel
                        id={selectedMaterialId}
                        onClose={() => setSelectedMaterialId(null)}
                    />
                )}
            </div>
        </div>
    );
}
