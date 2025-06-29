import React from "react";
import parse, { domToReact } from "html-react-parser";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";
import { Prism as SyntaxHighlighter } from "react-syntax-highlighter";
import { vscDarkPlus } from "react-syntax-highlighter/dist/esm/styles/prism";

// Настройки для парсинга html-блоков <pre><code>
const htmlParserOptions = {
    replace: (domNode) => {
        if (
            domNode.name === "pre" &&
            domNode.children &&
            domNode.children.length === 1 &&
            domNode.children[0].name === "code"
        ) {
            const codeNode = domNode.children[0];
            // Язык
            const className = codeNode.attribs && codeNode.attribs.class;
            let lang = "bash";
            if (className && className.startsWith("language-")) {
                lang = className.replace("language-", "");
            }
            // Код
            let code = domToReact(codeNode.children);
            if (Array.isArray(code)) code = code.join("");
            if (typeof code !== "string") code = String(code);

            return (
                <SyntaxHighlighter
                    style={vscDarkPlus}
                    language={lang}
                    PreTag="div"
                    customStyle={{
                        borderRadius: "0.7em",
                        fontSize: "1em",
                        margin: "1.2em 0",
                    }}
                >
                    {code}
                </SyntaxHighlighter>
            );
        }
        return undefined;
    },
};

export function renderMaterial(description, t) {
    // Если это html — парсим и заменяем code block
    if (/^\s*<(p|div|h\d|span|code|pre|ul|ol|li|b|i|strong|em|iframe)/i.test(description)) {
        return (
            <div className="prose prose-base max-w-none text-[var(--color-secondary)] opacity-95 dark:prose-invert custom-body"
                 style={{ wordBreak: "break-word" }}
            >
                {parse(description, htmlParserOptions)}
            </div>
        );
    }
    // Markdown — старое поведение
    return (
        <ReactMarkdown
            children={description}
            remarkPlugins={[remarkGfm]}
            components={{
                code({ node, inline, className, children, ...props }) {
                    const match = /language-(\w+)/.exec(className || "");
                    return !inline ? (
                        <SyntaxHighlighter
                            style={vscDarkPlus}
                            language={match?.[1] || "bash"}
                            PreTag="div"
                            customStyle={{
                                borderRadius: "0.7em",
                                fontSize: "1em",
                                margin: "1.2em 0",
                            }}
                            {...props}
                        >
                            {String(children).replace(/\n$/, "")}
                        </SyntaxHighlighter>
                    ) : (
                        <code
                            className="bg-gray-200 dark:bg-gray-800 rounded px-1 py-0.5 font-mono text-[0.97em]"
                            {...props}
                        >
                            {children}
                        </code>
                    );
                }
            }}
        />
    );
}
