# Design Documentation
## Software Architecture
![](https://user-images.githubusercontent.com/22556115/107891148-aaefe200-6ed1-11eb-8c12-0fbd81f796e7.jpg)

## CUJ
As a user, I want to: 
1. Play a Youtube Video
  * In Youtube app > [share] > Video plays and the video is saved in the play list
2. Play video from the video list
  * In the video list > Click on a video > video plays
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
    
|               | Portrait Phone | Landscape Phone
--------------- | -------------- | ----------------
Portrait Video  | 1 video        | 2 videos, right: zoomed in & left: full 
Landscape Video | 2 videos, top: zoomed in & bottom: full | 1 video
    
  
## Code flow
- CUJ-1, Play from Youtube
  - Codeflow:
    - ![](https://user-images.githubusercontent.com/22556115/107891147-aa574b80-6ed1-11eb-8aa9-9d839ba084b2.jpg)
  - Log:
    - ![](https://user-images.githubusercontent.com/22556115/107890884-36687380-6ed0-11eb-8ad1-e219de63a695.png)
- CUJ-2, Play exsiting video from list
  - Codeflow
    - ![](https://user-images.githubusercontent.com/22556115/107891146-aa574b80-6ed1-11eb-93fd-4c50a4755128.jpg)
  - Log: 
    - ![](https://user-images.githubusercontent.com/22556115/107890992-d4f4d480-6ed0-11eb-803a-891edbb2bcbd.png)
- CUJ-3, Manage video list
  - codeflow placehold
  - Log:
    - ![](https://user-images.githubusercontent.com/22556115/107166379-a88a0700-696a-11eb-860d-084f4a4ba489.png)
## Logs
Use I/com.alwin.youtubemobileplayer as filter
ToDo: Add log for Change video playback expirence
