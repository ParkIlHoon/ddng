package com.ddng.utils.homepage.canvas;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CANVAS")
public class Canvas
{
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "SUB_TITLE")
    private String subTitle;

    @Lob
    @Column(name = "CONTENTS")
    private String contents;

    @Column(name = "BANNER_IMG_PATH")
    private String bannerImgPath;

    @Column(name = "INSERT_DATE")
    private LocalDateTime insertDate;

    @Column(name = "ENABLE")
    private boolean enable;

    protected Canvas() { }
}
