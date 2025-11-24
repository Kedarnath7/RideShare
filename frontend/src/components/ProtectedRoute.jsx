import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

const ProtectedRoute = ({ children }) => {
    const { token } = useAuth();

    // If token exists, render the child component (e.g., CreateRide)
    // If not, redirect the user to the Login page
    return token ? children : <Navigate to="/login" />;
};

export default ProtectedRoute;