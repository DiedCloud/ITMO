import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import {UserService} from "./services/user.service";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit{
  title = 'lab4_front';
  constructor(public userService: UserService) {
  }

  ngOnInit(): void {
    if (!this.userService.user && typeof window !== 'undefined'){
      let login = localStorage.getItem('login');
      if (login) {
        this.userService.user = {login: login, password: ''};
      }
    }
  }
}
