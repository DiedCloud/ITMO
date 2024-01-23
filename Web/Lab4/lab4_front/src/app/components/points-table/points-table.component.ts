import {Component, OnInit} from '@angular/core';
import {PointsService} from "../../services/points.service";
import {PlanePointComponent} from "../plane-point/plane-point.component";
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-points-table',
  standalone: true,
  imports: [
    PlanePointComponent,
    NgForOf
  ],
  templateUrl: './points-table.component.html',
  styleUrl: './points-table.component.css'
})
export class PointsTableComponent implements OnInit{
  constructor(public pointsService: PointsService) {
  }

  ngOnInit(): void {
  }
}
