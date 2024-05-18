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

# 好感度消息，每 "changeLevel" 级一条消息
# %成员% 是触发消息用户
# %后缀 是上方设置的suffix
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