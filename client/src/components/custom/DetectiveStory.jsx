import { useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import detectiveGreeting from "../../assets/detectives/detective_greeting.png";
import detectiveCongrats from "../../assets/detectives/detective_congrats.png";
import { useSelector } from "react-redux";

export default function DetectiveStory({
                                           isVisible,
                                           onClose,
                                           difficulty = "easy",
                                           isEnd = false
                                       }) {
    const user = useSelector((state) => state.auth.user);

    const LEVEL_TEXTS = {
        easy: {
            start: [
                <>Willkommen in Table-Town, Detektiv <b>{user?.username}</b>.<br />
                    Was einst als ruhige Kleinstadt bekannt war, wird seit Wochen von einer beunruhigenden Welle von Verbrechen erschüttert.</>,
                <>Zunächst hielt man es für Zufälle: ein gestohlenes Fahrrad hier, ein Einbruch dort.<br />
                    Doch je mehr Vorfälle gemeldet wurden, desto deutlicher zeichnete sich ein Muster ab.</>,
                <>Und genau deshalb sitzen Sie nun hier, in Ihrem spartanisch eingerichteten Büro im Polizeipräsidium.</>,
                <>Ihr erster Schritt: ein tiefer Blick in die Polizeidatenbank.<br />
                    Vielleicht finden Sie einen Hinweis, einen ersten Faden, an dem Sie ziehen können…</>
            ],
            end: [
                <>Nach Tagen akribischer Arbeit fügen sich die Hinweise endlich zu einem klaren Bild zusammen.<br />
                    Die Datenbankrecherchen, die Tatortanalysen, die Aussagen der Zeugen — alles deutet auf eine Person hin:<br />
                    <b>Max Berger</b>, wohnhaft in einem heruntergekommenen Teil von Table-Town, mehrfach wegen Diebstahls vorbestraft, auffällige Schuhgröße 44, braune Haare, Mitte 30.</>,
                <>Es bleibt keine Zeit zu verlieren. Sie und Ihr Team beschließen, den Zugriff zu wagen.<br />
                    In den frühen Morgenstunden umstellen Sie das Viertel. Ein alter Wagen mit verdächtig flackerndem Licht parkt in der Einfahrt — es ist derselbe, der auf den Überwachungsvideos von mehreren Tatorten zu sehen war.</>,
                <>Max Berger spürt die Falle. Als er aus der Hintertür fliehen will, schnappen Ihre Kollegen bereits zu. Keine Chance. Widerstandslos lässt er sich festnehmen.</>,
                <>In seinem Wagen finden sich gestohlene Gegenstände aus mindestens drei aktuellen Fällen — darunter ein seltenes Gemälde, das vor Wochen aus dem Table-Town Museum verschwunden war.<br />
                    Der Fall ist gelöst.<br />
                    Dank Ihrer sorgfältigen Ermittlungsarbeit kann die Stadt endlich aufatmen.</>,
                <>Der Polizeichef gratuliert Ihnen persönlich:<br />
                    „Exzellente Arbeit, Detektiv <b>{user?.username}</b>. Table-Town verdankt Ihnen viel.“<br />
                    Und während die Sonne langsam über Table-Town aufgeht, wissen Sie:<br />
                    Dies war vielleicht der erste große Fall — aber sicher nicht der letzte…</>
            ]
        },
        medium: {
            start: [
                <>Willkommen zurück in Table-Town, Detektiv <b>{user?.username}</b>.<br />
                    Nach Monaten scheinbarer Ruhe mehren sich die Anzeichen, dass ein unsichtbares Netz krimineller Machenschaften die Stadt umspannt.</>,
                <>Fälle, die zunächst willkürlich erschienen, ergeben plötzlich ein Muster.<br />
                    Ein Netzwerk von Verdächtigen scheint aus dem Schatten heraus zu agieren.</>,
                <>Der Polizeichef hat Sie erneut beauftragt, Licht ins Dunkle zu bringen.<br />
                    Ihre Aufgabe ist es, die verstreuten Hinweise zu sammeln, Zeugen zu befragen und Zusammenhänge in der Polizeidatenbank aufzudecken.<br />
                    Mit jeder erfolgreich gelösten Aufgabe kommen Sie dem Kopf des Netzwerks – dem berüchtigten „Schatten“ – ein Stück näher.<br />
                    Nutzen Sie Ihr Können, Ihr Gespür und Ihre Fähigkeit zur Datenanalyse, um Table-Town wieder sicher zu machen.<br />
                    Viel Erfolg, Detektiv.</>
            ],
            end: [
                <>Tage voller Recherche, Verhöre und Analysen liegen hinter Ihnen.<br />
                    Immer wieder taucht derselbe Name auf: „Der Schatten“.<br />
                    Niemand kennt sein Gesicht. Niemand weiß, wo er wohnt. Doch jeder Hinweis führt näher an ihn heran.</>,
                <>In einer letzten Operation locken Sie ihn in eine Falle – ein scheinbar einfacher Deal, doch diesmal sind Sie vorbereitet.<br />
                    Ohne Widerstand lässt er sich festnehmen.</>,
                <>Table-Town kann aufatmen. Für den Moment.<br />
                    Denn Sie wissen es besser, Detektiv <b>{user?.username}</b>.<br />
                    In Table-Town gibt es immer neue Schatten.</>
            ]
        },
        hard: {
            start: [
                <>Willkommen zurück in Table-Town, Detektiv <b>{user?.username}</b>.<br />
                    Nach der Festnahme vom „Schatten“ sank die Zahl der Verbrechen zwar rapide ab, aber immer noch nicht nahe dem Niveau, wo es vor einigen Monaten war.</>,
                <>Es ist zu vermuten, dass der „Schatten“, der momentan hinter Gittern sitzt, nicht der einzige Drahtzieher ist, sondern nur ein Teil des Puzzles.<br />
                    Er wurde verhört und nach der altbewährten Methode des Guten und Bösen Cops flossen die Antworten nur so aus seinem Mund.</>,
                <>Der, der bis jetzt als „Schatten“ bezeichnet wurde, gehört zu einer Organisation von Verbrechern, die sich „Shadowhand“ nennen.<br />
                    Die Bande besteht aus vielen Mitgliedern, jeder ein Verbrecher mit unterschiedlichen Talenten, jedoch weiß keiner der Mitglieder die wahre Identität seiner Partner, um sicherzustellen, dass keiner in der Lage ist, die anderen auszuliefern.</>,
                <>Zu diesem Zweck haben die 5 ranghöchsten von ihnen einen Codenamen, nämlich jeder hat sich einen Finger zu eigen gemacht.<br />
                    „Schatten“ ist innerhalb der Bande als „Kleiner“ benannt, und als wäre das Schicksal auf der Seite der Gerechtigkeit, erzählte „Kleiner“, dass sich die Gruppe zu höherer Vorsicht verschrieben hat, nachdem das Mitglied „Mittel“ vor einigen Monaten gefasst wurde.<br />
                    Es ist schwer anzuzweifeln und es handelt sich um keinen Zufall, der Zeitpunkt und die Taten stimmen überein – Max Berger ist „Mittel“.<br />
                    Dies bedeutet, dass nun 2 der 5 Finger in Gewahrsam sind.</>,
                <>Nun ist es an der Zeit, Ihr Können auf die Spitze zu bringen und die „Hand“ zu ergreifen und dieser Welle an Verbrechen ein Ende zu setzen!</>
            ],
            end: [
                <>Nach einer langen und anstrengenden Ermittlung ist es gelungen, „Daumen“ zu entlarven und hinter Gitter zu bringen.<br />
                    „Daumen“ – oder wie sie wirklich heißt, Clara Fall – wurde bei dem Versuch erwischt, die Region zu verlassen, ertappt, nachdem ihre Identität von dir entschlüsselt wurde.</>,
                <>Sie ist zwar nur 21, aber wurde bisher fünfmal vorbestraft und hatte daher schon oft mit der Polizei zu tun und war daher gelernt, dieser auszuweichen und auf freien Fuß zu bleiben.<br />
                    Leider hatte sie diesmal wenig Glück, da Detektiv <b>{user?.username}</b> auf den Fall angesetzt war.</>,
                <>Nachdem der Kopf der Schlange abgetrennt wurde, dauerte es natürlich nicht lange, bis der Körper aufhörte, sich zu regen.<br />
                    Da die Chefin nun hinter Gittern ist, wurden ohne ihre Leitung immer mehr Verbrecher gefasst und bestraft.<br />
                    Und nach nur wenigen Wochen war die Stadt so ruhig wie zuvor.</>,
                <>Nun, da Table-Town wieder eine ruhige Stadt war, war es wohl an der Zeit, sich zu entspannen und die Füße hoch zu legen.</>,
                <>Doch bevor die Entspannung beginnen konnte, platzte der Polizeichef durch die Tür Ihres Büros.</>,
                <>Polizeichef: „Es gibt einen neuen Fall, für den wir wieder Ihre Hilfe brauchen.“<br />
                    Detektiv <b>{user?.username}</b> wuchs nur ein kleines Grinsen ins Gesicht.<br />
                    {user?.username}: „Wenn das so ist, dann wird die Datenbank wohl wieder glühen müssen. Geben Sie mir die Details.“<br />
                    Zeit, sich an die Arbeit zu machen – sollen sie nur kommen, wir werden schon erfahren, wer sie sind.</>
            ]
        }
    };

    const texts = LEVEL_TEXTS[difficulty] || LEVEL_TEXTS["easy"];
    const slides = isEnd ? texts.end : texts.start;
    const detectiveImage = isEnd ? detectiveCongrats : detectiveGreeting;

    const [currentSlide, setCurrentSlide] = useState(0);

    function handleNext() {
        if (currentSlide < slides.length - 1) {
            setCurrentSlide((s) => s + 1);
        } else {
            setCurrentSlide(0);
            onClose();
        }
    }

    // Если закрыли диалог - сбрасываем слайд (чтобы всегда стартовал с первого)
    // Можешь заменить этот useEffect на другой подход, если у тебя другая логика открытия окна
    // useEffect(() => { if (!isVisible) setCurrentSlide(0); }, [isVisible]);

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
                            marginRight: -32,
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
                            marginBottom: 85,
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
                        {/* SVG-хвостик к голове */}
                        <svg
                            width="55"
                            height="48"
                            viewBox="0 0 55 48"
                            style={{
                                position: "absolute",
                                left: -44,
                                bottom: 44,
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
                        <div style={{ zIndex: 4, minHeight: 90 }}>
                            {slides[currentSlide]}
                        </div>
                        <div className="w-full flex justify-between items-center" style={{ marginTop: 18 }}>
                            <span className="text-xs text-gray-500">
                                {currentSlide + 1} / {slides.length}
                            </span>
                            <button
                                onClick={handleNext}
                                className="px-6 py-2 bg-blue-600 text-white rounded-xl text-base shadow hover:bg-blue-700 transition"
                            >
                                {currentSlide === slides.length - 1 ? "OK" : "Weiter"}
                            </button>
                        </div>
                    </div>
                </motion.div>
            )}
        </AnimatePresence>
    );
}
