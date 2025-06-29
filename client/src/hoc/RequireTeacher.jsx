import { useSelector } from "react-redux";
import { Navigate } from "react-router-dom";

export default function RequireTeacher({ children }) {
    const { role, isAuthLoading } = useSelector(state => state.auth);

    if (isAuthLoading) {
        return <div className="flex justify-center items-center h-full">Loading...</div>;
    }
    if (role !== "TEACHER") {
        return <Navigate to="/" />;
    }
    return children;
}
