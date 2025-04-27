import { useState } from "react";
import { useDispatch } from "react-redux";
import { Link, useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { setUser, setToken } from "../../features/auth/authSlice";
import { useLoginMutation } from "../../features/auth/authApi";

export default function SignIn() {
  const { t } = useTranslation();
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });
  const [login, { isLoading, error }] = useLoginMutation();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const data = await login(formData).unwrap();
      dispatch(
        setUser({
          nickname: data.nickname,
          points: data.points || 0,
          purchasedThemes: data.purchasedThemes || ["default"],
          email: data.email || "",
          firstname: data.firstname || "",
          lastname: data.lastname || "",
        })
      );
      dispatch(setToken(data.token)); //Save token
      navigate("/");
    } catch (err) {
      console.error("Login error:", err);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="bg-white p-6 rounded-lg shadow-lg w-full max-w-md sm:max-w-sm">
        <h2 className="text-3xl font-bold text-center mb-6">SIGN IN</h2>
        {error && <p className="text-red-500 text-center mb-4">{error}</p>}
        <form onSubmit={handleSubmit}>
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
          <div className="mb-6">
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
          <button
            type="submit"
            disabled={isLoading}
            className="w-full bg-green-500 text-white p-3 rounded-lg hover:bg-green-600 transition disabled:opacity-50"
          >
            {isLoading ? "Loading..." : t("signIn")}
          </button>
        </form>
        <p className="mt-4 text-center text-sm">
          {t("noAccount")}{" "}
          <Link to="/signup" className="text-blue-500 hover:underline">
            {t("register")}
          </Link>
        </p>
      </div>
    </div>
  );
}
