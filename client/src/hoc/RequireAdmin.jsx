import {useSelector} from "react-redux";
import {Navigate} from "react-router-dom";

const RequireAdmin = ({children}) => {
    const role = useSelector((state) => state.auth.role);

    // if(role !== "ADMIN") {
    //     return <Navigate to="/" />
    // }

    console.log(role)

    return children;
}

export default RequireAdmin;
