// src/components/RealMap.jsx
import { MapContainer, TileLayer, Marker, Popup, useMapEvents } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';

// Fix for default Leaflet marker icons in React
import icon from 'leaflet/dist/images/marker-icon.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';

let DefaultIcon = L.icon({
    iconUrl: icon,
    shadowUrl: iconShadow,
    iconSize: [25, 41],
    iconAnchor: [12, 41]
});

L.Marker.prototype.options.icon = DefaultIcon;

// Location marker icon (blue)
const LocationIcon = new L.Icon({
    iconUrl: 'data:image/svg+xml;base64,' + btoa(`
        <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M10 0C6.13 0 3 3.13 3 7C3 12.25 10 20 10 20C10 20 17 12.25 17 7C17 3.13 13.87 0 10 0ZM10 9.5C8.62 9.5 7.5 8.38 7.5 7C7.5 5.62 8.62 4.5 10 4.5C11.38 4.5 12.5 5.62 12.5 7C12.5 8.38 11.38 9.5 10 9.5Z" fill="#3B82F6"/>
        </svg>
    `),
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
});

const RealMap = ({
    onLocationDetected,
    sourcePosition,
    destinationPosition,
    onMapClick
}) => {
    const defaultPosition = [28.6139, 77.2088];

    // Get user's current location
    const getCurrentLocation = () => {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    const { latitude, longitude } = position.coords;
                    onLocationDetected?.({
                        lat: latitude,
                        lng: longitude,
                        address: `Current Location (${latitude.toFixed(4)}, ${longitude.toFixed(4)})`
                    });
                },
                (error) => {
                    console.error('Error getting location:', error);
                    alert('Unable to get your location. Using default position.');
                    onLocationDetected?.({
                        lat: defaultPosition[0],
                        lng: defaultPosition[1],
                        address: 'Default Location'
                    });
                }
            );
        } else {
            alert('Geolocation is not supported by this browser.');
        }
    };

    // Map click handler component
    const MapClickHandler = () => {
        useMapEvents({
            click: (e) => {
                const { lat, lng } = e.latlng;
                onMapClick?.({
                    lat,
                    lng,
                    address: `Selected Location (${lat.toFixed(4)}, ${lng.toFixed(4)})`
                });
            },
        });
        return null;
    };

    return (
        <div className="w-full h-full relative z-0">
            {/* Location Button */}
            <button
                onClick={getCurrentLocation}
                className="absolute top-4 left-4 z-[1000] bg-white p-2 rounded-lg shadow-md hover:bg-gray-100 transition"
                title="Get Current Location"
            >
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                    <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path>
                    <circle cx="12" cy="10" r="3"></circle>
                </svg>
            </button>

            <MapContainer
                center={defaultPosition}
                zoom={13}
                scrollWheelZoom={true}
                style={{ height: "100%", width: "100%" }}
            >
                <TileLayer
                    attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />

                <MapClickHandler />

                {/* Source Marker */}
                {sourcePosition && (
                    <Marker position={[sourcePosition.lat, sourcePosition.lng]} icon={LocationIcon}>
                        <Popup>
                            <strong>Pickup Location</strong><br />
                            {sourcePosition.address}
                        </Popup>
                    </Marker>
                )}

                {/* Destination Marker */}
                {destinationPosition && (
                    <Marker position={[destinationPosition.lat, destinationPosition.lng]}>
                        <Popup>
                            <strong>Dropoff Location</strong><br />
                            {destinationPosition.address}
                        </Popup>
                    </Marker>
                )}

                {/* Default marker if no positions provided */}
                {!sourcePosition && !destinationPosition && (
                    <Marker position={defaultPosition}>
                        <Popup>
                            RideShare is available here.
                        </Popup>
                    </Marker>
                )}
            </MapContainer>
        </div>
    );
};

export default RealMap;