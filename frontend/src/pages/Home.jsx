import { Link } from "react-router-dom";
import { ArrowRight, Car, Users, Shield, MapPin, Clock } from "lucide-react";

const Home = () => {
    return (
        <div className="min-h-[calc(100vh-64px)] bg-gray-150 text-black flex flex-col items-center justify-center">

            <div className="container mx-auto px-6 py-12 md:py-20 max-w-5xl">

                {/* 1. Main Headings */}
                <div className="text-center space-y-6 mb-16">
                    <h1 className="text-5xl md:text-7xl font-bold leading-tight tracking-tight">
                        Tap. Ride. Done.
                    </h1>
                    <p className="text-xl text-gray-600 max-w-2xl mx-auto">
                        Your ride. Your schedule. Your city.
                    </p>
                </div>

                {/* 2. Action Cards */}
                <div className="grid md:grid-cols-2 gap-6 mb-20">

                    {/* Ride Card */}
                    <Link
                        to="/search-ride"
                        className="group bg-black text-white p-8 rounded-xl flex flex-col justify-between min-h-[240px] hover:bg-gray-900 transition-colors duration-300"
                    >
                        <div className="flex justify-between items-start">
                            <div className="w-14 h-14 bg-white text-black rounded-full flex items-center justify-center">
                                <Users className="w-6 h-6" />
                            </div>
                            <ArrowRight className="w-8 h-8 group-hover:translate-x-2 transition-transform duration-300" />
                        </div>
                        <div>
                            <h2 className="text-3xl font-bold mb-2">Ride</h2>
                            <p className="text-gray-300 font-medium text-lg">
                                Get where you need to go.
                            </p>
                        </div>
                    </Link>

                    {/* Drive Card */}
                    <Link
                        to="/create-ride"
                        className="group bg-gray-100 text-black p-8 rounded-xl flex flex-col justify-between min-h-[240px] hover:bg-gray-200 transition-colors duration-300"
                    >
                        <div className="flex justify-between items-start">
                            <div className="w-14 h-14 bg-black text-white rounded-full flex items-center justify-center">
                                <Car className="w-6 h-6" />
                            </div>
                            <ArrowRight className="w-8 h-8 group-hover:translate-x-2 transition-transform duration-300" />
                        </div>
                        <div>
                            <h2 className="text-3xl font-bold mb-2">Drive</h2>
                            <p className="text-gray-600 font-medium text-lg">
                                Earn on your own time.
                            </p>
                        </div>
                    </Link>
                </div>

                {/* 3. Features */}
                <div className="grid md:grid-cols-3 gap-12 text-center border-t border-gray-200 pt-16">
                    <div className="flex flex-col items-center space-y-4">
                        <div className="bg-black p-4 rounded-full">
                            <Shield className="w-6 h-6 text-white" />
                        </div>
                        <h3 className="font-bold text-lg">Safety first</h3>
                        <p className="text-gray-600 text-sm max-w-xs">
                            Verified drivers and real-time tracking for peace of mind.
                        </p>
                    </div>

                    <div className="flex flex-col items-center space-y-4">
                        <div className="bg-black p-4 rounded-full">
                            <Clock className="w-6 h-6 text-white" />
                        </div>
                        <h3 className="font-bold text-lg">Always on time</h3>
                        <p className="text-gray-600 text-sm max-w-xs">
                            Schedule ahead so you’ll never run late.
                        </p>
                    </div>

                    <div className="flex flex-col items-center space-y-4">
                        <div className="bg-black p-4 rounded-full">
                            <MapPin className="w-6 h-6 text-white" />
                        </div>
                        <h3 className="font-bold text-lg">Live updates</h3>
                        <p className="text-gray-600 text-sm max-w-xs">
                            Share your trip status instantly with loved ones.
                        </p>
                    </div>
                </div>

            </div>
        </div>
    );
};

export default Home;
