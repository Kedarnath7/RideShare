import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../../api/axiosConfig";

const Register = () => {
    const [formData, setFormData] = useState({
        fullName: "",
        email: "",
        password: "",
        phone: "",
        roles: "PASSENGER"
    });
    const navigate = useNavigate();
    const [isLoading, setIsLoading] = useState(false);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsLoading(true);
        try {
            await api.post("/auth/register", formData);
            alert("Account Created! Please Login.");
            navigate("/login");
        } catch (err) {
            alert(err.response?.data?.message || "Failed to register");
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="min-h-[calc(100vh-64px)] flex items-center justify-center bg-white px-4 py-12">
            <div className="w-full max-w-[400px]">
                <h2 className="text-2xl font-medium mb-6">Sign up to ride</h2>

                <form onSubmit={handleSubmit} className="space-y-4">
                    <div className="flex gap-4">
                        <input
                            type="text"
                            name="fullName"
                            value={formData.fullName}
                            placeholder="Full Name"
                            className="w-full bg-gray-100 p-3 rounded-lg focus:bg-white focus:ring-2 focus:ring-black outline-none transition"
                            onChange={handleChange}
                            required
                        />
                        <input
                            type="text"
                            name="phone"
                            value={formData.phone}
                            placeholder="Phone"
                            className="w-full bg-gray-100 p-3 rounded-lg focus:bg-white focus:ring-2 focus:ring-black outline-none transition"
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <input
                        type="email"
                        name="email"
                        value={formData.email}
                        placeholder="Enter your email"
                        className="w-full bg-gray-100 p-3 rounded-lg focus:bg-white focus:ring-2 focus:ring-black outline-none transition"
                        onChange={handleChange}
                        required
                    />

                    <input
                        type="password"
                        name="password"
                        value={formData.password}
                        placeholder="Create a password"
                        className="w-full bg-gray-100 p-3 rounded-lg focus:bg-white focus:ring-2 focus:ring-black outline-none transition"
                        onChange={handleChange}
                        required
                    />

                    <div className="pt-2">
                        <label className="block text-sm font-medium text-gray-700 mb-2">I want to...</label>
                        <select
                            name="roles"
                            value={formData.roles}
                            onChange={handleChange}
                            className="w-full bg-gray-100 p-3 rounded-lg focus:bg-white focus:ring-2 focus:ring-black outline-none transition appearance-none cursor-pointer"
                        >
                            <option value="PASSENGER">Go somewhere (Passenger)</option>
                            <option value="DRIVER">Drive (Driver)</option>
                        </select>
                    </div>

                    <button
                        type="submit"
                        disabled={isLoading}
                        className="w-full bg-black text-white py-3 rounded-lg font-bold hover:bg-gray-800 transition mt-6 disabled:opacity-70"
                    >
                        {isLoading ? "Creating Account..." : "Sign Up"}
                    </button>
                </form>

                <div className="mt-8 text-center text-sm">
                    <p className="text-gray-500">Already have an account?</p>
                    <Link to="/login" className="font-medium underline mt-2 inline-block">Log in</Link>
                </div>

                <p className="text-xs text-gray-400 mt-8 text-center">
                    By proceeding, you consent to get calls, WhatsApp or SMS messages, including by automated means, from RideShare and its affiliates to the number provided.
                </p>
            </div>
        </div>
    );
};

export default Register;