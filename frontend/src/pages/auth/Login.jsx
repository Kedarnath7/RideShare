import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import api from "../../api/axiosConfig";

const Login = () => {
    const [formData, setFormData] = useState({ email: "", password: "" });
    const [error, setError] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsLoading(true);
        setError("");
        try {
            const response = await api.post("/auth/login", formData);
            if (response.data.success || response.data.token) {
                const data = response.data.data || response.data;
                const { token, ...userData } = data;
                login(token, userData);
                navigate("/");
            }
        } catch (err) {
            setError("Invalid credentials");
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="min-h-[calc(100vh-64px)] flex items-center justify-center bg-white px-4">
            <div className="w-full max-w-[400px]">
                <h2 className="text-2xl font-medium mb-6">What's your email and password?</h2>

                {error && <div className="bg-red-50 text-red-600 p-3 rounded mb-4 text-sm">{error}</div>}

                <form onSubmit={handleSubmit} className="space-y-4">
                    <input
                        type="email"
                        placeholder="Enter your email"
                        className="w-full bg-gray-100 p-3 rounded-lg focus:bg-white focus:ring-2 focus:ring-black outline-none transition"
                        onChange={(e) => setFormData({...formData, email: e.target.value})}
                        required
                    />
                    <input
                        type="password"
                        placeholder="Enter your password"
                        className="w-full bg-gray-100 p-3 rounded-lg focus:bg-white focus:ring-2 focus:ring-black outline-none transition"
                        onChange={(e) => setFormData({...formData, password: e.target.value})}
                        required
                    />

                    <button
                        type="submit"
                        disabled={isLoading}
                        className="w-full bg-black text-white py-3 rounded-lg font-bold hover:bg-gray-800 transition flex justify-center mt-6"
                    >
                        {isLoading ? "Loading..." : "Log in"}
                    </button>
                </form>

                <div className="mt-8 text-center text-sm">
                    <p className="text-gray-500">Don't have an account?</p>
                    <Link to="/register" className="font-medium underline mt-2 inline-block">Sign up</Link>
                </div>
            </div>
        </div>
    );
};

export default Login;