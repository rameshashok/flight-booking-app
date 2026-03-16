export interface Flight {
  id: number;
  flightNumber: string;
  origin: string;
  destination: string;
  departureTime: string;
  arrivalTime: string;
  price: number;
  availableSeats: number;
}

export interface Booking {
  id: number;
  flight: Flight;
  passengerName: string;
  passengerEmail: string;
  seats: number;
  bookedAt: string;
  status: 'CONFIRMED' | 'CANCELLED';
}

export interface BookingRequest {
  flightId: number;
  flightNumber: string;
  origin: string;
  destination: string;
  departureTime: string;
  arrivalTime: string;
  price: number;
  availableSeats: number;
  passengerName: string;
  passengerEmail: string;
  seats: number;
}
