package com.example.lab4.service;

import com.example.lab4.entity.PointEntity;
import com.example.lab4.model.Point;
import com.example.lab4.repository.PointRepo;
import com.example.lab4.repository.UserRepo;
import com.example.lab4.request.PointRequestDTO;
import jakarta.transaction.Transactional;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@ToString
@EqualsAndHashCode
@Service
public class PointService {
    private final PointRepo pointRepo;
    private final UserRepo userRepo;

    @Autowired
    public PointService(final UserRepo userRepo, final PointRepo pointRepo) {
        this.userRepo = userRepo;
        this.pointRepo = pointRepo;
    }

    public PointEntity newPoint(final String username, PointRequestDTO rq){
        final long startTime = System.nanoTime();
        final PointEntity point = new PointEntity();
        point.setR(rq.getR());
        point.setX(rq.getX());
        point.setY(rq.getY());
        point.setResult(checkResult(rq.getR(), rq.getX(), rq.getY()));
        point.setCalculatedAt(LocalDateTime.now());
        point.setCalculationTime(System.nanoTime() - startTime);
        point.setUser(userRepo.findByUsername(username));
        return pointRepo.save(point);
    }

    private boolean checkResult(final Double r, final Double x, final Double y){
        if (r >= 0) {
            if (0 < x && x < r && 0 < y && y < r / 2) // 1 - rectangle
                return true;
            if (x < 0 && y > 0 && x * x + y * y < r * r) // 2 - circle
                return true;
            //--  3 - empty
            if (0 < x && y < 0 && y > x - r) // 4 - triangle
                return true;
        }
        else {
            if (0 > x && x > r && 0 > y && y > r / 2) // 1 - rectangle
                return true;
            if (x > 0 && y < 0 && x * x + y * y < r * r) // 2 - circle
                return true;
            //--  3 - empty
            if (0 > x && y > 0 && y < x - r) // 4 - triangle
                return true;
        }
        return false;
    }

    public List<Point> getAllByUsername(Principal principal) {
        List<PointEntity> resultEntity = pointRepo.findAllByUser(userRepo.findByUsername(principal.getName()));
        return resultEntity.stream().map(Point::toModel).toList();
    }

    @Transactional
    public void removeAllFromUser(Principal principal) {
        pointRepo.deleteAllByUser(userRepo.findByUsername(principal.getName()));
    }
}
