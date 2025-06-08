import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { motion } from "framer-motion";

const sectionVariants = {
  hidden: { opacity: 0, y: 40 },
  visible: (i = 1) => ({
    opacity: 1,
    y: 0,
    transition: {
      delay: i * 0.2,
      duration: 0.7,
      ease: "easeOut",
    },
  }),
};

const Home = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();

  return (
      <div className="min-h-screen font-mono bg-gradient-to-br from-[#f8fafc] to-[#c5e4fa] relative overflow-x-hidden">
        {/* Hero Section */}
        <motion.section
            initial={{ opacity: 0, y: -60 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.9, ease: "easeOut" }}
            className="py-32 md:py-64 text-center flex flex-col items-center justify-center relative"
        >
          {/* Фоновая декоративная волна */}
          <div className="absolute top-0 left-1/2 -translate-x-1/2 z-0 w-[90vw] h-72 md:h-96 bg-gradient-to-tr from-green-300 via-cyan-200 to-blue-300 opacity-50 blur-2xl rounded-full pointer-events-none" />

          {/* Animated Title */}
          <motion.h1
              className="text-5xl md:text-6xl font-extrabold mb-6 custom-font z-10 bg-gradient-to-r from-green-600 via-blue-600 to-cyan-400 bg-clip-text text-transparent drop-shadow-lg"
              initial={{ opacity: 0, scale: 0.96 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ delay: 0.2, duration: 0.8, ease: "backOut" }}
          >
            {t("heroTitle")}
          </motion.h1>

          <motion.p
              className="text-lg md:text-2xl mb-12 custom-font text-black/70 z-10"
              initial={{ opacity: 0, y: 40 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.5, duration: 0.8 }}
          >
            {t("heroSubtitle")}
          </motion.p>

          <motion.div
              whileHover={{ scale: 1.07, rotate: -2 }}
              whileTap={{ scale: 0.97 }}
              className="z-10"
          >
            <Button
                onClick={() => navigate("/play")}
                className="custom-font bg-gradient-to-r from-green-400 to-blue-400 px-12 py-6 text-xl rounded-2xl shadow-lg hover:from-cyan-300 hover:to-green-400 hover:text-black transition-all duration-300"
                style={{
                  boxShadow: "0 8px 32px 0 rgba(34,197,94,0.18)",
                }}
            >
              {t("startJourney")}
            </Button>
          </motion.div>
        </motion.section>

        {/* Content Section */}
        <div className="custom-font container mx-auto py-12 px-4 grid md:grid-cols-3 gap-8 z-10 relative">
          {/* Game Progress */}
          <motion.div
              className="bg-white/80 backdrop-blur-lg rounded-2xl shadow-2xl text-center py-16 px-8 flex flex-col items-center hover:scale-105 hover:shadow-[0_8px_48px_0_rgba(34,197,94,0.12)] transition-all duration-300"
              custom={1}
              initial="hidden"
              whileInView="visible"
              viewport={{ once: true, amount: 0.2 }}
              variants={sectionVariants}
          >
            <span className="mb-4 text-green-400 text-4xl">🏆</span>
            <h2 className="text-2xl md:text-3xl font-bold mb-2">{t("gameProgress")}</h2>
            <p className="mb-2 md:text-lg">{t("progressDescription")}</p>
            <p className="md:text-lg">{t("progressSummary")}</p>
          </motion.div>

          {/* Leaderboard */}
          <motion.div
              className="bg-white/80 backdrop-blur-lg rounded-2xl shadow-2xl text-center py-16 px-8 flex flex-col items-center hover:scale-105 hover:shadow-[0_8px_48px_0_rgba(37,99,235,0.13)] transition-all duration-300"
              custom={2}
              initial="hidden"
              whileInView="visible"
              viewport={{ once: true, amount: 0.2 }}
              variants={sectionVariants}
          >
            <span className="mb-4 text-blue-400 text-4xl">📈</span>
            <h2 className="text-2xl md:text-3xl font-bold">{t("leaderboard")}</h2>
          </motion.div>

          {/* News and Updates */}
          <motion.div
              className="bg-white/80 backdrop-blur-lg rounded-2xl shadow-2xl text-center py-16 px-8 flex flex-col items-center hover:scale-105 hover:shadow-[0_8px_48px_0_rgba(34,197,94,0.10)] transition-all duration-300"
              custom={3}
              initial="hidden"
              whileInView="visible"
              viewport={{ once: true, amount: 0.2 }}
              variants={sectionVariants}
          >
            <span className="mb-4 text-yellow-400 text-4xl">📰</span>
            <h2 className="text-2xl md:text-3xl font-bold">{t("newsAndUpdates")}</h2>
          </motion.div>
        </div>
      </div>
  );
};

export default Home;
