package com.cdwater.ticketbox.order.application.query.converter;

import com.cdwater.ticketbox.order.api.dto.BookingDetailDTO;
import com.cdwater.ticketbox.order.api.dto.BookingLineDTO;
import com.cdwater.ticketbox.order.api.dto.BookingListItemDTO;
import com.cdwater.ticketbox.order.api.dto.BookingSeatDTO;
import com.cdwater.ticketbox.order.domain.model.BookingLine;
import com.cdwater.ticketbox.order.domain.model.BookingOrder;
import com.cdwater.ticketbox.order.domain.model.BookingSeat;
import org.springframework.stereotype.Component;

@Component
public class BookingViewConverter {

    public BookingDetailDTO toDetail(BookingOrder order) {
        BookingDetailDTO view = new BookingDetailDTO();
        view.setOrderNo(String.valueOf(order.getOrderNo()));
        view.setProductType(order.getProductType());
        view.setStatus(order.getStatus());
        view.setTotalAmount(order.getTotalAmount());
        view.setExpireTime(order.getExpireTime());
        view.setPayTime(order.getPayTime());
        view.setCreateTime(order.getCreateTime());
        view.setLine(toLine(order.getLine()));
        view.setSeats(order.getSeats().stream().map(this::toSeat).toList());
        return view;
    }

    public BookingListItemDTO toListItem(BookingOrder order) {
        BookingListItemDTO view = new BookingListItemDTO();
        view.setOrderNo(String.valueOf(order.getOrderNo()));
        view.setProductType(order.getProductType());
        view.setStatus(order.getStatus());
        view.setTotalAmount(order.getTotalAmount());
        view.setExpireTime(order.getExpireTime());
        view.setPayTime(order.getPayTime());
        view.setCreateTime(order.getCreateTime());
        view.setLine(toLine(order.getLine()));
        return view;
    }

    private BookingLineDTO toLine(BookingLine line) {
        if (line == null) {
            return null;
        }
        BookingLineDTO view = new BookingLineDTO();
        view.setProductId(line.getProductId());
        view.setTicketTypeId(line.getTicketTypeId());
        view.setTitle(line.getTitle());
        view.setVenueName(line.getVenueName());
        view.setVenueAddress(line.getVenueAddress());
        view.setHallName(line.getHallName());
        view.setTicketTypeName(line.getTicketTypeName());
        view.setZone(line.getZone());
        view.setStartTime(line.getStartTime());
        view.setEndTime(line.getEndTime());
        view.setUnitPrice(line.getUnitPrice());
        view.setQuantity(line.getQuantity());
        return view;
    }

    private BookingSeatDTO toSeat(BookingSeat seat) {
        BookingSeatDTO view = new BookingSeatDTO();
        view.setSeatId(seat.getSeatId());
        view.setZone(seat.getZone());
        view.setSeatNo(seat.getSeatNo());
        view.setRowNum(seat.getRowNum());
        view.setColNum(seat.getColNum());
        view.setPrice(seat.getPrice());
        return view;
    }
}
