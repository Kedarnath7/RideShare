import { Link, useNavigate } from "react-router-dom";
import { User, Menu, X } from "lucide-react";
import { useAuth } from "../context/AuthContext";
import { useState } from "react";

const Navbar = () => {
    const { user, logout } = useAuth();
    const navigate = useNavigate();
    const [isOpen, setIsOpen] = useState(false);

    const handleLogout = () => {
        logout();
        navigate("/login");
        setIsOpen(false);
    };

    return (
        <nav className="bg-black text-white sticky top-0 z-50 h-16 flex items-center">
            <div className="container mx-auto px-6 w-full">
                <div className="flex justify-between items-center">
                    {/* Logo - Text based like Uber */}
                    <Link to="/" className="text-2xl font-bold tracking-tight">
                        RideShare
                    </Link>

                    {/* Desktop Menu */}
                    <div className="hidden md:flex items-center gap-8">
                        {user ? (
                            <>
                                <Link to="/search-ride" className="font-medium hover:text-gray-300 transition">Ride</Link>
                                <Link to="/create-ride" className="font-medium hover:text-gray-300 transition">Drive</Link>

                                <div className="flex items-center gap-3 ml-4">
                                    <div className="bg-white text-black w-8 h-8 rounded-full flex items-center justify-center font-bold">
                                        {user.fullName ? user.fullName[0].toUpperCase() : <User className="w-4 h-4"/>}
                                    </div>
                                    <button onClick={handleLogout} className="text-sm font-medium hover:text-gray-300">
                                        Logout
                                    </button>
                                </div>
                            </>
                        ) : (
                            <div className="flex items-center gap-6">
                                <Link to="/login" className="font-medium hover:text-gray-300">Log in</Link>
                                <Link to="/register" className="bg-white text-black px-4 py-2 rounded-full font-medium hover:bg-gray-200 transition">
                                    Sign up
                                </Link>
                            </div>
                        )}
                    </div>

                    {/* Mobile Menu Button */}
                    <div className="md:hidden">
                        <button onClick={() => setIsOpen(!isOpen)} className="text-white">
                            {isOpen ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
                        </button>
                    </div>
                </div>
            </div>

            {/* Mobile Dropdown */}
            {isOpen && (
                <div className="md:hidden bg-white text-black absolute top-16 left-0 w-full shadow-xl border-b border-gray-100">
                    <div className="flex flex-col p-4 space-y-4">
                        {user ? (
                            <>
                                <span className="font-bold text-xl px-2">Hi, {user.fullName}</span>
                                <Link to="/search-ride" onClick={() => setIsOpen(false)} className="text-lg font-medium px-2 py-2 hover:bg-gray-100 rounded-lg">Ride</Link>
                                <Link to="/create-ride" onClick={() => setIsOpen(false)} className="text-lg font-medium px-2 py-2 hover:bg-gray-100 rounded-lg">Drive</Link>
                                <button onClick={handleLogout} className="text-left text-lg font-medium px-2 py-2 text-red-600 hover:bg-gray-50 rounded-lg">Logout</button>
                            </>
                        ) : (
                            <>
                                <Link to="/login" onClick={() => setIsOpen(false)} className="text-lg font-medium px-2 py-2 hover:bg-gray-100 rounded-lg">Log in</Link>
                                <Link to="/register" onClick={() => setIsOpen(false)} className="text-lg font-medium px-2 py-2 hover:bg-gray-100 rounded-lg">Sign up</Link>
                            </>
                        )}
                    </div>
                </div>
            )}
        </nav>
    );
};

export default Navbar;