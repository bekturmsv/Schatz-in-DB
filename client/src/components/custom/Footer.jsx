import { useTranslation } from "react-i18next";
import { motion } from "framer-motion";

export default function Footer() {
    const { t } = useTranslation();

    return (
        <motion.footer
            initial={{ opacity: 0, y: 32 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.2, duration: 0.7 }}
            className="custom-card shadow-inner py-4 w-full"
            style={{
                background: "var(--card-bg)",
                boxShadow: "0 -4px 32px 0 rgba(0,0,0,0.03), var(--card-shadow)",
                borderTop: "1.5px solid var(--color-primary)",
                color: "var(--color-primary)",
                fontFamily: "var(--font-ui)",
                borderRadius: " var(--card-radius) var(--card-radius) 0 0",
                backdropFilter: "blur(10px)",
            }}
        >
            <div className="container mx-auto px-4 flex flex-col md:flex-row justify-between items-center gap-2 custom-font">
                <p className="text-sm md:text-base" style={{ color: "var(--color-primary)" }}>
                    {t("copyright")}
                </p>
                <p className="text-sm md:text-base" style={{ color: "var(--color-secondary)" }}>
                    {t("socialMediaLinks")}
                </p>
            </div>
        </motion.footer>
    );
}
