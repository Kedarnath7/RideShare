# RideShare

# ⚠️ Challenges Faced

The biggest challenges in this project were actually inside Spring Boot itself. Figuring out DTO handling took time because mapping data cleanly between layers wasn't intuitive at first. Spring Security was also completely new to me, so understanding JWTs, filters, and how requests get authenticated was a real learning curve.
On top of that, deploying the backend in Docker created confusion since Spring Boot wasn't reading the environment variables the way it did locally. Fixing the Dockerfile and reorganizing the config taught me a lot about how Spring actually loads settings in different environments. But I have learnt many new things through experimentation and want to do so in the future.

## Project Overview
RideShare is a comprehensive ride-hailing platform built as a modular monolith application. The system connects passengers with drivers, facilitating ride bookings, real-time tracking, and secure transactions.

## Architecture: Modular Monolith

### Why Modular Monolith?
- **Single Deployable Unit**: Easy development, debugging, and deployment
- **Domain Separation**: Clean code organization by business domains
- **Future-Ready**: Can evolve into microservices if needed without major refactoring

# 🌐 API Endpoints

## Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration

## Rides
- `POST /api/rides` - Create a new ride
- `GET /api/rides/search` - Search available rides
- `GET /api/rides/{id}` - Get ride details

## Bookings
- `POST /api/bookings` - Book a ride
- `PUT /api/bookings/{id}/cancel` - Cancel booking
- `GET /api/bookings/user/{userId}` - Get user bookings

## Tracking
- `POST /api/tracking/update` - Update ride location
- `GET /api/tracking/{rideId}` - Get current ride location

# 🔒 Security Features

## Authentication Flow
1. User logs in with credentials
2. Server validates and issues JWT token
3. Client includes token in Authorization header
4. JwtAuthFilter validates token on each request
5. User details loaded for authorization checks

## Protected Endpoints
- All endpoints except `/api/auth/**` require authentication
- Role-based access control for sensitive operations
- Password encryption using BCrypt

# 📱 Key Features

## For Passengers
- Search available rides based on route and time
- Book rides with estimated fare
- Real-time ride tracking
- Ride history and receipts
- Rating system for drivers

## For Drivers
- Create ride offers with route details
- Accept/decline booking requests
- Update ride status (started, completed)
- Earnings dashboard
- Profile management

## Shared Features
- Push notifications for ride updates
- In-app chat for communication
- Multiple payment options
- Review and rating system
- Location-based services
