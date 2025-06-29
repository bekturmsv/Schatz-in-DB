import { useGetAllTeachersQuery } from "@/features/admin/adminApi";
import { useTranslation } from "react-i18next";

export default function TeachersPage({ setActiveTab, setEditTeacherId }) {
    const { t } = useTranslation();
    const { data: teachers, isLoading, error } = useGetAllTeachersQuery();

    if (isLoading) return (
        <div className="flex items-center justify-center py-20 text-lg text-[var(--color-secondary)]">
            {t('loadingTeachers')}
        </div>
    );
    if (error) return (
        <div className="flex items-center justify-center py-20 text-lg text-red-500">
            {t('errorLoadingTeachers')}
        </div>
    );
    if (!teachers || teachers.length === 0) return (
        <div className="flex items-center justify-center py-20 text-lg text-[var(--color-secondary)]">
            {t('noTeachers')}
        </div>
    );

    const sortedTeachers = [...teachers].sort((a, b) => a.username.localeCompare(b.username));

    return (
        <div>
            <div className="text-2xl font-bold mb-6 text-[var(--color-primary)]">{t('teachers')}</div>
            <div className="overflow-x-auto">
                <table className="min-w-full custom-card rounded-2xl shadow-xl border border-[var(--color-secondary-light)]">
                    <thead>
                    <tr className="bg-[var(--color-secondary-light)] text-[var(--color-primary)] text-base">
                        <th className="p-3 font-bold text-left min-w-[60px]">{t('id')}</th>
                        <th className="p-3 font-bold text-left min-w-[130px]">{t('username')}</th>
                        <th className="p-3 font-bold text-left min-w-[180px]">{t('email')}</th>
                        <th className="p-3 font-bold text-left min-w-[180px]">{t('fullName')}</th>
                        <th className="p-3 font-bold text-left min-w-[160px]">{t('subject')}</th>
                        <th className="p-3 font-bold text-left min-w-[140px]">{t('actions')}</th>
                    </tr>
                    </thead>
                    <tbody>
                    {sortedTeachers.map(teacher => (
                        <tr key={teacher.id} className="hover:bg-[var(--color-secondary)]/10 transition">
                            <td className="p-3 align-middle">{teacher.id}</td>
                            <td className="p-3 align-middle">{teacher.username}</td>
                            <td className="p-3 align-middle">{teacher.email}</td>
                            <td className="p-3 align-middle">{teacher.firstName} {teacher.lastName}</td>
                            <td className="p-3 align-middle">{teacher.subject}</td>
                            <td className="p-3 align-middle">
                                <button
                                    onClick={() => {
                                        setEditTeacherId(teacher.id);
                                        setActiveTab("edit-teacher");
                                    }}
                                    className="px-4 py-1 rounded-xl font-semibold text-white bg-[var(--color-primary)] hover:bg-[var(--color-secondary)] transition duration-200 shadow"
                                >
                                    {t("editTeacher")}
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}
