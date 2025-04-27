import { useState } from "react";
import { useDispatch } from "react-redux";
import { Link, useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { setUser, setToken } from "../../features/auth/authSlice";
import { useRegisterMutation } from "../../features/auth/authApi";

export default function SignUp() {
  const { t } = useTranslation();
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    firstname: "",
    lastname: "",
    email: "",
    nickname: "",
    password: "",
    repeatPassword: "",
  });
  const [register, { isLoading, error }] = useRegisterMutation();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (formData.password !== formData.repeatPassword) {
      alert("Passwords do not match");
      return;
    }
    try {
      const userData = {
        firstname: formData.firstname,
        lastname: formData.lastname,
        email: formData.email,
        nickname: formData.nickname,
        password: formData.password,
      };
      const data = await register(userData).unwrap();
      dispatch(
        setUser({
          nickname: formData.nickname,
          points: 0,
          purchasedThemes: ["default"],
          email: formData.email,
          firstname: formData.firstname,
          lastname: formData.lastname,
        })
      );
      dispatch(setToken(data.token)); // Save token
      navigate("/signin");
    } catch (err) {
      console.error("Registration error:", err);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="bg-white p-6 rounded-lg shadow-lg w-full max-w-md sm:max-w-sm">
        <h2 className="text-3xl font-bold text-center mb-6">SIGN UP</h2>
        {error && <p className="text-red-500 text-center mb-4">{error}</p>}
        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <input
              type="text"
              name="firstname"
              value={formData.firstname}
              onChange={handleChange}
              placeholder={t("firstname")}
              className="w-full p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
              required
            />
          </div>
          <div className="mb-4">
            <input
              type="text"
              name="lastname"
              value={formData.lastname}
              onChange={handleChange}
              placeholder={t("lastname")}
              className="w-full p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
              required
            />
          </div>
          <div className="mb-4">
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              placeholder={t("email")}
              className="w-full p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
              required
            />
          </div>
          <div className="mb-4">
            <input
              type="text"
              name="nickname"
              value={formData.nickname}
              onChange={handleChange}
              placeholder={t("nickname")}
              className="w-full p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
              required
            />
          </div>
          <div className="mb-4">
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              placeholder={t("password")}
              className="w-full p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
              required
            />
          </div>
          <div className="mb-6">
            <input
              type="password"
              name="repeatPassword"
              value={formData.repeatPassword}
              onChange={handleChange}
              placeholder={t("repeatPassword")}
              className="w-full p-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
              required
            />
          </div>
          <button
            type="submit"
            disabled={isLoading}
            className="w-full bg-green-500 text-white p-3 rounded-lg hover:bg-green-600 transition disabled:opacity-50"
          >
            {isLoading ? "Loading..." : t("signUp")}
          </button>
        </form>
        <p className="mt-4 text-center text-sm">
          {t("alreadyHaveAccount")}{" "}
          <Link to="/signin" className="text-blue-500 hover:underline">
            {t("login")}
          </Link>
        </p>
      </div>
    </div>
  );
}
