import { AfterViewInit, Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FullCalendarModule } from '@fullcalendar/angular';
import interactionPlugin, { Draggable } from '@fullcalendar/interaction';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  standalone: true,
  imports: [FullCalendarModule, DatePipe, CommonModule],
  styleUrls: ['./calendar.component.scss'],
})
export class CalendarComponent implements OnInit, AfterViewInit {
  events: any[] = [];
  remainingEvents: any[] = []; // New array for external draggable events
  calendarOptions: any;

  constructor() {}

  ngOnInit(): void {
    this.initializeEvents();
    this.initializeRemainingEvents();
    this.setupCalendarOptions();
  }

  ngAfterViewInit() {
    this.initExternalEvents();
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

  // Initialize remaining events (external draggable events)
  initializeRemainingEvents(): void {
    const currentYear = new Date().getFullYear();
    const currentMonth = String(new Date().getMonth() + 1).padStart(2, '0');

    this.remainingEvents = [
      {
        title: 'Exam A',
        start: `${currentYear}-${currentMonth}-27 10:00:00`,
        end: `${currentYear}-${currentMonth}-27 11:00:00`,
        description: 'Remaining Exam A description...',
        className: 'bg-primary',
      },
      {
        title: 'Exam B',
        start: `${currentYear}-${currentMonth}-28 14:00:00`,
        end: `${currentYear}-${currentMonth}-28 15:00:00`,
        description: 'Remaining Exam B description...',
        className: 'bg-warning',
      },
    ];
  }

  setupCalendarOptions(): void {
    this.calendarOptions = {
      editable: true, // Enables drag-and-drop
      droppable: true, // Enables external drag-and-drop
      eventResizableFromStart: true, // Enables resizing from the start

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

    // Update the event data in backend or local storage here
    const index = this.events.findIndex(event => event.title === updatedEvent.title);
    if (index > -1) {
      this.events[index].start = updatedEvent.start.toISOString();
      this.events[index].end = updatedEvent.end.toISOString();
    }
  }

  // Initialize external events (draggable list items)
  initExternalEvents(): void {
    const externalEventElements = document.querySelectorAll('.draggable-event');
    externalEventElements.forEach((el: Element) => {
      // Type assertion to HTMLElement
      const element = el as HTMLElement; // Cast to HTMLElement
      new Draggable(element, {
        itemSelector: '.draggable-event',
        eventData: (eventEl: HTMLElement) => {
          return {
            title: eventEl.getAttribute('data-event-title'),
            className: eventEl.classList.toString(),
          };
        },
      });
    });
  }
}
