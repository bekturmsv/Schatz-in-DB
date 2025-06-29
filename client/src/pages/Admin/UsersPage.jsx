import { useGetAllUsersQuery } from "@/features/admin/adminApi";
import { useTranslation } from "react-i18next";

export default function UsersPage({ setActiveTab, setEditTeacherId, setEditPlayerId }) {
    const { t } = useTranslation();
    const { data: users, isLoading, error } = useGetAllUsersQuery();

    if (isLoading) return (
        <div className="flex items-center justify-center py-20 text-lg text-[var(--color-secondary)]">
            {t('loadingUsers')}
        </div>
    );
    if (error) return (
        <div className="flex items-center justify-center py-20 text-lg text-red-500">
            {t('errorLoadingUsers')}
        </div>
    );

    // Фильтруем и сортируем пользователей (без учителей, сортировка по username)
    const filteredUsers = users
        ?.filter(user => user.role !== "TEACHER")
        .sort((a, b) => a.username.localeCompare(b.username));

    return (
        <div>
            <div className="text-2xl font-bold mb-6 text-[var(--color-primary)]">{t('users')}</div>
            <div className="overflow-x-auto">
                <table className="min-w-full custom-card rounded-2xl shadow-xl border border-[var(--color-secondary-light)]">
                    <thead>
                    <tr className="bg-[var(--color-secondary-light)] text-[var(--color-primary)] text-base">
                        <th className="p-3 font-bold text-left min-w-[60px]">{t('id')}</th>
                        <th className="p-3 font-bold text-left min-w-[110px]">{t('role')}</th>
                        <th className="p-3 font-bold text-left min-w-[130px]">{t('username')}</th>
                        <th className="p-3 font-bold text-left min-w-[180px]">{t('email')}</th>
                        <th className="p-3 font-bold text-left min-w-[180px]">{t('fullName')}</th>
                        <th className="p-3 font-bold text-left min-w-[160px]">{t('actions')}</th>
                    </tr>
                    </thead>
                    <tbody>
                    {filteredUsers?.length > 0 ? (
                        filteredUsers.map(user => (
                            <tr key={user.id} className="hover:bg-[var(--color-secondary)]/10 transition">
                                <td className="p-3 align-middle">{user.id}</td>
                                <td className="p-3 align-middle">{user.role}</td>
                                <td className="p-3 align-middle">{user.username}</td>
                                <td className="p-3 align-middle">{user.email}</td>
                                <td className="p-3 align-middle">{user.firstName} {user.lastName}</td>
                                <td className="p-3 align-middle">
                                    {user.role === "PLAYER" && (
                                        <button
                                            onClick={() => { setEditPlayerId(user.id); setActiveTab("edit-player"); }}
                                            className="px-4 py-1 rounded-xl font-semibold text-white bg-[var(--color-primary)] hover:bg-[var(--color-secondary)] transition duration-200 shadow"
                                        >
                                            {t('editPlayer')}
                                        </button>
                                    )}
                                    {/* Если будут другие роли — добавить действия тут */}
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan={6} className="p-6 text-center text-[var(--color-secondary)]">
                                {t('noUsers')}
                            </td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>
        </div>
    );
}
