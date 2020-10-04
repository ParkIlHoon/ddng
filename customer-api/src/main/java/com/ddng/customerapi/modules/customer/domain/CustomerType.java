package com.ddng.customerapi.modules.customer.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>고객 품종 Enum 클래스</h1>
 *
 * @version 1.0
 */
@Getter
public enum CustomerType
{
    CHIN("친"),
    MALTESE("말티즈"),
    PAPILLON("파피용"),
    CHINESE_CRESTED("차이니스 크레스티드"),
    ITALIAN_GREYHOUND("이탈리안 그레이하운드"),
    PUG("퍼그"),
    MINIATURE_PINSCHER("미니어처 핀셔"),
    PEKINESE("페키니즈"),
    POMERANIAN("포메라니안"),
    AFFENPINCHER("아펜핀셔"),
    POODLE("푸들"),
    SHIH_TZU("시츄"),
    CHIHUAHUA("치와와"),
    YORKSHIRE_TERRIER("요크셔 테리어"),
    SILKY_TERRIER("실키 테리어"),
    SPITZ("스피츠"),
    KING_CHARLES_SPANIEL("킹 찰스 스패니얼"),
    CAVALIER_KING_CHARELS_SPANIEL("카발리에 킹 찰스 스패니얼"),
    TIBETAN_TERRIER("티베탄 테리어"),
    LHASA_APSO("라사압소"),
    FRENCH_BULLDOG("프렌치 불도그"),
    BOSTON_TERRIER("보스턴 테리어"),
    SCHIPPERKE("스키퍼키"),
    BULLDOG("불도그"),
    BICHON_FRISE("비숑 프리제"),
    CHOW_CHOW("차우차우"),
    DALMATIAN("달마시안"),
    SHIBA("시바견"),
    AKITA("아키타견"),
    DONGGYEONG("동경이"),
    SAPSAL("삽살개"),
    JINDO("진돗개"),
    PUNGSAN("풍산개"),
    FOX_TERRIER("폭스 테리어"),
    MANCHESTER_TERRIER("멘체스터 테리어"),
    BEDLINGTON_TERRIER("베들링턴 테리어"),
    BULL_TERRIER("불 테리어"),
    WELSH_TERRIER("웰시 테리어"),
    IRISH_TERRIER("아이리시 테리어"),
    STAFFORDSHIRE_BULL_TERRIER("스태퍼드셔 불 테리어"),
    DANDIE_DINMONT_TERRIER("댄디 딘몬트 테리어"),
    MINIATURE_SCHNAUZER("미니어처 슈나우저"),
    SCOTTISH_TERRIER("스코티시 테리어"),
    JACK_RUSSELL_TERRIER("잭 러셀 테리어"),
    PITBULL_TERRIER("핏불 테리어"),
    AMERICAN_COCKER_SPANIEL("아메리칸 코커 스패니얼"),
    WELSH_SPRINGER_SPANIEL("웰시 스프링어 스패니얼"),
    ENGLISH_SPRINGER_SPANIEL("잉글리시 스프링어 스패니얼"),
    ENGLISH_COCKER_SPANIEL("잉글리시 코커 스패니얼"),
    GOLDEN_RETRIEVER("골든 리트리버"),
    LABRADOR_RETRIEVER("래브라도 리트리버"),
    CHESAPEAKE_BAY_RETRIEVER("체서피크 베이 리트리버"),
    WEIMARANER("와이마라너"),
    VIZSLA("비즐라"),
    ENGLISH_POINTER("잉글리시 포인터"),
    PHARAOH_HOUND("파라오 하운드"),
    DACHSHUND("닥스훈트"),
    ENGLISH_FOX_HOUND("잉글리시 폭스 하운드"),
    HARRIER("해리어"),
    BEAGLE("비글"),
    AFGHAN_HOUND("아프간 하운드"),
    GREY_HOUND("그레이 하운드"),
    WHIPPET("휘핏"),
    BORZOI("보르조이"),
    SAMOYED("사모예드"),
    SIBERIAN_HUSKY("시베리안 허스키"),
    ALASKAN_MALAMUTE("알래스칸 맬러뮤트"),
    GERMAN_SHEPARD("저먼 셰퍼드"),
    KOMONDOR("코몬도르"),
    GREAT_PYRENEES("그레이트 피레니즈"),
    COLLIE("콜리"),
    KANGAL("캉갈"),
    OLD_ENGLISH_SHEEPDOG("올드 잉글리시 시프도그"),
    PULI("풀리"),
    SHETLAND_SHEEPDOG("셰틀랜드 십도그"),
    BEARDED_COLLIE("비어디드 콜리"),
    WELSH_CORGIE("웰시 코기"),
    BORDER_COLLIE("보더 콜리"),
    BELGIAN_SHEPHERD("벨지안 십도그"),
    TIBETAN_MASTIFF("티베탄 마스티프"),
    ENGLISH_MASTIFF("잉글리시 마스티프"),
    ROTTWEILER("로트바일러"),
    BOXER("복서"),
    GIANT_SCHNAUZER("자이언트 슈나우저"),
    STANDARD_SCHNAUZER("스탠더드 슈나우저"),
    DOBERMAN_PINSCHER("도베르만 핀셔"),
    GREAT_DANE("그레이트 데인"),
    BULL_MASTIFF("불 마스티프"),
    MIX_DOG("믹스견")
    ;

    private String korName;

    CustomerType(String korName)
    {
        this.korName = korName;
    }

    public static CustomerType findEnumByKorNameEq(String korName)
    {
        for(CustomerType type : CustomerType.values())
        {
            if(type.getKorName().equals(korName))
            {
                return type;
            }
        }
        return null;
    }

    public static List<CustomerType> findEnumByKorNameLike(String korName)
    {
        List<CustomerType> result = new ArrayList<>();
        for(CustomerType type : CustomerType.values())
        {
            if(type.getKorName().trim().indexOf(korName) > -1)
            {
                result.add(type);
            }
        }
        return result;
    }
}
