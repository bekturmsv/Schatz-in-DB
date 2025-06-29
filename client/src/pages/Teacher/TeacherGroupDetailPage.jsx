import { useGetGroupByIdQuery } from "@/features/teacher/teacherApi";
import { useTranslation } from "react-i18next";

export default function TeacherGroupDetailPage({ groupId, onBack }) {
    const { t } = useTranslation();
    const { data: group, isLoading, error } = useGetGroupByIdQuery(groupId);

    if (!groupId) return null;
    if (isLoading) return <div>{t("loadingGroup")}</div>;
    if (error) return <div>{t("errorLoadingGroup")}</div>;

    return (
        <div>
            <button
                type="button"
                onClick={onBack}
                className="mb-4 text-[var(--color-secondary)] underline text-base hover:text-[var(--color-primary)] transition self-start"
            >
                ← {t("back")}
            </button>
            <div className="text-2xl font-bold mb-6 text-[var(--color-primary)]">{group.name}</div>
            <div className="mb-2">{t("groupCode")}: <b>{group.code}</b></div>
            <div className="mb-2">{t("teacher")}: {group.teacher?.firstName} {group.teacher?.lastName}</div>
            {/* Добавь здесь список студентов, если он появится */}
        </div>
    );
}
