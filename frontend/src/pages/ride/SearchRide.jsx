import { useState, useEffect } from "react";
import { MapPin, ArrowRight, Clock, User, Circle, Square } from "lucide-react";
import api from "../../api/axiosConfig";
import { useAuth } from "../../context/AuthContext";
import RealMap from "../../components/RealMap";

const SearchRide = () => {
    const { user } = useAuth();
    const [search, setSearch] = useState({ source: "", destination: "" });
    const [rides, setRides] = useState([]);
    const [searched, setSearched] = useState(false);
    const [loading, setLoading] = useState(false);
    const [currentLocation, setCurrentLocation] = useState(null);
    const [mapPositions, setMapPositions] = useState({
        source: null,
        destination: null
    });

    // Request location permission on component mount
    useEffect(() => {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    const { latitude, longitude } = position.coords;
                    setCurrentLocation({
                        lat: latitude,
                        lng: longitude,
                        address: "Current Location"
                    });
                },
                (error) => {
                    console.log('Location permission denied or not available');
                }
            );
        }
    }, []);

    const handleLocationDetected = (location) => {
        setCurrentLocation(location);
        // Auto-fill source with current location
        if (!search.source) {
            setSearch(prev => ({ ...prev, source: location.address }));
            setMapPositions(prev => ({ ...prev, source: location }));
        }
    };

    const handleMapClick = (location) => {
        // If source is empty, set source, otherwise set destination
        if (!search.source) {
            setSearch(prev => ({ ...prev, source: location.address }));
            setMapPositions(prev => ({ ...prev, source: location }));
        } else if (!search.destination) {
            setSearch(prev => ({ ...prev, destination: location.address }));
            setMapPositions(prev => ({ ...prev, destination: location }));
        }
    };

    const setCurrentLocationAsSource = () => {
        if (currentLocation) {
            setSearch(prev => ({ ...prev, source: currentLocation.address }));
            setMapPositions(prev => ({ ...prev, source: currentLocation }));
        } else {
            alert("Current location not available. Please enable location services.");
        }
    };

    const handleSearch = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            // Include coordinates in search if available
            const searchParams = new URLSearchParams({
                source: search.source,
                destination: search.destination,
                ...(mapPositions.source && {
                    sourceLat: mapPositions.source.lat,
                    sourceLng: mapPositions.source.lng
                }),
                ...(mapPositions.destination && {
                    destinationLat: mapPositions.destination.lat,
                    destinationLng: mapPositions.destination.lng
                })
            });

            const res = await api.get(`/rides/search?${searchParams}`);
            const results = Array.isArray(res.data) ? res.data : (res.data.data || []);
            setRides(results);
            setSearched(true);
        } catch (err) {
            console.error(err);
            setRides([]);
        } finally {
            setLoading(false);
        }
    };

    const bookRide = async (rideId) => {
        if(!user) return alert("Please login to book.");
        if(!window.confirm("Confirm booking ($" + rides.find(r => r.id === rideId).pricePerSeat + ")?")) return;

        try {
            const passengerId = user.userId || user.id;
            await api.post(`/bookings?passengerId=${passengerId}`, { rideId, seats: 1 });
            alert("✅ Request sent to driver.");
            handleSearch({ preventDefault: () => {} });
        } catch (err) {
            alert("❌ Booking Failed: " + (err.response?.data?.message || "Unknown Error"));
        }
    };

    return (
        <div className="flex flex-col md:flex-row h-[calc(100vh-64px)] overflow-hidden">
            {/* LEFT SIDE: Search & Results */}
            <div className="w-full md:w-[450px] lg:w-[500px] flex flex-col bg-white border-r border-gray-200 shadow-xl z-10 relative">

                {/* Search Header Container */}
                <div className="p-6 bg-white border-b border-gray-100 shadow-sm z-20">
                    <h1 className="text-3xl font-bold mb-6">Get a ride</h1>

                    <form onSubmit={handleSearch} className="relative">
                        {/* Visual Connector Line */}
                        <div className="absolute left-[15px] top-[18px] bottom-[18px] w-[2px] bg-gray-200"></div>

                        {/* Source Input */}
                        <div className="relative mb-4 group">
                            <div className="absolute left-0 top-3.5 z-10">
                                <Circle className="w-3 h-3 bg-black text-black fill-current ml-2" />
                            </div>
                            <div className="flex gap-2">
                                <input
                                    type="text"
                                    placeholder="Pickup location"
                                    className="flex-1 pl-10 pr-4 py-3 bg-gray-100 rounded-lg focus:bg-white focus:ring-2 focus:ring-black outline-none transition font-medium text-sm"
                                    value={search.source}
                                    onChange={(e) => setSearch({...search, source: e.target.value})}
                                    required
                                />
                                <button
                                    type="button"
                                    onClick={setCurrentLocationAsSource}
                                    className="px-3 bg-blue-100 text-blue-700 rounded-lg hover:bg-blue-200 transition text-sm font-medium"
                                    title="Use Current Location"
                                >
                                    Locate
                                </button>
                            </div>
                        </div>

                        {/* Destination Input */}
                        <div className="relative mb-6 group">
                            <div className="absolute left-0 top-3.5 z-10">
                                <Square className="w-3 h-3 bg-black text-black fill-current ml-2" />
                            </div>
                            <input
                                type="text"
                                placeholder="Dropoff location"
                                className="w-full pl-10 pr-4 py-3 bg-gray-100 rounded-lg focus:bg-white focus:ring-2 focus:ring-black outline-none transition font-medium text-sm"
                                value={search.destination}
                                onChange={(e) => setSearch({...search, destination: e.target.value})}
                                required
                            />
                        </div>

                        <button
                            type="submit"
                            className="w-full bg-black text-white py-3.5 rounded-lg font-bold text-lg hover:bg-gray-800 transition"
                        >
                            {loading ? "Searching..." : "Search"}
                        </button>
                    </form>

                    {/* Location Instructions */}
                    <div className="mt-4 p-3 bg-gray-50 rounded-lg">
                        <p className="text-xs text-gray-600">
                            <strong>Tip:</strong> Use "Locate" for your current location or click on the map to set locations.
                        </p>
                    </div>
                </div>

                {/* Results List - Scrollable */}
                <div className="flex-1 overflow-y-auto p-4 space-y-2 bg-white">
                    {searched && rides.length === 0 && (
                        <div className="text-center py-10 text-gray-500">
                            No rides available for this route.
                        </div>
                    )}

                    {!searched && (
                        <div className="p-4 text-sm text-gray-500 font-medium">
                            Enter locations to see available rides.
                        </div>
                    )}

                    {rides.map((ride) => (
                        <div key={ride.id} className="group border border-transparent hover:border-black rounded-xl p-4 cursor-pointer transition bg-white hover:bg-gray-50 shadow-sm hover:shadow-md">
                            <div className="flex justify-between items-center">
                                {/* Left Info */}
                                <div className="flex items-start gap-4">
                                    <div className="w-16 h-16 bg-gray-200 rounded-lg flex items-center justify-center">
                                        <img src="https://cdn-icons-png.flaticon.com/512/741/741407.png" alt="car" className="w-12 opacity-80" />
                                    </div>
                                    <div>
                                        <div className="flex items-center gap-2">
                                            <h3 className="font-bold text-lg">RideShareX</h3>
                                            <div className="flex items-center text-xs text-gray-500 bg-gray-100 px-1.5 py-0.5 rounded">
                                                <User className="w-3 h-3 mr-1" /> {ride.availableSeats}
                                            </div>
                                        </div>
                                        <p className="text-sm text-gray-500 mt-1">{ride.carModel}</p>
                                        <p className="text-xs text-gray-400 mt-0.5">
                                            {new Date(ride.departureTime).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})} • Drop-off by 18:00
                                        </p>
                                        <p className="text-xs text-gray-500 mt-1 font-medium">Driver: {ride.driverName || "Verified Partner"}</p>
                                    </div>
                                </div>

                                {/* Right Price & Button */}
                                <div className="text-right flex flex-col items-end">
                                    <span className="text-xl font-bold mb-2">${ride.pricePerSeat}</span>
                                    <button
                                        onClick={() => bookRide(ride.id)}
                                        className="bg-black text-white text-sm font-bold px-4 py-2 rounded-lg opacity-0 group-hover:opacity-100 transition-opacity"
                                    >
                                        Book
                                    </button>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>

            {/* RIGHT SIDE: Map */}
            <div className="hidden md:block flex-1 bg-gray-100 h-full">
                <RealMap
                    onLocationDetected={handleLocationDetected}
                    onMapClick={handleMapClick}
                    sourcePosition={mapPositions.source}
                    destinationPosition={mapPositions.destination}
                />
            </div>
        </div>
    );
};

export default SearchRide;