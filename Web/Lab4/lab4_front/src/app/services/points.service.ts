import {HttpClient, HttpHeaders} from '@angular/common/http'
import {catchError, Observable, retry, tap} from 'rxjs'
import {IPoint} from '../models/point'
import {Injectable} from "@angular/core";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class PointsService {
  constructor(private http: HttpClient) {
  }

  points: IPoint[] = []

  getAll(): Observable<IPoint[]> {
    return this.http.get<IPoint[]>(
      environment.backendURL + "/point"
    ).pipe(
      retry(2),
      tap(points => this.points = points),
      catchError(err => {
        console.log(err);
        return [];
      })
    )
  }

  create(request : Object) {
    return this.http.post<IPoint>(
      environment.backendURL + "/point",
      request
    ).pipe(
      retry(2),
      tap(res =>  {
        if (res) {
          this.points.push(res);
        }
      }),
      catchError(err => {
        console.log(err);
        return [];
      })
    )
  }

  clear(){
    return this.http.delete(
      environment.backendURL + "/point"
    ).pipe(
      retry(2),
      tap(() => this.points = []),
      catchError(err => {
        console.log(err);
        return [];
      })
    )
  }
}
