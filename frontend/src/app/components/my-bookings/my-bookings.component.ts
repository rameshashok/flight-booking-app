import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { FlightService } from '../../services/flight.service';
import { Booking } from '../../models/flight.model';
import { ReplacePipe } from '../../pipes/replace.pipe';

@Component({
  selector: 'app-my-bookings',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule,
    MatFormFieldModule, MatInputModule, MatButtonModule,
    MatCardModule, MatChipsModule, MatSnackBarModule,
    ReplacePipe
  ],
  templateUrl: './my-bookings.component.html'
})
export class MyBookingsComponent {
  form: ReturnType<FormBuilder['group']>;
  bookings: Booking[] = [];
  searched = false;

  constructor(
    private fb: FormBuilder,
    private flightService: FlightService,
    private snackBar: MatSnackBar
  ) {
    this.form = this.fb.group({ email: ['', [Validators.required, Validators.email]] });
  }

  search() {
    if (this.form.invalid) return;
    this.flightService.getBookingsByEmail(this.form.value.email!)
      .subscribe(b => { this.bookings = b; this.searched = true; });
  }

  cancel(id: number) {
    this.flightService.cancelBooking(id).subscribe(() => {
      this.snackBar.open('Booking cancelled', 'Close', { duration: 2000 });
      this.search();
    });
  }
}
