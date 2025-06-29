import { useState } from "react";
import { useCreateGroupMutation } from "@/features/teacher/teacherApi";
import { useTranslation } from "react-i18next";

export default function TeacherCreateGroupPage() {
    const { t } = useTranslation();
    const [form, setForm] = useState({ name: "" });
    const [createGroup, { isLoading, isSuccess, data, error }] = useCreateGroupMutation();

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        await createGroup(form);
    };

    return (
        <div className="max-w-md">
            <div className="text-2xl font-bold mb-6 text-[var(--color-primary)]">{t("createGroup")}</div>
            <form
                onSubmit={handleSubmit}
                className="flex flex-col gap-6 bg-[var(--card-bg)] rounded-2xl shadow-xl border border-[var(--color-secondary-light)] px-8 py-8"
            >
                <label>
                    <span className="font-semibold text-[var(--color-primary)] mb-1 block">{t("groupName")}</span>
                    <input
                        name="name"
                        value={form.name}
                        onChange={handleChange}
                        placeholder={t("groupName")}
                        required
                        className="input w-full"
                    />
                </label>
                <button
                    type="submit"
                    className="btn-primary w-fit"
                    disabled={isLoading}
                >
                    {isLoading ? t("creating") : t("create")}
                </button>
                {isSuccess && data?.code && (
                    <div className="text-green-600 mt-2">
                        {t("groupCreated")} <br />
                        <b>{t("groupCode")}: {data.code}</b>
                    </div>
                )}
                {error && <div className="text-red-600 mt-2">{t("errorCreatingGroup")}</div>}
            </form>
        </div>
    );
}
