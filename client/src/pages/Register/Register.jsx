import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { toast } from "sonner";
import { useRegisterMutation, useGetSpecializationsQuery } from "../../features/auth/authApi.js";

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
    matriculationNumber: 0,
    specialistGroup: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: name === "matriculationNumber" ? parseInt(value) || 0 : value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.specialistGroup) {
      toast.error(t("selectSpecializationRequired"));
      return;
    }
    try {
      await register(formData).unwrap();
      toast.success(t("registrationSuccessful"));
      navigate("/login");
    } catch (err) {
      const errorMessage = err?.data?.message || err?.message || "Unknown error";
      toast.error(t("registrationFailed") + ": " + errorMessage);
      console.error("Registration error details:", err);
    }
  };

  return (
      <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100 font-mono">
        <div className="bg-white p-8 rounded-lg shadow-md w-full max-w-md">
          <h1 className="text-3xl font-bold mb-6 text-center text-black uppercase">
            {t("register")}
          </h1>
          {registerError && (
              <p className="text-red-500 text-center mb-4">
                {t("registrationFailed")}: {registerError?.data?.message || registerError?.message}
              </p>
          )}
          {specializationsError && (
              <p className="text-red-500 text-center mb-4">
                {t("failedToLoadSpecializations")}: {specializationsError?.data?.message || specializationsError?.message}
              </p>
          )}
          <div className="space-y-4">
            <input
                type="text"
                name="username"
                value={formData.username}
                onChange={handleChange}
                placeholder={t("username")}
                className="w-full p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400"
                disabled={isRegisterLoading}
            />
            <input
                type="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                placeholder={t("password")}
                className="w-full p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400"
                disabled={isRegisterLoading}
            />
            <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                placeholder={t("email")}
                className="w-full p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400"
                disabled={isRegisterLoading}
            />
            <input
                type="text"
                name="firstName"
                value={formData.firstName}
                onChange={handleChange}
                placeholder={t("firstName")}
                className="w-full p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400"
                disabled={isRegisterLoading}
            />
            <input
                type="text"
                name="lastName"
                value={formData.lastName}
                onChange={handleChange}
                placeholder={t("lastName")}
                className="w-full p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400"
                disabled={isRegisterLoading}
            />
            <input
                type="number"
                name="matriculationNumber"
                value={formData.matriculationNumber}
                onChange={handleChange}
                placeholder={t("matriculationNumber")}
                className="w-full p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400"
                disabled={isRegisterLoading}
            />
            <select
                name="specialistGroup"
                value={formData.specialistGroup}
                onChange={handleChange}
                className="w-full p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400"
                disabled={isRegisterLoading || isSpecializationsLoading}
            >
              <option value="" disabled>
                {isSpecializationsLoading ? t("loading") : t("selectSpecialization")}
              </option>
              {specializations?.map((specialization, index) => (
                  <option key={index} value={specialization}>
                    {specialization}
                  </option>
              ))}
            </select>
            <button
                onClick={handleSubmit}
                className="w-full bg-green-500 text-white py-3 rounded-lg hover:bg-green-600 transition disabled:bg-gray-400"
                disabled={isRegisterLoading}
            >
              {isRegisterLoading ? t("loading") : t("register")}
            </button>
            <p className="text-center text-gray-600">
              {t("alreadyHaveAccount")}{" "}
              <a href="/login" className="text-green-500 hover:underline">
                {t("login")}
              </a>
            </p>
          </div>
        </div>
      </div>
  );
}