package cn.travellerr.entity;

import cn.travellerr.utils.Log;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Entity
@Table
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Favourite {
    @Id
    private Long QQ; // 用户QQ号

    private String name; // 用户昵称

    @Builder.Default
    private Long exp = 0L; // 用户好感经验值


    private Long makeTime; // 用户制作时长

    private Integer itemLevel; // 用户物品等级

    private boolean isMaking; // 用户是否正在制作

    @Builder.Default
    private Date startMakeTime = new Date(); // 用户开始制作时间

    @Builder.Default
    private Date regTime = new Date(); // 用户注册时间

    public void printAll() {
        Log.debug("QQ号:" + this.QQ);
        Log.debug("昵称:" + this.name);
        Log.debug("好感经验值:" + this.exp);
        Log.debug("制作时长:" + this.makeTime);
        Log.debug("物品等级:" + this.itemLevel);
        Log.debug("是否正在制作:" + this.isMaking);
        Log.debug("开始制作时间:" + this.startMakeTime);
        Log.debug("注册时间:" + this.regTime);

    }
}
