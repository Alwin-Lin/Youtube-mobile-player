# Design Documentation
## Software Architecture
![](https://user-images.githubusercontent.com/22556115/97812716-0c988300-1c38-11eb-8e17-40813dcea985.jpg)

## User journey
- Select video from Youtube
  - Youtube,[share] > Video plays > App exits out to the video list
- Select video from list
  - Click on a video > video plays > App exits out to the video list
- Add video to list
  - Click on bottom right button > Enter name and url > [Done] > App exits to video list
- Edit video on list
  - Long click on video > edit > [Done] > Back to video list
- Delete video on list 
  - Click on [X] > Video deleted
  
## User flow
- Select video from Youtube
  - Youtube [Share] > IntentReciver recives and processes url > IntentReciver sends url to VideoList and PlayVideoActivity via intent > VideoList calls addVideo() > PlayVideoActivity takes url, builds mediaSource, plays video > Exit out to video list
- Select video from list
  - Clicks on a video > VideoList sends intent to start PlayVideoActivity > Video plays > Exit out to video list
- Edit video on list
  - Long click > VideoEditDialogFragment called > Check if video name or url is altered > If altered make changes, if not Exit out to video list
- Manualy add video to list
  - Add video action button clicked > VideoList sends intent, starts AddVideoActivity > User input name and url > If both are not empty, AddVideoActivity sends intent back to VideoList and exits > VideoList calls addVideo() > Video is added
- Delete video on list
  - [X] is clicked > videoDeleteViewModel.delete() called in VideoList > Video deleted
  
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
