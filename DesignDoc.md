# Design Documentation
## Software Architecture
![](https://user-images.githubusercontent.com/22556115/97812716-0c988300-1c38-11eb-8e17-40813dcea985.jpg)

## Logs
Use I/com.alwin.youtubemobileplayer as filter

Sharing from Youtube, then deleteing entry

![](https://user-images.githubusercontent.com/22556115/104980530-9811ec80-59bb-11eb-907f-1d7540f1a9d4.png)

Manualy entering video, then deleting entry

![](https://user-images.githubusercontent.com/22556115/104980527-96e0bf80-59bb-11eb-90fd-6bdf6e95b815.png)
## Project structure
### MainActivity
- Entry point of the app, main activity
### IntentReceiver
- Recives intent from Youtube, proccesses it, and sends to VideoList
### videoListUI
- VideoList
  - Fragment class, sets up list and handels intent from addVideo and IntentReciver
- VideoListAdapter
  - Allows actions such as onEdit, onVideoClick
### videoRecordUI
- Handels adding and editing exsisting videos 
### videoModel
- Video class, requires a name an a url as input
- VideoDao defines functions that allows interaction with database
- VideoDeleteViewModel deletes videos
- VideoAdditionViewModel adds videos

## Continious building and continuious testing
### What is it?
A process using Google Cloud Platform to automaticly test and build new commits, outputs built APK and test reports.
### What would I need?
A google account
Credit card for 
### How 
