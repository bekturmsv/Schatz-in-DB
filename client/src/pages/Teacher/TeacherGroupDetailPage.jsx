import { useGetGroupByIdQuery } from "@/features/teacher/teacherApi";
import { useTranslation } from "react-i18next";

export default function TeacherGroupDetailPage({ groupId, onBack }) {
    const { t } = useTranslation();
    const { data, isLoading, error } = useGetGroupByIdQuery(groupId);

    if (!groupId) return null;
    if (isLoading) return <div>{t("loadingGroup")}</div>;
    if (error) return <div>{t("errorLoadingGroup")}</div>;
    if (!data?.group) return <div>{t("noGroupData")}</div>;

    const group = data.group;
    const students = data.students || [];

    // Для адаптивности и красоты карточки, иначе можно table
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
            <div className="mt-4">
                <div className="font-semibold mb-2">{t("students")}:</div>
                {students.length === 0 ? (
                    <div className="text-[var(--color-secondary)]">{t("noStudents")}</div>
                ) : (
                    <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4">
                        {students.map(stu => {
                            // Для прогресса (если добавишь позже в ответ — подправь тут)
                            // Если нет поля "solved"/"total", можешь добавить эти поля в ответе сервера
                            const solved = stu.progress?.tasksSolved ?? stu.solved ?? 0;
                            const total = stu.progress?.totalTasks ?? stu.totalTasks ?? 0;
                            return (
                                <div
                                    key={stu.id}
                                    className="rounded-xl bg-[var(--color-card-bg,#ececec)] shadow p-4 flex flex-col gap-2"
                                >
                                    <div className="font-bold text-lg">{stu.firstName} {stu.lastName}</div>
                                    <div className="text-[var(--color-secondary)] text-sm">{stu.email}</div>
                                    <div className="flex flex-wrap gap-2 text-sm mt-1">
                                        <span>
                                            <b>{t("specialistGroup") || "Specialist"}</b>: {stu.specialist_group || "-"}
                                        </span>
                                        <span>
                                            <b>{t("matriculationNumber") || "№"}</b>: {stu.matriculation_number || "-"}
                                        </span>
                                    </div>
                                    <div className="flex items-center gap-4 mt-1">
                                        <span className="text-[var(--color-primary)] font-semibold">
                                            {t("points")}: {stu.total_points ?? stu.points ?? 0}
                                        </span>
                                        {/* Решённых задач */}
                                        <span className="bg-[var(--color-secondary)]/10 rounded px-2 py-0.5 text-xs">
                                            {t("solved") || "Solved"}: {solved} / {total || 51}
                                        </span>
                                    </div>
                                </div>
                            )
                        })}
                    </div>
                )}
            </div>
        </div>
    );
}
