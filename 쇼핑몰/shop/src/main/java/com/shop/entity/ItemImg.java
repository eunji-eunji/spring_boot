package com.shop.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "item_img")
@Data
public class ItemImg extends BaseEntity{
    @Id
    @Column(name = "item_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public void updateItemImg(String oriImgName, String imgName, String imgUrl){
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

    public ItemImg toEntity() {
        ItemImg entity = new ItemImg();
        entity.setId(this.id);
        entity.setImgName(this.imgName);
        entity.setOriImgName(this.oriImgName);
        entity.setImgUrl(this.imgUrl);
        entity.setRepImgYn(this.repImgYn);
        return entity;
    }

}
