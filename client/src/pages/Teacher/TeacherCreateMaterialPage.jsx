import { useState } from "react";
import { useUploadMaterialMutation } from "@/features/teacher/teacherApi";
import { useSelector } from "react-redux";
import { useTranslation } from "react-i18next";
import MDEditor from "@uiw/react-md-editor";
import "@uiw/react-md-editor/dist/mdeditor.css";
import "@uiw/react-markdown-preview/dist/markdown.css";

const sqlCategories = [
    "SELECT", "WHERE", "JOIN", "GROUP_BY", "HAVING",
    "SUBQUERY", "AGGREGATE", "ORDER_BY", "DISTINCT",
    "COUNT", "SUM", "AND", "OR", "NOT", "AVG"
];

export default function TeacherCreateMaterialPage() {
    const { t } = useTranslation();
    const [form, setForm] = useState({
        title: "",
        description: "",
        sqlKategorie: "SELECT"
    });
    const [uploadMaterial, { isLoading, isSuccess, error }] = useUploadMaterialMutation();
    const teacher = useSelector(state => state.auth.user);

    const handleChange = (e) => {
        setForm(prev => ({ ...prev, [e.target.name]: e.target.value }));
    };

    // MDEditor изменяет description:
    const handleDescriptionChange = (value) => {
        setForm(prev => ({ ...prev, description: value || "" }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        await uploadMaterial({
            ...form,
            teacherId: teacher.id
        });
    };

    return (
        <div className="max-w-2xl">
            <div className="text-2xl font-bold mb-6 text-[var(--color-primary)]">{t("createMaterial")}</div>
            <form
                onSubmit={handleSubmit}
                className="flex flex-col gap-6 bg-[var(--card-bg)] rounded-2xl shadow-xl border border-[var(--color-secondary-light)] px-8 py-8"
            >
                <label>
                    <span className="font-semibold text-[var(--color-primary)] mb-1 block">{t("title")}</span>
                    <input
                        name="title"
                        value={form.title}
                        onChange={handleChange}
                        placeholder={t("title")}
                        required
                        className="input w-full"
                    />
                </label>
                <label>
                    <span className="font-semibold text-[var(--color-primary)] mb-1 block">{t("description")}</span>
                    <div data-color-mode="light" className="rounded-xl overflow-hidden border border-[var(--color-secondary-light)] bg-[var(--color-card-bg)]">
                        <MDEditor
                            value={form.description}
                            onChange={handleDescriptionChange}
                            height={350}
                            preview="edit"
                        />
                    </div>
                    <div className="text-xs mt-2 text-gray-500 dark:text-gray-400">
                        {t("markdownHint") ||
                            "Supports Markdown: links, code, YouTube, tables, etc. For code, use triple backticks. For video: paste YouTube URL or use [text](https://youtu.be/...)."}
                    </div>
                </label>
                <label>
                    <span className="font-semibold text-[var(--color-primary)] mb-1 block">{t("sqlCategory")}</span>
                    <select
                        name="sqlKategorie"
                        value={form.sqlKategorie}
                        onChange={handleChange}
                        className="input w-full"
                    >
                        {sqlCategories.map(cat => (
                            <option key={cat} value={cat}>{cat}</option>
                        ))}
                    </select>
                </label>
                <button
                    type="submit"
                    className="btn-primary w-fit"
                    disabled={isLoading}
                >
                    {isLoading ? t("creating") : t("create")}
                </button>
                {isSuccess && <div className="text-green-600 mt-2">{t("materialCreated")}</div>}
                {error && <div className="text-red-600 mt-2">{t("errorCreatingMaterial")}</div>}
            </form>
        </div>
    );
}
