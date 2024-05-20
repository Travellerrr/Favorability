# 好感度插件

[![](https://img.shields.io/github/v/release/Travellerrr/Favorability)](https://github.com/Travellerrr/Favorability/releases)

## 介绍

本好感度插件基于了HuyanEconomy的经济系统，如果有想法的话后续或许能适配更多的经济插件

~~（其实就是懒得自建方法）~~

## 指令

|           指令           |              功能               |
|:----------------------:|:-----------------------------:|
|       `/制造 [金币]`       |           使用金币制造物品            |
|        `/查看制造`         |            查看制造队列             |
|        `/查看好感度`        |          查看机器人对你的好感度          |
| `/Favorability reload` |             重载配置              |
|       `/盒 [@某人]`       | 发送被@的人的QQ个人信息（just a kidding) |
|         `/盒我`          | 发送你自己的QQ个人信息（just a kidding)  |

## 权限节点

|           指令           |                       权限节点                        |
|:----------------------:|:-------------------------------------------------:|
|       `/制造 [金币]`       |   `cn.travellerr.favorability.command.makeitem`   |
|        `/查看制造`         |  `cn.travellerr.favorability.command.checkmake`   |
|        `/查看好感度`        |  `cn.travellerr.favorability.command.checklove`   |
| `/Favorability reload` | `cn.travellerr.favorability.command.favorability` |
|       `/盒 [@某人]`       |   `cn.travellerr.favorability.command.doxxing`    |
|         `/盒我`          |  `cn.travellerr.favorability.command.doxxingme`   |

## 配置

### config.yml ——主要配置数字内容

```yaml
# 制造至少消耗金币
atLeastCoin: 30

# 至少需要多久制造/分钟
atLeastMin: 10

# 至多需要多久制造/分钟
atMostMin: 180

# 人物后缀
suffix: Sensei

# 每多少级改变一次好感度消息
changeLevel: 5

# 当好感经验值超出下方定义数量
# 每几exp升一级
perLevel: 1810

# 好感信息上升计算自定义
levelList: [15, 45, 75, 110, 145, 180, 220, 260, 300, 360, 450, 555, 675, 815, 975, 1155, 1360, 1590, 1845, 2130, 2445, 2790, 3165, 3575, 4020, 4500, 5020, 5580, 6180, 6825, 7515, 8250, 9030, 9860, 10740, 11670, 12655, 13695, 14790, 15945, 17160, 18435, 19770, 21170, 22635, 24165, 25765, 27435, 29175]
```
<br><br>
### MsgConfig.yml —— 主要配置发送消息内容

```yaml
# 好感度查看
# "%成员%"该成员名称
# "%机器人%"机器人名称
# "%好感%"好感度
# "%好感信息%"好感度消息
checkLove: "你对%机器人%的好感度为: %好感度%\n%好感信息%"

# 群好感度排行信息
# "%成员%"该成员名称
# "%机器人%"机器人名称
# "%好感%"好感度
# "%后缀%"设置的后缀
groupLoveMsg: "这位是%成员% %后缀%,\n%机器人%对Ta的好感度为: %好感%"

# 全体好感度排行信息
# "%成员%"该成员QQ号
# "%机器人%"机器人名称
# "%好感%"好感度
# "%后缀%"设置的后缀
# "%排名%"当前排名
totalLoveMsg: "第 %排名% 名 %后缀%, \n %机器人% 对Ta的好感度为: %好感"

# 好感度消息，每 "changeLevel" 级一条消息
LoveMessage: 
  - %成员% %后缀%您……您好……
  - %成员% %后缀%是一个好人
  - %成员% %后缀%一直对我很好呢！
  - %成员% %后缀%, 很关心我呢……
  - %成员% %后缀%, 稀饭！
  - 最喜欢 %成员% %后缀%了！
  - 呜哇！%成员% %后缀% 也太好了！好喜欢 %成员% %后缀%！
  - 'わたしは %成员% %后缀% せんせいが大好きです! '

# 金币不够至少所需提示
notEnough: 
  - 没有商家愿意接单
  - 导致在路上被风刮走了，费了很大劲才捡回来
  - 中途遇到土匪，看见你摇了摇头转身走了
  - 不好意思出门找商家做礼物
  - 商家吓得像见了鬼，纷纷逃之夭夭。
  - 商家欲哭无泪，宁愿关门大吉。
  - 商家眼神闪烁，仿佛看到了世界末日。
  - 商家们一个个避而远之，生怕倒霉。
  - 商家们纷纷摇头叹息，不知所措。
  - 商家们眼神暗淡，装作没看见。
  - 商家们一个个闭门不出，宁可躲起来。
  - 商家们面露难色，仿佛碰到了麻烦。
  - 商家们一个个摇头苦笑，无可奈何。
```

---

## 礼物配置示例

礼物json位于`./data/cn.travellerr.Favorability/gift.json`

```json
{
    "1": {
      "Name": "百科全书",
      "Describe": "从系住面包口袋的“那个东西”到放在便当里像草一样的“那个东西“，\n这里有你想知道的一切。",
      "Level": 2,
      "Love": 20,
      "Url": "5/635505.png"
    },
    "[id](数字)": {
      "Name": "[礼物名称]",
      "Describe": "[礼物描述]",
      "Level": "[礼物制造等级](数字)",
      "Love": "[好感度](数字)",
      "Url": "[图片网址](http打头)"
    },
    "3": {
      "Name": "示例礼物", 
      "Describe": "这是介绍", 
      "Level": 4, 
      "Love": 123, 
      "Url": "https://mirai.mamoe.net/assets/uploads/system/favicon.ico"
    }
}
```
