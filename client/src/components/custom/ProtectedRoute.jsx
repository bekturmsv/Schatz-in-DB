import { useSelector } from "react-redux";
import { Navigate } from "react-router-dom";

const ProtectedRoute = ({ children }) => {
  const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);
  const isAuthLoading = useSelector((state) => state.auth.isAuthLoading);

  // Пока авторизация загружается, просто ничего не рендерим
  if (isAuthLoading) return null;

  if (!isAuthenticated) {
    return <Navigate to="/login" />;
  }
  return children;
};

export default ProtectedRoute;
