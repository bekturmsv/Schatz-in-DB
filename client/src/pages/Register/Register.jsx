import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { toast } from "sonner";
import { useRegisterMutation, useGetSpecializationsQuery } from "../../features/auth/authApi.js";
import { motion } from "framer-motion";

export default function Register() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const [register, { isLoading: isRegisterLoading, error: registerError }] = useRegisterMutation();
  const { data: specializations, isLoading: isSpecializationsLoading, error: specializationsError } = useGetSpecializationsQuery();

  const [formData, setFormData] = useState({
    username: "",
    password: "",
    email: "",
    firstName: "",
    lastName: "",
    matriculationNumber: "",
    specialistGroup: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: name === "matriculationNumber" ? value.replace(/\D/, "") : value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.specialistGroup) {
      toast.error(t("selectSpecializationRequired"));
      return;
    }
    try {
      await register({ ...formData, matriculationNumber: Number(formData.matriculationNumber) }).unwrap();
      toast.success(t("registrationSuccessful"));
      navigate("/login");
    } catch (err) {
      const errorMessage = err?.data?.message || err?.message || "Unknown error";
      toast.error(t("registrationFailed") + ": " + errorMessage);
      console.error("Registration error details:", err);
    }
  };

  // Анимация появления для полей
  const fieldVariants = {
    hidden: { opacity: 0, y: 32 },
    visible: (i = 1) => ({
      opacity: 1,
      y: 0,
      transition: { delay: i * 0.1, duration: 0.6, ease: "easeOut" },
    }),
  };

  return (
      <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-green-100 via-cyan-100 to-blue-200 font-mono relative">
        {/* Фоновая волна/блюр */}
        <div className="absolute top-0 left-1/2 -translate-x-1/2 w-[65vw] h-[38vh] bg-gradient-to-tr from-green-200 via-blue-200 to-green-100 opacity-40 blur-2xl rounded-full pointer-events-none z-0"></div>

        <motion.form
            initial={{ opacity: 0, scale: 0.98, y: 34 }}
            animate={{ opacity: 1, scale: 1, y: 0 }}
            transition={{ duration: 0.7, ease: "easeOut" }}
            onSubmit={handleSubmit}
            className="relative z-10 bg-white/90 shadow-2xl rounded-2xl px-8 py-10 w-full max-w-lg flex flex-col items-center"
            autoComplete="off"
        >
          <motion.h1
              initial={{ opacity: 0, y: -24 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.1, duration: 0.7 }}
              className="text-3xl md:text-4xl font-extrabold mb-8 bg-gradient-to-r from-green-500 via-blue-500 to-cyan-400 bg-clip-text text-transparent uppercase tracking-wider"
          >
            {t("register")}
          </motion.h1>
          {registerError && (
              <motion.p
                  initial={{ opacity: 0 }}
                  animate={{ opacity: 1 }}
                  className="text-red-500 text-center mb-4"
              >
                {t("registrationFailed")}: {registerError?.data?.message || registerError?.message}
              </motion.p>
          )}
          {specializationsError && (
              <motion.p
                  initial={{ opacity: 0 }}
                  animate={{ opacity: 1 }}
                  className="text-red-500 text-center mb-4"
              >
                {t("failedToLoadSpecializations")}: {specializationsError?.data?.message || specializationsError?.message}
              </motion.p>
          )}
          <div className="space-y-4 w-full">
            <motion.input
                variants={fieldVariants}
                initial="hidden"
                animate="visible"
                custom={1}
                type="text"
                name="username"
                value={formData.username}
                onChange={handleChange}
                placeholder={t("username")}
                className="w-full p-3 border-2 border-blue-100 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400 transition"
                disabled={isRegisterLoading}
                autoFocus
                required
            />
            <motion.input
                variants={fieldVariants}
                initial="hidden"
                animate="visible"
                custom={2}
                type="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                placeholder={t("password")}
                className="w-full p-3 border-2 border-blue-100 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400 transition"
                disabled={isRegisterLoading}
                required
            />
            <motion.input
                variants={fieldVariants}
                initial="hidden"
                animate="visible"
                custom={3}
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                placeholder={t("email")}
                className="w-full p-3 border-2 border-blue-100 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400 transition"
                disabled={isRegisterLoading}
                required
            />
            <div className="flex flex-col md:flex-row gap-4">
              <motion.input
                  variants={fieldVariants}
                  initial="hidden"
                  animate="visible"
                  custom={4}
                  type="text"
                  name="firstName"
                  value={formData.firstName}
                  onChange={handleChange}
                  placeholder={t("firstName")}
                  className="w-full p-3 border-2 border-blue-100 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400 transition"
                  disabled={isRegisterLoading}
                  required
              />
              <motion.input
                  variants={fieldVariants}
                  initial="hidden"
                  animate="visible"
                  custom={5}
                  type="text"
                  name="lastName"
                  value={formData.lastName}
                  onChange={handleChange}
                  placeholder={t("lastName")}
                  className="w-full p-3 border-2 border-blue-100 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400 transition"
                  disabled={isRegisterLoading}
                  required
              />
            </div>
            <motion.input
                variants={fieldVariants}
                initial="hidden"
                animate="visible"
                custom={6}
                type="text"
                name="matriculationNumber"
                inputMode="numeric"
                value={formData.matriculationNumber}
                onChange={handleChange}
                placeholder={t("matriculationNumber")}
                className="w-full p-3 border-2 border-blue-100 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400 transition"
                disabled={isRegisterLoading}
                required
            />
            <motion.select
                variants={fieldVariants}
                initial="hidden"
                animate="visible"
                custom={7}
                name="specialistGroup"
                value={formData.specialistGroup}
                onChange={handleChange}
                className="w-full p-3 border-2 border-blue-100 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400 transition bg-white"
                disabled={isRegisterLoading || isSpecializationsLoading}
                required
            >
              <option value="" disabled>
                {isSpecializationsLoading ? t("loading") : t("selectSpecialization")}
              </option>

              {specializations?.map((specialization, index) => (
                  <option key={index} value={specialization}>
                    {specialization}
                  </option>
              ))}
            </motion.select>
            <motion.button
                whileHover={{ scale: 1.04, backgroundColor: "#22c55e", color: "#fff" }}
                whileTap={{ scale: 0.97 }}
                type="submit"
                className="w-full bg-gradient-to-r from-green-500 to-cyan-400 text-white py-3 rounded-xl font-bold text-lg shadow-lg hover:from-green-400 hover:to-green-600 transition disabled:bg-gray-400"
                disabled={isRegisterLoading}
            >
              {isRegisterLoading ? t("loading") : t("register")}
            </motion.button>
            <motion.p
                initial={{ opacity: 0, y: 12 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.18, duration: 0.5 }}
                className="text-center text-gray-600"
            >
              {t("alreadyHaveAccount")}{" "}
              <span
                  className="text-green-500 hover:underline font-bold cursor-pointer"
                  onClick={() => navigate("/login")}
              >
              {t("login")}
            </span>
            </motion.p>
          </div>
        </motion.form>
      </div>
  );
}
