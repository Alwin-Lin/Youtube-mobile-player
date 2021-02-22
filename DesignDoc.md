# Design Documentation
## Software Architecture
![](https://user-images.githubusercontent.com/22556115/108634412-577c1780-742e-11eb-8f76-0a7145fea756.jpg)

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
      - In the video list > Long click on video > edit > [Done]
    * Delete
      - In the video list > Click on [X] > Video deleted
4. Change video playback expirence
    * Landscape video playback
    * portrait video playback
    * Change close-up central point
    
|               | Portrait Phone                         | Landscape Phone
--------------- | -------------------------------------  | ----------------
Portrait Video  | 1 view                                 | 2 views, right: zoomed in & left: full 
Landscape Video | 2 views, top: zoomed in & bottom: full | 1 view
    
  
## Code flow
Use I/com.alwin.youtubemobileplayer as filter for logs
- CUJ-1, Play from Youtube
  - Codeflow:
    
    ![](https://user-images.githubusercontent.com/22556115/108645799-d7bf6e80-7468-11eb-9299-fee8d15f4dba.jpg)
  - Log:
    
    ![](https://user-images.githubusercontent.com/22556115/107890884-36687380-6ed0-11eb-8ad1-e219de63a695.png)
- CUJ-2, Play exsiting video from list
  - Codeflow
    
    ![](https://user-images.githubusercontent.com/22556115/108645796-d68e4180-7468-11eb-85b6-45974c33a17a.jpg)
  - Log: 
    
    ![](https://user-images.githubusercontent.com/22556115/108646341-58cb3580-746a-11eb-9196-c8e3733dae2d.png)
- CUJ-3, Manage video list
  - Codeflow
  
  ![](https://user-images.githubusercontent.com/22556115/108645800-d7bf6e80-7468-11eb-9467-d23982c83120.jpg)
  - Log:
  
   ![](https://user-images.githubusercontent.com/22556115/107891365-0f5f7100-6ed3-11eb-827a-f70e3ed2fb2a.png)
