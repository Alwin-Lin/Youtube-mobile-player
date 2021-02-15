# Design Documentation
## Software Architecture
<<<<<<< HEAD
![](https://user-images.githubusercontent.com/22556115/97812716-0c988300-1c38-11eb-8e17-40813dcea985.jpg)

## Logs
Set Locat filter to info, and the whole process should all be there

## List of functions
### MainActivity
- Handles intent coming from Youtube and sends bundle to VideoEntryDialogFragment
### Notifier
- Builds the list items
### PlayVideoFragment
- Placeholder, going to be replaced with a working videoPlayer
### Video
- Data class, contains the following
  - id 
  - name 
  - url
### VideoEntryDialogFragment
- Set up the small popup window once the floating action button is clicked
- Adds intent to list
### VideoEntryViewModel
- Interacts with LiveData<>
### VideoList
- Sets up the list view on entry, sets onClickListener to floating button
### VideoListAdapter
- Defines what goes into the the list
  - videoId
  - nameView
  - url
  - thumbnail
### ViewListModle
  - Handels delettion of items
  
## Continious building and continuious testing
### What is it?
A process using Google Cloud Platform to automaticly test and build new commits, outputs built APK and test reports.
### What would I need?
A google account
Credit card for 
### How 
=======
![](https://user-images.githubusercontent.com/22556115/107891882-4be09c00-6ed6-11eb-9e23-317e4498c102.jpg)

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
    
|               | Portrait Phone                         | Landscape Phone
--------------- | -------------------------------------  | ----------------
Portrait Video  | 1 view                                 | 2 views, right: zoomed in & left: full 
Landscape Video | 2 views, top: zoomed in & bottom: full | 1 view
    
  
## Code flow
Use I/com.alwin.youtubemobileplayer as filter for logs
- CUJ-1, Play from Youtube
  - Codeflow:
    
    ![](https://user-images.githubusercontent.com/22556115/107891824-dd9bd980-6ed5-11eb-8acd-dbbcc5d5b55e.jpg)
  - Log:
    
    ![](https://user-images.githubusercontent.com/22556115/107890884-36687380-6ed0-11eb-8ad1-e219de63a695.png)
- CUJ-2, Play exsiting video from list
  - Codeflow
    
    ![](https://user-images.githubusercontent.com/22556115/107891879-471be800-6ed6-11eb-99d2-71255e049fe0.jpg)
  - Log: 
    
    ![](https://user-images.githubusercontent.com/22556115/107890992-d4f4d480-6ed0-11eb-803a-891edbb2bcbd.png)
- CUJ-3, Manage video list
  - Codeflow
  
  ![](https://user-images.githubusercontent.com/22556115/107891696-24d59a80-6ed5-11eb-9793-6ff754700b84.jpg)
  - Log:
  
   ![](https://user-images.githubusercontent.com/22556115/107891365-0f5f7100-6ed3-11eb-827a-f70e3ed2fb2a.png)
>>>>>>> 97d0b2ecb5fa92ac4dbecce3384fc453d92c8ca4
