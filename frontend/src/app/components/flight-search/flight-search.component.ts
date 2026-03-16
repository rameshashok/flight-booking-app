import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-flight-search',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule,
    MatFormFieldModule, MatInputModule, MatButtonModule,
    MatDatepickerModule, MatNativeDateModule, MatCardModule
  ],
  templateUrl: './flight-search.component.html'
})
export class FlightSearchComponent {
  form: ReturnType<FormBuilder['group']>;

  constructor(private fb: FormBuilder, private router: Router) {
    this.form = this.fb.group({
      origin: ['', Validators.required],
      destination: ['', Validators.required],
      date: [new Date(), Validators.required]
    });
  }

  search() {
    if (this.form.invalid) return;
    const { origin, destination, date } = this.form.value;
    const dateStr = (date as Date).toISOString().split('T')[0];
    this.router.navigate(['/flights'], { queryParams: { origin, destination, date: dateStr } });
  }
}
