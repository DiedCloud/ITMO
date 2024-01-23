import {Component, Inject, OnInit, PLATFORM_ID} from '@angular/core';
import {CommonModule, DatePipe, isPlatformBrowser} from '@angular/common';

@Component({
  selector: 'app-clock',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './clock.component.html',
  styleUrl: './clock.component.css'
})
export class ClockComponent implements OnInit {
  time: string|null = '-';

  constructor(
    private datePipe: DatePipe,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      // Выполните асинхронные операции только на стороне клиента
      this.updateTime();
    }
  }

  private updateTime(): void {
    const updateView = () => {
      const currentDate = new Date();
      this.time = this.datePipe.transform(currentDate, 'dd.MM.yyyy HH:mm:ss');
    };

    setInterval(updateView, 1000);
  }
}
