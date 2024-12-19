import { Component, OnInit } from '@angular/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import { FullCalendarModule } from '@fullcalendar/angular';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  standalone: true,
  imports: [FullCalendarModule],
  styleUrls: ['./calendar.component.scss'],
})
export class CalendarComponent implements OnInit {
  events: any[] = [];
  calendarOptions: any;

  constructor() {}

  ngOnInit(): void {
    this.initializeEvents();
    this.setupCalendarOptions();
  }

  initializeEvents(): void {
    const currentYear = new Date().getFullYear();
    const currentMonth = String(new Date().getMonth() + 1).padStart(2, '0');

    this.events = [
      {
        title: 'Boot Camp',
        start: `${currentYear}-${currentMonth}-01 10:00:00`,
        end: `${currentYear}-${currentMonth}-03 16:00:00`,
        description: 'Boston Harbor Now event description...',
        className: 'bg-soft-success',
      },
      // Add more events here
    ];
  }

  setupCalendarOptions(): void {
    this.calendarOptions = {
      eventColor: '#F4C584',
      plugins: [dayGridPlugin], // Register the plugin here
      initialView: 'dayGridMonth',
      events: this.events,
      eventClick: (info: any) => {
        alert(`Event: ${info.event.title}`);
      },
    };
  }
}
