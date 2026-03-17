import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';
import { FlightService } from '../../services/flight.service';
import { Flight } from '../../models/flight.model';
import { BookingDialogComponent } from '../booking-dialog/booking-dialog.component';

@Component({
  selector: 'app-flight-list',
  standalone: true,
  imports: [
    CommonModule, MatCardModule, MatButtonModule,
    MatProgressSpinnerModule, MatChipsModule, MatDialogModule
  ],
  templateUrl: './flight-list.component.html'
})
export class FlightListComponent implements OnInit {
  flights: Flight[] = [];
  loading = true;
  searchParams: any = {};

  constructor(
    private route: ActivatedRoute,
    private flightService: FlightService,
    private dialog: MatDialog
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.searchParams = params;
      this.loading = true;
      this.flightService.searchFlights(params['depIata'], params['arrIata'])
        .subscribe({ next: f => { this.flights = f; this.loading = false; }, error: () => this.loading = false });
    });
  }

  book(flight: Flight) {
    this.dialog.open(BookingDialogComponent, {
      width: '620px',
      maxHeight: '90vh',
      data: flight
    });
  }

  duration(dep: string, arr: string): string {
    const diff = new Date(arr).getTime() - new Date(dep).getTime();
    const h = Math.floor(diff / 3600000);
    const m = Math.floor((diff % 3600000) / 60000);
    return `${h}h ${m}m`;
  }
}
