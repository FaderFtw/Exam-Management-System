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
  remainingEvents: any[] = [];
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

  initializeRemainingEvents(): void {
    this.remainingEvents = [
      {
        title: 'Exam A',
        description: 'Remaining Exam A description...',
        className: 'bg-primary',
      },
      {
        title: 'Exam B',
        description: 'Remaining Exam B description...',
        className: 'bg-warning',
      },
    ];
  }

  setupCalendarOptions(): void {
    this.calendarOptions = {
      editable: true,
      droppable: true,
      eventResizableFromStart: true,
      plugins: [interactionPlugin, dayGridPlugin, timeGridPlugin],
      initialView: 'timeGridWeek',
      allDaySlot: false,
      slotDuration: '00:30:00',
      slotMinTime: '08:00:00',
      slotMaxTime: '18:30:00',
      events: this.events,
      eventClick: (info: any) => {
        alert(`Event: ${info.event.title}`);
      },
      eventDrop: this.handleEventChange.bind(this),
      eventResize: this.handleEventChange.bind(this),
      eventReceive: this.handleEventReceive.bind(this), // Add handler for receiving dropped events
    };
  }

  handleEventChange(info: any) {
    console.log('Event dropped:', info.event.title);
    console.log('New start date:', info.event.start);
    console.log('New end date:', info.event.end);

    const updatedEvent = info.event;
    const index = this.events.findIndex(event => event.title === updatedEvent.title);
    if (index > -1) {
      this.events[index].start = updatedEvent.start.toISOString();
      this.events[index].end = updatedEvent.end.toISOString();
    }
  }

  // New method to handle receiving external events
  // Removes the event from "remainingEvents" and adds them to "events"
  handleEventReceive(info: any) {
    const droppedEvent = info.event;

    // Find and remove the event from remainingEvents
    const remainingIndex = this.remainingEvents.findIndex(event => event.title === droppedEvent.title);

    if (remainingIndex > -1) {
      // Get the full event data from remainingEvents
      const originalEvent = this.remainingEvents[remainingIndex];

      // Create new event with combined data
      const newEvent = {
        ...originalEvent,
        start: droppedEvent.start.toISOString(),
        end: droppedEvent.end.toISOString(),
      };

      // Add to events array
      this.events = [...this.events, newEvent];

      // Remove from remainingEvents
      this.remainingEvents.splice(remainingIndex, 1);
    }
  }

  initExternalEvents(): void {
    const externalEventElements = document.querySelectorAll('.draggable-event');
    externalEventElements.forEach((el: Element) => {
      const element = el as HTMLElement;
      new Draggable(element, {
        itemSelector: '.draggable-event',
        eventData: (eventEl: HTMLElement) => {
          const eventTitle = eventEl.getAttribute('data-event-title');
          // Find the corresponding event data
          const eventData = this.remainingEvents.find(event => event.title === eventTitle);
          return {
            title: eventTitle,
            className: eventData?.className || '',
            duration: '01:30', // Default duration when dropped
          };
        },
      });
    });
  }
}
