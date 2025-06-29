import { useState, useEffect } from "react";
import { useUploadMaterialMutation } from "@/features/teacher/teacherApi";
import { useSelector } from "react-redux";
import { useTranslation } from "react-i18next";
import { EditorContent, useEditor } from "@tiptap/react";
import StarterKit from "@tiptap/starter-kit";
import Link from "@tiptap/extension-link";
import { createLowlight } from "lowlight";
import CodeBlockLowlight from "@tiptap/extension-code-block-lowlight";
import Youtube from "@tiptap/extension-youtube";
import "highlight.js/styles/github-dark.css";
import sql from "highlight.js/lib/languages/sql";
import bash from "highlight.js/lib/languages/bash";

// Создаём lowlight instance и регистрируем языки
const lowlight = createLowlight();
lowlight.register("sql", sql);
lowlight.register("bash", bash);

const sqlCategories = [
    "SELECT", "WHERE", "JOIN", "GROUP_BY", "HAVING",
    "SUBQUERY", "AGGREGATE", "ORDER_BY", "DISTINCT",
    "COUNT", "SUM", "AND", "OR", "NOT", "AVG"
];

export default function TeacherCreateMaterialPage() {
    const { t } = useTranslation();
    const [form, setForm] = useState({
        title: "",
        sqlKategorie: "SELECT"
    });
    const [uploadMaterial, { isLoading, isSuccess, error }] = useUploadMaterialMutation();
    const teacher = useSelector(state => state.auth.user);
    const [html, setHtml] = useState("");

    const editor = useEditor({
        extensions: [
            StarterKit,
            Link,
            CodeBlockLowlight.configure({ lowlight }),
            Youtube.configure({
                controls: true,
                width: 500,
                height: 300,
                noCookie: false,
            }),
        ],
        content: "",
        onUpdate: ({ editor }) => {
            setHtml(editor.getHTML());
        },
        editorProps: {
            attributes: {
                class: "prose dark:prose-invert min-h-[300px] p-3 outline-none bg-transparent",
                style: "background: var(--color-card-bg); color: var(--color-primary); border-radius: 1rem;"
            }
        }
    });

    useEffect(() => {
        if (isSuccess && editor) {
            editor.commands.setContent("");
        }
    }, [isSuccess]);

    const handleChange = e => {
        setForm(prev => ({ ...prev, [e.target.name]: e.target.value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        await uploadMaterial({
            title: form.title,
            description: html, // тут хранится HTML из TipTap
            sqlKategorie: form.sqlKategorie,
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
                    <div className="border border-[var(--color-secondary-light)] rounded-lg bg-[var(--color-card-bg)]">
                        <MenuBar editor={editor} />
                        <EditorContent editor={editor} />
                    </div>
                    <div className="text-xs mt-2 text-gray-500 dark:text-gray-400">
                        {t("tiptapHint") ||
                            <>To insert a code block, use the <b>{"</>"}</b> button above. For SQL/Bash, type your code, then change the language in the code block if needed.<br />
                                <b>Example:</b> SQL and Bash are both highlighted. <br />
                                To insert YouTube, click the "YT" button.</>
                        }
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
                <button type="submit" className="btn-primary w-fit" disabled={isLoading}>
                    {isLoading ? t("creating") : t("create")}
                </button>
                {isSuccess && (
                    <div className="text-green-600 mt-2">{t("materialCreated")}</div>
                )}
                {error && (
                    <div className="text-red-600 mt-2">{t("errorCreatingMaterial")}</div>
                )}
            </form>
        </div>
    );
}

function MenuBar({ editor }) {
    if (!editor) return null;

    // Простая реализация
    return (
        <div className="flex flex-wrap gap-2 border-b border-[var(--color-secondary-light)] px-1 py-2 bg-[var(--color-card-bg)] rounded-t-lg mb-1">
            <button type="button"
                    onClick={() => editor.chain().focus().toggleBold().run()}
                    className={editor.isActive('bold') ? "btn-tiptap-active" : "btn-tiptap"}
                    aria-label="Bold"
            ><b>B</b></button>
            <button type="button"
                    onClick={() => editor.chain().focus().toggleItalic().run()}
                    className={editor.isActive('italic') ? "btn-tiptap-active" : "btn-tiptap"}
                    aria-label="Italic"
            ><i>I</i></button>
            <button type="button"
                    onClick={() => editor.chain().focus().toggleStrike().run()}
                    className={editor.isActive('strike') ? "btn-tiptap-active" : "btn-tiptap"}
                    aria-label="Strike"
            ><s>S</s></button>
            <button type="button"
                    onClick={() => editor.chain().focus().toggleBulletList().run()}
                    className={editor.isActive('bulletList') ? "btn-tiptap-active" : "btn-tiptap"}
                    aria-label="Bulleted List"
            >• List</button>
            <button type="button"
                    onClick={() => editor.chain().focus().toggleOrderedList().run()}
                    className={editor.isActive('orderedList') ? "btn-tiptap-active" : "btn-tiptap"}
                    aria-label="Numbered List"
            >1. List</button>
            <button type="button"
                    onClick={() => editor.chain().focus().toggleBlockquote().run()}
                    className={editor.isActive('blockquote') ? "btn-tiptap-active" : "btn-tiptap"}
                    aria-label="Blockquote"
            >"</button>
            <button type="button"
                    onClick={() => editor.chain().focus().toggleCodeBlock().run()}
                    className={editor.isActive('codeBlock') ? "btn-tiptap-active" : "btn-tiptap"}
                    aria-label="Code Block"
            >{"</>"}</button>
            <button type="button"
                    onClick={() => {
                        const url = prompt("Enter YouTube URL");
                        if (url) editor.chain().focus().setYoutubeVideo({ src: url }).run();
                    }}
                    className="btn-tiptap"
                    aria-label="YouTube Video"
            >YT</button>
            <button type="button"
                    onClick={() => {
                        const url = prompt("Enter URL for link:");
                        if (url) editor.chain().focus().toggleLink({ href: url }).run();
                    }}
                    className={editor.isActive('link') ? "btn-tiptap-active" : "btn-tiptap"}
                    aria-label="Link"
            >🔗</button>
            <button type="button"
                    onClick={() => editor.chain().focus().undo().run()}
                    className="btn-tiptap"
                    aria-label="Undo"
            >↺</button>
            <button type="button"
                    onClick={() => editor.chain().focus().redo().run()}
                    className="btn-tiptap"
                    aria-label="Redo"
            >↻</button>
        </div>
    );
}
