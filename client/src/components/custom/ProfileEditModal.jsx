// src/components/ProfileEditModal.jsx
import { useState, useEffect } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { Button } from "@/components/ui/button";
import { useTranslation } from "react-i18next";
import { useUpdatePlayerMutation } from "@/features/profile/profileApi";
import { toast } from "sonner";
import { useGetMeQuery } from "@/features/auth/authApi.js";

export default function ProfileEditModal({ user, open, onClose }) {
    const { t } = useTranslation();
    const [form, setForm] = useState({
        firstName: "",
        lastName: "",
        username: "",
        email: "",
    });
    const [updatePlayer, { isLoading }] = useUpdatePlayerMutation();

    const { refetch: refetchMe } = useGetMeQuery(undefined, { skip: true });

    useEffect(() => {
        if (user && open) {
            setForm({
                firstName: user.firstName || "",
                lastName: user.lastName || "",
                username: user.username || user.nickname || "",
                email: user.email || "",
            });
        }
    }, [user, open]);

    const handleChange = (e) => {
        setForm((prev) => ({
            ...prev,
            [e.target.name]: e.target.value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!form.firstName || !form.lastName || !form.username || !form.email) {
            toast.error(t("allFieldsRequired"));
            return;
        }
        try {
            await updatePlayer({
                id: user.id,
                data: {
                    firstName: form.firstName,
                    lastName: form.lastName,
                    username: form.username,
                    email: form.email,
                },
            }).unwrap();
            await refetchMe();
            toast.success(t("profileUpdated") );
            onClose();
        } catch (err) {
            toast.error(
                t("updateFailed")
            );
        }
    };

    return (
        <AnimatePresence>
            {open && (
                <motion.div
                    className="fixed inset-0 bg-black/40 z-50 flex items-center justify-center"
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    exit={{ opacity: 0 }}
                    style={{ backdropFilter: "blur(2px)" }}
                >
                    <motion.div
                        className="custom-card p-8 rounded-2xl shadow-2xl w-[95vw] max-w-md relative"
                        initial={{ scale: 0.93, y: 48, opacity: 0 }}
                        animate={{ scale: 1, y: 0, opacity: 1 }}
                        exit={{ scale: 0.93, y: 48, opacity: 0 }}
                        transition={{ type: "spring", stiffness: 350, damping: 30 }}
                    >
                        <button
                            className="absolute top-3 right-4 text-xl font-bold text-[var(--color-secondary)] hover:scale-125 transition"
                            onClick={onClose}
                            aria-label="Close"
                            tabIndex={0}
                            style={{
                                background: "none",
                                border: "none",
                                cursor: "pointer",
                            }}
                        >
                            &times;
                        </button>
                        <h2
                            className="custom-title mb-5 text-center"
                            style={{ fontSize: "2rem" }}
                        >
                            {t("editProfile") }
                        </h2>
                        <form className="flex flex-col gap-4" onSubmit={handleSubmit}>
                            <div>
                                <label className="custom-font mb-1 block" htmlFor="firstName">
                                    {t("firstName")}
                                </label>
                                <input
                                    className="custom-input w-full"
                                    id="firstName"
                                    name="firstName"
                                    value={form.firstName}
                                    onChange={handleChange}
                                    autoComplete="off"
                                    required
                                />
                            </div>
                            <div>
                                <label className="custom-font mb-1 block" htmlFor="lastName">
                                    {t("lastName")}
                                </label>
                                <input
                                    className="custom-input w-full"
                                    id="lastName"
                                    name="lastName"
                                    value={form.lastName}
                                    onChange={handleChange}
                                    autoComplete="off"
                                    required
                                />
                            </div>
                            <div>
                                <label className="custom-font mb-1 block" htmlFor="username">
                                    {t("username")}
                                </label>
                                <input
                                    className="custom-input w-full"
                                    id="username"
                                    name="username"
                                    value={form.username}
                                    onChange={handleChange}
                                    autoComplete="off"
                                    required
                                />
                            </div>
                            <div>
                                <label className="custom-font mb-1 block" htmlFor="email">
                                    {t("email")}
                                </label>
                                <input
                                    className="custom-input w-full"
                                    type="email"
                                    id="email"
                                    name="email"
                                    value={form.email}
                                    onChange={handleChange}
                                    autoComplete="off"
                                    required
                                />
                            </div>
                            <Button
                                className="custom-btn mt-3"
                                type="submit"
                                disabled={isLoading}
                            >
                                {isLoading
                                    ? t("saving")
                                    : t("saveChanges") }
                            </Button>
                        </form>
                    </motion.div>
                </motion.div>
            )}
        </AnimatePresence>
    );
}
