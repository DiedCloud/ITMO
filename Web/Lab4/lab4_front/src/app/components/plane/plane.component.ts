import {AfterContentChecked, Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {PlanePointComponent} from "../plane-point/plane-point.component";
import {PointsService} from "../../services/points.service"
import {NgForOf, NgIf} from "@angular/common";

@Component({
  selector: 'app-plane',
  standalone: true,
  imports: [
    PlanePointComponent,
    NgForOf,
    NgIf
  ],
  templateUrl: './plane.component.html',
  styleUrl: './plane.component.css'
})
export class PlaneComponent implements OnInit{
  @ViewChild('#plane', {static: true}) plane: ElementRef
  @ViewChild('canvas', { static: true }) myCanvas!: ElementRef;
  canvas: HTMLCanvasElement
  context: CanvasRenderingContext2D | null
  public radius: number = 0;

  constructor(public pointsService: PointsService) {
  }

  ngOnInit(): void {
    this.canvas  = this.myCanvas.nativeElement;
    this.context = this.canvas.getContext('2d');
  }

  drawAxes(){
    if (this.context) {
      this.context.strokeStyle = 'rgb(0, 0, 0)';
      this.context.fillStyle = 'rgb(0, 0, 0)';

      this.context.lineWidth = 2;
      this.context.lineCap = 'round';

      // x ax
      this.context.beginPath();
      this.context.moveTo(0, 210);
      this.context.lineTo(420, 210);
      this.context.stroke();
      // y ax
      this.context.beginPath();
      this.context.moveTo(210, 0);
      this.context.lineTo(210, 420);
      this.context.stroke();
      //y arrow
      this.context.beginPath();
      this.context.moveTo(205, 5);
      this.context.lineTo(210, 1);
      this.context.lineTo(215, 5);
      this.context.stroke();
      //x arrow
      this.context.beginPath();
      this.context.moveTo(415, 205);
      this.context.lineTo(419, 210);
      this.context.lineTo(415, 215);
      this.context.stroke();
      //x ticks
      for(let i = 0; i < 11; i++){
        this.context.fillRect((35*i)+34, 207, 2, 6);
      }
      //y ticks
      for(let i = 0; i < 11; i++){
        this.context.fillRect(207,(35*i)+34, 6, 2);
      }
    }
  }

  drawFieldsByR(r: number){
    this.radius = r;
    const x0 = 210
    const y0 = 210
    const rStep = 35

    if (this.context) {
      this.context.reset()
      this.context.strokeStyle = 'red';
      this.context.fillStyle = 'rgb(126, 126, 253)';

      // rect
      this.context.fillRect(x0, y0 - rStep * 0.5 * r, rStep * r, rStep * 0.5 * r);

      // triangle
      this.context.beginPath();
      this.context.moveTo(x0, y0);
      this.context.lineTo(x0 + rStep * r, y0);
      this.context.lineTo(x0, y0 + rStep * r);
      this.context.fill();

      // circle
      const angle = r > 0 ? 180 : 0;
      r = r > 0 ? r : -r;
      this.context.beginPath();
      this.context.arc(x0, y0, rStep*r,
        (Math.PI / 180) * angle, (Math.PI / 180) * (angle+90));
      this.context.lineTo(x0, y0);
      this.context.fill();

      // axes
      this.drawAxes()
    }
  }

  handleClick(evt: any){
    let el = evt.target as HTMLDivElement
    console.log(el)
    if (!el.classList.contains('point')){
      const rect = el.getBoundingClientRect();
      const x = (evt.clientX - rect.left - 210) / 35;
      const y = (- (evt.clientY - rect.top) + 210) / 35;
      this.sendDataGraph(x, y);
    }
  }

  sendDataGraph(x: number, y: number) {
    const request = {
      r: this.radius,
      x: x,
      y: y
    }
    console.log(request)
    this.pointsService.create(request).subscribe({
      error: (err) => {
        console.error(err);
        console.log(err.message);
      }
    });
  }
}
