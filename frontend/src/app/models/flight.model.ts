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

export type SeatClass = 'ECONOMY' | 'BUSINESS' | 'FIRST';

export interface SeatClassOption {
  value: SeatClass;
  label: string;
  multiplier: number;
  description: string;
}

export interface AncillaryOption {
  code: string;
  label: string;
  price: number;
}

export interface Booking {
  id: number;
  flight: Flight;
  passengerName: string;
  passengerEmail: string;
  seats: number;
  seatClass: SeatClass;
  ancillaries: string[];
  totalPrice: number;
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
  seatClass: SeatClass;
  ancillaries: string[];
}
