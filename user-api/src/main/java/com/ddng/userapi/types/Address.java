package com.ddng.userapi.types;

import lombok.Getter;

import javax.persistence.Embeddable;
import java.util.Objects;

/**
 * <h1>주소 값 타입 객체</h1>
 *
 * 주소를 Address 값 타입으로 제공하도록 지원한다.<br>
 * 불변 객체로, setter 메서드는 지원하지 않는다.
 *
 * @author ParkIlHoon
 * @version 1.0
 */
@Embeddable
@Getter
public class Address
{
    /**
     * 도로명 주소
     */
    private String roadAddr;
    /**
     * 지번 주소
     */
    private String jibunAddr;
    /**
     * 우편번호
     */
    private String zipNo;
    /**
     * 기타 주소
     */
    private String etcAddr;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 생성자
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    protected Address()
    {

    }

    /**
     * 주소 값타입 객체를 생성한다
     *
     * @param zipNo 우편번호
     * @param roadAddr 도로명주소
     * @param etcAddr 사용자입력(추가)주소
     * @param jibunAddr 지번주소
     */
    public Address(String zipNo, String roadAddr, String etcAddr, String jibunAddr)
    {
        this.zipNo = zipNo;
        this.roadAddr = roadAddr;
        this.etcAddr = etcAddr;
        this.jibunAddr = jibunAddr;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 오버라이딩 메서드
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(roadAddr, address.roadAddr) &&
                Objects.equals(jibunAddr, address.jibunAddr) &&
                Objects.equals(zipNo, address.zipNo) &&
                Objects.equals(etcAddr, address.etcAddr);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(roadAddr, jibunAddr, zipNo, etcAddr);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 기타 메서드
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 전체 주소를 생성해 반환한다
     *
     * @return 도로명(지번)주소 + 사용자입력주소
     */
    public String createFullAddr()
    {
        String returnValue = "";

        if(roadAddr == null || "".equals(roadAddr))
        {
            returnValue = jibunAddr;
        }

        if(etcAddr != null && !"".equals(etcAddr))
        {
            returnValue += " " + etcAddr;
        }

        return returnValue;
    }
}