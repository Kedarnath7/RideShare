import React, { useEffect } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import Navbar from "./components/Navbar";
import ProtectedRoute from "./components/ProtectedRoute";
import Login from "./pages/auth/Login";
import Register from "./pages/auth/Register";
import Home from "./pages/Home";
import CreateRide from "./pages/ride/CreateRide";
import SearchRide from "./pages/ride/SearchRide";

function App() {
     useEffect(() => {
             // Change the tab title
             document.title = "RideShare";

             const link = document.createElement("link");
             link.rel = "icon";
             link.href = "/favicon.ico";
             document.head.appendChild(link);
         }, []);
    return (
        <AuthProvider>
            <Router>
                <div className="min-h-screen flex flex-col font-sans bg-white text-slate-900">
                    <Navbar />
                    <main className="flex-grow">
                        <Routes>
                            <Route path="/" element={<Home />} />
                            <Route path="/login" element={<Login />} />
                            <Route path="/register" element={<Register />} />

                            {/* Protected Routes */}
                            <Route path="/create-ride" element={
                                <ProtectedRoute>
                                    <CreateRide />
                                </ProtectedRoute>
                            } />
                            <Route path="/search-ride" element={
                                <ProtectedRoute>
                                    <SearchRide />
                                </ProtectedRoute>
                            } />
                        </Routes>
                    </main>
                    <footer className="bg-black text-white py-12 px-6 mt-auto">
                        <div className="container mx-auto">
                            <h2 className="text-2xl font-bold mb-6">RideShare</h2>
                            <div className="grid md:grid-cols-4 gap-8 text-sm text-gray-400">
                                <div>
                                    <h3 className="text-white font-medium mb-4">Company</h3>
                                    <ul className="space-y-2">
                                        <li>About us</li>
                                        <li>Our offerings</li>
                                        <li>Newsroom</li>
                                        <li>Investors</li>
                                        <li>Blog</li>
                                    </ul>
                                </div>
                                <div>
                                    <h3 className="text-white font-medium mb-4">Products</h3>
                                    <ul className="space-y-2">
                                        <li>Ride</li>
                                        <li>Drive</li>
                                        <li>Deliver</li>
                                        <li>Eat</li>
                                        <li>Business</li>
                                    </ul>
                                </div>
                                <div>
                                    <h3 className="text-white font-medium mb-4">Global citizenship</h3>
                                    <ul className="space-y-2">
                                        <li>Safety</li>
                                        <li>Diversity and Inclusion</li>
                                        <li>Sustainability</li>
                                    </ul>
                                </div>
                                <div>
                                    <h3 className="text-white font-medium mb-4">Travel</h3>
                                    <ul className="space-y-2">
                                        <li>Airports</li>
                                        <li>Cities</li>
                                    </ul>
                                </div>
                            </div>
                            <div className="mt-12 pt-8 border-t border-gray-800 flex flex-col md:flex-row justify-between items-center gap-4 text-xs text-gray-500">
                                <p>© 2025 RideShare Technologies Inc.</p>
                                <div className="flex gap-6">
                                    <span>Privacy</span>
                                    <span>Accessibility</span>
                                    <span>Terms</span>
                                </div>
                            </div>
                        </div>
                    </footer>
                </div>
            </Router>
        </AuthProvider>
    );
}

export default App;