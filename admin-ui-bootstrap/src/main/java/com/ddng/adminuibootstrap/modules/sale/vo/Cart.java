package com.ddng.adminuibootstrap.modules.sale.vo;

import com.ddng.adminuibootstrap.modules.item.dto.ItemDto;
import com.ddng.adminuibootstrap.modules.sale.dto.CouponDto;
import com.ddng.adminuibootstrap.modules.schedules.dto.ScheduleDto;
import org.apache.commons.lang.NullArgumentException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Cart
{
    private List<CartItem> items = new ArrayList<>();

    public List<CartItem> getItems()
    {
        return this.items;
    }

    public void addCartItem (ItemDto item, ScheduleDto schedule)
    {
        if (item == null)
        {
            throw new NullArgumentException("item");
        }

        List<CartItem> sameCartItems = findSameCartItems(item.getId(), schedule.getCustomerId(), schedule.getId());

        if (sameCartItems.size() == 0)
        {
            CartItem cartItem = CartItem.builder()
                    .itemId(item.getId())
                    .count(1)
                    .salePrice(item.getPrice())
                    .scheduleId(schedule.getId())
                    .customerId(schedule.getCustomerId())
                    .couponId(null)
                    .build();

            this.items.add(cartItem);
        }
        else if (sameCartItems.size() == 1)
        {
            CartItem cartItem = sameCartItems.get(0);
            cartItem.setCount(cartItem.getCount() + 1);
        }
        else
        {
            //TODO
        }

    }

    public void addCartItem (ItemDto item, ScheduleDto schedule, CouponDto coupon)
    {
        if (item == null)
        {
            throw new NullArgumentException("item");
        }

        List<CartItem> sameCartItems = findSameCartItems(item.getId(), schedule.getCustomerId(), schedule.getId(), coupon.getId());

        if (sameCartItems.size() == 0)
        {
            CartItem cartItem = CartItem.builder()
                                            .itemId(item.getId())
                                            .count(1)
                                            .salePrice(item.getPrice())
                                            .scheduleId(schedule.getId())
                                            .customerId(schedule.getCustomerId())
                                            .couponId(coupon.getId())
                                        .build();

            this.items.add(cartItem);
        }
        else if (sameCartItems.size() == 1)
        {
            CartItem cartItem = sameCartItems.get(0);
            cartItem.setCount(cartItem.getCount() + 1);
        }
        else
        {
            //TODO
        }

    }

    public List<CartItem> findSameCartItems (Long itemId, Long customerId, Long scheduleId)
    {
        Stream<CartItem> stream = this.items.stream()
                .filter(i -> i.getItemId().equals(itemId)
                        && customerId.equals(i.getCustomerId())
                        && scheduleId.equals(i.getScheduleId()));

        return stream.collect(Collectors.toList());
    }

    public List<CartItem> findSameCartItems (Long itemId, Long customerId, Long scheduleId, Long couponId)
    {
        Stream<CartItem> stream = this.items.stream()
                .filter(i -> i.getItemId().equals(itemId)
                        && customerId.equals(i.getCustomerId())
                        && scheduleId.equals(i.getScheduleId())
                        && couponId.equals(i.getCouponId()));

        return stream.collect(Collectors.toList());
    }
}
