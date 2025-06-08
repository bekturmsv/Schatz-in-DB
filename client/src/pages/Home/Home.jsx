import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { motion } from "framer-motion";

const sectionVariants = {
  hidden: { opacity: 0, y: 40 },
  visible: (i = 1) => ({
    opacity: 1,
    y: 0,
    transition: {
      delay: i * 0.18,
      duration: 0.7,
      ease: "easeOut",
    },
  }),
};

const Home = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();

  return (
      <div className="min-h-screen font-mono relative overflow-x-hidden bg-[var(--bg-image)]" style={{ background: 'var(--bg-image)' }}>
        {/* Hero Section */}
        <motion.section
            initial={{ opacity: 0, y: -60 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.9, ease: "easeOut" }}
            className="py-32 md:py-64 text-center flex flex-col items-center justify-center relative"
        >
          {/* Декоративная волна */}
          <div className="absolute top-0 left-1/2 -translate-x-1/2 z-0 w-[90vw] h-72 md:h-96 pointer-events-none"
               style={{
                 background: 'var(--bg-image)',
                 opacity: 0.32,
                 filter: 'blur(80px)',
                 borderRadius: '9999px',
               }} />

          {/* Animated Title */}
          <motion.h1
              className="custom-title z-10 drop-shadow-lg"
              initial={{ opacity: 0, scale: 0.96 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ delay: 0.2, duration: 0.8, ease: "backOut" }}
          >
            {t("heroTitle")}
          </motion.h1>

          <motion.p
              className="text-lg md:text-2xl mb-12 custom-font z-10"
              initial={{ opacity: 0, y: 40 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.5, duration: 0.8 }}
          >
            {t("heroSubtitle")}
          </motion.p>

          <motion.div
              whileHover={{ scale: 1.08, rotate: -2 }}
              whileTap={{ scale: 0.97 }}
              className="z-10"
          >
            <button
                onClick={() => navigate("/play")}
                className="custom-btn text-xl"
            >
              {t("startJourney")}
            </button>
          </motion.div>
        </motion.section>

        {/* Content Section */}
        <div className="custom-font container mx-auto py-12 px-4 grid md:grid-cols-3 gap-8 z-10 relative">
          {/* Game Progress */}
          <motion.div
              className="custom-card text-center py-16 px-8 flex flex-col items-center hover:scale-105 transition-all duration-300"
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
              className="custom-card text-center py-16 px-8 flex flex-col items-center hover:scale-105 transition-all duration-300"
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
              className="custom-card text-center py-16 px-8 flex flex-col items-center hover:scale-105 transition-all duration-300"
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
