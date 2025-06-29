import { useSelector } from "react-redux";
import { Navigate } from "react-router-dom";

const RequireAdmin = ({ children }) => {
    const { role, isAuthLoading } = useSelector(state => state.auth);

    // Пока идёт загрузка — ничего не рендерим, только спиннер/лоадер
    if (isAuthLoading) {
        return <div className="flex justify-center items-center h-full">Loading...</div>;
    }

    if (role !== "ADMIN") {
        return <Navigate to="/" />;
    }

    return children;
};

export default RequireAdmin;
