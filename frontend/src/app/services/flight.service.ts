import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Booking, BookingRequest, Flight } from '../models/flight.model';

@Injectable({ providedIn: 'root' })
export class FlightService {
  private api = '/api';

  constructor(private http: HttpClient) {}

  searchFlights(depIata: string, arrIata: string): Observable<Flight[]> {
    const params = new HttpParams().set('depIata', depIata).set('arrIata', arrIata);
    return this.http.get<Flight[]>(`${this.api}/flights/search`, { params });
  }

  getAllFlights(): Observable<Flight[]> {
    return this.http.get<Flight[]>(`${this.api}/flights`);
  }

  createBooking(request: BookingRequest): Observable<Booking> {
    return this.http.post<Booking>(`${this.api}/bookings`, request);
  }

  getBookingsByEmail(email: string): Observable<Booking[]> {
    const params = new HttpParams().set('email', email);
    return this.http.get<Booking[]>(`${this.api}/bookings`, { params });
  }

  cancelBooking(id: number): Observable<Booking> {
    return this.http.put<Booking>(`${this.api}/bookings/${id}/cancel`, {});
  }
}
