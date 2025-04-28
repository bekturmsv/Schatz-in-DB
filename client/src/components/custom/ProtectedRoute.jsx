import { useSelector } from "react-redux";
import { Navigate } from "react-router-dom";

const ProtectedRoute = ({ children }) => {
  const isAuthewnticated = useSelector((state) => state.auth.isAuthenticated);
  if (!isAuthewnticated) {
    return <Navigate to="/login" />;
  }
  return children;
};

export default ProtectedRoute;
