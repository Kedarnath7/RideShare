import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import api from "../../api/axiosConfig";
import RealMap from "../../components/RealMap";

const CreateRide = () => {
    const { user } = useAuth();
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const [currentLocation, setCurrentLocation] = useState(null);

    // State
    const [formData, setFormData] = useState({
        source: "",
        destination: "",
        departureTime: "",
        pricePerSeat: "",
        availableSeats: "",
        carModel: ""
    });

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
                },
                {
                    enableHighAccuracy: true,
                    timeout: 10000,
                    maximumAge: 60000
                }
            );
        }
    }, []);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleLocationDetected = (location) => {
        setCurrentLocation(location);
        // Auto-fill source with current location
        if (!formData.source) {
            setFormData(prev => ({ ...prev, source: location.address }));
            setMapPositions(prev => ({ ...prev, source: location }));
        }
    };

    const handleMapClick = (location) => {
        // If source is empty, set source, otherwise set destination
        if (!formData.source) {
            setFormData(prev => ({ ...prev, source: location.address }));
            setMapPositions(prev => ({ ...prev, source: location }));
        } else if (!formData.destination) {
            setFormData(prev => ({ ...prev, destination: location.address }));
            setMapPositions(prev => ({ ...prev, destination: location }));
        }
    };

    const setCurrentLocationAsSource = () => {
        if (currentLocation) {
            setFormData(prev => ({ ...prev, source: currentLocation.address }));
            setMapPositions(prev => ({ ...prev, source: currentLocation }));
        } else {
            alert("Current location not available. Please enable location services.");
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            const driverId = user.userId || user.id;

            // Include coordinates if available
            const rideData = {
                ...formData,
                ...(mapPositions.source && {
                    sourceLat: mapPositions.source.lat,
                    sourceLng: mapPositions.source.lng
                }),
                ...(mapPositions.destination && {
                    destinationLat: mapPositions.destination.lat,
                    destinationLng: mapPositions.destination.lng
                })
            };

            await api.post(`/rides?driverId=${driverId}`, rideData);
            alert("Success! Ride published.");
            navigate("/");
        } catch (err) {
            alert("Error: " + (err.response?.data?.message || err.message));
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="flex flex-col md:flex-row h-[calc(100vh-64px)] overflow-hidden">
            {/* Left Side: Form */}
            <div className="w-full md:w-[450px] lg:w-[500px] bg-white border-r border-gray-200 overflow-y-auto">
                <div className="p-8">
                    <h1 className="text-3xl font-bold mb-2">Driver details</h1>
                    <p className="text-gray-500 mb-8">Publish a ride and save on travel costs.</p>

                    <form onSubmit={handleSubmit} className="space-y-6">
                        {/* Source */}
                        <div>
                            <div className="flex justify-between items-center mb-2">
                                <label className="block text-sm font-bold text-gray-700">Where from?</label>
                                <button
                                    type="button"
                                    onClick={setCurrentLocationAsSource}
                                    className="text-xs text-blue-600 hover:text-blue-800 font-medium"
                                >
                                    Use Current Location
                                </button>
                            </div>
                            <input
                                type="text"
                                name="source"
                                value={formData.source}
                                onChange={handleChange}
                                placeholder="Enter pickup location or click on map"
                                className="w-full bg-gray-100 p-3 rounded-lg focus:bg-white focus:ring-2 focus:ring-black outline-none transition"
                                required
                            />
                        </div>

                        {/* Destination */}
                        <div>
                            <label className="block text-sm font-bold mb-2 text-gray-700">Where to?</label>
                            <input
                                type="text"
                                name="destination"
                                value={formData.destination}
                                onChange={handleChange}
                                placeholder="Enter dropoff location or click on map"
                                className="w-full bg-gray-100 p-3 rounded-lg focus:bg-white focus:ring-2 focus:ring-black outline-none transition"
                                required
                            />
                        </div>

                        {/* Time */}
                        <div>
                            <label className="block text-sm font-bold mb-2 text-gray-700">When?</label>
                            <input
                                type="datetime-local"
                                name="departureTime"
                                value={formData.departureTime}
                                onChange={handleChange}
                                className="w-full bg-gray-100 p-3 rounded-lg focus:bg-white focus:ring-2 focus:ring-black outline-none transition"
                                required
                            />
                        </div>

                        {/* Two Columns */}
                        <div className="flex gap-4">
                            <div className="w-1/2">
                                <label className="block text-sm font-bold mb-2 text-gray-700">Price ($)</label>
                                <input
                                    type="number"
                                    name="pricePerSeat"
                                    value={formData.pricePerSeat}
                                    onChange={handleChange}
                                    className="w-full bg-gray-100 p-3 rounded-lg focus:bg-white focus:ring-2 focus:ring-black outline-none transition"
                                    required
                                />
                            </div>
                            <div className="w-1/2">
                                <label className="block text-sm font-bold mb-2 text-gray-700">Seats</label>
                                <input
                                    type="number"
                                    name="availableSeats"
                                    value={formData.availableSeats}
                                    onChange={handleChange}
                                    className="w-full bg-gray-100 p-3 rounded-lg focus:bg-white focus:ring-2 focus:ring-black outline-none transition"
                                    required
                                />
                            </div>
                        </div>

                        {/* Car */}
                        <div>
                            <label className="block text-sm font-bold mb-2 text-gray-700">Vehicle</label>
                            <input
                                type="text"
                                name="carModel"
                                value={formData.carModel}
                                onChange={handleChange}
                                placeholder="e.g. Toyota Camry, Grey"
                                className="w-full bg-gray-100 p-3 rounded-lg focus:bg-white focus:ring-2 focus:ring-black outline-none transition"
                                required
                            />
                        </div>

                        {/* Location Instructions */}
                        <div className="bg-blue-50 p-4 rounded-lg">
                            <p className="text-sm text-blue-800">
                                <strong>Tip:</strong> Click the location button on the map to use your current location,
                                or click anywhere on the map to set pickup/dropoff points.
                            </p>
                        </div>

                        {/* Submit */}
                        <div className="pt-4">
                            <button
                                type="submit"
                                disabled={loading}
                                className="w-full bg-black text-white py-4 rounded-lg font-bold text-lg hover:bg-gray-800 transition disabled:opacity-50"
                            >
                                {loading ? "Publishing..." : "Publish Ride"}
                            </button>
                        </div>
                    </form>
                </div>
            </div>

            {/* Right Side: Map */}
            <div className="hidden md:block flex-1 bg-gray-100 relative h-full">
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

export default CreateRide;