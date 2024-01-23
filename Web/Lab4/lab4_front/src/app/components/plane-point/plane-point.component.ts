import {Component, Input, OnInit} from '@angular/core';
import {IPoint} from "../../models/point";

@Component({
  selector: 'app-plane-point',
  standalone: true,
  imports: [],
  templateUrl: './plane-point.component.html',
  styleUrl: './plane-point.component.css'
})
export class PlanePointComponent{
  @Input() point: IPoint = {r: 1, x: 1, y:1, result: true, calculationTime: 1, calculatedAt: new Date()}

  getXY(): string{
    let x_cord = this.point.x * 35;//
    let y_cord = this.point.y * 35;//
    let point_radius = 3;
    return "left: " + (210 - point_radius + x_cord) + "px; top: " + (210 - point_radius - y_cord) + "px;"
  }
}
