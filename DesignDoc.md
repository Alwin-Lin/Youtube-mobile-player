# Design Documentation
## Software Architecture
![](https://user-images.githubusercontent.com/22556115/97812716-0c988300-1c38-11eb-8e17-40813dcea985.jpg)

## User journey
As a user, I want to: 
1. Play a Youtube Video
  - In Youtube app > [share] > Video plays and the video is saved in the play list
2. Play video from the video list
  - In the video list > Click on a video > video plays
3. Manage video list
    * Add
      - In the video list > Click on bottom right button > Enter name and url > [Done]
    * Edit
      - In the video list > Long click on video > edit > [Done] >
    * Delete
      - In the video list > Click on [X] > Video deleted
4. Change video playback expirence
    * Landscape video playback
    * portrait video playback
    * Change close-up central point
    
  
## Code flow
- Select video from Youtube
  - Youtube [Share] > IntentReciver recives and processes url > IntentReciver sends url to VideoList and PlayVideoActivity via intent > VideoList calls addVideo() > PlayVideoActivity takes url, builds mediaSource, plays video > Exit out to video list
- Select video from list
  - Clicks on a video > VideoList sends intent to start PlayVideoActivity > Video plays > Exit out to video list
- Edit video on list
  - Long click > VideoEditDialogFragment called > Check if video name or url is altered > If altered make changes, if not Exit out to video list
- Manualy add video to list
  - Add video action button clicked > VideoList sends intent, starts AddVideoActivity > User input name and url > If both are not empty, AddVideoActivity sends intent back to VideoList and exits > VideoList calls addVideo() > Video is added
- Delete video on list
  - The [X] button is clicked > videoDeleteViewModel.delete() called in VideoList > Video deleted
  
## Logs
Use I/com.alwin.youtubemobileplayer as filter

Playing a Youtube Video
![](https://user-images.githubusercontent.com/22556115/107166380-a9229d80-696a-11eb-821a-9b3911f70c9e.png)

Play video from the video list

![](https://user-images.githubusercontent.com/22556115/107166378-a7f17080-696a-11eb-9eae-84f1517773e7.png)

Managing video list

![](https://user-images.githubusercontent.com/22556115/107166379-a88a0700-696a-11eb-860d-084f4a4ba489.png)

ToDo: Add log for Change video playback expirence
