import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatStepperModule } from '@angular/material/stepper';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDividerModule } from '@angular/material/divider';
import { MatChipsModule } from '@angular/material/chips';
import { FlightService } from '../../services/flight.service';
import { AncillaryOption, Flight, SeatClass, SeatClassOption } from '../../models/flight.model';

@Component({
  selector: 'app-booking-dialog',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule, MatDialogModule,
    MatFormFieldModule, MatInputModule, MatButtonModule,
    MatSelectModule, MatStepperModule, MatCheckboxModule,
    MatSnackBarModule, MatDividerModule, MatChipsModule
  ],
  templateUrl: './booking-dialog.component.html',
  styleUrls: ['./booking-dialog.component.css']
})
export class BookingDialogComponent implements OnInit {
  passengerForm: ReturnType<FormBuilder['group']>;
  loading = false;
  ancillaries: AncillaryOption[] = [];
  selectedAncillaries = new Set<string>();

  seatClasses: SeatClassOption[] = [
    { value: 'ECONOMY', label: 'Economy', multiplier: 1.0, description: 'Standard seating' },
    { value: 'BUSINESS', label: 'Business', multiplier: 2.5, description: 'Extra legroom & priority service' },
    { value: 'FIRST', label: 'First Class', multiplier: 4.0, description: 'Luxury seating & premium dining' }
  ];

  constructor(
    private fb: FormBuilder,
    private flightService: FlightService,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<BookingDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public flight: Flight
  ) {
    this.passengerForm = this.fb.group({
      passengerName: ['', Validators.required],
      passengerEmail: ['', [Validators.required, Validators.email]],
      seats: [1, [Validators.required, Validators.min(1), Validators.max(9)]],
      seatClass: ['ECONOMY', Validators.required]
    });
  }

  ngOnInit() {
    this.flightService.getAncillaries().subscribe(a => this.ancillaries = a);
  }

  toggleAncillary(code: string) {
    this.selectedAncillaries.has(code)
      ? this.selectedAncillaries.delete(code)
      : this.selectedAncillaries.add(code);
  }

  get selectedSeatClass(): SeatClassOption {
    return this.seatClasses.find(s => s.value === this.passengerForm.value.seatClass)!;
  }

  get basePrice(): number {
    const { seats, seatClass } = this.passengerForm.value;
    const multiplier = this.seatClasses.find(s => s.value === seatClass)?.multiplier ?? 1;
    return this.flight.price * multiplier * (seats || 1);
  }

  get ancillaryTotal(): number {
    return this.ancillaries
      .filter(a => this.selectedAncillaries.has(a.code))
      .reduce((sum, a) => sum + a.price, 0);
  }

  get totalPrice(): number {
    return this.basePrice + this.ancillaryTotal;
  }

  confirm() {
    if (this.passengerForm.invalid) return;
    this.loading = true;
    const { passengerName, passengerEmail, seats, seatClass } = this.passengerForm.value;
    this.flightService.createBooking({
      flightId: this.flight.id,
      flightNumber: this.flight.flightNumber,
      origin: this.flight.origin,
      destination: this.flight.destination,
      departureTime: this.flight.departureTime,
      arrivalTime: this.flight.arrivalTime,
      price: this.flight.price,
      availableSeats: this.flight.availableSeats,
      passengerName: passengerName!,
      passengerEmail: passengerEmail!,
      seats: seats!,
      seatClass: seatClass as SeatClass,
      ancillaries: Array.from(this.selectedAncillaries)
    }).subscribe({
      next: () => {
        this.snackBar.open('Booking confirmed!', 'Close', { duration: 3000 });
        this.dialogRef.close(true);
      },
      error: () => {
        this.snackBar.open('Booking failed. Please try again.', 'Close', { duration: 3000 });
        this.loading = false;
      }
    });
  }
}
