import { useEffect, useState } from "react";
import { useGetAllUsersQuery, useUpdateTeacherMutation } from "@/features/admin/adminApi";
import { useTranslation } from "react-i18next";

export default function EditTeacherPage({ id, onBack }) {
    const { t } = useTranslation();
    const { data: users, isLoading } = useGetAllUsersQuery();
    const [updateTeacher, { isLoading: isUpdating, isSuccess, error }] = useUpdateTeacherMutation();

    const user = users?.find(u => u.id === id);
    const [form, setForm] = useState({
        firstName: "",
        lastName: "",
        subject: "",
        groupIds: [],
    });

    useEffect(() => {
        if (user) {
            setForm({
                firstName: user.firstName || "",
                lastName: user.lastName || "",
                subject: user.subject || "",
                groupIds: user.groupIds || [],
            });
        }
    }, [user]);

    const handleChange = (e) => {
        setForm(prev => ({ ...prev, [e.target.name]: e.target.value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        await updateTeacher({ id, ...form });
    };

    if (isLoading || !user) return (
        <div className="flex items-center justify-center py-20 text-lg text-[var(--color-secondary)]">
            {t("loadingTeacher")}
        </div>
    );

    return (
        <div className="w-full h-full flex flex-col items-stretch justify-center">
            <button
                type="button"
                onClick={onBack}
                className="mb-4 text-[var(--color-secondary)] underline text-base hover:text-[var(--color-primary)] transition self-start"
            >
                ← {t("back")}
            </button>
            <div className="text-2xl font-bold mb-8 text-[var(--color-primary)]">{t("editTeacherTitle")}</div>
            <form
                onSubmit={handleSubmit}
                className="w-full flex flex-col gap-6 bg-[var(--card-bg)] rounded-2xl shadow-xl border border-[var(--color-secondary-light)] px-12 py-10 max-w-2xl"
                autoComplete="off"
            >
                <FormField
                    label={t("firstName")}
                    name="firstName"
                    value={form.firstName}
                    onChange={handleChange}
                    placeholder={t("firstName")}
                />
                <FormField
                    label={t("lastName")}
                    name="lastName"
                    value={form.lastName}
                    onChange={handleChange}
                    placeholder={t("lastName")}
                />
                <FormField
                    label={t("subject")}
                    name="subject"
                    value={form.subject}
                    onChange={handleChange}
                    placeholder={t("subject")}
                />
                {/* Для групп можно добавить select точно так же */}
                <div className="flex justify-end items-center mt-4 gap-6">
                    <button
                        type="submit"
                        className="px-7 py-2 rounded-2xl font-bold text-white bg-[var(--color-primary)] hover:bg-[var(--color-secondary)] transition duration-200 shadow"
                        disabled={isUpdating}
                    >
                        {isUpdating ? t("saving") : t("save")}
                    </button>
                </div>
                {isSuccess && (
                    <div className="text-green-600 mt-1">{t("teacherUpdated")}</div>
                )}
                {error && (
                    <div className="text-red-600 mt-1">
                        {t("errorUpdatingTeacher")} {error?.error || ""}
                    </div>
                )}
            </form>
        </div>
    );
}

function FormField({ label, name, value, onChange, placeholder, required, type = "text" }) {
    return (
        <div className="flex flex-col w-full">
            <label htmlFor={name} className="mb-1 font-semibold text-[var(--color-primary)]">
                {label}
                {required && <span className="ml-1 text-red-500">*</span>}
            </label>
            <input
                id={name}
                name={name}
                type={type}
                value={value}
                onChange={onChange}
                placeholder={placeholder}
                required={required}
                className="w-full py-3 px-4 rounded-xl border border-[var(--color-secondary)] bg-transparent outline-none focus:ring-2 focus:ring-[var(--color-primary)] focus:border-[var(--color-primary)] transition font-medium text-[var(--color-primary)] placeholder:text-[var(--color-secondary)] shadow-sm"
            />
        </div>
    );
}
