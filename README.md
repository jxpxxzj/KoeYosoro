# KoeYosoro

KoeYosoro是一款基于Andoird平台的教育应用。

提供了以下的功能进行选择：

- 词汇训练
- 场景训练
- 听力训练
- 随机训练
- 口语特训
- 个人中心

## 项目技术组成

Android作为应用外侧框架。

前后端通过HTTP进行交互，用户向后端发送语音，后端可以返回语音与原词的匹配情况。

使用IBM WATSON语音识别接口，配合LCS算法。

## 项目使用指南

首页，点击六个功能块进行选择功能，就可以进行对应的功能训练

在进入训练执行模块，根据提示进行朗读，并进行录音，当朗读结束，结束录音。

之后会将匹配结果返回，错误的词汇会加入错词集。

个人中心可以查看数据统计

## SDK

IBM WATSON: 进行语音匹配 

## 截图
<img src="https://cdn.hackx.org/o_1bu4nup001khiac519kh1hf0bc88.png" width="320px" />
<img src="https://cdn.hackx.org/o_1bu4nup00nh6l2r1sao1fsa1bcn9.png" width="320px" />
<img src="https://cdn.hackx.org/o_1bu4u7738loctitopp171kavk7.png" width="320px" />
