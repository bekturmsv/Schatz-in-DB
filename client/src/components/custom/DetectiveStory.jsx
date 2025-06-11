import { motion, AnimatePresence } from "framer-motion";
import detectiveGreeting from "../../assets/detectives/detective_greeting.png";
import detectiveCongrats from "../../assets/detectives/detective_congrats.png";

const LEVEL_TEXTS = {
    easy: {
        start: <>Ich bin Detective Banks!<br />Es fehlen Süßigkeiten in der Stadt.<br /><b>Helfen Sie mir, das herauszufinden!<br/>Wählen Sie ein Thema und lösen Sie Aufgaben auf Anfängerniveau.</b></>,
        end: <>Gut gemacht!<br />Candy gefunden dank Ihrer Antworten!<br /><b>Detective Banks ist mit seiner Arbeit zufrieden!</b></>,
    },
    medium: {
        start: <>Sie schon wieder, Herr Abgeordneter!<br />In der Stadt ist ein Hund entführt worden.<br /><b>Sie sind der Einzige, der diese Ermittlungen auf mittlerer Ebene führen kann!</b></>,
        end: <>Die Untersuchung ist abgeschlossen!<br />Hündchen gefunden.<br /><b>Danke für die Logik und den Witz!</b></>,
    },
    hard: {
        start: <>Ein Fall von extremer Schwierigkeit!<br />Die Bankräuber sind entkommen.<br /><b>Sie brauchen einen Profi, um sich auf den Weg zu machen - wählen Sie ein Thema und beweisen Sie Ihr Können!</b></>,
        end: <>Fantastisch!<br />Sie haben das schwierigste Verbrechen von allen aufgeklärt.<br /><b>Die Stadt ist stolz auf Sie!</b></>,
    }
};

export default function DetectiveStory({ isVisible, onClose, difficulty = "easy", isEnd = false }) {
    const texts = LEVEL_TEXTS[difficulty] || LEVEL_TEXTS["easy"];
    const detectiveImage = isEnd ? detectiveCongrats : detectiveGreeting;

    return (
        <AnimatePresence>
            {isVisible && (
                <motion.div
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    exit={{ opacity: 0 }}
                    className="fixed z-50 left-0 bottom-0 p-6"
                    style={{
                        display: "flex",
                        alignItems: "flex-end",
                        justifyContent: "flex-start",
                        minHeight: 240,
                        pointerEvents: "auto"
                    }}
                >
                    {/* Детектив */}
                    <div
                        style={{
                            position: 'relative',
                            zIndex: 2,
                            marginRight: -32, // чтобы облако заходило за картинку
                            background: "none",
                            boxShadow: "none",
                            borderRadius: 0,
                        }}
                    >
                        <img
                            src={detectiveImage}
                            alt="Детектив"
                            style={{
                                width: 148,
                                height: 210,
                                objectFit: 'contain',
                                background: "none",
                                boxShadow: 'none',
                                borderRadius: 0,
                                border: "none"
                            }}
                        />
                    </div>

                    {/* Облако */}
                    <div
                        style={{
                            position: 'relative',
                            zIndex: 3,
                            marginLeft: 0,
                            marginBottom: 85, // выставляет облако чуть выше, чтобы совпало с ртом/головой
                            minWidth: 290,
                            maxWidth: 420,
                            background: "#fff",
                            borderRadius: "2rem",
                            boxShadow: "0 8px 32px 0 rgba(36,42,54,0.13)",
                            border: "2px solid #264653",
                            padding: "1.5rem 1.8rem 1.3rem 1.7rem",
                            fontWeight: 500,
                            fontSize: 20,
                            color: "#264653",
                            display: "flex",
                            flexDirection: "column"
                        }}
                    >
                        {/* SVG-хвостик к голове (координируй left/bottom чтобы идеально попасть в рот) */}
                        <svg
                            width="55"
                            height="48"
                            viewBox="0 0 55 48"
                            style={{
                                position: "absolute",
                                left: -44,  // двигай влево/вправо
                                bottom: 44, // двигай вверх/вниз чтобы попасть в рот/голову
                                zIndex: 3,
                                pointerEvents: 'none'
                            }}
                        >
                            <path
                                d="M54 7 Q20 48 2 40 Q18 38 26 22 Q36 8 54 7"
                                fill="#fff"
                                stroke="#264653"
                                strokeWidth="2"
                            />
                        </svg>
                        {/* Сам текст */}
                        <div style={{ zIndex: 4 }}>
                            {isEnd ? texts.end : texts.start}
                        </div>
                        <div className="w-full text-right" style={{ marginTop: 18 }}>
                            <button
                                onClick={onClose}
                                className="px-6 py-2 bg-blue-600 text-white rounded-xl text-base shadow hover:bg-blue-700 transition"
                            >
                                OK
                            </button>
                        </div>
                    </div>
                </motion.div>
            )}
        </AnimatePresence>
    );
}
