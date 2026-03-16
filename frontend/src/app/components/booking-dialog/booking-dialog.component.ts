import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { FlightService } from '../../services/flight.service';
import { Flight } from '../../models/flight.model';

@Component({
  selector: 'app-booking-dialog',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule, MatDialogModule,
    MatFormFieldModule, MatInputModule, MatButtonModule,
    MatSelectModule, MatSnackBarModule
  ],
  templateUrl: './booking-dialog.component.html'
})
export class BookingDialogComponent {
  form: ReturnType<FormBuilder['group']>;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private flightService: FlightService,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<BookingDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public flight: Flight
  ) {
    this.form = this.fb.group({
      passengerName: ['', Validators.required],
      passengerEmail: ['', [Validators.required, Validators.email]],
      seats: [1, [Validators.required, Validators.min(1)]]
    });
  }

  confirm() {
    if (this.form.invalid) return;
    this.loading = true;
    const { passengerName, passengerEmail, seats } = this.form.value;
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
      seats: seats!
    }).subscribe({
      next: () => {
        this.snackBar.open('Booking confirmed! 🎉', 'Close', { duration: 3000 });
        this.dialogRef.close(true);
      },
      error: () => {
        this.snackBar.open('Booking failed. Please try again.', 'Close', { duration: 3000 });
        this.loading = false;
      }
    });
  }
}
