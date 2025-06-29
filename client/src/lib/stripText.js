export function stripHtml(html) {
    if (!html) return "";
    return html.replace(/<[^>]+>/g, "");
}

export function stripMarkdown(md) {
    if (!md) return "";
    return md
        .replace(/(```[\s\S]*?```)/g, "")
        .replace(/`([^`]+)`/g, "$1")
        .replace(/!\[.*?\]\(.*?\)/g, "")
        .replace(/\[([^\]]+)\]\([^)]+\)/g, "$1")
        .replace(/[*_~#>`-]/g, "")
        .replace(/!\[.*?\]/g, "")
        .replace(/\n+/g, " ")
        .replace(/\s+/g, " ")
        .trim();
}

export function getPlainText(str) {
    if (!str) return "";
    if (/^\s*<.+?>/.test(str)) return stripHtml(str);
    return stripMarkdown(str);
}
