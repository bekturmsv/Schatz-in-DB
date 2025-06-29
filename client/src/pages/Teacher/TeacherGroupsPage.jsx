import { useGetGroupsQuery } from "@/features/teacher/teacherApi";
import { useTranslation } from "react-i18next";

export default function TeacherGroupsPage({ setSelectedGroupId, setActiveTab }) {
    const { t } = useTranslation();
    const { data: groups, isLoading, error } = useGetGroupsQuery();

    if (isLoading) return <div>{t("loadingGroups")}</div>;
    if (error) return <div>{t("errorLoadingGroups")}</div>;
    if (!groups || groups.length === 0) return <div>{t("noGroups")}</div>;

    return (
        <div>
            <div className="text-2xl font-bold mb-6 text-[var(--color-primary)]">{t("groups")}</div>
            <div className="grid gap-6 grid-cols-1 sm:grid-cols-2 xl:grid-cols-3">
                {groups.map(group => (
                    <div
                        key={group.id}
                        className="custom-card p-6 rounded-2xl shadow-md border border-[var(--color-secondary-light)] bg-[var(--card-bg)] cursor-pointer hover:bg-[var(--color-secondary)]/10"
                        onClick={() => {
                            setSelectedGroupId(group.id);
                            setActiveTab("group-detail");
                        }}
                    >
                        <div className="text-lg font-semibold mb-2">{group.name}</div>
                        <div className="text-sm">{t("groupCode")}: <b>{group.code}</b></div>
                    </div>
                ))}
            </div>
        </div>
    );
}
