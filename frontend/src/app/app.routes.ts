import { Routes } from '@angular/router';
import { FlightSearchComponent } from './components/flight-search/flight-search.component';
import { FlightListComponent } from './components/flight-list/flight-list.component';
import { MyBookingsComponent } from './components/my-bookings/my-bookings.component';

export const routes: Routes = [
  { path: '', component: FlightSearchComponent },
  { path: 'flights', component: FlightListComponent },
  { path: 'my-bookings', component: MyBookingsComponent },
  { path: '**', redirectTo: '' }
];
