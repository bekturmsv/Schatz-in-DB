import { useGetMaterialsQuery } from "@/features/teacher/teacherApi";
import { useTranslation } from "react-i18next";
import { getPlainText } from "@/lib/stripText.js";

export default function TeacherMaterialsPage({ setSelectedMaterialId }) {
    const { t } = useTranslation();
    const { data: materials, isLoading, error } = useGetMaterialsQuery();

    if (isLoading) return <div>{t("loadingMaterials")}</div>;
    if (error) return <div>{t("errorLoadingMaterials")}</div>;
    if (!materials || materials.length === 0) return <div>{t("noMaterials")}</div>;

    return (
        <div>
            <div className="text-2xl font-bold mb-6 text-[var(--color-primary)]">{t("materials")}</div>
            <div className="grid gap-6 grid-cols-1 sm:grid-cols-2 xl:grid-cols-3">
                {materials.map(mat => (
                    <div
                        key={mat.id}
                        className="custom-card p-6 rounded-2xl shadow-md border border-[var(--color-secondary-light)] bg-[var(--card-bg)] hover:bg-[var(--color-secondary)]/10 transition cursor-pointer"
                        onClick={() => setSelectedMaterialId(mat.id)}
                    >
                        <div className="text-lg font-semibold mb-2">{mat.title}</div>
                        <div className="mb-2 text-sm">{mat.sqlKategorie}</div>
                        <div className="mb-2 text-[var(--color-secondary)] opacity-90 line-clamp-4">
                            {getPlainText(mat.description).slice(0, 120) || ""}
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}
