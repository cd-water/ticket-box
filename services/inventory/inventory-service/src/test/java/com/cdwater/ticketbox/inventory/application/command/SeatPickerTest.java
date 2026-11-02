package com.cdwater.ticketbox.inventory.application.command;

import com.cdwater.ticketbox.inventory.domain.model.ShowSeat;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SeatPickerTest {

    private static ShowSeat seat(int no) {
        ShowSeat seat = new ShowSeat();
        seat.setSeatNo(no);
        return seat;
    }

    @Test
    void picks_first_contiguous_run() {
        var picked = SeatPicker.pick(List.of(seat(1), seat(2), seat(3), seat(5), seat(6)), 2);
        assertEquals(List.of(1, 2), picked.stream().map(ShowSeat::getSeatNo).toList());
    }

    @Test
    void falls_back_to_first_seats_when_no_run() {
        var picked = SeatPicker.pick(List.of(seat(1), seat(3), seat(5)), 2);
        assertEquals(List.of(1, 3), picked.stream().map(ShowSeat::getSeatNo).toList());
    }

    @Test
    void picks_run_later_in_list() {
        var picked = SeatPicker.pick(List.of(seat(1), seat(4), seat(5), seat(6)), 2);
        assertEquals(List.of(4, 5), picked.stream().map(ShowSeat::getSeatNo).toList());
    }
}
