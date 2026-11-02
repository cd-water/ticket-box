package com.cdwater.ticketbox.show.application.command;

import com.cdwater.ticketbox.show.domain.model.ShowSeat;

import java.util.ArrayList;
import java.util.List;

public final class SeatPicker {

    private SeatPicker() {
    }

    /** 优先返回第一段连续座位，否则返回列表前 n 个。调用方需保证 seats.size() >= n。 */
    public static List<ShowSeat> pick(List<ShowSeat> seats, int n) {
        int start = 0;
        for (int i = 1; i <= seats.size(); i++) {
            boolean runEnds = i == seats.size()
                    || seats.get(i).getSeatNo() != seats.get(i - 1).getSeatNo() + 1;
            if (runEnds) {
                if (i - start >= n) {
                    return new ArrayList<>(seats.subList(start, start + n));
                }
                start = i;
            }
        }
        return new ArrayList<>(seats.subList(0, n));
    }
}
