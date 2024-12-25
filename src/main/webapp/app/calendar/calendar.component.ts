import { Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common'; // Time Grid View
import dayGridPlugin from '@fullcalendar/daygrid';
import { FullCalendarModule } from '@fullcalendar/angular';
import interactionPlugin from '@fullcalendar/interaction'; // Drag-n-Drop, Resizing
import timeGridPlugin from '@fullcalendar/timegrid';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  standalone: true,
  imports: [FullCalendarModule, DatePipe, CommonModule],
  styleUrls: ['./calendar.component.scss'],
})
export class CalendarComponent implements OnInit {
  events: any[] = [];
  calendarOptions: any;

  constructor() {}

  ngOnInit(): void {
    this.initializeEvents();
    console.log(this.events);
    this.setupCalendarOptions();
  }

  initializeEvents(): void {
    const currentYear = new Date().getFullYear();
    const currentMonth = String(new Date().getMonth() + 1).padStart(2, '0');

    this.events = [
      {
        title: 'Boot Camp',
        start: `${currentYear}-${currentMonth}-25 10:00:00`,
        end: `${currentYear}-${currentMonth}-25 11:00:00`,
        description: 'Boston Harbor Now event description...',
        className: 'bg-soft-success',
      },
      {
        title: 'Boot Camp 2',
        start: `${currentYear}-${currentMonth}-25 12:00:00`,
        end: `${currentYear}-${currentMonth}-25 13:00:00`,
        description: 'Boston Harbor Now event description...',
        className: 'bg-soft-success',
      },
    ];
  }

  setupCalendarOptions(): void {
    this.calendarOptions = {
      editable: true, // Enables drag-and-drop
      droppable: true, // Enables external drag-and-drop
      eventResizableFromStart: true, // Enables resizing from the start

      // register plugins here!
      plugins: [interactionPlugin, dayGridPlugin, timeGridPlugin],

      initialView: 'timeGridWeek',
      allDaySlot: false, // Hides the "all-day" section

      slotDuration: '00:30:00', // Duration of each time slot (30 minutes)
      slotMinTime: '08:00:00', // Start time of the calendar
      slotMaxTime: '18:30:00', // End time of the calendar
      events: this.events,
      eventClick: (info: any) => {
        alert(`Event: ${info.event.title}`);
      },

      eventDrop: this.handleEventChange.bind(this), // Handle event drag-and-drop
      eventResize: this.handleEventChange.bind(this), // Handle event resizing
    };
  }

  // Event handler
  handleEventChange(info: any) {
    console.log('Event dropped:', info.event.title);
    console.log('New start date:', info.event.start);
    console.log('New end date:', info.event.end);

    // Updated event object with the new start and end dates
    const updatedEvent = info.event;

    // Update the event data in  backend or local storage here
    // For example, update the event in the `this.events` array using the title
    const index = this.events.findIndex(event => event.title === updatedEvent.title);
    if (index > -1) {
      this.events[index].start = updatedEvent.start.toISOString();
      this.events[index].end = updatedEvent.end.toISOString();
    }
  }

  protected readonly event = event;
}
