import { useTranslation } from "react-i18next";
import { motion } from "framer-motion";

export default function Footer() {
    const { t } = useTranslation();

    return (
        <motion.footer
            initial={{ opacity: 0, y: 32 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.2, duration: 0.7 }}
            className="bg-white/80 backdrop-blur-lg border-t border-gray-200 shadow-inner py-4 "
        >
            <div className="container mx-auto px-4 flex flex-col md:flex-row justify-between items-center gap-2 custom-font text-black">
                <p className="text-sm md:text-base">{t("copyright")}</p>
                <p className="text-sm md:text-base">{t("socialMediaLinks")}</p>
            </div>
        </motion.footer>
    );
}
