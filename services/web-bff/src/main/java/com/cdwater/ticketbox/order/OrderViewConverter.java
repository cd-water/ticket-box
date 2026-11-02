package com.cdwater.ticketbox.order;

import com.cdwater.ticketbox.order.api.dto.BookingDetailDTO;
import com.cdwater.ticketbox.order.api.dto.BookingListItemDTO;
import com.cdwater.ticketbox.order.api.dto.BookingSeatDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class OrderViewConverter {

    public MovieOrderView toMovie(BookingDetailDTO source) {
        MovieOrderView view = new MovieOrderView();
        fillMovie(view, source.getOrderNo(), source.getStatus(), source.getTotalAmount(),
                source.getExpireTime(), source.getPayTime(), source.getCreateTime(),
                source.getLine().getTitle(), source.getLine().getVenueName(),
                source.getLine().getHallName(), source.getLine().getStartTime(),
                source.getLine().getEndTime());
        view.setSeats(source.getSeats().stream().map(this::toSeat).toList());
        return view;
    }

    public MovieOrderView toMovie(BookingListItemDTO source) {
        MovieOrderView view = new MovieOrderView();
        fillMovie(view, source.getOrderNo(), source.getStatus(), source.getTotalAmount(),
                source.getExpireTime(), source.getPayTime(), source.getCreateTime(),
                source.getLine().getTitle(), source.getLine().getVenueName(),
                source.getLine().getHallName(), source.getLine().getStartTime(),
                source.getLine().getEndTime());
        return view;
    }

    public ShowOrderView toShow(BookingDetailDTO source) {
        ShowOrderView view = new ShowOrderView();
        fillShow(view, source.getOrderNo(), source.getStatus(), source.getTotalAmount(),
                source.getExpireTime(), source.getPayTime(), source.getCreateTime(),
                source.getLine().getTitle(), source.getLine().getVenueName(),
                source.getLine().getVenueAddress(), source.getLine().getStartTime(),
                source.getLine().getTicketTypeName(), source.getLine().getZone(),
                source.getLine().getUnitPrice(), source.getLine().getQuantity());
        view.setSeats(source.getSeats().stream().map(this::toSeat).toList());
        return view;
    }

    public ShowOrderView toShow(BookingListItemDTO source) {
        ShowOrderView view = new ShowOrderView();
        fillShow(view, source.getOrderNo(), source.getStatus(), source.getTotalAmount(),
                source.getExpireTime(), source.getPayTime(), source.getCreateTime(),
                source.getLine().getTitle(), source.getLine().getVenueName(),
                source.getLine().getVenueAddress(), source.getLine().getStartTime(),
                source.getLine().getTicketTypeName(), source.getLine().getZone(),
                source.getLine().getUnitPrice(), source.getLine().getQuantity());
        return view;
    }

    public PlayOrderView toPlay(BookingDetailDTO source) {
        return toPlay(source.getOrderNo(), source.getStatus(), source.getTotalAmount(),
                source.getExpireTime(), source.getPayTime(), source.getCreateTime(),
                source.getLine().getTitle(), source.getLine().getVenueAddress(),
                source.getLine().getUnitPrice(), source.getLine().getQuantity());
    }

    public PlayOrderView toPlay(BookingListItemDTO source) {
        return toPlay(source.getOrderNo(), source.getStatus(), source.getTotalAmount(),
                source.getExpireTime(), source.getPayTime(), source.getCreateTime(),
                source.getLine().getTitle(), source.getLine().getVenueAddress(),
                source.getLine().getUnitPrice(), source.getLine().getQuantity());
    }

    private void fillMovie(MovieOrderView view, String orderNo, Integer status,
                           BigDecimal totalAmount, LocalDateTime expireTime,
                           LocalDateTime payTime, LocalDateTime createTime,
                           String movieTitle, String cinemaName, String hallName,
                           LocalDateTime startTime, LocalDateTime endTime) {
        view.setOrderNo(orderNo);
        view.setStatus(status);
        view.setTotalAmount(totalAmount);
        view.setExpireTime(expireTime);
        view.setPayTime(payTime);
        view.setCreateTime(createTime);
        view.setMovieTitle(movieTitle);
        view.setCinemaName(cinemaName);
        view.setHallName(hallName);
        view.setStartTime(startTime);
        view.setEndTime(endTime);
    }

    private void fillShow(ShowOrderView view, String orderNo, Integer status,
                          BigDecimal totalAmount, LocalDateTime expireTime,
                          LocalDateTime payTime, LocalDateTime createTime,
                          String showTitle, String venueName, String venueAddress,
                          LocalDateTime startTime, String ticketTypeName,
                          String zone, BigDecimal unitPrice, Integer quantity) {
        view.setOrderNo(orderNo);
        view.setStatus(status);
        view.setTotalAmount(totalAmount);
        view.setExpireTime(expireTime);
        view.setPayTime(payTime);
        view.setCreateTime(createTime);
        view.setShowTitle(showTitle);
        view.setVenueName(venueName);
        view.setVenueAddress(venueAddress);
        view.setStartTime(startTime);
        view.setTicketTypeName(ticketTypeName);
        view.setZone(zone);
        view.setUnitPrice(unitPrice);
        view.setQuantity(quantity);
    }

    private PlayOrderView toPlay(String orderNo, Integer status,
                                 BigDecimal totalAmount, LocalDateTime expireTime,
                                 LocalDateTime payTime, LocalDateTime createTime,
                                 String itemTitle, String itemAddress,
                                 BigDecimal unitPrice, Integer quantity) {
        PlayOrderView view = new PlayOrderView();
        view.setOrderNo(orderNo);
        view.setStatus(status);
        view.setTotalAmount(totalAmount);
        view.setExpireTime(expireTime);
        view.setPayTime(payTime);
        view.setCreateTime(createTime);
        view.setItemTitle(itemTitle);
        view.setItemAddress(itemAddress);
        view.setUnitPrice(unitPrice);
        view.setQuantity(quantity);
        return view;
    }

    private OrderSeatView toSeat(BookingSeatDTO source) {
        OrderSeatView view = new OrderSeatView();
        view.setSeatId(source.getSeatId());
        view.setZone(source.getZone());
        view.setSeatNo(source.getSeatNo());
        view.setRowNum(source.getRowNum());
        view.setColNum(source.getColNum());
        view.setPrice(source.getPrice());
        return view;
    }
}
