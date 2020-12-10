package com.ddng.adminuibootstrap.modules.sale.vo;

import com.ddng.adminuibootstrap.modules.item.dto.ItemDto;
import com.ddng.adminuibootstrap.modules.sale.dto.CouponDto;
import com.ddng.adminuibootstrap.modules.schedules.dto.ScheduleDto;
import org.apache.commons.lang.NullArgumentException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <h1>카트 클래스</h1>
 *
 * 결제 예정인 상품을 담는 클래스
 */
public class Cart
{
    private List<CartItem> items = new ArrayList<>();

    /**
     * 카트에 담긴 상품 목록을 반환한다.
     * @return 카트에 담긴 상품 목록
     */
    public List<CartItem> getItems()
    {
        return this.items;
    }

    /**
     * 총 결제 예정 금액을 반환한다.
     * @return
     */
    public int getTotalPrice ()
    {
        int totalPrice = 0;

        for (CartItem item : this.items)
        {
            totalPrice += item.getSalePrice() * item.getCount();
        }

        return totalPrice;
    }

    /**
     * 카트를 초기화 한다.
     */
    public void reset()
    {
        this.items = new ArrayList<>();
    }

    /**
     * 카트에 상품을 추가한다.
     * @param item 카트에 추가할 상품
     */
    public void addCartItem (ItemDto item)
    {
        if (item == null)
        {
            throw new NullArgumentException("item");
        }

        List<CartItem> sameCartItems = findSameCartItems(item.getId());

        if (sameCartItems.size() == 0)
        {
            CartItem cartItem = CartItem.builder()
                    .itemId(item.getId())
                    .itemName(item.getName())
                    .count(1)
                    .salePrice(item.getPrice())
                    .scheduleId(null)
                    .customerId(null)
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

    /**
     * 카트에 스케쥴과 함께 상품을 담는다.
     * @param item 카트에 추가할 상품
     * @param schedule 연관 스케쥴
     */
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
                    .itemName(item.getName())
                    .count(1)
                    .salePrice(item.getPrice())
                    .scheduleId(schedule.getId())
                    .scheduleName(schedule.getName())
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

    /**
     * 쿠폰을 적용한 상품을 카트에 담는다.
     * @param item 카트에 추가할 상품
     * @param schedule 연관 스케쥴
     * @param coupon 적용할 쿠폰
     */
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
                                            .itemName(item.getName())
                                            .count(1)
                                            .salePrice(item.getPrice())
                                            .scheduleId(schedule.getId())
                                            .scheduleName(schedule.getName())
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

    /**
     * 카트에 동일한 상품이 담겨있는지 확인한다.
     * @param itemId 상품 아이디
     * @return
     */
    public List<CartItem> findSameCartItems (Long itemId)
    {
        Stream<CartItem> stream = this.items.stream()
                .filter(i -> i.getItemId().equals(itemId)
                        && (i.getCustomerId() == null)
                        && (i.getScheduleId() == null)
                        && (i.getCouponId() == null));

        return stream.collect(Collectors.toList());
    }

    /**
     * 카트에 동일한 스케쥴에 의해 담긴 상품이 있는지 확인한다.
     * @param itemId 상품 아이디
     * @param customerId 고객 아이디
     * @param scheduleId 스케쥴 아이디
     * @return
     */
    public List<CartItem> findSameCartItems (Long itemId, Long customerId, Long scheduleId)
    {
        Stream<CartItem> stream = this.items.stream()
                .filter(i -> i.getItemId().equals(itemId)
                        && customerId.equals(i.getCustomerId())
                        && scheduleId.equals(i.getScheduleId())
                        && (i.getCouponId() == null));

        return stream.collect(Collectors.toList());
    }

    /**
     * 카트에 동일한 쿠폰을 적용한 동일 스케쥴 상품이 있는지 확인한다.
     * @param itemId 상품 아이디
     * @param customerId 고객 아이디
     * @param scheduleId 스케쥴 아이디
     * @param couponId 쿠폰 아이디
     * @return
     */
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
