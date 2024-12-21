package com.join.lesson.core.entity.web;

import com.join.lesson.core.common.TimeBaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Table(name = "reservation_change")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ReservationChangeEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "change_id")
    private Long id;

    @Column(name = "origin_start", nullable = false)
    private LocalDateTime originStart;

    @Column(name = "origin_end", nullable = false)
    private LocalDateTime originEnd;

    @Column(name = "request_start", nullable = false)
    private LocalDateTime requestStart;

    @Column(name = "request_end", nullable = false)
    private LocalDateTime requestEnd;

    @Column(name = "state", nullable = false)
    private int state;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "unavailable_id", nullable = false)
    private Long unavailableId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private ReservationEntity reservation;

    @Builder
    public ReservationChangeEntity(Long id, LocalDateTime originStart, LocalDateTime originEnd, LocalDateTime requestStart, LocalDateTime requestEnd, int state, String reason, Long unavailableId, ReservationEntity reservation) {
        this.id = id;
        this.originStart = originStart;
        this.originEnd = originEnd;
        this.requestStart = requestStart;
        this.requestEnd = requestEnd;
        this.state = state;
        this.reason = reason;
        this.unavailableId = unavailableId;
        this.reservation = reservation;
    }

    public void approve() {
        this.state = 1;
    }

    public void reject() {
        this.state = 2;
    }
}
