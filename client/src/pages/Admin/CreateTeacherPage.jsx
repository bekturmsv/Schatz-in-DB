import { useState } from "react";
import { useCreateTeacherMutation } from "@/features/admin/adminApi";
import { useTranslation } from "react-i18next";

export default function CreateTeacherPage() {
    const { t } = useTranslation();
    const [form, setForm] = useState({
        username: "",
        email: "",
        password: "",
        firstName: "",
        lastName: "",
        subject: ""
    });
    const [createTeacher, { isLoading, isSuccess, error }] = useCreateTeacherMutation();

    const handleChange = (e) => {
        setForm(prev => ({ ...prev, [e.target.name]: e.target.value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        await createTeacher(form);
    };

    return (
        <div className="w-full h-full flex flex-col items-stretch justify-center">
            <div className="text-2xl font-bold mb-8 text-[var(--color-primary)]">
                {t("createTeacher")}
            </div>
            <form
                onSubmit={handleSubmit}
                className="w-full flex flex-col gap-6 bg-[var(--card-bg)] rounded-2xl shadow-xl border border-[var(--color-secondary-light)] px-12 py-10 max-w-2xl"
                autoComplete="off"
            >
                <FormField
                    label={t("username")}
                    name="username"
                    value={form.username}
                    onChange={handleChange}
                    placeholder={t("username")}
                    required
                />
                <FormField
                    label={t("email")}
                    name="email"
                    type="email"
                    value={form.email}
                    onChange={handleChange}
                    placeholder={t("email")}
                    required
                />
                <FormField
                    label={t("password")}
                    name="password"
                    type="password"
                    value={form.password}
                    onChange={handleChange}
                    placeholder={t("password")}
                    required
                    autoComplete="new-password"
                />
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
                <div className="flex justify-end items-center mt-4 gap-6">
                    <button
                        type="submit"
                        className="px-7 py-2 rounded-2xl font-bold text-white bg-[var(--color-primary)] hover:bg-[var(--color-secondary)] transition duration-200 shadow"
                        disabled={isLoading}
                    >
                        {isLoading ? t("creating") : t("create")}
                    </button>
                </div>
                {isSuccess && (
                    <div className="text-green-600 mt-1">{t("teacherCreated")}</div>
                )}
                {error && (
                    <div className="text-red-600 mt-1">
                        {t("errorCreatingTeacher")} {error?.error || ""}
                    </div>
                )}
            </form>
        </div>
    );
}

// Отдельный компонент поля формы для лаконичности
function FormField({ label, name, value, onChange, placeholder, required, type = "text", autoComplete }) {
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
                autoComplete={autoComplete}
                className="w-full py-3 px-4 rounded-xl border border-[var(--color-secondary)] bg-transparent outline-none focus:ring-2 focus:ring-[var(--color-primary)] focus:border-[var(--color-primary)] transition font-medium text-[var(--color-primary)] placeholder:text-[var(--color-secondary)] shadow-sm"
            />
        </div>
    );
}
